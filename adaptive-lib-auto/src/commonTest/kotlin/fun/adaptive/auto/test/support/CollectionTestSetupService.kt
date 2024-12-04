package `fun`.adaptive.auto.test.support

import `fun`.adaptive.auto.api.autoCollectionNode
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.internal.persistence.CollectionFilePersistence
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path

class CollectionTestSetupService(
    autoTest : AutoTest,
    testDir: Path,
    initialValue: List<TestData>,
    trace: Boolean = false
) {
    val originPath = Path(testDir, "origin").ensure()
    val originMetaPath = Path(originPath, "meta.json")

    val originPersistence = CollectionFilePersistence<TestData>(originMetaPath, Json) { itemId, item ->
        Path(originPath, "${itemId.peerId}.${itemId.timestamp}.json")
    }

    val origin = autoCollectionOrigin(
        initialValue,
        worker = autoTest.serverWorker,
        persistence = originPersistence,
        trace = trace
    )

    val nodePath = Path(testDir, "node").ensure()
    val nodeMetaPath = Path(nodePath, "meta.json")

    val nodePersistence = CollectionFilePersistence<TestData>(nodeMetaPath, Json) { itemId, item ->
        Path(nodePath, "${itemId.peerId}.${itemId.timestamp}.json")
    }

    val node = autoCollectionNode(
        worker = autoTest.clientWorker,
        persistence = nodePersistence,
        trace = trace
    ) {
        origin.connectInfo()
    }
}