package `fun`.adaptive.grove.hydration.lfm

import `fun`.adaptive.adat.Adat

@Adat
class LfmConst(
    val signature : String,
    val value: Any?
) : LfmExpression()