package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.misc.todo

object LibFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("lib:codefence", ::codeFence)
        add("lib:todo", ::todo)
    }
}