/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.descriptor.kotlin.bool.BooleanDefault
import `fun`.adaptive.adat.descriptor.kotlin.bool.BooleanValue
import `fun`.adaptive.adat.descriptor.kotlin.integer.IntDefault
import `fun`.adaptive.adat.descriptor.kotlin.integer.IntMaximum
import `fun`.adaptive.adat.descriptor.kotlin.integer.IntMinimum
import `fun`.adaptive.adat.descriptor.kotlin.string.StringBlank
import `fun`.adaptive.adat.descriptor.kotlin.string.StringDefault
import `fun`.adaptive.adat.descriptor.kotlin.string.StringMaxLength
import `fun`.adaptive.adat.descriptor.kotlin.string.StringMinLength
import `fun`.adaptive.adat.descriptor.kotlin.string.StringPattern
import `fun`.adaptive.adat.descriptor.kotlin.string.StringSecret

object DefaultDescriptorFactory : DescriptorFactory() {
    init {
        add("BooleanDefault") { BooleanDefault(it.asBoolean()) }
        add("BooleanValue") { BooleanValue(it.asBoolean()) }

        add("IntDefault") { IntMinimum(it.asInt()) }
        add("IntMaximum") { IntMaximum(it.asInt()) }
        add("IntMinimum") { IntDefault(it.asInt()) }

        add("StringBlank") { StringBlank(it.asBoolean()) }
        add("StringDefault") { StringDefault(it.parameters) }
        add("StringMinLength") { StringMinLength(it.asInt()) }
        add("StringMaxLength") { StringMaxLength(it.asInt()) }
        add("StringPattern") { StringPattern(it.parameters) }
        add("StringSecret") { StringSecret(it.asBoolean()) }
    }
}