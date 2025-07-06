# Value

[value](def://?inline)

[AvValue](class://) consists of two parts:

- common data all values have such as `uuid`, `name` or `revision`,
- domain-specific data in the `spec` property of the class.

[lib-value](def://) provides functions to work with values, including:

- create, read, update and delete values
- [ACL](def://) based access control
- subscribe to changes
- persist values into files or into a database

[values](def://) are standard [Adat classes](def://) everything that can be done with
an Adat class can be done with values (deep copy, diff, serialization, etc).

## Identification

Each value has a UUID (typically Version 7), a name and a friendly ID, stored
in properties [uuid](property://AvValue), [friendlyId](property://AvValue),
[name](property://AvValue) respectively.

The UUID of a value never changes. Once created, it remains the same.

The friendly ID is an optional, "supposedly unique", easy-to-read ID. "TH-0001" for example.

It is "supposedly unique" because sometimes it is system assigned, sometimes
the user can change it, depending on the application.

Name is an optional string that can be changed by the user.

The friendly ID and the name should never be used to identify a value
programmatically, use the UUID instead.

## Last change and revision

Time of the last change and the revision of the value are stored in the
[lastChange](property://AvValue) and the [revision](property://AvValue)
properties of [AvValue](class://).

These are managed automatically by the [value store](def://).

The store rejects updates with a revision lower than the revision
currently in the store.

The store always increased the revision by 1, the revision received
in the value is used only for sanity-check.

If you want to store domain-specific last change information (the last time
there was a communication with a device, for example), you have
to add it to the [spec](def://).

## Status

Status of a [value](def://) is a set of domain-specific strings, stored in
the [statusOrNull](property://AvValue). property of [AvValue](class://).

Many values do not have any status information, it depends on the domain.

## Markers

[Markers](def://) label [values](def://) with type information.

Markers are similar to tags or labels, but they are domain-specific
and in many cases calculated by the system automatically.

For example, in the IoT domain, markers might be "temperature", "hot-water" etc.

The concept originates from [Project Haystack](https://project-haystack.org).

Markers are stored in the [markersOrNull](property://AvValue). property of [AvValue](class://).

## References

The [refsOrNull](property://AvValue) property of a [value](def://) stores references to other [values](def://).

Each reference has a [reference label](def://) which is a string, a key in [refsOrNull](property://AvValue).

For example, a measurement point might have a reference to the device it is connected to.
In this case the reference label would be "device".

Reference labes typically end with "Ref" to make separation from markers easier.

To store one-to-many references, a value with [AvRefListSpec](class://) [spec](def://) is used.