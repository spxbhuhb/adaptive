package `fun`.adaptive.ui.loading

import `fun`.adaptive.foundation.testing.T1
import `fun`.adaptive.foundation.testing.test
import kotlin.test.Test

class LoadingTest {

    @Test
    fun loadingTest() {
        test(printTrace = true) {
            val a = listOf(1,2,3)
            loading(a) { b ->
                for (c in (b as List<Int>)) {
                    T1(c)
                }
            }
        }
    }
}