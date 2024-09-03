# Auto

The `adaptive-lib-auto` module provides high-level data synchronization utilities.

The general idea is to:

- define the *origin* data (typically on a server)
- connect to the *origin* data
- expect that all data changes appear on all peers
- expect that conflicts are resolved automatically and deterministically

This feature takes reactivity to another level as it is not simple reactivity
inside the UI but reactivity between peers.

All Auto features use [Conflict-free replicated data types](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type)
to make sure that the actual data is the same on all peers.

The following origin/peer functions are available:

* `originInstance` and `autoInstance` - a simple Adat instance
* `originList` and `autoList` - a list of Adat instances (same class)
* `originPolyList` and `autoPolyList` - a polymorphic list of Adat instances (any Adat class)

Check the cookbook for examples.

## Internals

Auto implementations have a type-specific backend and a use-case specific frontend.

**backend**

* `PropertyBackend`, `SetBackend`, `TreeBackend`
* stores the CRTD data
* responsible for the replication
* typically not used from application-level code

**frontend**

* specific to the use-case, examples:
  * include the fragments of an `TreeBackend` into the UI directly
  * use the data in an `SetBackend` to build a table
  * store the content of an `SetBackend` into SQL
  * store the content of an `TreeBackend` in files

### SetBackend

Stores additions and removals in `SetBackend.additions` and `SetBackend.removals` respectively.

Both `additions` and `removals` store item ids, that is `clientId:timestamp` pairs.

The resulting list is defined as `(additions - removals).sorted()`.

This does not care about interweaving, but I think that it is irrelevant for general use cases.