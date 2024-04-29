/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin

import org.jetbrains.kotlin.compiler.plugin.*
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import java.io.File

@OptIn(ExperimentalCompilerApi::class)
class Z2CommandLineProcessor : CommandLineProcessor {

    override val pluginId = "z2"

    override val pluginOptions = listOf(
        OPTION_RESOURCE_DIR,
        OPTION_PLUGIN_DEBUG,
        OPTION_PLUGIN_LOG_DIR
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option) {
            OPTION_RESOURCE_DIR -> configuration.put(CONFIG_KEY_RESOURCE_DIR, value.toWritableDirectory())
            OPTION_PLUGIN_DEBUG -> configuration.put(CONFIG_KEY_PLUGIN_DEBUG, value.toBooleanStrictOrNull() ?: false)
            OPTION_PLUGIN_LOG_DIR -> configuration.put(CONFIG_KEY_PLUGIN_LOG_DIR, value.toWritableDirectory())
            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
        }
    }

    fun String.toWritableDirectory() : File =
        File(this).also { require(it.isDirectory && it.canWrite()) { "missing or non-writable directory: >$this<" } }

    companion object {

        // -------------------------------------------------------------------------------------------------
        // Resource directory
        // -------------------------------------------------------------------------------------------------

        const val OPTION_NAME_RESOURCE_DIR = "resource-dir"

        val CONFIG_KEY_RESOURCE_DIR = CompilerConfigurationKey.create<File>(OPTION_NAME_RESOURCE_DIR)

        val OPTION_RESOURCE_DIR = CliOption(
            OPTION_NAME_RESOURCE_DIR, "string", "Path to the directory to write generated resources into.",
            required = false, allowMultipleOccurrences = false
        )

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