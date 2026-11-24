package de.comahe.i18n4k.cldr.plurals

/**
 * Language-dependent plural forms, per CLDR specifications.
 */
enum class PluralCategory(val id: String) {
    ZERO("zero"),
    ONE("one"),
    TWO("two"),
    FEW("few"),
    MANY("many"),
    OTHER("other");
}