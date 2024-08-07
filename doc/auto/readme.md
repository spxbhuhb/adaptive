# Conflict-free Replicated Data Types

> [!CAUTION]
>
> This module is under active development, the information below is mostly obsoleted.
>

The `adaptive-lib-auto` module provides data types that are automatically synchronized between peers
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

* `PropertyBackend`, `ListBackend`, `TreeBackend`
* stores the CRTD data
* responsible for the replication
* typically not used from application-level code

**frontend**

* specific to the use-case, examples:
  * include the fragments of an `TreeBackend` into the UI directly
  * use the data in an `ListBackend` to build a table
  * store the content of an `ListBackend` into SQL
  * store the content of an `TreeBackend` in files

### ListBackend

Stores additions and removals in `ListBackend.adds` and `ListBackend.removals` respectively.

Both `adds` and `removals` store item ids, that is `clientId:timestamp` pairs.

The resulting list is defined as `(adds - removals).sorted()`.

This does not care about interweaving, but I think that it is irrelevant for general use cases.