/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.template

import hu.simplexion.adaptive.adat.metadata.AdatClassMetaData
import hu.simplexion.adaptive.utility.UUID

class AdaptiveTemplate(
    val name : String,
    val uuid : UUID<AdaptiveTemplate>,
    val metaData : AdatClassMetaData<*>,
    val defaultState : ByteArray,
    val items : AdaptiveTemplateItem
)