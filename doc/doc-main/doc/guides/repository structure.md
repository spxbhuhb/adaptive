# Repository structure

The git repository of [Adaptive](def://) is structured as follows:

```text
<root>
├── .github      - files related to GitHub (policies, templates, etc.)
├── core         - core subprojects
│   ├── core-core
│   ├── core-gradle-plugin
│   ├── core-kotlin-plugin
│   └── core-ui
├── doc          - documentation subprojects
├── gradle       - gradle wrapper and libs.versions.toml
├── grove        - grove subprojects
│   ├── grove-app
│   ├── grove-doc
│   ├── grove-host
│   ├── grove-lib
│   └── grove-runtime
├── lib          - general library subprojects
│   ├── lib-app
│   ├── lib-auth
│   ├── lib-chart
│   ├── lib-document
│   ├── lib-graphics
│   ├── lib-ktor
│   ├── lib-process
│   ├── lib-test
│   ├── lib-ui
│   ├── lib-util
│   └── lib-value
├── sandbox      - sandbox subprojects for experimenting
│   ├── sandbox-app
│   └── sandbox-app-echo
└── site         - code of https://adaptive.fun
    ├── site-app
    └── site-lib-cookbook
```

## Core subprojects

Core consists of the subprojects:

[core-core](def://)

The lowest level [subproject](def://) of Adaptive. Contains foundational functionality used by all
other subprojects, including the [Gradle plugin](def://) and the Kotlin [compiler plugin](def://).

[core-gradle-plugin](def://)

Contains the [Gradle plugin](def://) that applies the [compiler plugin](def://) to source codes, 
compiles resources and performs other build-related tasks.

[core-kotlin-plugin](def://)

Contains the Kotlin [compiler plugin](def://) that transforms Kotlin source code (reactivity, 
data model, API support, serialization, etc.)

[core-ui](def://)

Foundations for [user interfaces](def://). [Platform-dependent](def://) UI implementations,
[layout system](guide://) basic [UI fragments](def://).

## Documentation subprojects

[doc-examples](def://)

Examples that cannot be placed into other [subprojects](def://) because of dependencies.

[doc-main](def://)

Meta-documentation (such as this file) describing overall concepts, the documentation system itself.

## Grove subprojects

Grove is (will be) an application that supports application development with [Adaptive](def://).
For now, it is very preliminary; only [grove-doc](def://) is used actively as it contains
the documentation compiler.

[grove-app](def://)

The Grove application to-be. There was a time when it worked, but as of now it is not in focus.

[grove-doc](def://)

[Documentation compiler](def://) to build the documentation of [Adaptive](def://) and the documentation
of other projects built with [Adaptive](def://).

[grove-host](def://)

Source code of the [Grove Host](def://) application, a supervisor for other [Adaptive](def://) applications.

[grove-lib](def://)

A collection of Grove library features such as the UI fragment designer, data model architect, etc.
These are not well-developed yet, but some functionality such as [grove sheet](def://), a drawing 
component already work.

[grove-runtime](def://)

A library that applications developed with [Adaptive](def://) use to access some advanced features such
as [fragment hydration](def://), on-demand load of [fragment models](def://), etc.

## Lib subprojects

[lib-app](def://)

Contains the framework to glue [application modules](def://) together into complete [applications](def://).
This includes some [platform-dependent](def://) parts such as browser or JVM application startup and
some commonly used application components such as a list of users, password change, sign-in forms, etc.

[lib-auth](def://)

Authentication, role-based authorization and session management. Does not contain UI, that is in [lib-app](def://).

[lib-chart](def://)

Chart drawing from data series. Works but needs some improvements and cleanup.

[lib-document](def://)

Functions to parse, transform, create and display documents such as Markdown files, Excel sheets, etc.

[finish repository structure](todo://)
