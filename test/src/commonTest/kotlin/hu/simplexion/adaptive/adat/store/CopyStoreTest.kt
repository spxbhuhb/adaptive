package hu.simplexion.adaptive.adat.store

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.foundation.query.first
import hu.simplexion.adaptive.foundation.testing.AdaptiveT1
import hu.simplexion.adaptive.foundation.testing.T1
import hu.simplexion.adaptive.foundation.testing.test
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

@Adat
private class T(
    val someInt: Int
) : AdatClass<T>

@Adat
private class M(
    val someInt: Int,
    val t: T
) : AdatClass<M>

@Adaptive
private fun t(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<Int>? = null,
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

        val store = value.adatContext?.store as? CopyStore<*>
        assertNotNull(store)

        assertEquals(12, t1.p0)
        store.setValue(listOf("someInt"), 23)

        assertEquals(23, t1.p0)
        store.setValue(listOf("someInt"), 34)
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

        val store = value.adatContext?.store as? CopyStore<*>
        assertNotNull(store)

        assertEquals(23, t1.p0)
        store.setValue(listOf("t", "someInt"), 34)
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

        adapter.printTrace = true

        val root = adapter.rootFragment
        val t = root.first(true) { tag in it.instructions }
        val t1 = root.first<AdaptiveT1>(true)
        val value = root.state[0]

        assertIs<M>(value)

        val store = value.adatContext?.store as? CopyStore<*>
        assertNotNull(store)

        assertEquals(23, t1.p0)
        store.setValue(listOf("t", "someInt"), 34)
        assertEquals(34, t1.p0)

        val binding = t.state.filterIsInstance<AdaptiveStateVariableBinding<*>>().single()
        binding.setValue(45, false)
        assertEquals(45, t1.p0)

    }
}