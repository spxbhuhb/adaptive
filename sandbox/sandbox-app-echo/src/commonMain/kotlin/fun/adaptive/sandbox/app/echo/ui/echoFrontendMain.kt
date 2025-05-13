package `fun`.adaptive.sandbox.app.echo.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.sandbox.app.echo.api.EchoApi
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import kotlinx.coroutines.launch

@Adaptive
fun echoFrontendMain(): AdaptiveFragment {
    var message = ""
    var response = ""

    grid {
        padding { 16.dp } .. gap { 16.dp }
        colTemplate(100.dp, 300.dp, 100.dp)
        rowTemplate(30.dp, 30.dp)

        text("Message:")

        input { v -> message = v }

        button("Send") {
            adapter().scope.launch { response = getService<EchoApi>(adapter().transport).send(message) }
        }

        text("Response:")

        text(response)
    }

    return fragment()
}

@Adaptive
fun input(change: (String) -> Unit) {
    singleLineTextInput("") { change(it) } ..
        border(color = color(0x0u), width = 1.dp)
}

@Adaptive
fun button(text: String, onClick: () -> Unit) {
    text(text) ..
        tabIndex { 0 } ..
        cursor.pointer ..
        textColor(color(0xffffffu)) ..
        backgroundColor { color(0x0000ffu) } ..
        cornerRadius { 4.dp } ..
        paddingVertical { 3.dp } ..
        paddingHorizontal { 16.dp } ..
        onClick { onClick() }
}