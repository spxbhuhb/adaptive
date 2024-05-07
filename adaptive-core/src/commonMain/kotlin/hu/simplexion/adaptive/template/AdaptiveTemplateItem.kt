/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.template

import hu.simplexion.adaptive.utility.UUID

class AdaptiveTemplateItem(
    val uuid : UUID<AdaptiveTemplateItem>,
    val bindings : AdaptiveTemplateBinding,
    val template : UUID<AdaptiveTemplate>,
    val impl : String // qualified name of an actual AdaptiveFragment
)