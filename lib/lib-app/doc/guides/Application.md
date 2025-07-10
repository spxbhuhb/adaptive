# Applications

[application](def://?inline)

Adaptive provides a framework for client and server [applications](def://).

The very basic classes are in [core-core](def://) but the majority of the application framework
is in [lib-app](def://), including:

- ready-to-go application bootstrap functions
- server and client application classes
- basic application [user interface](def://) items:
  - sign-in, user account, user management, role management
  - administration tool
  - inspect tool

Check [Built-in lib-app features](guide://) for detailed information.

Note: you can build applications that use Adaptive without using `lib-app`. It is a convenience library
that handles most of the common application building steps.

## Basic concepts

The application framework uses three concepts:

- [application](def://)
- [workspace](def://)
- [application module](def://)

## Application

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

- [AbstractApplication](class://)
  - [ServerApplication](class://)
    - [JvmServerApplication](class://)
  - [ClientApplication](class://)
    - [BasicBrowserClientApplication](class://)
    - [MpwBrowserClientApplication](class://)

## Workspace

Each [application](def://) has two [workspaces](def://):

[backend workspace](def://?inline)

[frontend workspace](def://?inline)

The [workspaces](def://) are stored in the [backendWorkspace](property://AbstractApplication) 
and [frontendWorkspace](property://AbstractApplication) properties of [AbstractApplication](class://).

The [application workspace](def://) can be whatever class you decide, but typically these are used:

- [AbstractWorkspace](class://)
  - [ServerWorkspace](class://) for [server applications](def://)
  - [ClientWorkspace](class://) for basic [client applications](def://)
    - [MultiPaneWorkspace](class://) for complex, IntelliJ like, multi-pane workspace [client applications](def://)

## Application Module

[application module](def://?inline)

An [application module](def://) groups the components of a feature together. [Applications](def://) are typically built
by adding modules to the [application](def://).

Source code of modules is typically organized into a directory structure like this
(assuming the module name is `example` and it is in the `my.project` package).

```text
src/commonMain/kotlin/my/project
├── example/
│   ├── api
│   ├── app
│   ├── lib
│   ├── model
│   ├── server
│   ├── ui
│   └── ws
```

- `example` is the top-level directory of the module.
- `api` contains service API interface definitions.
- `app` contains the application module definition classes, see [Writing an application module](guide://).
- `lib` contains function shared by components across the module
- `model` contains the data model of the module, typically Adat classes and enumerations.
- `server` contains server fragment implementations.
- `ui` contains UI fragments that are workspace-independent.
- `ws` contains UI fragments that are workspace-dependent.

These directories are optional, you can omit them if not needed.

## Application startup

Currently, JVM and Browser/JS [platforms](def://) are implemented. Android, iOS, Desktop will
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

# See Also

- [Writing a basic application](guide://)
- [Writing an MPW application](guide://)
- [Writing an application module](guide://)