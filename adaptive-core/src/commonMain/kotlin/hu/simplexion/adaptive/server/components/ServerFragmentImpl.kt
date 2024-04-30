/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.components

import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.utility.manualOrPlugin

interface ServerFragmentImpl {

    /**
     * Adapter of the adaptive server if the fragment is part of one. Replaced by
     * the plugin with a field initialized to `null`. Set by the server builder
     * functions such as `store`, `service` and `worker`.
     */
    var serverAdapter : AdaptiveServerAdapter?
        get() = manualOrPlugin("serverAdapter")
        set(value) = manualOrPlugin("serverAdapter", value)

    /**
     * Called when the fragment that contains this implementation is created by
     * Adaptive. Fragments are created in the order they are declared. In [create] you
     * have to be careful with dependencies as they may not present yet. If you
     * try to use a dependency retrieved by `store`, `service` or `worker` the call
     * might throw an exception.
     */
    fun create() = Unit

    /**
     * Called when the fragment that contains this implementation is mounted by
     * Adaptive. Fragments are mounted in the order they are declared after all
     * fragments of the current configuration are created. You can use
     * dependencies in mount, but it might be possible that the dependencies are not
     * mounted yet.
     */
    fun mount() = Unit

}