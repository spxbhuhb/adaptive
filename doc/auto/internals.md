# Auto - Internals

Auto implementations have a type-specific backend and a use-case specific frontend.

**backend**

* `PropertyBackend`, `SetBackend`, `TreeBackend`
* stores the CRTD data
* responsible for the replication
* typically not used from application-level code

**frontend**

*** specific to the use-case, examples:
* include the fragments of an `TreeBackend` into the UI directly
* use the data in an `SetBackend` to build a table
* store the content of an `SetBackend` into SQL
* store the content of an `TreeBackend` in files

### SetBackend

Stores additions and removals in `SetBackend.additions` and `SetBackend.removals` respectively.

Both `additions` and `removals` store item ids, that is `peerId:timestamp` pairs.

The resulting list is defined as `(additions - removals).sorted()`.

This does not care about interweaving, but I think that it is irrelevant for general use cases.

## Definitions

**Endpoint**

An *auto instance* that:
- does not have an initial value
- does not accept connection from other auto instances
- connects to a *controller*

**Node**

An *auto instance* that:
- downloads the initial value from a *controller* at first run
- keeps the value between restarts
- may accept connection from other auto instances
- may connect to other *nodes*

**Controller**

A *node* that:
- may initiate garbage collection

**Auto Property**

A (time, name, value) triple kept synchronized across all peers.

**Auto Item**

A set of *auto properties* with a unique item id.

**Auto Set**

A set of *auto items*.

**Garbage Collection**

A cleanup of *auto set* remove information. The information about
removed items must be kept until all connected peers reach the same
*milestone*.

**Milestone**

A point in time when remove information has been synchronized to all
known peers.

## Operations

**property update**

Updates an *auto property*, changing the time and value of the property.

**item add**

Add item(s) to an *auto set*.

**item remove**

Remove item(s) from an *auto set*.

**connect**

Connects an *auto instance* to a *controller*.

**disconnect**

Disconnects an *auto instance* from a *controller*.

## Use Cases

- `autoList` is an endpoint
- `autoList` is a producer
- any changes in the list (structural or property) generate a new list
- any given generated list is immutable
- update results in a new list
- the items in the new list are the same instances except the one which has been updated

```kotlin
@Adat
class Item(
    val counter : Int
)
    
@Adaptive
fun showList() {
     val list = autoList<Item>()
     
     for (item in list) {
         showItem(item)
     }
}

@Adaptive
fun showItem(item : Item) {
    text(item.counter) .. onClick { item.update { counter = 1 } }
}
```

## Use cases 2

**Controller in global variable**

```kotlin
private val appNavState = autoInstance(Routes.zones)
```

**Endpoint with a producer as an internal state variable, direct connection to a node in a global variable**

```kotlin
val navState = autoInstance(appNavState)
```

**Endpoint with a producer as an internal state variable, service connection**

```kotlin
val dayOverrides = autoList<DayOverride> { hvacService.dayOverridesAuto() }
```

**Endpoint with a listener**

```kotlin
val connectInfo = worker.modelStore.pointAuto<ZigBeeDataPoint>(deviceId, pointId)
if (connectInfo == null) return data

val instance = autoInstance(
    worker.autoWorker,
    ZigBeeDataPoint,
    handle = connectInfo.connectingHandle,
    itemId = connectInfo.connectingHandle.itemId!!,
    listener = PointListener(worker, updateFun),
    register = false
)

instance.connectDirect(10.seconds) { connectInfo }
```

**Endpoint with persistence, service connection, newly established (itemId passed)**

```kotlin
val connectInfo = driver.modelService.networkAuto<NT>(networkId)

network = autoFile(
    driver.autoWorker,
    driver.networkCompanion,
    networkPath,
    handle = connectInfo.connectingHandle,
    itemId = connectInfo.connectingHandle.itemId!!,
    listener = networkListener
)

// Connect to the origin list on the server side. This call
// will start synchronization between the two list. As the client
// side is empty, it will load everything from the server side.

network.connect(waitForSync = 10.seconds) { connectInfo }
```

**Endpoint with persistence, service connection, previously established**

```kotlin
val connectInfo = driver.modelService.networkAuto<NT>(networkId)

network = autoFile<NT>(
    driver.autoWorker,
    driver.networkCompanion,
    path = networkFile,
    handle = connectInfo.connectingHandle,
    listener = networkListener
)

network.connect(waitForSync = 2.seconds) { connectInfo }
```

**Controller with persistence and with listener**

```kotlin
autoFolder(
    autoWorker,
    path,
    { _, item -> fileNameFun(item) },
    listener = itemListener()
)
```

**Endpoint with persistence**

```kotlin
 val connectInfo = driver.modelService.commandsAuto(networkId)

commands = autoFolder<AioCommand>(
    driver.autoWorker,
    driver.commandsPath(networkId).ensure(),
    { _, c -> driver.commandFilename(c.id) },
    handle = connectInfo.connectingHandle,
    includeFun = { it.name.let { n -> n.startsWith("command.") && n.endsWith(".json") } },
    listener = commandListener
)

commands.connect(waitForSync = 10.seconds) { connectInfo }
```


## Service Connection

Service connection uses `AutoApi` and `ServiceConnector` to connect two auto instances.
The connecting instance uses `AutoApi.addPeer` and `BackendBase.addPeer` to register 
the two directions of the connection.

The connection is made by calling the `serviceConnect` global function.

`serviceConnect`
    - runs the function passed to get the connection info
    - calls `AutoApi.addPeer` to add the connection on the remote peer
    - 

`AutoApi.addPeer` - this runs on the "connected to" peer
    - calls `AutoWorker.addPeer`
        - creates a `ServiceConnector` with reconnect `false`
        - calls `BackendBase.addPeer`
        - starts the connector
    - adds `PeerCleanup` to the session

`BackendBase.addPeer` - this runs on both peers
    - adds the connector to the context

