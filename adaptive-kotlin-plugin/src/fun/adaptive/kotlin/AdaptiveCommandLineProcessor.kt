/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin

import org.jetbrains.kotlin.compiler.plugin.*
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import java.io.File

@OptIn(ExperimentalCompilerApi::class)
class AdaptiveCommandLineProcessor : CommandLineProcessor {

    override val pluginId = "adaptive"

    override val pluginOptions = listOf(
        OPTION_PLUGIN_DEBUG,
        OPTION_DEBUG_FILTER,
        OPTION_PLUGIN_LOG_DIR
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option) {
            OPTION_PLUGIN_DEBUG -> configuration.put(CONFIG_KEY_PLUGIN_DEBUG, value.toBooleanStrictOrNull() == true)
            OPTION_DEBUG_FILTER -> configuration.put(CONFIG_KEY_DEBUG_FILTER, value)
            OPTION_PLUGIN_LOG_DIR -> configuration.put(CONFIG_KEY_PLUGIN_LOG_DIR, value.toWritableDirectory())
            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
        }
    }

    fun String.toWritableDirectory() : File =
        File(this).also {
            // FIXME I'm not sure I want to make the plugin debug directory if it does not exists, figure it out
            if (!it.exists()) {
                it.mkdirs()
            } else {
                require(it.isDirectory && it.canWrite()) { "missing or non-writable directory: >$this<" }
            }
        }

    companion object {

        // -------------------------------------------------------------------------------------------------
        // Plugin debug
        // -------------------------------------------------------------------------------------------------

        const val OPTION_NAME_PLUGIN_DEBUG = "plugin-debug"

        val CONFIG_KEY_PLUGIN_DEBUG = CompilerConfigurationKey.create<Boolean>(OPTION_NAME_PLUGIN_DEBUG)

        val OPTION_PLUGIN_DEBUG = CliOption(
            OPTION_NAME_PLUGIN_DEBUG, "plugin-debug", "Output plugin debug information.",
            required = false, allowMultipleOccurrences = false
        )

        // -------------------------------------------------------------------------------------------------
        // Debug filter
        // -------------------------------------------------------------------------------------------------

        const val OPTION_NAME_DEBUG_FILTER = "debug-filter"

        val CONFIG_KEY_DEBUG_FILTER = CompilerConfigurationKey.create<String>(OPTION_NAME_DEBUG_FILTER)

        val OPTION_DEBUG_FILTER = CliOption(
            OPTION_NAME_DEBUG_FILTER, "string", "Filter debug information by element name.",
            required = false, allowMultipleOccurrences = false
        )

        // -------------------------------------------------------------------------------------------------
        // Plugin log directory
        // -------------------------------------------------------------------------------------------------

        const val OPTION_NAME_PLUGIN_LOG_DIR = "plugin-log-dir"

        val CONFIG_KEY_PLUGIN_LOG_DIR = CompilerConfigurationKey.create<File>(OPTION_NAME_PLUGIN_LOG_DIR)

        val OPTION_PLUGIN_LOG_DIR = CliOption(
            OPTION_NAME_PLUGIN_LOG_DIR, "string", "Save plugin log output into a file in this directory.",
            required = false, allowMultipleOccurrences = false
        )
    }
}