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

@Adaptive
private fun t(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<Int>? = null,
    selector: () -> Int
) {
    T1(binding !!.value)
}

val tag = name("t")

class CopyStoreTest {

    @Test
    fun basic() {
        val adapter = test {
            val value = copyStore(T(12))
            t(tag) { value.someInt }
        }

        adapter.printTrace = true

        val root = adapter.rootFragment
        val t = root.first(true) { tag in it.instructions }
        val t1 = root.first<AdaptiveT1>(true)
        val value = root.state[0]

        assertEquals(T(12), value)
        assertIs<T>(value)

        @Suppress("UNCHECKED_CAST")
        val store = value.adatContext?.store as? CopyStore<T>
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
}