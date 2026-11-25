package de.comahe.i18n4k.toolchain.tasks

import org.slf4j.Marker
import org.slf4j.event.Level
import org.slf4j.helpers.LegacyAbstractLogger
import org.slf4j.helpers.MessageFormatter

/**
 * Minimal SLF4J logger that writes to standard output.
 *
 * The Kotlin Toolchain attributes standard output of a task to that task.
 * Standard error would be reported as an error, and adding a real SLF4J
 * provider just for the few INFO messages of the generator would add another
 * runtime dependency to this thin plugin.
 */
internal object PrintlnLogger : LegacyAbstractLogger() {

    override fun isTraceEnabled(): Boolean = false
    override fun isDebugEnabled(): Boolean = false
    override fun isInfoEnabled(): Boolean = true
    override fun isWarnEnabled(): Boolean = true
    override fun isErrorEnabled(): Boolean = true

    override fun getFullyQualifiedCallerName(): String? = null

    override fun handleNormalizedLoggingCall(
        level: Level,
        marker: Marker?,
        messagePattern: String?,
        arguments: Array<out Any?>?,
        throwable: Throwable?,
    ) {
        val message = when {
            messagePattern == null -> ""
            arguments == null -> messagePattern
            else -> MessageFormatter.basicArrayFormat(messagePattern, arguments)
        }
        println(message)
        throwable?.printStackTrace()
    }
}
