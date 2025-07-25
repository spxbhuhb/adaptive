package `fun`.adaptive.ui.input.duration

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.focus
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.input.decoratedInput
import `fun`.adaptive.ui.input.integer.intInput
import `fun`.adaptive.ui.input.integer.intInputBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.item.selectInputValueText
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputDropdown
import `fun`.adaptive.ui.instruction.dp
import kotlinx.datetime.DateTimeUnit

@Adaptive
fun durationInput(
    viewBackend: DurationInputViewBackend
): AdaptiveFragment {

    val observed = observe { viewBackend }

    val unitList = listOf(
        Strings.millisecond to DateTimeUnit.MILLISECOND,
        Strings.second to DateTimeUnit.SECOND,
        Strings.minute to DateTimeUnit.MINUTE,
        Strings.hour to DateTimeUnit.HOUR,
        Strings.day to DateTimeUnit.DAY,
        Strings.week to DateTimeUnit.WEEK
    )

    val amountAndUnit = observed.fromDuration()

    val amountBackend = observe {
        intInputBackend(amountAndUnit.first ?: 0) {
            onChange = { if (it != null) observed.setInputValue(it, amountAndUnit.second) }
        }
    }

    val unitBackend = observe {
        selectInputBackend {
            inputValue = unitList.find { it.second == amountAndUnit.second }
            options = unitList
            isNullable = false
            onChange = { if (it != null) observed.setInputValue(amountBackend.inputValue ?: 0, it.second) }
        }
    }

    val focus = focus()

    decoratedInput(focus, observed) {
        row(instructions()) {
            gap { 8.dp }

            intInput(amountBackend) .. width { 80.dp }

            selectInputDropdown(
                unitBackend,
                { v -> selectInputOptionText(v) { v.option.first } },
                { v -> selectInputValueText(v) { v.option.first } }
            ) .. width { 160.dp }
        }
    }

    return fragment()
}

