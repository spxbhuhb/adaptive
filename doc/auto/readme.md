# Conflict-free Replicated Data Types

The `adaptive-lib-crdt` module provides data types that are automatically synchronized between peers
(server-client, client-client, internal):

* `AutoData` - a single [Adat](../adat/readme.md) class
* `AutoList` - a list of [Adat](../adat/readme.md) classes
* `AutoTree` - a tree of fragments

These stores use [CRDT](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type) data types under the hood to make sure that the actual data is the same on all peers.

```kotlin
import hu.simplexion.adaptive.service.transport.ServiceResponseListener

@Adat
class SomeData(
    val someText: String
)

@ServiceApi
interface ItemApi {
    suspend fun items(listener: UUID<ServiceResponseListener>) : AutoList<SomeFragment>
}

@Adaptive
fun someList() {
    val items = autoList { itemService.items() }

    for (item in items) {
        text(item.someText)
    }
}

class ItemService : ServiceImpl<ItemService>, ItemApi {
    val fragmentStoreWorker by worker<FragmentStoreWorker>()

    override suspend fun items(listener: UUID<ServiceResponseListener>) : AutoList<SomeFragment> {
        return fragmentStoreWorker.makeList(listener, initialState)
    }
}
```

`AdatFragment` turns the [Adat](../adat/README.md) class into an `AdaptiveFragment`, so it can be used
as a fragment. This is necessary as the fragment store synchronizes fragment trees.

`ItemApi` is the API we use to get the global ID of the fragment store we want to connect.

`fragmentStore` creates a store instance for you and connects it to the peer based on the global store ID we got
from the item API.

After that you can use the fragments in the store freely.

## Internals

Auto implementations have a type-specific backend and a use-case specific frontend.

**backend**

* specific to the type of the Auto class (`Auto`, `AutoList`, `AutoTree`)
* stores the CRTD data
* is responsible for the replication
* typically not used from application-level code

**frontend**

* specific to the use-case, examples:
    * include the fragments of an `AutoTree` into the UI directly
    * use the data in an `AutoList` to build a table
    * store the content of an `AutoList` into SQL
    * store the content of an `AutoTree` in files