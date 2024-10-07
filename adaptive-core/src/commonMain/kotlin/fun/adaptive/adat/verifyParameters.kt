package `fun`.adaptive.adat

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.signature.KotlinSignatures

class MissingParameterException(
    val className: String,
    val propertyName: String,
    val id: UUID<*>?
) : RuntimeException(
    "missing value (and no default) for $propertyName in $className${id?.let { " (id=$it)" } ?: ""}"
)

fun verifyParameters(instance: AdatClass, parameters: Array<Any?>) {
    for (property in instance.adatCompanion.adatMetadata.properties) {
        if (property.isNullable) continue
        if (property.hasDefault) continue
        if (parameters[property.index] == null) {
            throw MissingParameterException(
                instance.adatCompanion.adatMetadata.name,
                property.name,
                findId(instance, parameters)
            )
        }
    }
}

private fun findId(instance: AdatClass, parameters: Array<Any?>): UUID<*>? {
    for (property in instance.adatCompanion.adatMetadata.properties) {
        if (property.name == "id" && property.signature == KotlinSignatures.UUID) {
            return parameters[property.index] as UUID<*>
        }
    }
    return null
}