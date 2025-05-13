package `fun`.adaptive.ui.value

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.local.AvTreeSubscriber
import kotlin.reflect.KClass

class AvUiTree<ST : Any>(
    backend: BackendAdapter,
    specClass: KClass<ST>,
    override val parentRefLabel: AvMarker,
    vararg conditions: AvSubscribeCondition
) : AvTreeSubscriber<ST, TreeItem<AvValueId>>(
    backend, specClass, *conditions
) {

    override fun newTreeItem(item: AvValue<ST>, parentNode: Node<ST, TreeItem<AvValueId>>?): TreeItem<AvValueId> =
        TreeItem(
            iconFor(item),
            title = item.nameLike,
            data = item.uuid,
            parent = parentNode?.treeItem,
        )

    override fun updateTreeItem(item: AvValue<ST>, treeItem: TreeItem<AvValueId>) {
        // TODO treeItem.title = item.name // treeItem is observable
    }

    override fun updateChildren(treeItem: TreeItem<AvValueId>, children: List<TreeItem<AvValueId>>) {
        treeItem.children = children // treeItem is observable
    }

    override fun process(value: AvValue<*>) {
        TODO("Not yet implemented")
    }

}