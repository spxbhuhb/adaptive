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
private class SomeAdat(
    val s1: String,
    val soa: SomeOtherAdat
) : AdatClass<SomeAdat>

@Adat
private class SomeOtherAdat(
    val i1: Int,
    val yaa: YetAnotherAdat
)

@Adat
private class YetAnotherAdat(
    val i2: Int
)

@Adaptive
private fun t(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<Int>? = null,
    selector: () -> Int
) {
    T1(binding !!.value)
}

private val tag = name("t")

@Adaptive
private fun yetAnotherEditor(yaa: YetAnotherAdat) {
    t(tag) { yaa.i2 }
}

class CopyStoreTest2 {

    @Test
    fun multiLevel3() {
        val adapter = test {
            val someAdat = copyStore { SomeAdat("", SomeOtherAdat(12, YetAnotherAdat(23))) }

            yetAnotherEditor(someAdat.soa.yaa)
        }

        val root = adapter.rootFragment
        val t = root.first(true) { tag in it.instructions }
        val t1 = root.first<AdaptiveT1>(true)

        assertEquals(23, t1.p0)

        val value = root.state[0]

        assertIs<SomeAdat>(value)

        val store = value.adatContext?.store as? CopyStore<*>
        assertNotNull(store)

        store.setValue(listOf("soa", "yaa", "i2"), 34)
        assertEquals(34, t1.p0)

        val binding = t.state.filterIsInstance<AdaptiveStateVariableBinding<*>>().single()
        binding.setValue(45, false)
        assertEquals(45, t1.p0)
    }

}