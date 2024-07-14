package hu.simplexion.adaptive.adat

@Adat
class DPixel(
    override val value: Double
) : Track {

    override val isFix
        get() = true

}

interface Track {

    val value: Double

    val isFix: Boolean
        get() = false
}

fun box(): String {

    if (DPixel.adatMetadata.isMutable) return "Fail: DPixel.isMutable"
    if (DPixel.adatMetadata.properties.first().isMutable) return "Fail: value is mutable"

    return "OK"
}