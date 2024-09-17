package `fun`.adaptive.cookbook.auto.autoListPoly_autoFolder_autoListPoly

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant


@Adat
class StringItem(
    val id: UUID<StringItem>,
    val field1: String
)

@Adat
class IntItem(
    val id: UUID<IntItem>,
    val field2: Int
)

@Adat
class InstantItem(
    val id: UUID<InstantItem>,
    val field3: Instant
)