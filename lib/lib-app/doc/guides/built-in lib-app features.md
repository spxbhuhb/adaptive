# Built-in lib-app features

Focus is on browser for now. Theoretically can run on any [platform](def://), but some [dependent ui fragments](def://)
are not yet implemented on Android/iOS/Desktop, hence "theoretically".

## Application modules

[lib-app](def://) provides a number of commonly used [application modules](def://). With
these you can quickly bootstrap your application development and customize these later if needed.

The modules build up the application registries such as fragments, wire formats, workspace tools,
etc.

By convention, module names ending with `Mpw` are [multi-pane workspace](def://)-aware ones. They typically
register tools, content panes, item types for the workspace.

You can check any of the modules above to see how to initialize workspace elements. The code
is quite straightforward.

### Server

- [AppMainModuleServer](class://) is a generic server [application module](def://) that starts all registered workers and services.

### Client

#### Main (entry point) modules

- [AppMainModuleBasic](class://) is a generic client [application module](def://) with `basic` [user interface](def://). 
- [AppMainModuleMpw](class://) is a client [application module](def://) with a [multi-pane workspace](def://) [user interface](def://).

The backend and the frontend are initialized by actualizing fragments with
`BACKEND_MAIN_KEY` and `FRONTEND_MAIN_KEY`, respectively.

By default, these keys point to fragments from `lib-app`, which - hopefully - provide reasonable defaults.

You can access the application from any fragment:

```kotlin
val app = fragment().application
```

or, in the case of [multi-pane workspace](def://)

```kotlin
val app = fragment().mpwApplication
```

#### MPW modules

- [AppAdminModuleMpw](class://) adds the [Admin tool](#admin-tool) to the workspace.
- [AppInspectModuleMpw](class://) adds the [Inspect tool](#inspect-tool) to the workspace.
- [AppAuthModuleMpw](class://) adds authentication and authorization related panes to the workspace, see [Fragments](#fragments)

## Fragments

[lib-app](def://) provides a number of commonly used UI fragments such as sign-in, account manager, etc. With
these fragments you can quickly bootstrap your application development and customize these later if needed.

In the following list:

- `common` means that the fragment is available both for `basic` and [multi-pane workspace](def://) [user interfaces](def://)
- `MPW` means that the fragment is available only for the [multi-pane workspace](def://) [user interface](def://)

- Authentication:
    - [signIn](fragment://) - common
    - sign out action - MPW
- User account:
    - [accountSelf](fragment://) - common
- Management:
    - [accountManager](fragment://) - common, admin plugin (see [Admin tool](#admin-tool))
    - [roleManager](fragment://) - common, admin plugin (see [Admin tool](#admin-tool))
    - [adminTool](fragment://) - MPW
- Development:
    - [appInspect](fragment://) - common
    - [appInspectTool](fragment://) - MPW

## Admin tool

The admin tool provides a centralized list of administration plugins for [multi-pane workspace](def://).

The tool shows a list of the plugins, built automatically from the content of [plugins](property://AppAdminModuleMpw).

To add a plugin, use the [addAdminItem](function://) function.

## Inspect tool

Some - hopefully useful - tools for inspecting the client application.

Shows:

- installed modules
- session
- registered wire format
