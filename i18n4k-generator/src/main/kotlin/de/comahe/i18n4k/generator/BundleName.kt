package de.comahe.i18n4k.generator

/** Name of a message bundle */
data class BundleName(
    /** name of the package */
    val packageName: String,
    /** name of the file */
    val name: String
) {
    override fun toString(): String {
        if (packageName.isEmpty())
            return name
        return "$packageName.$name"
    }
}
