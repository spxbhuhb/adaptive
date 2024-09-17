# Auto

The `adaptive-lib-auto` module provides a high-level data synchronization feature.

The general idea is to:

- define the *origin* data (typically on a server)
- connect to the *origin* data (maybe from multiple clients)
- expect that all data changes appear on all peers
- expect that conflicts are resolved automatically and deterministically

This feature takes reactivity to another level as it is not simple reactivity
inside the UI but reactivity between peers.

All Auto features use [Conflict-free replicated data types](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type)
to make sure that the actual data is the same on all peers.

The following *origin* functions are available:

| Function         | Storage                  | Stored data                                     |
|------------------|--------------------------|-------------------------------------------------|
| `autoInstance`   | In-memory                | Adat instance.                                  |
| `autoList`       | In-memory                | Adat instance list (same class or polymorphic). |
| `autoFile`       | Disk (one file)          | Adat instance.                                  |
| `autoFolder`     | Disk (one file per item) | Adat instance list (same class or polymorphic). |

The following *producer* functions (to be used in fragments) are available:

| Function           | Storage                  | Stored data                                     |
|--------------------|--------------------------|-------------------------------------------------|
| `autoInstance`     | In-memory                | Adat instance.                                  |
| `autoList`         | In-memory                | Adat instance list (same class or polymorphic). |

## Performance

Auto is a very high-level feature focusing on convenience and data consistency instead of performance.

I don't think there will be a performance bottleneck for normal use cases (a few thousand items). 
That said, Auto is definitely not optimized well enough to handle millions of items.

There is one **very important** point you have to keep in mind and that is list age. The implementation
of lists uses two sets for item add/removal bookkeeping and (as of now) those sets will **never** 
shrink. Modifying the properties of the items already in the list does not have this impact.

In a long-running application or in lists that add/remove items very fast you have to consider this.
For the target use cases this won't be a problem, but keep it in mind. If unsure, ask on the Slack channel.

## Recipes

**backend - backend**

- [autoFile - autoFile](/cookbook/src/commonMain/kotlin/fun/adaptive/cookbook/auto/autoFile_autoFile/Recipe.kt)
- [autoFolder - autoFile](/cookbook/src/commonMain/kotlin/fun/adaptive/cookbook/auto/autoFolder_autoFile/Recipe.kt)
- [autoFolder - autoList](/cookbook/src/commonMain/kotlin/fun/adaptive/cookbook/auto/autoFolder_autoList/Recipe.kt)
- [autoFolder - autoListPoly](/cookbook/src/commonMain/kotlin/fun/adaptive/cookbook/auto/autoFolder_autoListPoly/Recipe.kt)
- [autoListPoly - autoFolderPoly - autoListPoly](/cookbook/src/commonMain/kotlin/fun/adaptive/cookbook/auto/autoListPoly_autoFolder_autoListPoly/Recipe.kt)
- [autoFolder](/cookbook/src/commonMain/kotlin/fun/adaptive/cookbook/auto/autoFolder/Recipe.kt)

## Setup

> [!NOTE]
>
> A note on terminology.
>
> In Auto, the terms *frontend* and *backend* have module-specific meaning, that is different from
> the usual.
>
> - *Auto backend* means the class/instance that handles data synchronization, conflict resolution, etc.
> - *Auto frontend* means the class/instance that:
    >   - builds the actual instances, lists the application code sees
>   - handles the persistence (in files, folders, databases)
>
> In the documentation we specifically use *Auto frontend* and *Auto backend* whenever we use this
> specific meaning.
>
> 
To use the Auto, you have to set up a backend on each peer:

**headless code (no UI)**

```kotlin
backend {
    auto()
}
```

**UI code**

```kotlin
val backend = backend { auto() }

withJsonWebSocketTransport(window.location.origin, serviceImplFactory = backend)

browser(backend = backend) {
    // ...
}
```

The `auto` fragment:

- registers wire formats
- adds an `AutoWorker`
- adds an `AutoService`

## Defining origins

Origins can be defined anywhere if you have access to an `AutoWorker` instance.

### In a service

This example shows how to create an origin instance in a service.

Important points:

- return value of the service function should be `AutoConnectInfo`
- passing `serviceContext` to `origin*` functions registers a cleanup function:
  - if there is a session, the instance will be de-registered when the session closes
  - if there is no session, the instance will be de-registered when the connection closes
- not passing `serviceContext` means a memory-leak in this particular use pattern

```kotlin
@ServiceApi
interface AutoTestApi {
    suspend fun testInMemoryInstance(): AutoConnectInfo
}

class AutoTestService : AutoTestApi, ServiceImpl<AutoTestService> {

    val worker by worker<AutoWorker>()

    override suspend fun testInMemoryInstance(): AutoConnectInfo<TestData> {
        return originInstance(worker, TestData(12, "a"), serviceContext).connectInfo()
    }

}
```

## Use in fragments

Auto provides producers to connect to origins easily from fragments:

```kotlin
@Adaptive
fun thermostats() {
    val all = autoList(Thermostat) { getService<ThermostatApi>().list() }
    
    for (thermostat in all) {
        text(thermostat.name)
    }
}
```

In this example `all` is updated automatically whenever there is a change on any
peers and in turn the fragment will update the text.