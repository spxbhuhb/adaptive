/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.utility.vmNowMicro

class TraceEvent(
    val name: String,
    val id: Long,
    val point: String,
    val data: List<String>
) {

    constructor(name: String, id: Long, point: String, vararg data: Any?) : this(name, id, point, data.map { it.toString() })

    val createdAt = vmNowMicro()

    override fun toString(): String {
        return "[ ${name.padEnd(30)} ${id.toString().padStart(4)} ]  ${point.padEnd(25)}  |  ${data.joinToString(" ")}"
    }

    fun toCode(): String {
        val nameOrRoot = if (name.startsWith("AdaptiveRoot")) "<root>" else name
        return "TraceEvent(\"$nameOrRoot\", ${id}, \"${point}\"${if (data.isNotEmpty()) ", " else ""}${data.joinToString(", ") { "\"$it\"" }})"
    }

    fun println(startedAt: Long): TraceEvent {
        println("${((createdAt - startedAt) / 1000).toString().padStart(9, ' ')}  $this")
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is TraceEvent) return false
        if (other.point != this.point) return false
        if (other.data != this.data) return false
        if (other.name == this.name) return true

        if (this.name == "<root>" && other.name.startsWith("AdaptiveRoot")) return true
        if (other.name == "<root>" && this.name.startsWith("AdaptiveRoot")) return true

        return false
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + point.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }
}