package `fun`.adaptive.grove.sheet.operation

/**
 * Operations return with an [OperationResult] to tell the controller how
 * to handle the undo stack.
 *
 * Some operations such as empty group/ungroup or move should not be put
 * on the stack to make undo/redo more natural.
 *
 * Some others such as resize/move should be merged if they are part of
 * one transform (pointer down, move around, pointer up).
 */
enum class OperationResult {
    PUSH, // put the operation on the top of the undo stack
    REPLACE, // replace the operation currently on top of the stack
    DROP // do not put this operation into the undo stack
}