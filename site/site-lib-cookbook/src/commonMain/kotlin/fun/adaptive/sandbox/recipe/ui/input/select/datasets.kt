package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.cookbook.generated.resources.eco
import `fun`.adaptive.cookbook.generated.resources.zigbee
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.account_box
import `fun`.adaptive.utility.UUID.Companion.uuid7

val optionsOptions = listOf(
    uuid7<Any>() to "Option 1",
    uuid7<Any>() to "Option 2",
    uuid7<Any>() to "Option 3"
)

val networkOptions = listOf(
    "Zigbee" to Graphics.zigbee,
    "Modbus" to Graphics.account_box,
    "SPXB" to Graphics.eco
)

val roleOptions = listOf(
    uuid7<Any>() to "Content Manager",
    uuid7<Any>() to "Editor",
    uuid7<Any>() to "Viewer"
)