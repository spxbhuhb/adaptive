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