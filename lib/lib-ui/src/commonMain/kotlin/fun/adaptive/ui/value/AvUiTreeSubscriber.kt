package `fun`.adaptive.ui.value

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.remote.AvRemoteTreeSubscriber
import `fun`.adaptive.value.model.AvTreeDef
import kotlin.reflect.KClass

class AvUiTreeSubscriber<SPEC : Any>(
    backend: BackendAdapter,
    specClass: KClass<SPEC>,
    treeDef: AvTreeDef
) : AvRemoteTreeSubscriber<SPEC, TreeItem<AvValue<SPEC>>>(
    backend, specClass, treeDef
) {

    override fun newTreeItem(item: AvValue<SPEC>, parentNode: Node<SPEC, TreeItem<AvValue<SPEC>>>?): TreeItem<AvValue<SPEC>> =
        TreeItem(
            iconFor(item),
            title = item.nameLike,
            data = item,
            parent = parentNode?.treeItem,
        )

    override fun updateTreeItemParent(treeItem: TreeItem<AvValue<SPEC>>, parentItem: TreeItem<AvValue<SPEC>>?) {
        treeItem.parent = parentItem
    }

    override fun updateTreeItemData(item: AvValue<SPEC>, treeItem: TreeItem<AvValue<SPEC>>) {
        treeItem.title = item.nameLike // treeItem is observable
    }

    override fun updateChildren(treeItem: TreeItem<AvValue<SPEC>>, children: List<TreeItem<AvValue<SPEC>>>) {
        treeItem.children = children // treeItem is observable
    }

}