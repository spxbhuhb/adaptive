package `fun`.adaptive.value.local

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.model.AvTreeSetup

// Test implementation of TreeSubscriber for testing purposes
class TestTreeSubscriber(
    backend: BackendAdapter,
    setup: AvTreeSetup
) : AvTreeSubscriber<String, TestTreeItem>(
    backend,
    String::class,
    setup
) {

    override fun newTreeItem(item: AvValue<String>, parentNode: Node<String, TestTreeItem>?): TestTreeItem {
        return TestTreeItem(item.spec)
    }

    override fun updateTreeItem(item: AvValue<String>, treeItem: TestTreeItem) {
        treeItem.value = item.spec
    }

    override fun updateChildren(treeItem: TestTreeItem, children: List<TestTreeItem>) {
        treeItem.children = children
    }
}
