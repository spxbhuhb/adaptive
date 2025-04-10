package `fun`.adaptive.auto.test.support

import `fun`.adaptive.auto.api.AutoGeneric

fun wait(node1: AutoGeneric, node2: AutoGeneric) {
    while (node1.time.timestamp != node2.time.timestamp) {
        continue
    }
}