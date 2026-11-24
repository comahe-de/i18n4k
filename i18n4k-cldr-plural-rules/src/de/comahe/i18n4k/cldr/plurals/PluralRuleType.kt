// Copyright © 2020 xyzsd
//
// See the COPYRIGHT file at the top-level directory of this
// distribution.
//
// Licensed under the Apache License, Version 2.0 <LICENSE-APACHE or
// http://www.apache.org/licenses/LICENSE-2.0> or the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT>, at your
// option. This file may not be copied, modified, or distributed
// except according to those terms.

// converted to Kotlin MP by Marcel Heckel
package de.comahe.i18n4k.cldr.plurals

/**
 * Plural rule type based on CLDR.
 *
 *
 *
 * The CLDR "range" type is not yet supported.
 *
 */
enum class PluralRuleType {
    /** Cardinal numbers are natural numbers. (e.g., "5" : the set consists of 5 items)  */
    CARDINAL,

    /** Ordinal numbers are words representing position or rank in a sequence (e.g., "3" : the third item in a set)  */
    ORDINAL
}
