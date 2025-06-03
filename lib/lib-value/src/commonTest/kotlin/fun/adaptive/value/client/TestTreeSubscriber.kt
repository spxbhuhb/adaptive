package `fun`.adaptive.value.client

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.model.AvTreeDef

// Test implementation of TreeSubscriber for testing purposes
class TestTreeSubscriber(
    backend: BackendAdapter,
    treeDef: AvTreeDef
) : AvTreeSubscriber<String, TestTreeItem>(
    backend,
    String::class,
    treeDef
) {

    override fun newTreeItem(item: AvValue<String>, parentNode: Node<String, TestTreeItem>?): TestTreeItem {
        return TestTreeItem(item.spec)
    }

    override fun updateTreeItemParent(treeItem: TestTreeItem, parentItem: TestTreeItem?) {

    }

    override fun updateTreeItemData(item: AvValue<String>, treeItem: TestTreeItem) {
        treeItem.value = item.spec
    }

    override fun updateChildren(treeItem: TestTreeItem, children: List<TestTreeItem>) {
        treeItem.children = children
    }
}
