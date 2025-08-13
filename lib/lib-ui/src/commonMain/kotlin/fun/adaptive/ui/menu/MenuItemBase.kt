package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.runtime.application
import `fun`.adaptive.utility.UUID

abstract class MenuItemBase<T> : SelfObservable<MenuItemBase<T>>() {
    abstract val role : UUID<*>?

    open fun isVisible(fragment : AdaptiveFragment) : Boolean {
        return role?.let { fragment.application.hasRole(it) } ?: true
    }
}