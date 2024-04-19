package hu.simplexion.z2.adaptive.worker

interface AdaptiveWorker {

    fun start()

    fun stop()

    /**
     * Checks if this worker replaces [other]. If so [other] will be unmounted and disposed before
     * this worker is created and mounted.
     */
    fun replaces(other : AdaptiveWorker) : Boolean
}