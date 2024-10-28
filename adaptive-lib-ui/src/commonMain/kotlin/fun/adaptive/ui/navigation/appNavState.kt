package `fun`.adaptive.ui.navigation

import `fun`.adaptive.auto.api.autoItem

/**
 * Nav state of the whole application. Changes update navigation UI elements
 * automatically.
 */
val appNavState = autoItem(NavState())