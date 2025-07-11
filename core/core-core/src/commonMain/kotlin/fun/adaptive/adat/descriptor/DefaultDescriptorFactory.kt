/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.descriptor.general.Hidden
import `fun`.adaptive.adat.descriptor.general.Readonly
import `fun`.adaptive.adat.descriptor.general.UseToString
import `fun`.adaptive.adat.descriptor.kotlin.bool.BooleanDefault
import `fun`.adaptive.adat.descriptor.kotlin.bool.BooleanValue
import `fun`.adaptive.adat.descriptor.kotlin.double.DoubleDefault
import `fun`.adaptive.adat.descriptor.kotlin.double.DoubleMaximum
import `fun`.adaptive.adat.descriptor.kotlin.double.DoubleMinimum
import `fun`.adaptive.adat.descriptor.kotlin.long.LongDefault
import `fun`.adaptive.adat.descriptor.kotlin.long.LongMaximum
import `fun`.adaptive.adat.descriptor.kotlin.long.LongMinimum
import `fun`.adaptive.adat.descriptor.kotlin.integer.IntDefault
import `fun`.adaptive.adat.descriptor.kotlin.integer.IntMaximum
import `fun`.adaptive.adat.descriptor.kotlin.integer.IntMinimum
import `fun`.adaptive.adat.descriptor.kotlin.string.*

object DefaultDescriptorFactory : DescriptorFactory() {
    init {
        add("BooleanDefault") { BooleanDefault(it, it.asBoolean()) }
        add("BooleanValue") { BooleanValue(it, it.asBoolean()) }

        add("DoubleMinimum") { DoubleMinimum(it, it.asDouble()) }
        add("DoubleMaximum") { DoubleMaximum(it, it.asDouble()) }
        add("DoubleDefault") { DoubleDefault(it, it.asDouble()) }
        
        add("IntMinimum") { IntMinimum(it, it.asInt()) }
        add("IntMaximum") { IntMaximum(it, it.asInt()) }
        add("IntDefault") { IntDefault(it, it.asInt()) }

        add("LongMinimum") { LongMinimum(it, it.asLong()) }
        add("LongMaximum") { LongMaximum(it, it.asLong()) }
        add("LongDefault") { LongDefault(it, it.asLong()) }

        add("StringBlank") { StringBlank(it, it.asBoolean()) }
        add("StringDefault") { StringDefault(it, it.parameters) }
        add("StringMinLength") { StringMinLength(it, it.asInt()) }
        add("StringMaxLength") { StringMaxLength(it, it.asInt()) }
        add("StringPattern") { StringPattern(it, it.parameters) }
        add("StringSecret") { StringSecret(it, it.asBoolean()) }

        add("Hidden") { Hidden(it, it.asBoolean()) }
        add("Readonly") { Readonly(it, it.asBoolean()) }
        add("UseToString") { UseToString(it, it.asBoolean()) }
    }
}