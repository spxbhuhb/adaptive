package `fun`.adaptive.cookbook.recipe.ui.input.number

import `fun`.adaptive.cookbook.generated.resources.double_input
import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.document.ui.direct.hr
import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.document.Documents
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.number.doubleInput
import `fun`.adaptive.ui.input.number.doubleOrNullInput
import `fun`.adaptive.ui.input.number.doubleOrNullUnitInput
import `fun`.adaptive.ui.input.number.doubleUnitInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.withLabel

@Adaptive
fun doubleInputRecipe(): AdaptiveFragment {
    var value: Double? = 12.3
    var safeValue = 12.3

    column {
        maxHeight .. verticalScroll .. padding { 16.dp } .. gap { 16.dp }

        docDocument(Documents.double_input)

        h2("No unit variants")

        text("Nullable value: ${value ?: "null"}")
        text("Non-nullable value: $safeValue")

        withLabel("Zero decimals - nullable") { state ->
            doubleOrNullInput(value, decimals = 0, state) { value = it } .. width { 200.dp }
        }

        withLabel("1 decimal - nullable") { state ->
            doubleOrNullInput(value, decimals = 1, state) { value = it } .. width { 200.dp }
        }

        withLabel("2 decimals - nullable") { state ->
            doubleOrNullInput(value, decimals = 2, state = state) { value = it } .. width { 200.dp }
        }

        withLabel("8 decimals - nullable") { state ->
            doubleOrNullInput(value, decimals = 8, state = state) { value = it } .. width { 200.dp }
        }

        withLabel("2 decimals - non nullable") { state ->
            doubleInput(safeValue, decimals = 2, state = state) { safeValue = it } .. width { 200.dp }
        }

        hr()

        markdown(
            """
                ## Unit variants    
                All fields are nullable with 2 decimals.
            """.trimIndent()
        )

        withLabel("200 dp width") { state ->
            doubleOrNullUnitInput(value, decimals = 2, unit = "m²", state = state) { value = it } .. width { 200.dp }
        }

        withLabel("long unit name, 200 dp width") { state ->
            doubleOrNullUnitInput(value, decimals = 2, unit = "kg·m⁻²·s⁻¹", state = state) { value = it } .. width { 200.dp }
        }

        withLabel("no width specified") { state ->
            doubleUnitInput(safeValue, decimals = 2, unit = "m²", state = state) { value = it }
        }

    }

    return fragment()
}