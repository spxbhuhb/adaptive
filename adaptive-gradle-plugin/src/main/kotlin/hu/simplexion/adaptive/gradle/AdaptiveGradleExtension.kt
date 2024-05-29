/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.gradle

import hu.simplexion.adaptive.gradle.resources.ResourcesExtension
import org.gradle.api.Action
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Settings for the Adaptive compiler plugin.
 */
open class AdaptiveGradleExtension {

    /**
     * When `true` the plugin generates output into [pluginLogDir]
     *
     * Default: `false`
     */
    var pluginDebug: Boolean = false

    /**
     * When [pluginDebug] is true the plugin saves logs into this directory. Each run creates a file named
     * "rui-log-yyyyMMdd-HHmmss.txt" in this directory. This is mostly useful during the development of the
     * plugin itself and/or troubleshooting.
     *
     * Generates large amount of data, be careful with it.
     *
     * Relative paths save the data into the gradle daemons log directory. On my machine it is:
     *
     * `/Users/<username>/Library/Application Support/kotlin/daemon`
     */
    var pluginLogDir: Path = Paths.get(".")

    /**
     * Configuration of resource handling.
     */
    val resources: ResourcesExtension = ResourcesExtension()

    fun resources(action: ResourcesExtension.() -> Unit) {
        resources.action()
    }
}

@Suppress("unused")
fun org.gradle.api.Project.adaptive(configure: Action<AdaptiveGradleExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("adaptive", configure)
