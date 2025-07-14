package `fun`.adaptive.sandbox.recipe.ui.icon

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.icon.icon

/**
 * # Default icon
 *
 * - Default size is 24 x 24.
 * - Non-focusable.
 * - No change on hover.
 */
@Adaptive
fun iconDefaultExample(): AdaptiveFragment {

    icon(Graphics.account_circle)

    return fragment()
}
