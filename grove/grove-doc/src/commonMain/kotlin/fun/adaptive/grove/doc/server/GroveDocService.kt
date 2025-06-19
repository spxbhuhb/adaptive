package `fun`.adaptive.grove.doc.server

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.grove.doc.api.GroveDocApi
import `fun`.adaptive.grove.doc.model.GroveDocSpec
import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.grove.doc.model.groveDocDomain
import `fun`.adaptive.value.AvValueWorker

class GroveDocService : ServiceImpl<GroveDocService>(), GroveDocApi {

    val values by workerImpl<AvValueWorker>()

    override suspend fun getByPath(path: List<String>): GroveDocValue? {
        val top = values.firstOrNull<GroveDocSpec>(groveDocDomain.node) {
            // hasRef filters for top nodes only
            it.name == path.first() && ! it.hasRef(groveDocDomain.parentRef)
        } ?: return null
        if (path.size == 1) return top

        var remaining = path.drop(1)
        var current = top

        while (remaining.isNotEmpty()) {
            val children = values.refList<GroveDocSpec>(current.uuid, groveDocDomain.childListRef)
            current = children.firstOrNull { it.name == remaining.first() } ?: return null
            remaining = remaining.drop(1)
        }

        return current
    }

}