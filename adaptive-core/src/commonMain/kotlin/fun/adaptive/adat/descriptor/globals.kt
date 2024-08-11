/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.descriptor

import `fun`.adaptive.adat.descriptor.constraint.IntMaximum
import `fun`.adaptive.adat.descriptor.constraint.IntMinimum
import `fun`.adaptive.adat.descriptor.info.IntDefault

object DefaultDescriptorFactory : DescriptorFactory() {
    init {
        add("int:minimum") { pm, dm -> IntMinimum(pm, dm) }
        add("int:maximum") { pm, dm -> IntMaximum(pm, dm) }
        add("int:default") { pm, dm -> IntDefault(pm, dm) }
    }
}