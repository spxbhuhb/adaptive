package hu.simplexion.z2.wireformat

import hu.simplexion.z2.util.UUID

open class TestValues {
    val booleanVal = true
    val intVal = 123
    val longVal = 1234L
    val floatVal = 12.34f
    val doubleVal = 12.34
    val stringVal = "abc"
    val byteArrayVal = byteArrayOf(9, 8, 7)
    val uuidVal = UUID<Any>()
    val instanceVal = A(true, 12, "hello")
    val enumVal = E.V1

    val booleanListVal = listOf(true, false, true)
    val intListVal = listOf(1, 2, 3)
    val longListVal = listOf(1L, 2L, 3L, 4L)
    val floatListVal = listOf(1.0f, 2.0f, 3.0f)
    val doubleListVal = listOf(1.0, 2.0, 3.0)
    val stringListVal = listOf("a", "b", "c")
    val byteArrayListVal = listOf(byteArrayOf(1), byteArrayOf(2), byteArrayOf(3))
    val uuidListVal = listOf(UUID<Any>(), UUID(), UUID())
    val enumListVal = listOf(E.V2, E.V1)

    val instanceListVal = listOf(
        B(A(true, 123, "a", mutableListOf(1, 2, 3)), "AA"),
        B(A(false, 456, "b", mutableListOf(4, 5, 6)), "BB"),
        B(A(true, 789, "c", mutableListOf(7, 8, 9)), "CC")
    )
}