/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.template

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.utility.UUID

@Adat
class AdaptiveTemplateVariable(
    val name: String,
    val index: Int,
    val signature: String,
    val json : String
)

@Adat
class AdaptiveTemplateMapping(
    val sourceVariableIndex: Int,
    val targetVariableIndex: Int
)

@Adat
class AdaptiveTemplateFragment(
    val impl: String,
    val index: Int,
    val mapping: List<AdaptiveTemplateMapping>
)

@Adat
class AdaptiveTemplateData(
    val uuid: UUID<AdaptiveTemplateData>,
    val name: String,
    val variables: Map<Int, AdaptiveTemplateVariable>,
    val fragments: Map<Int, AdaptiveTemplateFragment>
)

enum class AdaptiveTemplateVariableKind {
    External,
    Fix,
    Choice,
    Calculated
}