package `fun`.adaptive.site.server

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.sandbox.support.ExampleValueSpec
import `fun`.adaptive.sandbox.support.exampleDomain
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueWorker

class SiteWorker : WorkerImpl<SiteWorker>() {

    val values by workerImpl<AvValueWorker>()

    override suspend fun run() {

        if (values.get<ExampleValueSpec>(exampleDomain.treeDef.nodeMarker).isNotEmpty()) return

        values.execute {
            for (rootIndex in 1 .. 3) {
                val rootNode = addTreeNode(exampleDomain.treeDef) {
                    AvValue(
                        name = "root $rootIndex",
                        spec = ExampleValueSpec(rootIndex, "root $rootIndex")
                    )
                }

                for (childIndex in 1 .. 3) {
                    addTreeNode(exampleDomain.treeDef, rootNode.uuid) {
                        AvValue(
                            name = "child $rootIndex.$childIndex",
                            spec = ExampleValueSpec(rootIndex * 10 + childIndex, "child $rootIndex.$childIndex")
                        )
                    }
                }
            }
        }
    }
}