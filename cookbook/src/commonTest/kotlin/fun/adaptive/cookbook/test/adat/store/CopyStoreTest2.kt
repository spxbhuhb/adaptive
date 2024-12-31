package `fun`.adaptive.cookbook.test.adat.store

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.store
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.foundation.testing.AdaptiveT1
import `fun`.adaptive.foundation.testing.T1
import `fun`.adaptive.foundation.testing.test
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

@Adat
private class SomeAdat(
    val s1: String,
    val soa: SomeOtherAdat
)

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
    @PropertySelector
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

        val store = value.store()
        assertNotNull(store)

        store.update(value, arrayOf("soa", "yaa", "i2"), 34)
        assertEquals(34, t1.p0)

        val binding = t.state.filterIsInstance<AdaptiveStateVariableBinding<*>>().single()
        binding.setValue(45, false)
        assertEquals(45, t1.p0)
    }

}