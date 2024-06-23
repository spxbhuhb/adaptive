# Code organization

The UI fragments are sorted into namespaces, for example `common`, `canvas`, `svg`.

Each namespace has a package in `commonMain`, `hu.simplexion.adaptive.grapics.canvas` for the `canvas` namespace for example.

The layout of this package shall be the same for all namespaces as detailed below. Empty directories
may be omitted.

`.`
* entry function (optional)
* adapter class for the namespace
* base fragment class for the namespace (optional)
* namespace declaration
* fragment factory

`fragment`
* declarations of platform-independent fragments (`@Adaptive`)
* expect declarations for manually implemented fragments (`@AdaptiveExpect`)
* actual declarations for manually implemented fragments (`@AdaptiveActual`)

`instruction`
* instruction classes and helper functions

`platform`
* platform specific UI classes (container views, UI abstraction implementations such as `ActualCanvas`)
* resource support (`with<platform>Resources`)
* media support (`mediaMetrics`)

`render`
* render data class declarations

`support`

* classes that store common functionality for platform-specific implementations