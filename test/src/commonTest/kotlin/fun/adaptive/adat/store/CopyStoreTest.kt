package `fun`.adaptive.adat.store

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.api.store
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.foundation.testing.AdaptiveT1
import `fun`.adaptive.foundation.testing.T1
import `fun`.adaptive.foundation.testing.test
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

@Adat
private class T(
    val someInt: Int
) : AdatClass

@Adat
private class M(
    val someInt: Int,
    val t: T
) : AdatClass

@Adaptive
private fun t(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<Int>? = null,
    @PropertySelector
    selector: () -> Int
) {
    T1(binding !!.value)
}

@Adaptive
private fun m1(m: M) {
    t(tag) { m.t.someInt }
}

@Adaptive
private fun m2(t: T) {
    t(tag) { t.someInt }
}

private val tag = name("t")

class CopyStoreTest {

    @Test
    fun basic() {
        val adapter = test {
            val value = copyStore { T(12) }
            t(tag) { value.someInt }
        }

        val root = adapter.rootFragment
        val t = root.first(true) { tag in it.instructions }
        val t1 = root.first<AdaptiveT1>(true)
        val value = root.state[0]

        assertEquals(T(12), value)
        assertIs<T>(value)

        val store = value.store()

        assertEquals(12, t1.p0)
        store.update(value, arrayOf("someInt"), 23)

        assertEquals(23, t1.p0)
        store.update(value, arrayOf("someInt"), 34)
        assertEquals(34, t1.p0)

        val binding = t.state.filterIsInstance<AdaptiveStateVariableBinding<*>>().single()
        binding.setValue(45, false)
        assertEquals(45, t1.p0)
    }

    @Test
    fun multiLevel1() {
        val adapter = test {
            val value = copyStore { M(12, T(23)) }

            m1(value)
        }

        val root = adapter.rootFragment
        val t = root.first(true) { tag in it.instructions }
        val t1 = root.first<AdaptiveT1>(true)
        val value = root.state[0]

        assertIs<M>(value)

        val store = value.store()
        assertNotNull(store)

        assertEquals(23, t1.p0)
        store.update(value, arrayOf("t", "someInt"), 34)
        assertEquals(34, t1.p0)

        val binding = t.state.filterIsInstance<AdaptiveStateVariableBinding<*>>().single()
        binding.setValue(45, false)
        assertEquals(45, t1.p0)
    }

    @Test
    fun multiLevel2() {
        val adapter = test {
            val value = copyStore { M(12, T(23)) }

            m2(value.t)
        }

        val root = adapter.rootFragment
        val t = root.first(true) { tag in it.instructions }
        val t1 = root.first<AdaptiveT1>(true)
        val value = root.state[0]

        assertIs<M>(value)

        val store = value.store()
        assertNotNull(store)

        assertEquals(23, t1.p0)
        store.update(value, arrayOf("t", "someInt"), 34)
        assertEquals(34, t1.p0)

        val binding = t.state.filterIsInstance<AdaptiveStateVariableBinding<*>>().single()
        binding.setValue(45, false)
        assertEquals(45, t1.p0)
    }

    @Test
    fun onChangeTest() {
        var out = 0

        val adapter = test {
            val value = copyStore({ out = it.someInt }) { T(12) }
        }

        val root = adapter.rootFragment
        val value = root.state[0]

        assertIs<T>(value)

        val store = value.store()
        assertNotNull(store)

        store.update(value, arrayOf("someInt"), 23)

        assertEquals(23, out)
    }
}