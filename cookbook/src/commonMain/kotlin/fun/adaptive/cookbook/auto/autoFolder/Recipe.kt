package `fun`.adaptive.cookbook.auto.autoFolder

import `fun`.adaptive.auto.api.autoFolder
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path

class Recipe {
    val path = Path("./cookbook/var/auto/standaloneFolder").ensure()

    fun run() {
        val standalone = autoFolder(
            null,
            MasterDataItem,
            Json,
            path,
            // This function generates the name of the files.
            // The actual file name is not important, but it should not start with a '.'
            // character as those files are ignored at list load.
            { itemId, _ -> "${itemId.peerId}.${itemId.timestamp}.json" }
        )

        standalone.frontend.add(MasterDataItem(UUID(), "record-1"))
        standalone.frontend.add(MasterDataItem(UUID(), "record-2"))
    }

}