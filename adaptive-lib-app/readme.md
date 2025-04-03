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
- get a client id from the server
- call `resourceInit` for all modules
- load resources
- create the service transport to the server
- create the client backend
  - call `backendAdapterInit` for all modules
  - actualize the main backend fragment
- get the session from the server (if exists)
- build the workspace
  - call `contextInit` for all modules
  - call `workspaceInit` for all modules
- create the client frontend
  - call `frontendAdapterInit` for all modules
  - call `workspaceInit` for all modules
  - actualize the main frontend fragment
 
## Workspace based UI

This UI features an IntelliJ IDEA inspired workspace with tools at side/bottom and tabbed main content.

Focus is on browser for now. Theoretically can run on any platform, but a some platform dependent fragments
are not currently implemented on Android/iOS/Desktop, hence "theoretically".

Initialization involves an `index.html` (examples in site, iot, sandbox) and a `main.kt`. This example
shows content of `main.kt` for the IoT app.

```kotlin
fun main() {
    wsBrowserClient {
        module { LibUiClientModule() }
        module { GroveRuntimeModule() }
        module { AuthClientModule() }
        module { ValueClientModule() }
        module { ChartWsModule() }
        module { DocWsModule() }
        module { IotWsModule() }
        module { ZigbeeModule() }
        module { AppAdminWsModule() }
        module { AppInspectWsModule() }
        module { AppMainWsModule() }
        module { AppAuthWsModule() }
        module { IotAppWsModule() }
    }
}
```

The modules build up the application registries such as fragments, wire formats, workspace tools
etc.

By convention, module names ending with `WsModule` are workspace aware ones. They typically
register tools, content panes, item types for the workspace.

You can check any of the modules above to see how to initialize workspace elements. The code
is quite straightforward.

The backend and the frontend are initialized by actualizing fragments with
`AppMainWsModule.BACKEND_MAIN_KEY` and `AppMainWsModule.FRONTEND_MAIN_KEY`, respectively.

If you use the `AppMainWsModule`, these keys point to fragments from `adaptive-lib-app`,
which - hopefully - provide reasonable defaults.

- `wsAppBackendMain`
- `wsAppFrontendMain`

You can easily access the application from any fragment:

```kotlin
val app = fragment().wsApplication
```

> [!NOTE]
>
> Note that `wsApplication` returns with a `ClientApplication<Workspace>`. This is because
> `WsBrowserApplication` is platform dependent while `ClientApplication` is not. Hence, from
> common code you can access only `ClientApplication`.
> 
