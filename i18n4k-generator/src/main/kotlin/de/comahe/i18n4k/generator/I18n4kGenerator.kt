package de.comahe.i18n4k.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.MessageBundle
import de.comahe.i18n4k.messages.MessageBundleLocalizedString
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory1
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory10
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory10Typed
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory1Typed
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory2
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory2Typed
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory3
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory3Typed
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory4
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory4Typed
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory5
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory5Typed
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory6
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory6Typed
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory7
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory7Typed
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory8
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory8Typed
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory9
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory9Typed
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactoryN
import de.comahe.i18n4k.messages.NameToIndexMapperNumbersFrom1
import de.comahe.i18n4k.messages.providers.MessagesProvider
import de.comahe.i18n4k.strings.LocalizedAttributable
import de.comahe.i18n4k.strings.LocalizedString
import de.comahe.i18n4k.toTag
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.StandardCharsets
import com.squareup.kotlinpoet.TypeSpec.Companion as TypeSpec1

/**
 *  Generates for one message bundle [MessagesDataBundle] the corresponding messages class
 *  (with the indexes for each key).
 *
 *  Additionally translations can be directly generates as class, so that they must not be
 *  loaded from an external file (see [sourceCodeLocales])
 */
class I18n4kGenerator(
    /**
     * Directory where the generated Kotlin source code file should be
     * stored
     */
    private val sourceDir: File,
    /**
     * Directory where the language file with the list of string be
     * stored
     */
    private val languageFilesDir: File,
    /**
     * don't use packages but file prefixes, lowercase file names, etc.
     */
    private val languageFilesDirAndroidRawResourceStyle: Boolean,
    /** the message bundle the process */
    private val bundle: MessagesDataBundle,
    /**
     * Locale which message bundle content should be added as comment.
     * Null for no comments.
     */
    private val commentLocale: Locale?,
    /**
     * for this Locales source code will be generated to have the
     * translations in the Kotlin code without the need to load external
     * language file at runtime. null for all languages, empty for no
     * language.
     */
    private var sourceCodeLocales: List<Locale>?,
    /** The target platform for generation */
    private val generationTarget: GenerationTargetPlatform,

    /**
     * Enable usage of generic LocalizedString factories, if the text files contain messages with
     * parameters with value type declaration.
     */
    private var valueTypesEnabled: Boolean,

    /**
     * Mapping of value type names to fully qualified class names of real classes.
     *
     * If null (default), only the default value classes will be applied.
     *
     * Only evaluated if [valueTypesEnabled] is true
     */
    private var valueTypesMapping: Map<String, String>,
) {

    /**
     * sorted map key: all keys that are present in the language files value: the name of the field
     * in the generated Kotlin file
     */
    val fieldNames = sortedMapOf<String, String>(AlphanumComparator.Companion.INSTANCE_ENGLISH)

    init {
        bundle.messageDataMap.values.forEach { data ->
            data.messages.keys.forEach { key ->
                fieldNames.computeIfAbsent(key) { absentKey -> makeValidIdentifier(absentKey) }
            }
        }
        // check for unique identifier names
        val fieldNameKeys = mutableMapOf<String, String>()
        fieldNames.forEach { (key, fieldName) ->
            val oldKey = fieldNameKeys.put(fieldName, key);
            if (oldKey != null)
                throw  IllegalArgumentException("The fields '$key' and '$oldKey' map both to the same field name '$fieldName'")
        }
    }

    /** Creates a field name suitable to class member names  */
    private fun makeValidIdentifier(str: String): String {
        val id = StringBuilder(str)
        // If first character is invalid
        if (!id[0].isJavaIdentifierStart()) {
            if (id[0].isDigit())
                id.insert(0, '_')
            else
                id[0] = '_'
        }

        // Traverse the string for the rest of the characters
        for (i in 1 until str.length) {
            val c = id[i]
            if (!c.isJavaIdentifierPart()) {
                id[i] = '_'
            }
        }
        return id.toString()
    }

    /** execute the generation */
    fun run() {
        generateSourceCode()
        generateLanguageFiles()
    }

    /** generate all language files */
    private fun generateLanguageFiles() {
        val fileLanguages =
            bundle.messageDataMap.values.filter {
                !(sourceCodeLocales?.contains(it.locale) ?: true)
            }
        fileLanguages.forEach { generateLocalisationFile(it) }
    }

    /** generate all Kotlin source code files */
    private fun generateSourceCode() {
        val sourceCodeLanguages =
            (sourceCodeLocales ?: bundle.messageDataMap.keys)
                .mapNotNull { bundle.messageDataMap[it] }


        val file = FileSpec.builder(bundle.name.packageName, bundle.name.name)

        file.addType(generateMessagesObject(sourceCodeLanguages))
        sourceCodeLanguages.forEach { file.addType(generateLocalisationObject(it)) }

        file.build().writeTo(sourceDir)
    }


    /** generate the Kotlin source code file with the constants to access the messages */
    private fun generateMessagesObject(
        sourceCodeLanguages: List<MessagesData>
    ): TypeSpec {

        val messageObject = TypeSpec1.objectBuilder(bundle.name.name)
            .superclass(MessageBundle::class)
            .addSuperclassConstructorParameter("\"${bundle.name.name}\", \"${bundle.name.packageName}\"")
            .addKdoc("Massage constants for bundle '${bundle.name.name}'. Generated by i18n4k.")

        var index = 0

        val paramCountToClass: (Int, List<TypeName>) -> TypeName = { paramsCount, paramsTypeNames ->
            if (paramsCount < 1)
                MessageBundleLocalizedString::class.asTypeName()
            else if (paramsCount > 10)
                MessageBundleLocalizedStringFactoryN::class.asTypeName()
            else if (paramsTypeNames.isEmpty()) {
                when (paramsCount) {
                    1 -> MessageBundleLocalizedStringFactory1::class
                    2 -> MessageBundleLocalizedStringFactory2::class
                    3 -> MessageBundleLocalizedStringFactory3::class
                    4 -> MessageBundleLocalizedStringFactory4::class
                    5 -> MessageBundleLocalizedStringFactory5::class
                    6 -> MessageBundleLocalizedStringFactory6::class
                    7 -> MessageBundleLocalizedStringFactory7::class
                    8 -> MessageBundleLocalizedStringFactory8::class
                    9 -> MessageBundleLocalizedStringFactory9::class
                    10 -> MessageBundleLocalizedStringFactory10::class
                    else -> throw IllegalStateException()
                }.asTypeName()
            } else {
                when (paramsCount) {
                    1 -> MessageBundleLocalizedStringFactory1Typed::class
                    2 -> MessageBundleLocalizedStringFactory2Typed::class
                    3 -> MessageBundleLocalizedStringFactory3Typed::class
                    4 -> MessageBundleLocalizedStringFactory4Typed::class
                    5 -> MessageBundleLocalizedStringFactory5Typed::class
                    6 -> MessageBundleLocalizedStringFactory6Typed::class
                    7 -> MessageBundleLocalizedStringFactory7Typed::class
                    8 -> MessageBundleLocalizedStringFactory8Typed::class
                    9 -> MessageBundleLocalizedStringFactory9Typed::class
                    10 -> MessageBundleLocalizedStringFactory10Typed::class
                    else -> throw IllegalStateException()
                }.asTypeName().parameterizedBy(paramsTypeNames)
            }
        }

        fieldNames.forEach { (key, fieldName) ->
            val params = bundle.getMessageParametersNames(key)
                .filter { it.key != "~" }// remove the null token
                .mapKeys { it.key.toString() }
                .toSortedMap(AlphanumComparator.INSTANCE_ENGLISH)

            val paramNames = params.keys.toList()
            // value type. empty for no value types.
            var paramsTypeNames = listOf<TypeName>()
            if (valueTypesEnabled) {
                paramsTypeNames = convertValueTypeToTypeNames(params.values.toList())
                if (allTypeNamesAreAny(paramsTypeNames))
                    paramsTypeNames = listOf()
            }

            val paramCount = params.size
            val keyEscaped = key.replace("%", "%%");
            val property = PropertySpec.builder(fieldName, paramCountToClass(paramCount, paramsTypeNames))

            val initializerArguments = mutableListOf<Any>()

            var genericArgumentsSuffix = ""
            if (paramsTypeNames.isNotEmpty())
                genericArgumentsSuffix = "Typed"

            property.initializer(
                when (paramCount) {
                    0 -> "getLocalizedString0(\"$keyEscaped\", $index)"
                    in 1..MAX_PARAMETER_LIST_COUNT ->
                        "getLocalizedStringFactory$paramCount$genericArgumentsSuffix(\"$keyEscaped\", $index" + (
                            if (isOnlySortedDigitsStartingAt0(paramNames))
                                ")"
                            else if (isOnlySortedDigitsStartingAt1(paramNames)) {
                                initializerArguments.add(NameToIndexMapperNumbersFrom1::class);
                                ", %T)"
                            } else
                                ", ${paramNames.joinToString(", ") { "\"$it\"" }})"

                            )

                    else -> "getLocalizedStringFactoryN(\"$keyEscaped\", $index)"
                }, *initializerArguments.toTypedArray()
            )
            if (generationTarget == GenerationTargetPlatform.JVM
                || generationTarget == GenerationTargetPlatform.ANDROID
                || generationTarget == GenerationTargetPlatform.MULTI_PLATFORM
            )
                property.addAnnotation(JvmField::class)

            if (commentLocale != null) {
                bundle.messageDataMap[commentLocale]?.messages?.get(key)?.let { text ->
                    property.addKdoc(text.replace("%", "%%") + "\n\n")
                }
            }
            if (params.isNotEmpty()) {
                property.addKdoc("Parameters: \n")
                for ((paramIndex, param) in params.entries.withIndex()) {
                    property.addKdoc("* p$paramIndex: ${param.key}")
                    if (!param.value.isNullOrBlank())
                        property.addKdoc(" : ${param.value}")

                    property.addKdoc("\n")
                }

            }

            messageObject.addProperty(property.build())
            index++
        }


        if (sourceCodeLanguages.isNotEmpty()) {
            messageObject.addInitializerBlock(
                sourceCodeLanguages.fold(CodeBlock.builder()) { initCode, messagesData ->
                    initCode.addStatement("registerTranslation(${bundle.name.name}_${messagesData.locale.toTag().replace("-","_")})")
                }.build()
            )
        }

        messageObject.addInitializerBlock(
            CodeBlock.builder()
                .addStatement(
                    "registerMessageBundleEntries("
                        + fieldNames.map { it.value }.joinToString(", ")
                        + ")"
                )
                .build()
        )

        return messageObject.build()
    }

    /**
     * generate the Kotlin source code file to store a translation in source
     * code
     */
    private fun generateLocalisationObject(messagesData: MessagesData): TypeSpec {
        val localeClassName = ClassName("de.comahe.i18n4k", "Locale")
        val forLocaleTag = MemberName("de.comahe.i18n4k", "forLocaleTag")

        val localisationObject =
            TypeSpec1.objectBuilder(bundle.name.name + "_" + messagesData.locale.toTag().replace("-","_"))
                .addModifiers(KModifier.PRIVATE)
                .addSuperinterface(MessagesProvider::class)
                .addKdoc("Translation of message bundle '${bundle.name.name}' for locale '${messagesData.locale.toTag()}'. Generated by i18n4k.")

        val arrayParameter = mutableListOf<String?>()
        val arrayText = StringBuilder("arrayOf(\n")
        var first = true;
        fieldNames.keys.forEach { key ->
            if (first)
                first = false
            else
                arrayText.append(",\n")

            arrayText.append("%S")
            arrayParameter.add(messagesData.messages[key])
        }
        arrayText.append(")")

        localisationObject.addProperty(
            PropertySpec.builder(
                name = "_data",
                type = Array<String?>::class.asClassName().parameterizedBy(
                    String::class.asTypeName().copy(nullable = true)
                ),
                modifiers = arrayOf(KModifier.PRIVATE)
            ).apply
            {
                initializer(arrayText.toString(), *arrayParameter.toTypedArray())
            }.build()
        )

        localisationObject.addProperty(
            PropertySpec.builder("locale", localeClassName, KModifier.OVERRIDE)
                .initializer("""%M("${messagesData.locale.toTag()}")""", forLocaleTag)
                .build()
        )

        localisationObject.addProperty(
            PropertySpec.builder("size", Int::class, KModifier.OVERRIDE)
                .getter(
                    FunSpec.getterBuilder().addCode("return _data.size").build()
                )
                .build()
        )

        localisationObject.addFunction(
            FunSpec.builder("get")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(
                    ParameterSpec.builder("index", Int::class)
                        .build()
                )
                .returns(String::class.asTypeName().copy(nullable = true))
                .addCode("return _data[index]")
                .build()
        )

        return localisationObject.build()
    }

    /** generate a I18nk4 language file to be loaded at runtime of the program */
    private fun generateLocalisationFile(messagesData: MessagesData) {
        languageFilesDir.mkdirs()

        val file = if (languageFilesDirAndroidRawResourceStyle) {
            File(
                languageFilesDir, convertCamelToSnakeCase(
                    bundle.name.packageName.replace(".", "_") + "_" +
                        bundle.name.name + "_" + messagesData.locale.toTag() + "_i18n4k.txt"
                )
            )
        } else {
            val dir = File(languageFilesDir, bundle.name.packageName.replace(".", "/"))
            dir.mkdirs()
            File(dir, bundle.name.name + "_" + messagesData.locale.toTag() + ".i18n4k.txt")
        }

        FileOutputStream(file).use { out ->
            OutputStreamWriter(out, StandardCharsets.UTF_8).use { writer ->
                generateLocalisationText(writer, messagesData)
            }
        }
    }

    /**
     * generate a I18nk4 language file content (appended to the
     * [Writer])
     */
    private fun generateLocalisationText(writer: Writer, messagesData: MessagesData) {
        writer.append(messagesData.locale.toTag()).append("\n^\n");
        fieldNames.keys.forEach { key ->
            val text = messagesData.messages[key]
            text?.lineSequence()?.forEach { line ->
                if (line.startsWith("^"))
                    writer.append("^")
                writer.append(line).append("\n")
            }
            writer.append("^\n")
        }
    }

    private fun convertCamelToSnakeCase(camelCase: String): String {
        val snakeCase = StringBuilder(camelCase.length * 2)
        var prevCharacter: Char? = null
        for (character in camelCase) {
            if (character.isUpperCase()) {
                if (prevCharacter != null && prevCharacter.isLowerCase())
                    snakeCase.append("_")
                snakeCase.append(character.lowercase())
            } else {
                snakeCase.append(character)
            }
            prevCharacter = character
        }
        return snakeCase.toString()
    }

    /** Converts the given value type names to [TypeName] */
    private fun convertValueTypeToTypeNames(valueClassNames: List<CharSequence?>): List<TypeName> {
        val result = mutableListOf<TypeName>()
        for (valueTypeName in valueClassNames) {
            if (valueTypeName == null) {
                result.add(Any::class.asTypeName())
                continue
            }
            @Suppress("NAME_SHADOWING")
            var valueTypeName = valueTypeName.toString()
            valueTypesMapping[valueTypeName]
                ?.let { valueTypeName = it }

            result.add(
                when (valueTypeName) {
                    "bool", "boolean", "Bool", "Boolean" -> Boolean::class.asTypeName()
                    "byte", "Byte" -> Byte::class.asTypeName()
                    "short", "Short" -> Short::class.asTypeName()
                    "int", "Int" -> Int::class.asTypeName()
                    "long", "Long" -> Long::class.asTypeName()
                    "float", "Float" -> Float::class.asTypeName()
                    "double", "Double" -> Double::class.asTypeName()
                    "number", "Number" -> Number::class.asTypeName()
                    "char", "Char" -> Char::class.asTypeName()
                    "string", "String" -> String::class.asTypeName()
                    "enum", "Enum" -> Enum::class.asTypeName().parameterizedBy(STAR)
                    "LocalizedAttributable" -> LocalizedAttributable::class.asTypeName()
                    "LocalizedString" -> LocalizedString::class.asTypeName()
                    else -> run {
                        val index = valueTypeName.lastIndexOf(".")

                        if (index > 0)
                            ClassName(valueTypeName.substring(0, index), valueTypeName.substring(index + 1))
                        else if (valueTypeName.isNotBlank())
                            ClassName("", valueTypeName)
                        else
                            Any::class.asTypeName()
                    }
                }
            )
        }
        return result
    }

    /** Check if all TypeNames are of type Any. True for empty collections. */
    private fun allTypeNamesAreAny(typeNames: Collection<TypeName>): Boolean {
        val anyTypeName = Any::class.asTypeName()
        for (typeName in typeNames) {
            if (typeName != anyTypeName)
                return false
        }
        return true
    }

    companion object {
        /**
         * Max count of parameters that can be used as a parameter list. For more a
         * [de.comahe.i18n4k.messages.formatter.MessageParameters] object must be used.
         */
        const val MAX_PARAMETER_LIST_COUNT = 10

        private fun isOnlySortedDigitsStartingAt0(parameters: List<String>): Boolean {
            if (parameters.size > MAX_PARAMETER_LIST_COUNT)
                return false

            var character = '0'

            for (param in parameters) {
                if (param.length != 1)
                    return false
                if (param[0] != character)
                    return false
                character++
            }
            return true
        }

        private fun isOnlySortedDigitsStartingAt1(parameters: List<String>): Boolean {
            if (parameters.size > MAX_PARAMETER_LIST_COUNT)
                return false

            var character = '1'
            val character10 = '9' + 1
            for (param in parameters) {
                if (param.length != 1) // only 10 allowed with more than 2 digits.
                    return (character10 == character && param == "10")
                if (param[0] != character)
                    return false
                character++
            }
            return true
        }
    }
}