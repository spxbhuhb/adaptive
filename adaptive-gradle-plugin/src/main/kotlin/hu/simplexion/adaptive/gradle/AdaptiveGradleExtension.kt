/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.gradle

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Settings for the Adaptive compiler plugin.
 */
open class AdaptiveGradleExtension(objects: ObjectFactory) {
    /**
     * ```
     * Category: Generated resources
     * ```
     *
     * Generated resources are put into this directory.
     * ```
     *
     * Default: `.`
     */
    val resourceDir: Property<Path> = objects.property(Path::class.java).also { it.set(Paths.get(".")) }

    /**
     * ```
     * Category: Plugin development/troubleshooting
     * ```
     *
     * When `true` the plugin generates output into [pluginLogDir]
     * ```
     *
     * Default: `false`
     */
    val pluginDebug: Property<Boolean> = objects.property(Boolean::class.java).also { it.set(false) }

    /**
     * ```
     * Category: Plugin development/troubleshooting
     * ```
     *
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
    val pluginLogDir: Property<Path> = objects.property(Path::class.java).also { it.set(Paths.get(".")) }
}

@Suppress("unused")
fun org.gradle.api.Project.adaptive(configure: Action<AdaptiveGradleExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("adaptive", configure)
