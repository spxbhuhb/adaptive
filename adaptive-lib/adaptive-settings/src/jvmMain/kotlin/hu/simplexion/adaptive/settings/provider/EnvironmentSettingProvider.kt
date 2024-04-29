package hu.simplexion.adaptive.settings.provider

import hu.simplexion.adaptive.settings.model.Setting

/**
 * Get settings from environment variables. Supports only [get], [put] throws an
 * exception.
 *
 * @property environmentVariablePrefix A string to add to property names as a prefix.
 */
class EnvironmentSettingProvider(
    val environmentVariablePrefix: String
) : SettingProvider {

    override val isReadOnly: Boolean
        get() = true

    override fun put(path: String, value: String?) {
        throw NotImplementedError("environment variable settings are read-only")
    }

    override fun get(path: String, children: Boolean): List<Setting> {
        val value = System.getenv("$environmentVariablePrefix$this")

        return if (value == null) {
            emptyList()
        } else {
            listOf(Setting(path, value))
        }
    }

}