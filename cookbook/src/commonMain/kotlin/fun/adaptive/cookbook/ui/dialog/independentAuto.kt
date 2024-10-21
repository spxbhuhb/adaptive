package `fun`.adaptive.cookbook.ui.dialog

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.check
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.colSpan
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.gridCol
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds

@Adat
private data class Data(
    val s : String
)

private val sharedData = autoInstance(Data("Hello"))

@Adaptive
fun independentAuto(close: () -> Unit) {
    val autoData = autoInstance(sharedData) ?: sharedData.frontend.value

    @Independent
    var iData = copyStore { autoData }

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
        editor { autoData.s }

        text("Independent data:")
        editor { iData.s }

        button("Save", Res.drawable.check) .. gridCol(2) .. alignSelf.endBottom .. onClick {
            sharedData.update(sharedData.frontend.value.copy(s = iData.s))
            close()
        }
    }

}

@Adaptive
fun updateShared() {
    val time = poll(1.seconds) { now() } ?: now()
    val s = time.toString().replace("T", " ").replace("Z", " ").substringBeforeLast('.')
    sharedData.update(sharedData.frontend.value.copy(s = s))
}