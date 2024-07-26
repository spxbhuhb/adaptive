# Conflict-free Replicated Fragment Trees

The `adaptive-lib-crdt` module provides a fragment tree store which is synchronized between peers
(server-client, client-client, internal).

All tree and state changes are sent to all other connected stores, which in turn apply their changes to
all their fragments.

These stores use [CRDT](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type) data types under the hood to
make sure that the fragment tree and the fragment states are the same on all peers.

```kotlin
@AdatFragment
class SomeFragment(
    val someText: String
)

@ServiceApi
interface ItemApi {
    suspend fun itemStore(): UUID<FragmentStore<SomeFragment>>
}

@Adaptive
fun someList() {
    val itemStore = fragmentStore { itemService.itemStore() }

    for (item in itemStore) {
        text(item.someText)
    }
}

class ItemService : ServiceImpl<ItemService>, ItemApi {
    val fragmentStoreWorker by worker<FragmentStoreWorker>()
    
    override suspend fun itemStore(): UUID<FragmentStore<SomeFragment>> {
        return fragmentStoreWorker.makeStore()
    }
}
```

`AdatFragment` turns the [Adat](../adat/README.md) class into an `AdaptiveFragment`, so it can be used
as a fragment. This is necessary as the fragment store synchronizes fragment trees.

`ItemApi` is the API we use to get the global ID of the fragment store we want to connect.

`fragmentStore` creates a store instance for you and connects it to the peer based on the global store ID we got
from the item API.

After that you can use the fragments in the store freely.

