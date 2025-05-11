/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json.elements

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.json.formatting.JsonFormat
import `fun`.adaptive.wireformat.json.visitor.JsonTransformer
import `fun`.adaptive.wireformat.json.visitor.JsonVisitor

abstract class JsonElement {

    open val asUnit: Unit
        get() = throw IllegalStateException("JsonElement.asUnit")

    open val asBoolean: Boolean
        get() = throw IllegalStateException("JsonElement.asBoolean")

    open val asInt: Int
        get() = throw IllegalStateException("JsonElement.asInt")

    open val asShort: Short
        get() = throw IllegalStateException("JsonElement.asShort")

    open val asByte: Byte
        get() = throw IllegalStateException("JsonElement.asByte")

    open val asLong: Long
        get() = throw IllegalStateException("JsonElement.asLong")

    open val asFloat: Float
        get() = throw IllegalStateException("JsonElement.asFloat")

    open val asDouble: Double
        get() = throw IllegalStateException("JsonElement.asDouble")

    open val asChar: Char
        get() = throw IllegalStateException("JsonElement.asChar")

    open val asString: String
        get() = throw IllegalStateException("JsonElement.asString")

    open fun <T> asUuid(): UUID<T> {
        throw IllegalStateException("JsonElement.asUuid")
    }

    open val asUInt: UInt
        get() = throw IllegalStateException("JsonElement.asUInt")

    open val asUShort: UShort
        get() = throw IllegalStateException("JsonElement.asUShort")

    open val asUByte: UByte
        get() = throw IllegalStateException("JsonElement.asUByte")

    open val asULong: ULong
        get() = throw IllegalStateException("JsonElement.asULong")

    val asPrettyString: String
        get() = JsonFormat().format(this)

    fun toPrettyString(format: JsonFormat = JsonFormat()) = format.format(this)

    /**
     * Runs the provided [visitor] on the JSON subtree with the root at this node.
     *
     * @param visitor The visitor to accept.
     * @param data An arbitrary context to pass to each invocation of [visitor]'s methods.
     * @return The value returned by the topmost `visit*` invocation.
     */
    abstract fun <R, D> accept(visitor: JsonVisitor<R, D>, data: D): R

    /**
     * Runs the provided [transformer] on the Doc subtree with the root at this node.
     *
     * @param transformer The transformer to use.
     * @param data An arbitrary context to pass to each invocation of [transformer]'s methods.
     * @return The transformed node.
     */
    abstract fun <D> transform(transformer: JsonTransformer<D>, data: D): JsonElement

    /**
     * Runs the provided [visitor] on subtrees with roots in this node's children.
     *
     * Basically, calls `accept(visitor, data)` on each child of this node.
     *
     * Does **not** run [visitor] on this node itself.
     *
     * @param visitor The visitor for children to accept.
     * @param data An arbitrary context to pass to each invocation of [visitor]'s methods.
     */
    open fun <D> acceptChildren(visitor: JsonVisitor<Unit, D>, data: D) {

    }

    /**
     * Recursively transforms this node's children *in place* using [transformer].
     *
     * Basically, executes `this.child = this.child.transform(transformer, data)` for each child of this node.
     *
     * Does **not** run [transformer] on this node itself.
     *
     * @param transformer The transformer to use for transforming the children.
     * @param data An arbitrary context to pass to each invocation of [transformer]'s methods.
     */
    open fun <D> transformChildren(transformer: JsonTransformer<D>, data: D) {

    }

}