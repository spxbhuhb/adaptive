package `fun`.adaptive.cookbook.auto.standaloneFolder

import `fun`.adaptive.auto.api.originFolder
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path

class Recipe {
    val path = "./cookbook/var/auto/standaloneFolder"

    fun run() {
        val standalone = originFolder(
            null,
            MasterDataItem,
            Json,
            Path(path),
            // This function generates the name of the files.
            // The actual file name is not important, but it should not start with a '.'
            // character as those files are ignored at list load.
            { itemId, _ -> "${itemId.peerId}.${itemId.timestamp}.json" }
        )

        standalone.frontend.add(MasterDataItem(UUID(), "record-1"))
        standalone.frontend.add(MasterDataItem(UUID(), "record-2"))
    }

}