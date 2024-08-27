[![Maven Central](https://img.shields.io/maven-central/v/fun.adaptive/adaptive-core)](https://mvnrepository.com/artifact/fun.adaptive/adaptive-core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

> [!CAUTION]
>
> Project status: **initial development**
>
> Please note the project status, many basic things you would expect to work are still
> broken, especially around the code transformation.
>
> Preview release is scheduled for the last week of August.
>

* [What is Adaptive](./doc/what-is-adaptive.md)
* [Status](./doc/status.md)
* [Getting Started](./doc/getting-started.md)
* [Tools](./doc/tools.md)
* [User Interface](./doc/ui/readme.md)
* [Server](./doc/server/readme.md)
* [Impressum](./doc/impressum.md)
* [Deep Waters](./doc/internals)

> [!IMPORTANT]
> 
> Adaptive uses the K2 compiler heavily. Starting from IntelliJ IDEA 2024.2 K2 support is available for multiplatform
> projects. To have it, you have to enable K2 and the non-bundled plugins.
>
> 1. Enable K2:
>   1. Settings
>   2. Languages & Frameworks
>   3. Kotlin
>   4. Enable K2 mode
>
> 
> 1. Enable 3rd party compiler plugins in the IntelliJ registry
>   1. https://youtrack.jetbrains.com/issue/KTIJ-29248/K2-IDE-Enable-non-bundled-compiler-plugins-in-IDE-by-default
>   2. `Shift-Shift` 
>   3. select `Actions` on top
>   4. type in `Registry`
>   5. find `kotlin.k2.only.bundled.compiler.plugins.enabled` and set it OFF
> 
> Technically you could avoid the registry setting by adding some boilerplate manually, but I think it's just
> easier to have it. Also, the issue above is scheduled for 2024.3, so hopefully this setting won't be needed
> anymore.
>