package `fun`.adaptive.doc.example.dateInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.date.dateInput
import `fun`.adaptive.ui.input.date.dateInputBackend

/**
 * # Basic date input
 *
 * - Click on the input to show the popup.
 * - Pressing `Esc` closes the popup.
 * - `Cancel` resets the date to the value before the popup has been opened.
 * - `Ok` just closes the popup.
 *
 * The text is not editable, that's a can of worms I really don't want to open now.
 *
 * > Closing with `Esc` saves the value, I'm not sure if I like that. To change this
 * > and keep the field showing the picked value, `PopupSourceViewBackend` has to
 * > be extended to have a callback that notifies the input about closing.
 */
@Adaptive
fun dateInputExample(): AdaptiveFragment {

    dateInput(dateInputBackend())

    return fragment()
}