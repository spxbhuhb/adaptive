package `fun`.adaptive.adat

@Adat
class Padding(
    override val top: DPixel?,
    override val right: DPixel?,
    override val bottom: DPixel?,
    override val left: DPixel?
) : Surrounding

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

interface Surrounding {
    val top: DPixel?
    val right: DPixel?
    val bottom: DPixel?
    val left: DPixel?
}

fun box(): String {

    if (Padding.adatMetadata.isMutable) return "Fail: Padding.isMutable"
    if (Padding.adatMetadata.properties.first().isMutable) return "Fail: top is mutable"

    if (DPixel.adatMetadata.isMutable) return "Fail: DPixel.isMutable"
    if (DPixel.adatMetadata.properties.first().isMutable) return "Fail: value is mutable"

    return "OK"
}