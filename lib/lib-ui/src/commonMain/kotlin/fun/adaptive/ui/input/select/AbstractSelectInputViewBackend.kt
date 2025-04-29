package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.fragment.structural.AbstractPopup
import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping
import `fun`.adaptive.ui.instruction.event.Keys
import `fun`.adaptive.ui.instruction.event.UIEvent
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

abstract class AbstractSelectInputViewBackend<SVT, IVT, OT>(
    value: SVT? = null,
    val mapping: SelectOptionMapping<IVT, OT>,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<SVT, AbstractSelectInputViewBackend<SVT, IVT, OT>>(
    value, label, isSecret
), PopupSourceViewBackend {

    var options by observable(emptyList<OT>(), ::notify)
    var isMultiSelect by observable(false, ::notify)

    var toText by observable<(OT) -> String>({ it.toString() }, ::notify)
    var toIcon by observable<(OT) -> GraphicsResourceSet>({ Graphics.empty }, ::notify)

    var listInputTheme: SelectInputTheme = SelectInputTheme.default
    var withSurfaceContainer: Boolean = false
    var withDropDown: Boolean = false

    var items by observable(listOf<SelectItem>(), ::notify)

    val selectedItems = mutableSetOf<SelectItem>()
    val selectedValues = mutableSetOf<IVT>()

    fun toggle(item: SelectItem, closeAfter : Boolean = true) {
        if (isDisabled) return

        val itemValue = item.itemValue
        val oldValue = selectedItems.map { it.itemValue }.toSet()

        if (itemValue in selectedValues) {
            if (isNullable) {
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

        if (withDropDown && closeAfter && isPopupOpen) {
            hidePopup?.invoke()
        }

        updateInputValue(oldValue)
    }

    abstract fun updateInputValue(oldValue: Set<IVT>)

    fun optionListContainerInstructions(focused: Boolean): AdaptiveInstruction =
        if (withSurfaceContainer) {
            val base = containerThemeInstructions(focused)
            when {
                isDisabled -> base + listInputTheme.surfaceListContainerDisabled
                focused -> base + listInputTheme.surfaceListContainerBaseFocused
                else -> base + listInputTheme.surfaceListContainerBase
            }
        } else {
            if (isDisabled) {
                emptyInstructions
            } else {
                listInputTheme.listContainer
            }
        }

    fun optionContainerInstructions(item: SelectItem): AdaptiveInstruction =
        if (item.isSelected) {
            listInputTheme.optionContainerSelected
        } else {
            listInputTheme.optionContainerBase
        }

    fun optionIconInstructions(item: SelectItem): AdaptiveInstruction {
        return listInputTheme.optionIcon
    }

    fun optionTextInstructions(item: SelectItem): AdaptiveInstruction {
        return listInputTheme.optionText
    }

    fun dropdownSelectedContainerInstructions(focused: Boolean): AdaptiveInstruction {
        return containerThemeInstructions(focused) + height { inputTheme.inputHeightDp }
    }

    inner class SelectItem(
        option: OT,
        val itemValue: IVT,
        selected: Boolean = false
    ) : SelfObservable<SelectItem>() {

        var option: OT by observable(option, ::notify)
        var isSelected: Boolean by observable(selected, ::notify)

        fun optionContainerInstructions() = optionContainerInstructions(this)
        fun optionIconInstructions() = optionIconInstructions(this)
        fun optionTextInstructions() = optionTextInstructions(this)

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
        if (property.name == ::options.name) {
            selectedItems.clear()
            items = options.map {
                val itemValue = mapping.optionToValue(it)
                SelectItem(it, itemValue, itemValue in selectedValues).also { item ->
                    if (item.isSelected) selectedItems += item
                }
            }
        }
        super.notify(property, oldValue, newValue)
    }

    fun onListKeydown(event: UIEvent, close: () -> Unit = {  }) {
        if (isMultiSelect) return // waaay too complex to handle right now
        val selected = selectedValues.firstOrNull()
        val index = items.indexOfFirst { it.itemValue == selected }

        when (event.keyInfo?.key) {
            Keys.ARROW_UP -> {
                event.preventDefault()

                if (items.isEmpty()) return

                if (index > 0) {
                    toggle(items[index - 1], closeAfter = false)
                } else {
                    toggle(items.last(), closeAfter = false)
                }
            }

            Keys.ARROW_DOWN -> {
                event.preventDefault()

                if (index == - 1 && items.isNotEmpty()) {
                    toggle(items.first(), closeAfter = false)
                    return
                }

                if (index < items.lastIndex) {
                    toggle(items[index + 1], closeAfter = false)
                } else {
                    toggle(items.first(), closeAfter = false)
                }
            }

            Keys.ENTER, Keys.TAB -> {
                close()
            }
        }

    }

    fun onDropdownSelectedKeydown(event: UIEvent) {
        when (event.keyInfo?.key) {
            Keys.ARROW_DOWN -> {
                event.preventDefault()
                event.fragment.first<AbstractPopup<*,*>>().show()
            }
        }
    }
}