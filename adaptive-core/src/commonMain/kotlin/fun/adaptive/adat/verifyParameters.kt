package `fun`.adaptive.adat

class MissingParameterException(
    val className: String,
    val propertyName: String,
    val values: String
) : RuntimeException(
    "missing value (and no default) for $propertyName in $className $values} "
)

fun verifyParameters(instance: AdatClass, parameters: Array<Any?>) {
    for (property in instance.adatCompanion.adatMetadata.properties) {
        if (property.isNullable) continue
        if (property.hasDefault) continue
        if (parameters[property.index] == null) {
            throw MissingParameterException(
                instance.adatCompanion.adatMetadata.name,
                property.name,
                parameters.contentToString()
            )
        }
    }
}