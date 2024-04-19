/*
 * Copyright (C) 2020 Brian Norman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.simplexion.z2.gradle

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Settings for the Z2 Counter compiler plugin.
 */
open class Z2GradleExtension(objects: ObjectFactory) {
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
fun org.gradle.api.Project.z2(configure: Action<Z2GradleExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("z2", configure)
