package de.comahe.i18n4k.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.MessageBundle
import de.comahe.i18n4k.messages.providers.MessagesProvider
import de.comahe.i18n4k.strings.LocalizedString
import de.comahe.i18n4k.strings.LocalizedStringFactory1
import de.comahe.i18n4k.strings.LocalizedStringFactory2
import de.comahe.i18n4k.strings.LocalizedStringFactory3
import de.comahe.i18n4k.strings.LocalizedStringFactory4
import de.comahe.i18n4k.strings.LocalizedStringFactory5
import de.comahe.i18n4k.toTag
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass
import com.squareup.kotlinpoet.TypeSpec.Companion as TypeSpec1

/**
 *  Generates for one message bundle [MessagesDataBundle] the corresponding messages class
 *  (with the indexes for each key).
 *
 *  Additionally translations can be directly generates as class, so that they must not be
 *  loaded from an external file (see [sourceCodeLocales])
 */
class I18n4kGenerator(
    /** Directory where the generated Kotlin source code file should be stored */
    private val sourceDir: File,
    /** Directory where the language file with the list of string be stored */
    private val languageFilesDir: File,
    /** the message bundle the process */
    private val bundle: MessagesDataBundle,
    /** Locale which message bundle content should be added as comment. Null for no comments. */
    private val commentLocale: Locale?,
    /** for this Locales source code will be generated to have the translations in the
     * Kotlin code without the need to load external language file at runtime.
     * null for all languages, empty for no language. */
    private var sourceCodeLocales: List<Locale>?,
    /** The target platform for generation */
    private val generationTarget: GenerationTargetPlatform
) {

    /** sorted map
     * key: all keys that are present in the language files
     * value: the name of the field in the generated Kotlin file */
    val fieldNames = sortedMapOf<String, String>()

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
            .addKdoc("Massage constants for bundle '${bundle.name.name}'. Generated by i18n4k.")

        var index = 0

        val paramCountToClass: (Int) -> KClass<*> = {
            when (it) {
                0 -> LocalizedString::class
                1 -> LocalizedStringFactory1::class
                2 -> LocalizedStringFactory2::class
                3 -> LocalizedStringFactory3::class
                4 -> LocalizedStringFactory4::class
                5 -> LocalizedStringFactory5::class
                else -> throw IllegalArgumentException()
            }
        }

        fieldNames.forEach { (key, fieldName) ->
            val paramCount = bundle.getMaxParameterIndexForKey(key) + 1
            if (paramCount > 5)
                throw  IllegalArgumentException("The field '$key' has more than 5 parameters!")

            val property = PropertySpec.builder(fieldName, paramCountToClass(paramCount))
                .initializer(
                    when (paramCount) {
                        0 -> "getLocalizedString0($index)"
                        else -> "getLocalizedStringFactory$paramCount($index)"
                    }
                )
            if (generationTarget == GenerationTargetPlatform.JVM || generationTarget == GenerationTargetPlatform.MULTI_PLATFORM)
                property.addAnnotation(JvmStatic::class)

            if (commentLocale != null) {
                bundle.messageDataMap[commentLocale]?.messages?.get(key)?.let { text ->
                    property.addKdoc(text)
                }
            }

            messageObject.addProperty(property.build())
            index++
        }


        if (sourceCodeLanguages.isNotEmpty()) {
            messageObject.addInitializerBlock(
                sourceCodeLanguages.fold(CodeBlock.builder()) { initCode, messagesData ->
                    initCode.addStatement("registerTranslation(${bundle.name.name}_${messagesData.locale})")
                }.build()
            )
        }

        return messageObject.build()
    }

    /** generate the Kotlin source code file to store a translation in source code */
    private fun generateLocalisationObject(messagesData: MessagesData): TypeSpec {
        val localeClassName = ClassName("de.comahe.i18n4k", "Locale")

        val localisationObject =
            TypeSpec1.objectBuilder(bundle.name.name + "_" + messagesData.locale)
                .addModifiers(KModifier.PRIVATE)
                .addSuperinterface(MessagesProvider::class)
                .addKdoc("Translation of message bundle '${bundle.name.name}' for locale '${messagesData.locale}'. Generated by i18n4k.")

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
                initializer(arrayText.toString(),  *arrayParameter.toTypedArray())
                if (generationTarget == GenerationTargetPlatform.JVM || generationTarget == GenerationTargetPlatform.MULTI_PLATFORM)
                    addAnnotation(JvmStatic::class)
            }.build()
        )

        localisationObject.addProperty(
            PropertySpec.builder("locale", localeClassName, KModifier.OVERRIDE)
                .initializer("""%T("${messagesData.locale}")""", localeClassName)
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
        val dir = File(languageFilesDir, bundle.name.packageName.replace(".", "/"))
        dir.mkdirs()
        val file = File(dir, bundle.name.name + "_" + messagesData.locale+".i18n4k.txt")
        FileOutputStream(file).use { out ->
            OutputStreamWriter(out, StandardCharsets.UTF_8).use { writer ->
                generateLocalisationText(writer, messagesData)
            }
        }
    }

    /** generate a I18nk4 language file content (appended to the [Writer]) */
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
}