package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.instruction.event.Keys
import `fun`.adaptive.ui.instruction.event.UIEvent
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class SelectInputViewBackend<T>(
    value: T? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<T>(
    value, label, isSecret
) {
    var options by observable(emptyList<T>(), ::notify)
    var isMultiSelect by observable(false, ::notify)

    var toText by observable<(T) -> String>({ it.toString() }, ::notify)
    var toIcon by observable<(T) -> GraphicsResourceSet>({ Graphics.empty }, ::notify)

    var listInputTheme: SelectInputTheme = SelectInputTheme.default
    var withSurfaceContainer : Boolean = false

    var items by observable(listOf<SelectItem<T>>(), ::notify)

    val selectedItems = mutableSetOf<SelectItem<T>>()
    val selectedOptions = mutableSetOf<T>()

    init {
        if (value != null) {
            selectedOptions += value
        }
    }

    fun toggle(item: SelectItem<T>) {
        val option = item.option

        if (option in selectedOptions) {
            selectedOptions -= option
            selectedItems -= item
            item.isSelected = false
        } else {
            if (! isMultiSelect) {
                selectedItems.forEach { it.isSelected = false }
                selectedItems.clear()
                selectedOptions.clear()
            }
            selectedOptions += option
            selectedItems += item
            item.isSelected = true
        }
    }

    fun optionListContainerInstructions(focused : Boolean): AdaptiveInstruction =
        if (withSurfaceContainer) {
            containerThemeInstructions(focused) + (if (focused) listInputTheme.surfaceListContainerBaseFocused else listInputTheme.surfaceListContainerBase)
        } else {
            listInputTheme.listContainer
        }

    fun optionContainerInstructions(item: SelectItem<T>): AdaptiveInstruction =
        if (item.isSelected) {
            listInputTheme.optionContainerSelected
        } else {
            listInputTheme.optionContainerBase
        }

    fun optionIconInstructions(item: SelectItem<T>): AdaptiveInstruction {
        return listInputTheme.optionIcon
    }

    fun optionTextInstructions(item: SelectItem<T>): AdaptiveInstruction {
        return listInputTheme.optionText
    }

    class SelectItem<T>(
        val viewBackend: SelectInputViewBackend<T>,
        option: T,
        selected: Boolean = false
    ) : SelfObservable<SelectItem<T>>() {

        var option: T by observable(option, ::notify)
        var isSelected: Boolean by observable(selected, ::notify)

        fun optionContainerInstructions() = viewBackend.optionContainerInstructions(this)
        fun optionIconInstructions() = viewBackend.optionIconInstructions(this)
        fun optionTextInstructions() = viewBackend.optionTextInstructions(this)

        fun toggle() {
            viewBackend.toggle(this)
        }

        override fun toString(): String {
            return viewBackend.toText(option)
        }

        fun icon() : GraphicsResourceSet {
            return viewBackend.toIcon(option)
        }
    }

    override fun <PT> notify(property: KProperty<*>, oldValue: PT, newValue: PT) {
        if (property.name == ::options.name) {
            selectedItems.clear()
            items = options.map {
                SelectItem(this, it, selected = it in selectedOptions).also { item ->
                    if (item.isSelected) selectedItems += item
                }
            }
        }
        super.notify(property, oldValue, newValue)
    }

    fun onKeydown(event : UIEvent) {
        if (isMultiSelect) return // waaay too complex to handle right now
        val selected = selectedOptions.firstOrNull()
        val index = options.indexOf(selected)

        when (event.keyInfo?.key) {
            Keys.ARROW_UP -> {
                if (index > 0) {
                    toggle(items[index - 1])
                }
            }
            Keys.ARROW_DOWN -> {
                if (index == -1 && items.isNotEmpty()) {
                    toggle(items.first())
                    return
                }

                if (index < items.lastIndex) {
                    toggle(items[index + 1])
                }
            }
        }

    }
}