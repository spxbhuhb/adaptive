import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.encodeToJsonString
import kotlin.test.Test

class TestSandbox {

    @Test
    fun test() {
        println(T().encodeToJsonString())
    }

}

abstract class Conversion : AdatClass {
    abstract fun from(value: String): String
    abstract fun to(value: String): String
}

@Adat
class T(
    val conversion: Conversion? = null,
    val readOnAdd: Boolean = false
)