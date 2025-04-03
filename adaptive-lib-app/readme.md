# Lib App

`lib-app` provides a framework for composing (pun intended) client and server applications
in a somewhat simple, well-defined way.

Note: you can build applications that use parts of Adaptive without using `lib-app`. It just
does not worth it IMHO.

The library is built on three main concepts:

- application
- module
- workspace

An application:

- in this context means a client or a server, no matter of the actual platform the application runs
- is composed of modules
- has exactly one workspace, the workspace contains the contexts of the application

Application classes (Android/iOS is not ready yet, but will follow the same concept):

```kotlin
AbstractApplication
  ServerApplication
    JvmServerApplication
  ClientApplication
    BrowserApplication
      WsBrowserApplication
```

An application module is a class that extends `AppModule`. During application startup these modules
are added to the application:

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

or

```kotlin
fun main() {
    wsBrowserClient {
        module { LibUiModule() }
        module { DocWsModule() }
        module { BasicAppWsModule() }
    }
}
```

## Workspace based UI

This UI features an IntelliJ IDEA inspired workspace with tools at side/bottom and tabbed main content.

Focus is on browser for now. Theoretically can run on any platform, but a some platform dependent fragments
are not currently implemented on Android/iOS/Desktop, hence "theoretically".

Initialization involves an `index.html` (examples in site, iot, sandbox) and a `main.js`:

```kotlin
fun main() {
    WsBrowserApplication(
        LibUiModule(),
        GroveRuntimeModule(),
        ChartWsModule(),
        DocWsModule(),
        IotWsModule(),
        ZigbeeModule(),
        BasicAppWsModule(),
        IotAppWsModule()
    ).main()
}
```

The modules build up the application registries such as fragments, wire formats, workspace tools
etc.

By convention, module names ending with `WsModule` are workspace aware ones. They typically
register tools, content panes, item types for the workspace.

Modules with the `<WorkSpace>` type parameter are actually workspace independent, they can work with
any module context.

Module functions are called in this order during application startup:

- `wireFormatInit`
- `resourceInit`
- `backendAdapterInit`
- `frontendAdapterInit`
- `workspaceInit`

You can check any of the modules above to see how to initialize workspace elements. The code
is quite straightforward.

The backend and the frontend are initialized by actualizing fragments with
`BasicAppWsModule.BACKEND_MAIN_KEY` and `BasicAppWsModule.FRONTEND_MAIN_KEY`, respectively.

If you use the `BasicAppWsModule`, these keys point to fragments from `adaptive-lib-app-basic`,
which - hopefully - provide reasonable defaults.

- `wsAppBackendMain`
- `wsAppFrontendMain`

Both main fragments are initialized in a local context which provides the application instance,
`WsBrowserApplication` in the example.

You can easily access this context from any fragment.

> [!NOTE]
>
> Note that here we look for `UiClientApplication`. This is because `WsBrowserApplication` is platform
> dependent while `UiClientApplication` is not. Hence, from common code you can access only `UiClientApplication`.

```kotlin
val app = fragment().firstContext<UiClientApplication<*,*>>()
```

## Server side

The server side setup is very similar to client side:

