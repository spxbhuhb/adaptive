/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.*

class ContainerRenderData(
    val adapter : AbstractCommonAdapter<*,*>
) {
    var gapWidth : Double? = null
    var gapHeight : Double? = null

    var alignContent : AlignContent? = null
    var alignItems : AlignItems? = null
    var justifyContent : JustifyContent? = null
    var justifyItems : JustifyItems? = null
}