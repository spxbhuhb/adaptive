package `fun`.adaptive.runtime

abstract class AbstractWorkspace {

    abstract val application : AbstractApplication<*,*>?

    val contexts = mutableListOf<Any>()

}