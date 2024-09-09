package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatChange
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.wireformat.toJson

/**
 * Creates a deep copy of [this]. The copy is fully independent of the original, any
 * changes made on it will not change the original in any ways.
 *
 * **Mutable collections are not supported yet.** See: [Adat problems and improvements #35](https://github.com/spxbhuhb/adaptive/issues/35)
 *
 * @throws  IllegalArgumentException  In case it is not possible to make a deep copy.
 */
fun <A : AdatClass> A.deepCopy(replace: AdatChange? = null): A {
    // TODO when A is immutable, return with A
    @Suppress("UNCHECKED_CAST")
    return this.genericDeepCopy(replace) as A
}

private fun AdatClass.genericDeepCopy(replace: AdatChange?): AdatClass {
    val properties = getMetadata().properties
    val values = arrayOfNulls<Any?>(properties.size)

    val replaceName = replace?.path?.first()

    for (property in properties) {
        val index = property.index
        values[index] = getValue(index)

        if (replaceName == property.name) {
            if (replace.path.size == 1) {
                values[index] = replace.value
            } else {
                values[index] = (getValue(index) as AdatClass).genericDeepCopy(replace.next())
            }
            continue
        }

        val value = getValue(index)

        values[index] = when {
            property.hasImmutableValue -> value
            value is AdatClass -> value.genericDeepCopy(null)
            else -> throw IllegalArgumentException("cannot deep copy ${getMetadata().name}.${property.name} ${getMetadata().toJson(AdatClassMetadata).decodeToString()}")
        }
    }

    return adatCompanion.newInstance(values) as AdatClass
}