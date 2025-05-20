package `fun`.adaptive.foundation

interface LifecycleBound {
    fun patch(fragment : AdaptiveFragment, index : Int) = Unit
    fun mount(fragment : AdaptiveFragment, index : Int) = Unit
    fun unmount(fragment : AdaptiveFragment, index : Int) = Unit
    fun dispose(fragment : AdaptiveFragment, index : Int) = Unit
}