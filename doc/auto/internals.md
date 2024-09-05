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

Both `additions` and `removals` store item ids, that is `clientId:timestamp` pairs.

The resulting list is defined as `(additions - removals).sorted()`.

This does not care about interweaving, but I think that it is irrelevant for general use cases.**