package `fun`.adaptive.value.example

import `fun`.adaptive.value.AvRefLabel
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker

//@example
suspend fun basicExample(valueWorker: AvValueWorker) {
    valueWorker.execute {
        addValue { AvValue(spec = "Hello World!") }
    }
}

//@example
suspend fun addExample(valueWorker: AvValueWorker) {
    valueWorker.execute {

        // add a value without using the created instance
        addValue { AvValue(spec = "Hello World!") }

        // add a value and use it immediately
        val other = addValue { AvValue(spec = "Hello World 2!") }
        println("added: ${other.uuid}")
    }
}

//@example
suspend fun getExample(
    valueWorker: AvValueWorker,
    valueId: AvValueId
) {
    valueWorker.execute {

        // get a value with the String `spec`
        val value = get<String>(valueId)

        // get a value or null if it doesn't exist
        // getOrNull does not cast to a spec itself, you can do so with `withSpec`
        val other = getOrNull(valueId)?.checkSpec<String>()

    }
}

//@example
suspend fun refExample(
    valueWorker: AvValueWorker,
    referringValueId: AvValueId
) {
    valueWorker.execute {

        // get the referred value with String `spec`
        // the reference label is `exampleRefLabel`
        val r1 = ref<String>(referringValueId, "exampleRefLabel")

        // get the referred value or null if no such value exists
        // the reference label is `exampleRefLabel`
        // cast the value to `String` spec if exists
        val r2 = refOrNull(referringValueId,"exampleRefLabel")?.checkSpec<String>()

        // get the referring value
        val referring = get<String>(referringValueId)

        // get the referred value with String `spec`
        // pass the referring value, not just the id of it
        // the reference label is `exampleRefLabel`
        val r3 = ref<String>(referring, "exampleRefLabel")

        // get the referred value or null if no such value exists
        // pass the referring value, not just the id of it
        // the reference label is `exampleRefLabel`
        val r4 = ref<String>(referring, "exampleRefLabel")

    }
}

//@example
suspend fun addRefExample(
    valueWorker: AvValueWorker,
    referringValueId: AvValueId,
    referredValueId: AvValueId,
    refLabel : AvRefLabel
) {
    valueWorker.execute {
        // add a reference with ids
        addRef(referringValueId, referredValueId, refLabel)

        // add a reference with the referring value and id for referred
        val referring = get<Any>(referringValueId)
        addRef(referring, referredValueId, refLabel)
    }
}

//@example
suspend fun removeRefExample(
    valueWorker: AvValueWorker,
    referringValueId: AvValueId,
    refLabel : AvRefLabel
) {
    valueWorker.execute {
        // remove a reference with ids
        removeRef(referringValueId, refLabel)
    }
}