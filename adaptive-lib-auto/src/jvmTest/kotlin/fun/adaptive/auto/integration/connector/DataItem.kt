package `fun`.adaptive.auto.integration.connector

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID

@Adat
data class DataItem(
    val value : String = "initial-value"
)