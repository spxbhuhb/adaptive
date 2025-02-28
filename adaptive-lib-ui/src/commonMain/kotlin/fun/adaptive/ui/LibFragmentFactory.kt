package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.misc.todo

object LibFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("lib:todo", ::todo)
    }
}