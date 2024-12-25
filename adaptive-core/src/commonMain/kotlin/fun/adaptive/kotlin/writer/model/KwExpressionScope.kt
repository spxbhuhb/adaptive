package `fun`.adaptive.kotlin.writer.model

/**
 * This scope is used to differentiate between container functions and expression
 * functions such as `kwCall`.
 *
 * When used in a container scope we have to add the element to the statements of the
 * container.
 *
 * When used in expression scope we should not add the element to the statements
 */
interface KwExpressionScope