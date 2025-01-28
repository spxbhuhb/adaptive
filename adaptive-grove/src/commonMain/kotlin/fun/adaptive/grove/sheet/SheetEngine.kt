package `fun`.adaptive.grove.sheet

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.grove.sheet.operation.Redo
import `fun`.adaptive.grove.sheet.operation.SheetOperation
import `fun`.adaptive.grove.sheet.operation.Undo
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.Stack
import `fun`.adaptive.utility.popOrNull
import `fun`.adaptive.utility.push
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class SheetEngine(
    val trace: Boolean,
    override val binding: AdaptiveStateVariableBinding<SheetViewModel>
) : AdaptiveProducer<SheetViewModel> {

    val logger = getLogger("SheetEngine.${binding.targetFragment.id}")

    lateinit var scope: CoroutineScope
    val operations = Channel<SheetOperation>()

    override var latestValue: SheetViewModel?
        get() = current
        set(_) = throw UnsupportedOperationException()

    var current: SheetViewModel =
        SheetViewModel(this, emptyList(), SheetViewModel.emptySelection)

    val undoStack : Stack<SheetViewModel> = mutableListOf()
    val redoStack : Stack<SheetViewModel> = mutableListOf()

    override fun start() {
        CoroutineScope(binding.targetFragment.adapter.dispatcher).apply {
            scope = this
            if (trace) logger.enableFine()
            launch { main() }
        }
    }

    override fun stop() {
        operations.close()
        scope.cancel()
    }

    suspend fun main() {
        for (operation in operations) {
            if (trace) {
                logger.fine { "$operation" }
            }

            when (operation) {
                is Undo -> undo()
                is Redo -> redo()
                else -> op(operation)
            }
            setDirtyBatch()
        }
    }

    fun undo() {
        val last = undoStack.popOrNull() ?: return
        redoStack.push(last)
        current = last
    }

    fun redo() {
        val last = redoStack.popOrNull() ?: return
        undoStack.push(last)
        current = last
    }

    fun op(operation: SheetOperation) {
        undoStack.push(current)

        val new = SheetViewModel(this, current.fragments.value, current.selection.value)
        operation.applyTo(new)

        current = new
    }

    companion object {

        @Producer
        fun sheetEngine(
            trace : Boolean = false,
            binding: AdaptiveStateVariableBinding<SheetViewModel>? = null,
        ): SheetViewModel =

            SheetEngine(trace, checkNotNull(binding))
                .also { binding.targetFragment.addProducer(it) }
                .latestValue !!

    }

}