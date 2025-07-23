/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend.builtin

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.BackendFragment
import `fun`.adaptive.foundation.query.single
import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.utility.manualOrPlugin

abstract class BackendFragmentImpl {

    /**
     * Set by the backend builder functions such as `store`, `service` and `worker`.
     */
    open var fragment: BackendFragment? = null
    /**
     * Adapter of the adaptive backend if the fragment is part of one.
     */
    open val adapter: BackendAdapter?
        get() = fragment?.adapter as? BackendAdapter

    /**
     * Adapter of the adaptive backend.
     *
     * @throws IllegalStateException The implementation is not part of an adaptive backend.
     */
    open val safeAdapter: BackendAdapter
        get() = checkNotNull(adapter) { "this implementation is not part of an adaptive backend" }

    /**
     * A logger that belongs to the given fragment if it is part of an adaptive
     * backend. Replaced by the plugin with a field initialized to
     * `adapter.getLogger(classFqName)`.
     */
    open var logger: AdaptiveLogger
        get() = manualOrPlugin("logger")
        set(value) = manualOrPlugin("logger", value)

    /**
     * Called when the fragment that contains this implementation is created by
     * Adaptive. Fragments are created in the order they are declared. In [create] you
     * have to be careful with dependencies as they may not present yet. If you
     * try to use a dependency retrieved by `store`, `service` or `worker` the call
     * might throw an exception.
     */
    open fun create() = Unit

    /**
     * Called when the fragment that contains this implementation is mounted by
     * Adaptive. Fragments are mounted in the order they are declared after all
     * fragments of the current configuration are created. You can use
     * dependencies in mount, but it might be possible that the dependencies are not
     * mounted yet.
     */
    open fun mount() = Unit

    /**
     * Called when the fragment that contains this implementation is unmounted by
     * Adaptive. Fragments are unmounted in the reverse order they are declared. You
     * can use dependencies in unmount, but it might be possible that the dependencies
     * are already unmounted.
     */
    open fun unmount() = Unit

    /**
     * Finds the worker of the given type [T].
     *
     * Throws exception when:
     *
     * - the fragment is not part of an adaptive backend
     * - there is no worker of the given type
     * - there is more than one worker of the given type
     */
    inline fun <reified T> workerImpl(crossinline filterFun: (T) -> Boolean = { true }): Lazy<T> =
        lazy {
            checkNotNull(adapter) { "this implementation is not part of an adaptive backend" }
                .single { f -> f is BackendFragment && f.impl.let { it is T && filterFun(it) } }
                .let { (it as BackendFragment) }
                .impl as T
        }
}