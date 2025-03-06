/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.canvas.api.fillText
import `fun`.adaptive.graphics.canvas.api.line
import `fun`.adaptive.graphics.canvas.api.path
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.graphics.canvas.path.ClosePath
import `fun`.adaptive.graphics.canvas.path.LineTo
import `fun`.adaptive.graphics.canvas.path.MoveTo
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.resource.document.DocumentResourceSet.Companion.inlineDocument
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.uiCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {
    //basicAppMain()
    main2()
}

@Adaptive
fun canvasMain() {
    box {
        size(402.dp, 402.dp) .. borders.outline

        canvas {
            fillText(40.0, 40.0, "Canvas") .. fill(0xff00ff)

            path(
                listOf(
                    MoveTo(10.0, 10.0),
                    LineTo(30.0, 30.0),
                    MoveTo(35.0, 35.0),
                    LineTo(5.0, 5.0),
                    ClosePath(5.0, 5.0, 35.0, 35.0)
                )
            ) .. stroke(0x0000ff)
        }
    }
}

fun main2() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        cookbookCommon()
        groveRuntimeCommon()

        commonMainStringsStringStore0.load()

        val localBackend = backend {
            auto()
            worker { SnackbarManager() }
        }

        browser(
            CanvasFragmentFactory,
            SvgFragmentFactory,
            LibFragmentFactory,
            backend = localBackend
        ) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            adapter.cookbookCommon()
            adapter.groveRuntimeCommon()

            //docMain()
            canvasMain()
        }
    }
}

fun docMain() {
    column {
        maxHeight .. verticalScroll .. padding { 16.dp } .. width { 375.dp } .. gap { 16.dp }
        borders.friendly

        //docDocument(Documents.markdown_demo)
        //docDocument(remoteDocument("http://127.0.0.1:3000/resources/fun.adaptive.cookbook/documents/markdown_demo.md"))
        //docDocument(inlineDocument(".md", "# Header\n\nJust some inline markdown".encodeToByteArray()))

        docDocument(inlineDocument(".md", source))
    }
}

val minSource = """
    Link in [Adaptive](https://adaptive.fun) text.
""".trimIndent()

val source = """
    # Header 1
    ## Header 2
    ### Header 3
    #### Header 4
    ##### Header 5
    ###### Header 6
    
    Some text **also bold** and _italic_ and some `code` and a [link](https://adaptive.fun).
        
    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt
    ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation
    ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in 
    reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur 
    sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id
    est laborum.
    
    [Good Morning](actualize://cookbook:recipe:goodmorning)
    
    ---
    
    * List item 1
      * List item 1.1
    * List item 2
    * List item 3
    
    1. List item 1
    2. List item 2
        1. List item 2.1
        2. List item 2.2
            1. List item 2.2.1
            2. Some text **also bold** and _italic_ and some `code` and a [link](https://adaptive.fun).
    3. List item 3
    
    [Standalone link](https://adaptive.fun)
    
    ```kotlin
    fun main() {
        println("Hello World!")
    }
    ```
    
    > One line quote.
    
    > Two lines of quote.
    > Second line.
    
    > Quote levels.
    > > Double quote.
    > > > Triple quote.
    
    ![An image](https://raw.githubusercontent.com/spxbhuhb/adaptive-site-resources/110801e15484cbe47db9396fc78827ab79408a82/images/deep-waters-50.jpg)
""".trimIndent().encodeToByteArray()