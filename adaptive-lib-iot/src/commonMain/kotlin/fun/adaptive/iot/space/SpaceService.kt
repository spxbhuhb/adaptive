package `fun`.adaptive.iot.space

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.p04
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.write
import `fun`.adaptive.wireformat.builtin.ListWireFormat
import `fun`.adaptive.wireformat.json.JsonBufferReader
import `fun`.adaptive.wireformat.json.JsonWireFormatDecoder
import `fun`.adaptive.wireformat.json.JsonWireFormatEncoder
import kotlinx.io.files.Path

class SpaceService : AioSpaceApi, ServiceImpl<SpaceService> {

    companion object {
        private val lock = getLock()
        private val spaces = mutableMapOf<AioSpaceId, AioSpace>()
        private val path = Path("./var/spaces.json")
    }

    override fun mount() {
        super.mount()
        loadSpaces()
    }

    override suspend fun query(): List<AioSpace> {
        publicAccess()
        lock.use {
            return spaces.values.toList()
        }
    }

    override suspend fun add(
        parentId: UUID<AioSpace>?,
        type: AioSpaceType,
        displayOrder: Int
    ): AioSpace {
        publicAccess()

        lock.use {
            require(parentId == null || spaces.containsKey(parentId)) { "Parent $parentId space does not exist" }

            val friendlyId = genFriendlyId()

            val space = AioSpace(
                uuid = UUID<AioSpace>(),
                friendlyId = friendlyId,
                name = "SP-${friendlyId.p04}",
                spaceType = type,
                displayOrder = displayOrder,
                parentId = parentId
            )

            spaces[space.uuid] = space

            saveSpaces()

            return space
        }
    }

    override suspend fun update(space: AioSpace) {
        publicAccess()

        lock.use {
            spaces[space.uuid] = space

            saveSpaces()
        }
    }

    fun genFriendlyId(): Int {
        var highest = 0
        for (space in spaces.values) {
            highest = maxOf(highest, space.friendlyId)
        }
        return highest + 1
    }

    fun loadSpaces() {
        if (! path.exists()) return

        lock.use {
            val bytes = path.read()
            val root = JsonBufferReader(bytes).read()
            val list = JsonWireFormatDecoder(root).rawInstance(root, ListWireFormat(AioSpace))

            for (space in list) {
                if (space == null) continue
                spaces[space.uuid] = space
            }
        }
    }

    fun saveSpaces() {
        path.write(
            JsonWireFormatEncoder().rawInstance(spaces.values.toList(), ListWireFormat(AioSpace)).pack(),
            overwrite = true,
            useTemporaryFile = true
        )
    }
}