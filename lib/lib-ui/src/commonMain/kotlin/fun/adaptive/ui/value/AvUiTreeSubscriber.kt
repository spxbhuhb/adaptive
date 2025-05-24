package `fun`.adaptive.ui.value

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.local.AvTreeSubscriber
import `fun`.adaptive.value.model.AvTreeSetup
import kotlin.reflect.KClass

class AvUiTreeSubscriber<SPEC : Any>(
    backend: BackendAdapter,
    specClass: KClass<SPEC>,
    setup: AvTreeSetup
) : AvTreeSubscriber<SPEC, TreeItem<AvValue<SPEC>>>(
    backend, specClass, setup
) {

    override fun newTreeItem(item: AvValue<SPEC>, parentNode: Node<SPEC, TreeItem<AvValue<SPEC>>>?): TreeItem<AvValue<SPEC>> =
        TreeItem(
            iconFor(item),
            title = item.nameLike,
            data = item,
            parent = parentNode?.treeItem,
        )

    override fun updateTreeItem(item: AvValue<SPEC>, treeItem: TreeItem<AvValue<SPEC>>) {
        treeItem.title = item.nameLike // treeItem is observable
    }

    override fun updateChildren(treeItem: TreeItem<AvValue<SPEC>>, children: List<TreeItem<AvValue<SPEC>>>) {
        treeItem.children = children // treeItem is observable
    }

}