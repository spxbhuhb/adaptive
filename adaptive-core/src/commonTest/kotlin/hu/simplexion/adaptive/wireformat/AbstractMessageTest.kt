/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import kotlin.test.Test
import kotlin.test.assertEquals

abstract class AbstractMessageTest<ST>(
    val wireFormatProvider: WireFormatProvider
) : AbstractWireFormatTest<ST>(wireFormatProvider) {

    val a1 = A(true, 12, "abc")
    val a2 = A(true, 23, "cde")
    val a3 = A(true, 34, "efg")

    val w1 = wireFormatProvider.encode(a1, A)
    val w2 = wireFormatProvider.encode(a2, A)
    val w3 = wireFormatProvider.encode(a3, A)

    class Writer() {
        var bytes = byteArrayOf()
    }

    @Test
    fun fullOne() {
        val writer = Writer()
        wireFormatProvider.writeMessage(w1) { writer.bytes += it }

        val (m1, rem1) = wireFormatProvider.readMessage(writer.bytes)
        assertEquals(1, m1.size)
        assertEquals(0, rem1.size)

        val rb1 = wireFormatProvider.decode(m1.first(), A)
        assertEquals(a1, rb1)
    }

    @Test
    fun fullThree() {
        val writer = Writer()
        wireFormatProvider.writeMessage(w1) { writer.bytes += it }
        wireFormatProvider.writeMessage(w2) { writer.bytes += it }
        wireFormatProvider.writeMessage(w3) { writer.bytes += it }

        val (ms, rem1) = wireFormatProvider.readMessage(writer.bytes)
        assertEquals(3, ms.size)
        assertEquals(0, rem1.size)

        val rb1 = wireFormatProvider.decode(ms.first(), A)
        assertEquals(a1, rb1)

        val rb2 = wireFormatProvider.decode(ms[1], A)
        assertEquals(a2, rb2)

        val rb3 = wireFormatProvider.decode(ms[2], A)
        assertEquals(a3, rb3)
    }

    @Test
    fun partialOne() {
        val writer = Writer()
        wireFormatProvider.writeMessage(w1) { writer.bytes += it }

        val (m1, rem1) = wireFormatProvider.readMessage(writer.bytes.take(10).toByteArray())
        assertEquals(0, m1.size)
        assertEquals(10, rem1.size)
    }

    @Test
    fun partialTwo() {
        val writer = Writer()
        wireFormatProvider.writeMessage(w1) { writer.bytes += it }
        wireFormatProvider.writeMessage(w2) { writer.bytes += it }

        val len = writer.bytes.size
        val withMessageLen = len / 2

        val (m1, rem1) = wireFormatProvider.readMessage(writer.bytes.take(withMessageLen + 10).toByteArray())
        assertEquals(1, m1.size)
        assertEquals(10, rem1.size)

        val rb1 = wireFormatProvider.decode(m1.first(), A)
        assertEquals(a1, rb1)
    }

    @Test
    fun partialThree() {
        val writer = Writer()
        wireFormatProvider.writeMessage(w1) { writer.bytes += it }
        wireFormatProvider.writeMessage(w2) { writer.bytes += it }
        wireFormatProvider.writeMessage(w2) { writer.bytes += it }

        val len = writer.bytes.size
        val withMessageLen = len / 3

        val (ms, rem1) = wireFormatProvider.readMessage(writer.bytes.take((withMessageLen * 2) + 10).toByteArray())
        assertEquals(2, ms.size)
        assertEquals(10, rem1.size)

        val rb1 = wireFormatProvider.decode(ms.first(), A)
        assertEquals(a1, rb1)

        val rb2 = wireFormatProvider.decode(ms[1], A)
        assertEquals(a2, rb2)
    }
}
