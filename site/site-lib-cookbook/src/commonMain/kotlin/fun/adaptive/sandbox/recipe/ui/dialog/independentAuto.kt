package `fun`.adaptive.sandbox.recipe.ui.dialog

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.cookbook.generated.resources.check
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds

@Adat
private class Data(
    val s : String
)

private val sharedData = storeFor { Data("Hello") }

@Adaptive
fun independentAuto(close: () -> Unit) {
    val autoData = valueFrom { sharedData }

    @Independent
    var iData = copyOf { autoData }

    updateShared()

    grid {
        size(700.dp, (288 + 64 + 3 * 16).dp)
        colTemplate(200.dp, 1.fr) .. rowTemplate(120.dp, 44.dp, 44.dp, 100.dp) .. padding { 32.dp }
        gap { 16.dp } .. alignItems.startCenter

        flowText(
            """
            This dialog shows how to avoid continuous data update with the use of @Independent.
            With it updating automatically, you cannot edit `data`.
            `iData` on the other hand is fine as it is initialized once and then you can 
            edit it without any problems. If you close the dialog with the X, the next open
            will show the time of close, if you close it with "Save", the next open shows the value.
        """.trimIndent()
        ) .. colSpan(2) .. maxWidth .. height { 120.dp }

        text("Dependent data:")
        textInput(autoData.s) { }

        text("Independent data:")
        textInput(iData.s) { iData = iData.copy(s = it) }

        button("Save", Graphics.check) .. gridCol(2) .. alignSelf.endBottom .. onClick {
            sharedData.value = iData
            close()
        }
    }

}

@Adaptive
fun updateShared() {
    val time = poll(1.seconds) { now() } ?: now()
    val s = time.toString().replace("T", " ").replace("Z", " ").substringBeforeLast('.')
    sharedData.value = sharedData.value.copy(s = s)
}