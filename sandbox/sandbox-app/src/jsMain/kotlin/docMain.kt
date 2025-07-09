/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.resource.document.DocumentResourceSet.Companion.inlineDocumentResource
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders

fun docMain() {
    column {
        maxHeight .. verticalScroll .. padding { 16.dp } .. width { 375.dp } .. gap { 16.dp }
        borders.friendly

        //docDocument(Documents.markdown_demo)
        //docDocument(remoteDocument("http://127.0.0.1:3000/resources/fun.adaptive.cookbook/documents/markdown_demo.md"))
        //docDocument(inlineDocument(".md", "# Header\n\nJust some inline markdown".encodeToByteArray()))

        docDocument(inlineDocumentResource(".md", source))
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