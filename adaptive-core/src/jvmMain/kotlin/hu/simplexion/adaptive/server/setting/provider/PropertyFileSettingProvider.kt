/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.setting.provider

import hu.simplexion.adaptive.server.setting.model.Setting
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.inputStream

/**
 * Get settings from a property file. Supports only [get], [put] throws an
 * exception. Must be security officer or internal call.
 *
 * @property  path  The file system path to the property file.
 * @property  optional  When true a missing file is treated as empty
 * @property  owner  The owner of the settings in this property file.
 */
class PropertyFileSettingProvider(
    val path: Path,
    val optional: Boolean
) : SettingProvider {

    override val isReadOnly: Boolean
        get() = true

    val prop = Properties()

    init {
        prop.clear()
        if (path.exists() || ! optional) {
            path.inputStream().use {
                prop.load(InputStreamReader(it, StandardCharsets.UTF_8))
            }
        }
    }

    override fun put(path: String, value: String?) {
        throw NotImplementedError("environment variable settings are read-only")
    }

    override fun get(path: String, children: Boolean): List<Setting> {
        val value = prop.getProperty(path)

        return if (value != null) {
            listOf(Setting(path, value))
        } else {
            emptyList()
        }
    }

}