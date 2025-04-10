package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.adat.Adat

@Adat
class HandleInfo(
    val name : String,
    val xActive : Boolean,
    val xReverse : Boolean,
    val yActive : Boolean,
    val yReverse : Boolean
)