import `fun`.adaptive.auto.api.autoFolder
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.ensure
import kotlinx.io.files.Path

class Recipe {
    val path = Path("./cookbook/var/auto/standaloneFolder").ensure()

    fun run() {
        val standalone = autoFolder<MasterDataItem>(
            null,
            path,
            { itemId, _ -> "${itemId.peerId}.${itemId.timestamp}.json" },
            defaultWireFormat = MasterDataItem.adatWireFormat,
            register = false
        )

        standalone.frontend.add(MasterDataItem(UUID(), "record-1"))
        standalone.frontend.add(MasterDataItem(UUID(), "record-2"))
    }

}