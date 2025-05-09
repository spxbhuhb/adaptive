package `fun`.adaptive.ui.value

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.local.AvLocalTree
import `fun`.adaptive.value.local.AvPublisher
import kotlin.reflect.KClass

class AvUiTree<ST : Any>(
    publisher: AvPublisher,
    backend: BackendAdapter,
    override val specClass: KClass<ST>,
    override val topListMarker: AvMarker,
    override val childListMarker: AvMarker
) : AvLocalTree<ST,TreeItem<AvValueId>>(
    publisher, backend.scope, backend.firstImpl<AvValueWorker>()
) {

    override fun newTreeItem(item: AvValue<ST>, parentNode: Node<ST, TreeItem<AvValueId>>?): TreeItem<AvValueId> =

        TreeItem(
            iconFor(item),
            title = item.name,
            data = item.uuid,
            parent = parentNode?.treeItem,
        )

    override fun updateTreeItem(item: AvValue<ST>, treeItem: TreeItem<AvValueId>) {
        treeItem.title = item.name // treeItem is observable
    }

    override fun updateChildren(treeItem: TreeItem<AvValueId>, children: List<TreeItem<AvValueId>>) {
        treeItem.children = children // treeItem is observable
    }

}