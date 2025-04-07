# Lib App

`lib-app` provides a framework for composing (pun intended) client and server applications
in a somewhat simple, well-defined way.

Note: you can build applications that use parts of Adaptive without using `lib-app`. It just
does not worth it IMHO.

The library is built on three main concepts:

- application
- module
- workspace

## Main concepts

### Application

- in this context means a client or a server, no matter of the actual platform the application runs
- is composed of modules
- has exactly one workspace, the workspace contains the contexts of the application

> [!IMPORTANT]
> 
> Server applications use the very same **REACTIVE** foundation as client side applications.
> Server workers and services are stored in a tree of Adaptive fragments, just as the client
> UI, there is no technical difference.
> 
> This approach somewhat blurs the line between clients and servers. You can move functionality
> easily from one side to another, dependencies permitting.
> 

Application classes (Android/iOS is not ready yet, but will follow the same concept):

```text
AbstractApplication
  ServerApplication
    JvmServerApplication
  ClientApplication
    BrowserApplication
      WsBrowserApplication
```

### Workspace

- is stored in the `AbstractApplication.workspace` property
- is supposed to be the main information central of your application
- is whatever class you decide, but
  - `ServerWorkspace` is typical for servers
  - `Workspace` is an IntelliJ like UI workspace implementation

> [!Note]
> 
> When I talk about "workspace based UI" I mean the IntelliJ like UI workspace which
> provides sophisticated workspace features such as tools, tabbed content groups, etc.
> 
> You actually don't have to use it, Adaptive works perfectly fine without it. The
> `cookbook-app-echo` project in the `cookbook` directory shows how to start such a UI.
> 

```text
AbstractWorkspace
  ServerWorkspace
  ClientWorkspace
    Workspace
```

### Module

- a class that extends `AppModule`
- during application startup you add modules to the application

## Application startup

At the moment I use these two startup types. Android and iOS is lagging behind as I
had no time to apply the application concept on those platforms, but that should
be quite straightforward. I'll do it when I get there.

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

### JS - browser - Workspace based

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