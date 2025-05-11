# How to write a service

## 1. Write a Service API

**File**: `src/commonMain/kotlin/fun/adaptive/example/api/ExampleApi.kt`

### Purpose

Service APIs describe the communication between a service consumer (client) and a 
service provider (server).

### Code

```kotlin
package `fun`.adaptive.example.api

import `fun`.adaptive.example.model.ExampleData
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface ExampleApi {

    suspend fun getExampleData() : ExampleData

    suspend fun saveExampleData(data: ExampleData)
    
}
```

### Explanation

Adaptive service APIs are Kotlin interfaces with the `@ServiceApi` annotation. 

The **suspend** functions define what the service provides.

Service call arguments and return type can be anything that WireFormat supports.

## 2. Get a Service Consumer

### Purpose

Get an instance that can be used to call the service.

### Code

```kotlin
val exampleService = getService<ExampleApi>(transport)

exampleService.getExampleData().also { println(it) }
```

### Explanation

The `getService` function:

- returns a service consumer instance
- the compiler plugin generates all the code for the consumer side
- functions defined by the API can be called directly

Service calls are suspending coroutine calls. This is by design and suitable for most use cases
as the application has to wait for the answer anyway.

You can call `getService` from anywhere, assuming you have a `transport`. 

There are many ways to get the transport.

From an Adaptive fragment you can use `fetch` or `poll`:

```kotlin
package `fun`.adaptive.example.ws

import `fun`.adaptive.example.api.ExampleApi
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.service.api.getService


@Adaptive
fun exampleFetchFun() {
    val data = fetch { getService<ExampleApi>(adapter().transport).getExampleData() }
}

@Adaptive
fun examplePollFun() {
    val data = fetch { getService<ExampleApi>(adapter().transport).getExampleData() }
}
```

From a workspace pane view backend you can use `backend.transport`

```kotlin
package `fun`.adaptive.example.ws

import `fun`.adaptive.example.api.ExampleApi
import `fun`.adaptive.example.app.ExampleWsModule
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController

class ExampleSettingsViewBackend(
    module: ExampleWsModule<*>
) : WsSingularPaneController(module.workspace, module.EXAMPLE_SETTINGS_ITEM) {
    
    val exampleService = getService<ExampleApi>(backend.transport)
    
}
```

## 3. Write a Service Implementation

### Purpose

Service implementations are server-side components that provide a service according to an API.

### Code

```kotlin
package `fun`.adaptive.example.server

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.example.api.ExampleApi
import `fun`.adaptive.example.model.ExampleData
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

class ExampleService : ServiceImpl<ExampleService>(), ExampleApi {
    
    companion object {
        var lock = getLock()
        var exampleData = ExampleData("abc", 12, emptyList())
    }
    
    override suspend fun getExampleData(): ExampleData {
        publicAccess()
        return lock.use { exampleData }
    }
    
    override suspend fun saveExampleData(data: ExampleData) {
        ensureLoggedIn()
        lock.use { exampleData = data }
    }
    
}
```

### Explanation

Service providers implement a service API. They are standard server-side fragments that extend `ServiceImpl`.

A new service implementation instance is created for each service call. This might seem a bit of overkill, but it
makes the handling of the [service context](what_is_a_service_context.md) very straightforward.

This example uses the companion to store the data protected by a lock. Synchronizing data access is
critical as there may be many implementations running at the same time. Here we use a simple lock, but you can
use any synchronization mechanism, as long as it ensures thread safety.

It is good practice to add authorization checks for **ALL** service API functions.

- `publicAccess` allows the function to be called by anyone
- `ensureLoggedIn` throws an exception if the call is from a client that is not signed in

There are many `ensure*` functions, check the `lib-auth` for more information about them.

> [!CAUTION]
>
> **IMPORTANT** This **DOES NOT WORK**. As each call gets a new instance, `data` will be the same all the time,
> no matter how you change it with `saveExampleData`.
>
> ```kotlin
> class ExampleService : ServiceImpl<ExampleService>(), ExampleApi {
>     
>    val data = ExampleData("abc", 12, emptyList())
> 
>    override suspend fun getExampleData(): ExampleData {
>        publicAccess()
>        return data.let { error("WRONG, THIS CODE DOES NOT WORK, RTFM") }
>    }
>    
>    override suspend fun saveExampleData(data: ExampleData) {
>        ensureLoggedIn()
>        exampleData = data
>        error("WRONG, THIS CODE DOES NOT WORK, RTFM")
>    }
> 
> }
> ```

## 4. Register the Service Implementation

### Purpose

Register the service in the server-side application module so it is added to the server during bootstrap.

### Code

```kotlin
package `fun`.adaptive.example.app

import `fun`.adaptive.example.server.ExampleService
import `fun`.adaptive.runtime.ServerWorkspace

class ExampleServerModule<WT : ServerWorkspace> : ExampleModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + ExampleService()
    }
}
```

### Explanation

Service implementations are registered by the application module in `workspaceInit`.

On the server-side the workspace is typically a `ServerWorkspace` which stores all the
services and workers known to the server.

## 5. Handle Exceptions

### Purpose

Handle exceptions from service calls.

### Code

```kotlin
@Adat
class OddNumberException : Exception()

@ServiceApi
interface NumberApi {
  suspend fun ensureEven(i : Int, illegal : Boolean)
}

// ----  SERVER SIDE  --------

class NumberService : NumberApi, ServiceImpl<NumberService>() {

  override suspend fun ensureEven(i : Int, illegal : Boolean) {
    publicAccess()
    if (i % 2 == 1) {
      if (illegal) throw IllegalArgumentException() else throw OddNumberException()
    }
  }

}

// ----  CLIENT SIDE  --------

suspend fun checkNumber(i : Int, illegal : Boolean) : String {
  try {
    getService<NumberApi>(clientBackend.transport).ensureEven(i, illegal)
    return "this is an even number"
  } catch (ex : OddNumberException) {
    return "this is an odd number"
  } catch (ex : ServiceCallException) {
    return "ServiceCallException"
  }
}
```

### Explanation

Exception handling depends on the transport implementation, see [Service Transport](what_is_a_service_transport.md).

With the Ktor implementation, server-side exceptions are thrown on the client-side according to these rules.

If the thrown exception is an Adat class and the class is registered on both client and server, 
the client receives an instance of the same class with the same data.

If not, the client receives a `ServiceCallException`.

The service transports may close the connection at specific events such as login and logout. When this
happens, all pending calls fail with `DisconnectException`.

## 6. Configure Logging

### Purpose

Configure service call logging on the server-side.

### Code

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
-->
<configuration
        xmlns="http://ch.qos.logback/xml/ns/logback"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://ch.qos.logback/xml/ns/logback xsd/logback.xsd">
  
    <appender name="ErrorLogFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- appender configuration -->
    </appender>

    <appender name="AccessLogFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <!-- appender configuration -->
    </appender>

    <root level="WARN">
        <appender-ref ref="ErrorLogFile"/>
    </root>

    <logger name="fun.adaptive.service.ServiceAccessLog" level="INFO" additivity="false">
        <appender-ref ref="AccessLogFile"/>
    </logger>
  
</configuration>
```

### Explanation

The default server-side transport implementation logs service access and service errors. These can be configured
in `logback.xml` as the example shows below.

Exceptions that extend `ReturnException` are logged as INFO rather than WARN or ERROR.

These exceptions indicate expected control-flow decisions, not application faults.

An example use case is `AccessDenied` which does not indicate an actual software error and does not need
investigation. In contrast, `NullPointerException` should be investigated.

## 7. Call from Server to Client

### Purpose

Call client-side functions from the server when there is an active connection.

### Code

```kotlin
getService<DuplexApi>(serviceContext.transport).process(value)
```

### Explanation

You can call client-side service functions from the server if:

- you have a `ServiceContext`,
- you have a service implementation added to the client backend.

> [!NOTE]
>
> Be careful, it is easy to create infinite loops by calling a service from a service
> implementation.
>

# See Also

- [What is a service Context](guide://)
- [What is a service transport](guide://)