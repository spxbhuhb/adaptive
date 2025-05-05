package `fun`.adaptive.sandbox.recipe.ui.dialog

import `fun`.adaptive.cookbook.generated.resources.check
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds

@Adaptive
fun independent(close: () -> Unit) {
    val time = poll(1.seconds) { now() } ?: now()

    var data = time.toString().replace("T", " ").replace("Z", " ").substringBeforeLast('.')

    @Independent
    var iData = data

    grid {
        size(700.dp, (288 + 64 + 3 * 16).dp)
        colTemplate(200.dp, 1.fr) .. rowTemplate(100.dp, 44.dp, 44.dp, 100.dp) .. padding { 32.dp }
        gap { 16.dp } .. alignItems.startCenter

        flowText(
            """
            This dialog shows how to avoid continuous data update with the use of @Independent.
            With it updating automatically, you cannot edit `data`.  
            `iData` on the other hand is fine as it is initialized once and then you can 
            edit it without any problems.
        """.trimIndent()
        ) .. colSpan(2) .. maxWidth .. height { 100.dp }

        text("Dependent data:")
        textInput(data) { data = it }

        text("Independent data:")
        textInput(iData) { iData = it }

        button("Save", Graphics.check) .. gridCol(2) .. alignSelf.endBottom .. onClick { close() }
    }

}