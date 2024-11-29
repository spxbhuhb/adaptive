package `fun`.adaptive.auto.test.support

import `fun`.adaptive.auto.api.CollectionBase

fun wait(node1: CollectionBase<TestData>, node2: CollectionBase<TestData>) {
    while (node1.time.timestamp != node2.time.timestamp) {
        continue
    }
}