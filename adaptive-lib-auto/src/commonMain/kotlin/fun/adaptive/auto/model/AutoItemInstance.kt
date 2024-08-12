package `fun`.adaptive.auto.model

import `fun`.adaptive.auto.ItemId

data class AutoItemInstance<A>(
    val itemId: ItemId,
    val instance: A
)