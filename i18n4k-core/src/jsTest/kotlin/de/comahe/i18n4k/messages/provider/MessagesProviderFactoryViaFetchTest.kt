package de.comahe.i18n4k.messages.provider

import da.comahe.i18n4k.MessageTest1
import de.comahe.i18n4k.config.I18n4kConfigImmutable
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.i18n4k
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class MessagesProviderFactoryViaFetchTest {


    /**
     * Ignore: Resource loading is not working in tests!
     *
     * https://youtrack.jetbrains.com/issue/KT-42923
     */
    @Ignore()
    @Test
    fun testLoading() = runTest {
        i18n4k = I18n4kConfigImmutable().withLocale(forLocaleTag("fr"))

        suspendCoroutine { cont ->
            MessageTest1.registerTranslationFactory(
                MessagesProviderFactoryViaFetch(
                    pathToResource = "MessagesTest1_fr.i18n4k.txt",
                    onLoaded = {
                        println("### loaded!!!")
                        cont.resume(true)
                    },
                    onFailed = { error ->
                        println("### error: ${error.message}")
                        cont.resumeWithException(error)
                    }
                )
            )
        }

        assertEquals("Bonjour World !", MessageTest1.HELLO_X1("World"))
    }

}