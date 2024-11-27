import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID


@Adat
data class DataItem(
    val id: UUID<DataItem> = UUID(),
    val recordName: String = "record-name-origin"
)