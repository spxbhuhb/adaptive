---
title: What is a Value Store?
tags: [ lib-value, value, store, persistence ]
type: conceptual
---

# Summary

This guide introduces the concept and architecture of value stores, provided by the `lib-value`
module. Value stores enable structured, subscribable value storage for applications that rely
on complex domain data.

# Objective

To understand how the value store models and manages data using a subscribable
structure, with emphasis on runtime behavior, data relationships, and worker responsibilities.

# Key Concepts

Each item in a value store is an instance of `AvValue` and has a unique identifier,
which is typically UUID version 4 or UUID version 7.

At the most basic level values can be accessed by their unique identifier, but
more complex relationships can be established by using markers and parent ids.

## AvValue

At the heart of the value store is the abstract class `AvValue`, which all value types must inherit
from. It defines the minimal set of properties required for a value to be identifiable,
timestamped, status-tracked, and optionally connected to a parent value:

```kotlin
abstract class AvValue : NamedItem() {
    abstract val uuid: AvValueId
    abstract val timestamp: Instant
    abstract val status: AvStatus
    abstract val parentId: AvValueId?
}
```

## AvItem

The most commonly used concrete implementation of `AvValue` is `AvItem`.
This class is intended to represent data entities, holding a `spec` (its content)
and optionally a set of markers:

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

- `markersOrNull` is a map of marker names to referenced values.
- `friendlyId` is an ID to show to the user (UUID is not particularly user-friendly)
- `parentId` and markers allow for hierarchical and relational modeling.

## AvMarkerValue

While not deeply elaborated here, `AvMarkerValue` is another subclass of `AvValue`,
often used to tag or annotate `AvItem`s with additional contextual metadata.

# Components and Configuration

## AvValueWorker

The core runtime component that manages and processes values is the `AvValueWorker`.

It is initialized with a domain and optionally a persistence provider. The domain
is used to segregate values into contexts (e.g., `auth`, `iot`, `client`).

- Workers support referencing other values via markers or parent ids.
- Cross-worker access by reference is not supported.
- Domain isolation ensures value grouping, though cross-domain referencing is possible but complex.

## Subscriptions

`AvValueWorker` supports reactive subscriptions:

- Subscriptions can be made based on value ids or markers.
- On subscription, the worker emits the current state of values and pushes future changes.
- Subscriptions are server-side to maintain controlled access and avoid overexposing data.

# Behavior or Flow

## Changing Values

Services are responsible for modifying values:

- They expose APIs to clients.
- Perform authorization and preprocessing.
- Call worker methods like `execute`, `update`, or `queue` to mutate values.

## Client-Server Flow

- The server maintains authoritative workers for each domain.
- Clients receive value updates through `AvValueReceiverService`.
- On the client side, the `client` domain holds these values (currently without write enforcement).

There is no generic server-side value service; instead, values are exposed only
via service-specific APIs to ensure encapsulation and controlled access.

# Usage Example

## Server-Side (JVM)

On the server side, the `ValueServerModule` is used to initialize a value worker.
In this example:

- the domain is `general`
- the worker is initialized with public access
- the persistence provider is `FilePersistence`.

```kotlin
import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.server.BasicAppServerModule
import `fun`.adaptive.auth.app.NoAuthServerModule
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.ktor.KtorJvmServerModule
import `fun`.adaptive.lib.util.app.UtilServerModule
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.value.app.ValueServerModule
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.io.files.Path

fun main() {
    jvmServer {
        module { UtilServerModule() }
        module { ValueServerModule("general", { publicAccess() }, FilePersistence(Path("./var/values").ensure(), 2)) }
        module { NoAuthServerModule() } // no authentication
        module { KtorJvmServerModule() }
        module { BasicAppServerModule() }
    }
}
```

## Client-Side (Browser)

On the client side, the `ValueClientModule` is used to initialize a value worker.
This typically does not require any configuration.

```kotlin
package my.project

import `fun`.adaptive.app.BasicBrowserClientApplication.Companion.basicBrowserClient
import `fun`.adaptive.value.app.ValueClientModule

fun main() {
    basicBrowserClient {
        module { ValueClientModule() }
    }
}
```

# Conclusion

Value stores provide a domain-separated, reactive system for managing structured values in
Adaptive applications. By modeling values with `AvValue` subclasses and managing them
through domain-scoped `AvValueWorker`s, it ensures modularity, reactivity, and scalability while
preserving strict control over access and mutation patterns.