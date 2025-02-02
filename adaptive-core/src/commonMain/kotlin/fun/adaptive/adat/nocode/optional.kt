package `fun`.adaptive.adat.nocode

import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

/**
 * Generate a no-code adat class companion from `this` so that all
 * properties are nullable and variable.
 *
 * Descriptors are kept as-is, that means readonly, hidden and constraints are
 * the same as for the original class.
 */
fun AdatCompanion<*>.optional(): NoCodeAdatCompanion =
    NoCodeAdatCompanion(this.adatMetadata.optional())

fun AdatClassMetadata.optional() =
    with(this) {
        AdatClassMetadata(
            version,
            "$name\$Optional",
            flags and AdatClassMetadata.IMMUTABLE.inv(),
            properties.map { optional(it) }
        )
    }

private fun optional(original: AdatPropertyMetadata) =
    with(original) {
        AdatPropertyMetadata(
            name,
            index,
            optionalPropertyFlags(flags),
            optionalPropertySignature(signature),
            descriptors
        )
    }

private fun optionalPropertyFlags(flags: Int) =
    with(AdatPropertyMetadata) {
        (flags and VAL.inv() and IMMUTABLE_VALUE.inv()) or NULLABLE
    }

private fun optionalPropertySignature(signature: String) =
    if (signature.endsWith('?')) signature else "$signature?"