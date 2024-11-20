package `fun`.adaptive.cookbook.auto.globalInstance_fragmentInstance

import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.testing.test
import kotlinx.coroutines.delay

val initial = DataItem()

val global = autoItemOrigin(initial)

var out: DataItem?
    get() = TODO()
    set(v) {
        result += v
    }

val result = mutableListOf<DataItem?>()

suspend fun recipe() {
    val backend = backend { }

    test(backend) {
        val data = autoItemOrigin(global) { global.connectInfo(AutoConnectionType.Direct) }
        out = data
    }

    delay(1000)

    val original = global.frontend.value !!
    val updated = original.copy(recordName = "Hello world!")
    global.frontend.update(original, updated)

    delay(1000)

    if (result.size != 3) throw IllegalStateException("result.size == ${result.size} (!= 2)")
    if (result != listOf(null, initial, updated)) throw IllegalStateException("result != listOf(null, initial, updated) $result")
}