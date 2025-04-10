# Lib Value

`lib-value` provides a subscribable value store that can be used to store
values. Each value in the store has to be a descendant of the `AvValue`
abstract class:

```kotlin
abstract class AvValue : NamedItem() {
    abstract val uuid: AvValueId
    abstract val timestamp: Instant
    abstract val status: AvStatus
    abstract val parentId: AvValueId?
}
```

There are two classes which extend `AvValue` are used widely: `AvItem` and `AvMarkerValue`.

## AvItem

`AvItem` is a final class intended to represent entities. The `spec` property
can be of any type supported by wireformat, typically it depends on the type
of the item. However, this is not enforced in any ways, which is by design.

```kotlin
@Adat
class AvItem<T>(
    override val name: String,
    override val type: NamedItemType,
    override val uuid: AvValueId = uuid7(),
    override val timestamp: Instant = now(),
    override val status: AvStatus = AvStatus.OK,
    override val parentId: AvValueId? = null,
    val friendlyId: FriendlyItemId,
    val markersOrNull: AvMarkerMap? = null,
    val spec: T
) : AvValue()
```

The `markersOrNull` is basically a map of `String` - `UUID<AvValue>?` pairs (after
resolving all type aliases).

`parentId` and `markersOrNull` is used to build relationships between items.

## AvValueWorker

Values are managed by `AvValueWorker` and can be persisted by passing
a persistence provider to the worker during initialization.

Each `AvValueWorker` has a so-called domain. Domains are used to separate
value sets. For example, the `auth` domain typically contains authentication
and authorization related data. The `iot` domain contains controllers, data
points etc.

Values in different domains can reference each other by markers or the parent
id but accessing the referenced value is not particularly easy. In the same domain
the reference access is as simple as `item.refItem("marker-name")`.

## Subscription

`AvValueWorker` supports subscription by value id and by markers. When a subscription
is created:

- the worker sends the current values,
- from then, it sends all changes to those values.

Subscriptions are typically created on the server side by a service. This
pattern has been chosen to avoid authorization problems by providing too 
generic access to the values.

## Changing values

Values stored by `AvValueWorker` are typically changed by services. The
services provide value type specific calls to the clients. They perform
the necessary authorization checks and preprocessing before calling
`execute`, `update` or one of the `queue` methods of the worker to 
actually change the values.

## Internals

### Domain separation

- Each worker has its own domain, passed to the constructor.
- Each service should look up the worker by the domain it serves.
- On the client side, a worker with the `client` domain contains values.
    - This should be a read-only worker, but that's not implemented yet.
  
`AvValueReceiverService` receives the value updates from the server and maintains
them in the local, client side worker.

There is no generic value service on the server side, so values are accessible
only through services.