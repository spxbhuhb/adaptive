package `fun`.adaptive.ui.input.select

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.instruction.event.Keys
import `fun`.adaptive.ui.instruction.event.UIEvent
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

abstract class AbstractSelectInputViewBackend<VT, OT>(
    value: VT? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<VT, AbstractSelectInputViewBackend<VT, OT>>(
    value, label, isSecret
) {
    var options by observable(emptyList<OT>(), ::notify)
    var isMultiSelect by observable(false, ::notify)

    var toText by observable<(OT) -> String>({ it.toString() }, ::notify)
    var toIcon by observable<(OT) -> GraphicsResourceSet>({ Graphics.empty }, ::notify)

    var listInputTheme: SelectInputTheme = SelectInputTheme.default
    var withSurfaceContainer: Boolean = false

    var items by observable(listOf<SelectItem<VT, OT>>(), ::notify)

    val selectedItems = mutableSetOf<SelectItem<VT, OT>>()
    val selectedOptions = mutableSetOf<OT>()

    fun toggle(item: SelectItem<VT, OT>) {
        if (isDisabled) return

        val option = item.option
        val oldValue = selectedOptions.toSet()

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

        updateInputValue(oldValue)
    }

    abstract fun updateInputValue(oldValue: Set<OT>)

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

    fun optionContainerInstructions(item: SelectItem<VT, OT>): AdaptiveInstruction =
        if (item.isSelected) {
            listInputTheme.optionContainerSelected
        } else {
            listInputTheme.optionContainerBase
        }

    fun optionIconInstructions(item: SelectItem<VT, OT>): AdaptiveInstruction {
        return listInputTheme.optionIcon
    }

    fun optionTextInstructions(item: SelectItem<VT, OT>): AdaptiveInstruction {
        return listInputTheme.optionText
    }

    class SelectItem<VT, OT>(
        val viewBackend: AbstractSelectInputViewBackend<VT, OT>,
        option: OT,
        selected: Boolean = false
    ) : SelfObservable<SelectItem<VT, OT>>() {

        var option: OT by observable(option, ::notify)
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

        fun icon(): GraphicsResourceSet {
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

    fun onKeydown(event: UIEvent) {
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
                if (index == - 1 && items.isNotEmpty()) {
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