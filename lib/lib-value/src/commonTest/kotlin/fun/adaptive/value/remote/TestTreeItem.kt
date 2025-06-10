package `fun`.adaptive.value.remote

// Simple tree item class for testing
data class TestTreeItem(
    var value: String,
    var children: List<TestTreeItem> = emptyList()
)