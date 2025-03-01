package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.richtext.richTextCodeFence
import `fun`.adaptive.ui.richtext.richTextParagraph
import `fun`.adaptive.ui.workspace.Workspace

object LibFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("lib:codefence", ::codeFence)
        add("lib:todo", ::todo)
        add("lib:richtext:codefence", ::richTextCodeFence)
        add("lib:richtext:paragraph", ::richTextParagraph)
    }
}