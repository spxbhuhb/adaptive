# What is a value store

Value stores are generic data containers that store [values](def://).

Each value is an instance of the [AvValue](class://) class. And can store
domain-specific data in the `spec` property of the class.

## Value access

There are two ways to access values in a store:

1. Call functions of [AvValueWorker](class://) directly from other [backend fragment implementations](def://).
2. Call functions of [AvValueApi](api://) though standard service calls.

The difference between direct and API access is that:

- direct access can **read and change** values
- API access can **only read** values

The reason for this separation is security and data integrity.

## Subscription

Clients can subscribe to values based on conditions.

When subscribed, value changes are propagated to the client automatically, so it can update
its own internal state and possibly the UI to show the latest values to the user.

## Access Control

Each value may have an Access Control List (ACL). The ACL specifies who can see/change the
value.

Value stores automatically check the ACL at value access, refusing the access or hiding
the value, depending on the situation.

## Persistence

Values can be transient or persisted into files or into a database, depending on the actual
deployment parameters.

Each [AvValueWorker](class://) has a persistence provider which is responsible
for loading and saving values.

Built-in persistence providers:

- [AvNoPersistence](class://)

## Components

## AvValueWorker

[AvValueWorker](class://) is a [worker implementation](def://)
that offers thread-safe access to a value store.

Each worker has a **domain** which can be used segregate values into contexts
(e.g., `auth`, `iot`, `client`).

[AvValueWorker](class://) performs operations sequentially to ensure that
there are no conflicting changes in the data.

### Subscriptions

[AvValueWorker](class://) supports reactive subscriptions:

- Subscriptions can be made based on value ids or markers.
- On subscription, the worker emits the current state of values and pushes future changes.

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