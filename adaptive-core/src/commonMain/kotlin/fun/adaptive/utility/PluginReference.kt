package `fun`.adaptive.utility

/**
 * Marks declarations that are referenced by the plugin and should be modified with checking
 * plugin related effects.
 *
 * @property  key  Helps the plugin to find functions and property symbols.
 */
internal annotation class PluginReference(val key : String = "")