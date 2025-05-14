# What is an application

[lib-app](def://) provides a framework for client and server [applications](def://) in a somewhat simple, well-defined way.

Note: you can build applications that use Adaptive without using `lib-app`. It is a convenience library
that handles most of the common application building steps.

[lib-app] uses three main concepts:

- [application](def://)
- [application module](def://)
- [application workspace](def://)

## Application

An [application](def://):

- in this context means a client or a server, regardless of the actual platform the application runs,
- it is composed of [application modules](def://),
- has exactly one [application workspace](def://).

> [!IMPORTANT]
> 
> Server applications use the very same **REACTIVE** foundation as client applications.
> Server workers and services are stored in a tree of Adaptive fragments (just as the client
> UI) there is no technical difference.
> 
> This approach somewhat blurs the line between clients and servers. You can move functionality
> easily from one side to another, dependencies permitting.
> 

Application classes (Android/iOS is not ready yet, but will follow the same concept):

```text
AbstractApplication
├── ServerApplication
│   └── JvmServerApplication
├── ClientApplication
│   └── BrowserApplication
│       ├── BasicBrowserClientApplication
│       └── WsBrowserClientApplication
```

## Application workspace

Each [application](def://) has an [application workspace](def://). In basic client applications,
it may act as a placeholder, while in other cases it is a sophisticated implementation with full
state and UI context management.

The [application workspace](def://) is stored in the [workspace](property://AbstractApplication) 
property of [AbstractApplication](class://).

The [application workspace](def://) can be whatever class you decide, but typically these are used:

- `ServerWorkspace` server [applications](def://)
- `ClientWorkspace` for clients without actual workspace support (a placeholder basically)
- `Workspace` is a complex, IntelliJ like UI workspace implementation

> [!Note]
> 
> In the context of Adaptive workspace-based UI typically means the IntelliJ like UI workspace which
> provides sophisticated workspace features such as tools, tabbed content groups, etc.
> 
> You actually don't have to use it, Adaptive works perfectly fine without it. The
> `sandbox-app-echo` project in the `sandbox` directory shows how to start such a UI.
> 

```text
AbstractWorkspace
├──  ServerWorkspace
├──  ClientWorkspace
│    └──  Workspace
```

## Application Module

An [application module](def://) groups the components of a feature together. Applications are typically built
by adding modules to the application.

Source code of modules is typically organized into a directory structure like this
(assuming the module name is `example` and it is in the `my.project` package).

```text
src/commonMain/kotlin/my/project
├── example/
│   ├── api
│   ├── app
│   ├── model
│   ├── server
│   ├── ui
│   └── ws
```

- `example` is the top-level directory of the module.
- `api` contains service API interface definitions.
- `app` contains the application module definition classes, see [How to write an application module](guide://).
- `model` contains the data model of the module, typically Adat classes and enumerations.
- `server` contains server fragment implementations.
- `ui` contains UI fragments that are workspace-independent.
- `ws` contains UI fragments that are workspace-dependent.

These directories are optional, you can omit them if not needed.

## Application startup

Currently, application platforms are implemented (JVM, Browser/JS). Android, iOS, Desktop will
follow the same concept.


### JVM - server

```kotlin
fun main() {
    jvmServer {
        module { UtilServerModule() }
        module { AuthServerModule() }
        module { IotServerModule() }
        module { BasicAppServerModule() }
    }
}
```

### JS - browser - workspace-based

```kotlin
fun main() {
    wsBrowserClient {
        module { LibUiModule() }
        module { DocWsModule() }
        module { BasicAppWsModule() }
    }
}
```

### Startup sequence

#### Server startup sequence

- the `application` property of the module is set to the application instance
- call `wireFormatInit` for all modules
- get a client id from the server
- call `resourceInit` for all modules
- build the workspace
  - call `contextInit` for all modules
  - call `workspaceInit` for all modules
- create the server backend
  - call `backendAdapterInit` for all modules
  - actualize the main backend fragment

#### Client startup sequence

- the `application` property of the module is set to the application instance
- call `wireFormatInit` for all modules
- create the service transport to the server
  - get a client id from the server
- get the session from the server (if there is one)
  - get the list of known roles from the server (if there is a session)
- create the client backend
  - call `backendAdapterInit` for all modules
  - actualize the main backend fragment
- call `resourceInit` for all modules
- load resources
- build the workspace
  - call `contextInit` for all modules
  - call `workspaceInit` for all modules
- create the client frontend
  - call `frontendAdapterInit` for all modules
  - call `workspaceInit` for all modules
  - actualize the main frontend fragment

## Accessing the Application

From UI fragments and server implementations, you can access the application through
the adapter.

### UI Fragments

```kotlin
package my.project.example.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter

@Adaptive
fun exampleFragment() {
    val app = adapter().application // AbstractApplication<*>()
}
```

### Server Implementations

```kotlin
package my.project.example.server

import `fun`.adaptive.backend.builtin.WorkerImpl

class ExampleWorker : WorkerImpl<ExampleWorker> {
    
    override suspend fun run() {
        logger.info("${adapter?.application}")
    }
    
}
```

# Usage Example

## Server Side

```kotlin
package my.project

import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.server.BasicAppServerModule
import `fun`.adaptive.auth.app.NoAuthServerModule
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.propertyFile
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.ktor.KtorJvmServerModule

fun main() {

    settings {    
        inline(
            "KTOR_PORT" to 8080,
            "KTOR_WIREFORMAT" to "json",
        )
        propertyFile { "./etc/my.project.properties" }
        environment()
    }

    jvmServer {
        module { NoAuthServerModule() } // no authentication
        module { KtorJvmServerModule() }
        module { BasicAppServerModule() }
    }
}
```

This application entry point:

- loads the application configuration according to the `settings` block
- uses JSON for client-server communication (default is `proto`)
- creates a `JvmServerApplication` and adds modules according to the `jvmServer` block
- starts the server application (which in turn will start Ktor as `KtorJvmServerModule` is added)

## Client Side (browser, no workspace)

```kotlin
import `fun`.adaptive.app.BasicBrowserClientApplication.Companion.basicBrowserClient
import `fun`.adaptive.app.client.basic.BasicAppClientModule
import `fun`.adaptive.auth.app.NoAuthClientModule
import `fun`.adaptive.wireformat.api.Json

fun main() {
    basicBrowserClient {
        wireFormatProvider = Json
        module { NoAuthClientModule() }
        module { BasicAppClientModule() }
    }
}
```

This application entry point:

- starts a basic browser application (without workspace support)
- uses JSON for client-server communication (default is `proto`)

# See Also

- [How to write an application module](guide://).