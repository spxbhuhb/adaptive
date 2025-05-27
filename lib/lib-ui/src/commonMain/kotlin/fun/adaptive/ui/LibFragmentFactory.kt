package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.popup.feedbackPopupContent

object LibFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("lib:feedback-popup-content", ::feedbackPopupContent)
        add("lib:codefence", ::codeFence)
        add("lib:todo", ::todo)
    }
}