package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.fragment.structural.AbstractPopup
import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.input.select.mapping.SelectInputMapping
import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.Keys
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.layout.Alignment
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

/**
 * @param   INPUT_VALUE_TYPE   Type of [inputValue], can be [ITEM_TYPE] or `Set<ITEM_TYPE>` (for multi-select)
 * @param   ITEM_TYPE          Type of one select item.
 * @param   OPTION_TYPE        Type of select options.
 */
abstract class AbstractSelectInputViewBackend<INPUT_VALUE_TYPE, ITEM_TYPE, OPTION_TYPE>(
    value: INPUT_VALUE_TYPE? = null,
    val optionToItemMapping: SelectOptionMapping<ITEM_TYPE, OPTION_TYPE>,
    val inputToItemMapping: SelectInputMapping<INPUT_VALUE_TYPE, ITEM_TYPE>,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<INPUT_VALUE_TYPE, AbstractSelectInputViewBackend<INPUT_VALUE_TYPE, ITEM_TYPE, OPTION_TYPE>>(
    value, label, isSecret
), PopupSourceViewBackend {

    var options by observable(emptyList<OPTION_TYPE>(), ::notify)
    var isMultiSelect by observable(false, ::notify)

    var toText by observable<(OPTION_TYPE) -> String>({ it.toString() }, ::notify)
    var toIcon by observable<(OPTION_TYPE) -> GraphicsResourceSet>({ Graphics.empty }, ::notify)

    var selectInputTheme: SelectInputTheme = SelectInputTheme.default
    var withSurfaceContainer: Boolean = false
    var withDropdown: Boolean = false

    // Changing `option` regenerates items. `items` does not have to be observable as
    // it changes only when `options` changes, so the notification from `options` is enough.
    var items = listOf<SelectItem>()
        private set

    val selectedItems = mutableSetOf<SelectItem>()
    val selectedValues = mutableSetOf<ITEM_TYPE>()

    /**
     * Dropdown open, initial list display and keyboard navigation uses [scrollAlignment]
     * to position the selected item correctly and with a good user experience.
     * When opening, the selected item should be centered, when using up/down keys, it
     * should be scrolled into view from top/bottom.
     */
    var scrollAlignment = Alignment.Center

    /**
     * When the user uses keyboard navigation, the hover background should be hidden.
     * When the mouse moves, the hover background should be shown.
     */
    var showHover by observable(true, ::notify)

    /**
     * When true, dropdowns show a filter field and filter options as the user types.
     */
    var filterable by observable(false, ::notify)

    /**
     * Filter text used to filter items.
     */
    var filter : String? = null

    /**
     * Function to convert the option into a text for filtering.
     */
    var toFilterText by observable<(OPTION_TYPE) -> String>({ it.toString() }, ::notify)

    val filterBackend = textInputBackend {
        onChange = {
            filter = it?.ifEmpty { null }
            mapToItems()
            notifyListeners()
        }
    }

    fun toggle(item: SelectItem, closeAfter: Boolean = true) {
        if (isDisabled) return

        val itemValue = item.itemValue
        val oldValue = inputValue

        if (itemValue in selectedValues) {
            if (isNullable || isMultiSelect) {
                selectedValues -= itemValue
                selectedItems -= item
                item.isSelected = false
            }
        } else {
            if (! isMultiSelect) {
                selectedItems.forEach { it.isSelected = false }
                selectedItems.clear()
                selectedValues.clear()
            }
            selectedValues += itemValue
            selectedItems += item
            item.isSelected = true
        }

        if (withDropdown && closeAfter && isPopupOpen) {
            hidePopup?.invoke()
        }

        if (!showHover) { // switch off hover when keyboard navigation is used
            items.forEach { if (it.isHovered) it.notifyListeners() }
        }

        updateInputValue(oldValue)
    }

    abstract fun updateInputValue(oldValue: INPUT_VALUE_TYPE?)

    fun optionListContainerInstructions(focused: Boolean): AdaptiveInstruction =
        if (withSurfaceContainer) {
            val base = containerThemeInstructions(focused)
            when {
                isDisabled -> base + selectInputTheme.surfaceListContainerDisabled
                focused -> base + selectInputTheme.surfaceListContainerBaseFocused
                else -> base + selectInputTheme.surfaceListContainerBase
            }
        } else {
            if (isDisabled) {
                emptyInstructions
            } else {
                selectInputTheme.listContainer
            }
        }

    fun optionContainerInstructions(item: SelectItem, hover: Boolean): AdaptiveInstruction =
        when {
            item.isSelected -> selectInputTheme.optionContainerSelected
            showHover && hover -> selectInputTheme.optionContainerHover
            else -> selectInputTheme.optionContainerBase
        }

    fun dropdownSelectedContainerInstructions(focused: Boolean): AdaptiveInstruction {
        return containerThemeInstructions(focused) + height { inputTheme.inputHeightDp } + paddingRight { 0.dp }
    }

    inner class SelectItem(
        option: OPTION_TYPE,
        val itemValue: ITEM_TYPE,
        selected: Boolean = false
    ) : SelfObservable<SelectItem>() {

        var option: OPTION_TYPE by observable(option, ::notify)
        var isSelected: Boolean by observable(selected, ::notify)

        /**
         * True when the pointer hovers over the item, false otherwise. Items set this
         * to support switching off the hover whenever keyboard navigation is used.
         */
        var isHovered: Boolean = false

        val scrollAlignment: Alignment
            get() = this@AbstractSelectInputViewBackend.scrollAlignment

        fun optionContainerInstructions(hover: Boolean) = optionContainerInstructions(this, hover)
        fun optionIconInstructions() = selectInputTheme.optionIcon
        fun optionTextInstructions() = selectInputTheme.optionText

        fun valueContainerInstructions() = selectInputTheme.valueContainer
        fun valueIconInstructions() = selectInputTheme.valueIcon
        fun valueTextInstructions() = selectInputTheme.valueText

        fun toggle() {
            this@AbstractSelectInputViewBackend.toggle(this)
        }

        override fun toString(): String {
            return toText(option)
        }

        fun icon(): GraphicsResourceSet {
            return toIcon(option)
        }
    }

    override fun <PT> notify(property: KProperty<*>, oldValue: PT, newValue: PT) {
        if (oldValue == newValue) return

        if (property.name == ::options.name) {
            selectedItems.clear()
            mapToItems()
        }

        if (property.name == ::isPopupOpen.name) {
            if (oldValue == false && newValue == true) scrollAlignment = Alignment.Center
        }

        super.notify(property, oldValue, newValue)
    }

    private fun mapToItems() {
        items = options.mapNotNull {
            val itemValue = optionToItemMapping.optionToValue(it)

            if (filterable && !filter.isNullOrEmpty() && !toFilterText(it).contains(filter!!, ignoreCase = true)) {
                return@mapNotNull null
            }

            SelectItem(it, itemValue, itemValue in selectedValues).also { item ->
                if (item.isSelected) selectedItems += item
            }
        }
    }

    fun onListKeydown(event: UIEvent, close: () -> Unit = { }) {
        if (isMultiSelect) return // waaay too complex to handle right now
        val selected = selectedValues.firstOrNull()
        val index = items.indexOfFirst { it.itemValue == selected }

        when (event.keyInfo?.key) {
            Keys.ARROW_UP -> {
                event.preventDefault()
                event.stopPropagation()

                if (items.isEmpty()) return

                scrollAlignment = Alignment.Start
                showHover = false

                if (index > 0) {
                    toggle(items[index - 1], closeAfter = false)
                } else {
                    toggle(items.last(), closeAfter = false)
                }

            }

            Keys.ARROW_DOWN -> {
                event.preventDefault()
                event.stopPropagation()

                scrollAlignment = Alignment.End
                showHover = false

                if (index == - 1 && items.isNotEmpty()) {
                    toggle(items.first(), closeAfter = false)
                    return
                }

                when {
                    index < items.lastIndex -> toggle(items[index + 1], closeAfter = false)
                    items.isNotEmpty() -> toggle(items.first(), closeAfter = false)
                }
            }

            Keys.ENTER, Keys.TAB -> {
                close()
            }
        }

    }

    fun onPointerMove() {
        if (! showHover) {
            showHover = true
        }
    }

    fun onDropdownSelectedKeydown(event: UIEvent) {
        when (event.keyInfo?.key) {
            Keys.ARROW_DOWN -> {
                event.preventDefault()
                event.fragment.first<AbstractPopup<*, *>>().show()
            }
        }
    }
}