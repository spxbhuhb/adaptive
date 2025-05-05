package `fun`.adaptive.foundation

class FragmentTask(
    val declaringFragment : AdaptiveFragment,
    val taskFun : (fragment : AdaptiveFragment) -> Unit
) {
    fun execute() {
        taskFun(declaringFragment)
    }
}