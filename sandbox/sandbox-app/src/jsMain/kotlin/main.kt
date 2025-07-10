/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.BasicBrowserClientApplication.Companion.basicBrowserClient
import `fun`.adaptive.app.ws.SandBoxClientModule
import `fun`.adaptive.backend.backend
import `fun`.adaptive.chart.app.ChartModule
import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.lib.util.log.CollectedLogData
import `fun`.adaptive.lib.util.log.CollectedLogItem
import `fun`.adaptive.lib.util.log.CollectingLogger
import `fun`.adaptive.log.LoggerFactory
import `fun`.adaptive.log.defaultLoggerFactory
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.sandbox.CookbookFragmentFactory
import `fun`.adaptive.sandbox.app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.sandbox.recipe.ui.form.formBasicExample
import `fun`.adaptive.sandbox.recipe.ui.input.select.selectInputCheckboxExample
import `fun`.adaptive.sandbox.recipe.ui.input.select.selectInputDropdownExample
import `fun`.adaptive.sandbox.recipe.ui.input.select.selectInputIconAndTextExample
import `fun`.adaptive.sandbox.recipe.ui.input.select.selectInputPlayground
import `fun`.adaptive.sandbox.recipe.ui.input.select.selectInputTextExample
import `fun`.adaptive.sandbox.recipe.ui.input.text.textInputAreaExample
import `fun`.adaptive.sandbox.recipe.ui.input.text.textInputSimpleExample
import `fun`.adaptive.sandbox.recipe.ui.text.paragraphLongExample
import `fun`.adaptive.sandbox.recipe.ui.tree.treeBasicExample
import `fun`.adaptive.sandbox.recipe.ui.tree.treePlayground
import `fun`.adaptive.sandbox.recipe.ui.tree.treeValueExample
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphViewBackend
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.input.color.colorInput
import `fun`.adaptive.ui.input.color.colorInputBackend
import `fun`.adaptive.ui.input.datetime.dateTimeInputBackend
import `fun`.adaptive.ui.input.integer.intInput
import `fun`.adaptive.ui.input.integer.intInputBackend
import `fun`.adaptive.ui.input.long_.longInput
import `fun`.adaptive.ui.input.long_.longInputBackend
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.popup.modal.basicModal
import `fun`.adaptive.ui.popup.modal.dialog
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.value.app.ValueClientModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {
    //virtualizedMain()
    //basicAppMain()
    sandboxMain()
    // iotMain()
}

class Option(
    val text: String,
    val icon: GraphicsResourceSet
)

fun sandboxMain() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
//        cookbookCommon()
//        groveRuntimeCommon()
//        chartCommon()

        commonMainStringsStringStore0.load()
        `fun`.adaptive.cookbook.generated.resources.commonMainStringsStringStore0.load()

        val localBackend = backend {
//            auto()
//            worker { SnackbarManager() }
        }

        val collectedLogData = CollectedLogData()
        defaultLoggerFactory = LoggerFactory { CollectingLogger(it, collectedLogData) }

        val rootUiLogger = getLogger("UI root")

        try {
            browser(
                CanvasFragmentFactory,
                SvgFragmentFactory,
                LibFragmentFactory,
                CookbookFragmentFactory,
                backend = localBackend
            ) { adapter ->

                with(adapter.defaultTextRenderData) {
                    fontName = "Open Sans"
                    fontSize = 16.sp
                    fontWeight = 300
                }

                var feedBack: String = ""
                val backend = observe {
                    dateTimeInputBackend {
                        onChange = { feedBack = it.toString() }
                    }
                }

                column {
                    maxSize .. margin { 16.dp } .. padding { 16.dp } .. gap { 16.dp } .. verticalScroll .. backgrounds.friendlyOpaque

//                    doubleInput(doubleInputBackend(12.3) { unit = "min" }) .. width { 100.dp} .. backgrounds.surfaceVariant
//                    text("FeedBack: $feedBack")
//                    formBasicExample()
                    // quickFilterRecipe()
//                  markdown(
//                            """
//                                The paragraph has a list of styles (instance of [AdaptiveInstructionGroup](class://)), and
//                                each item selects one of those styles to use.
//                            """.trimIndent()
//                    )
                    //treePlayground()
//                    button("hello") {
//                        dialog(adapter(), "", ::modal)
//                    }
//
//                    modal("hello", { })


                    //selectInputDropdownExample()

                    markdown(
                        """
                            ## Hard-coded examples

                            [Select input dropdown example](actualize://cookbook/input/select/example/dropdown)

                            ---

                            ## Playground

                        """.trimIndent()
                    )
                }
            }
        } catch (ex: Exception) {
            rootUiLogger.error(ex)
        }
    }
}

@Adaptive
fun modal(
    data: String,
    close: UiClose
) {
    basicModal("stuff") {
        width { 600.dp }

        column {
            padding { 24.dp } .. gap { 16.dp } .. maxWidth

            markdown("""**Kulcs, csatorna, PAN és EPAN változtatásánál a korábban csatlakoztatott eszközök le fognak szakadni a hálózatról és mindegyiket újra kell csatlakozatni.**""".trimIndent())

            textInput(textInputBackend("hello") { label = "hello" })
            textInput(textInputBackend("hello") { label = "hello" })

            row {
                gap { 16.dp }
                intInput(intInputBackend(12) { label = "hello" }) .. width { 100.dp }
                intInput(intInputBackend(12) { label = "hello" }) .. width { 100.dp }
                longInput(longInputBackend(12) { label = "hello" }) .. width { 200.dp }
            }

            row {
                gap { 16.dp }
                intInput(intInputBackend(12) { label = "hello" }) .. width { 100.dp }
            }
        }

    }
}

@Adaptive
fun collectedLog(data: CollectedLogData) {
    val observed = observe { data }

    box {
        maxSize .. zIndex { 100000 } .. noPointerEvents
        column {
            enablePointerEvents
            height { 400.dp } .. verticalScroll .. alignSelf.bottom .. maxWidth

            submitButton("Clear") .. onClick { data.clear() }
            for (item in observed.items) {
                collectedLogItem(item)
            }
        }
    }

}


@Adaptive
fun collectedLogItem(item: CollectedLogItem) {

    val lines = item.message.split("\n")

    column {
        row {
            maxWidth .. gap { 16.dp } .. height { 22.dp }

            text(item.time)
            text(item.logger)
            text(item.level)
            if (lines.size < 2) text(item.message)
        }
        if (lines.size > 1) {
            for (line in lines) {
                text(line) .. paddingLeft { 40.dp }
            }
        }
        if (item.exception != null) {
            text(item.exception)
        }
    }

}