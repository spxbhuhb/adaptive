/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend.setting.provider

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.backend.setting.model.Setting
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.inputStream

/**
 * Get settings from a property file. Supports only [get], [put] throws an
 * exception.
 *
 * @property  path  The file system path to the property file.
 * @property  optional  When true a missing file is treated as empty
 */
class PropertyFileSettingProvider(
    val path: Path,
    val optional: Boolean
) : SettingProvider {

    val logger = getLogger("property.file")

    override val isReadOnly: Boolean
        get() = true

    val prop = Properties()

    init {
        val absPath = path.toAbsolutePath().normalize()

        if (absPath.toString().let { it.trim() != it }) {
            logger.warning("property file name ends with a space: >$absPath<")
        }

        if (! absPath.exists() && ! optional) {
            logger.fatal("mandatory property file does not exists: >$absPath<")
        }

        if (absPath.exists()) {
            logger.info("loading properties from: $absPath")
            absPath.inputStream().use {
                prop.load(InputStreamReader(it, StandardCharsets.UTF_8))
            }
        } else {
            logger.info("optional property file does not exists: $absPath")
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