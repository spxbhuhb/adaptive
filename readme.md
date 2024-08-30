[![Maven Central](https://img.shields.io/maven-central/v/fun.adaptive/adaptive-core)](https://mvnrepository.com/artifact/fun.adaptive/adaptive-core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

> [!CAUTION]
>
> Project status: **preview**
>
> Please note the project status, many basic things you would expect to work are still
> broken, especially around the code transformation.
>

For support, please join the `#fun-adaptive` channel on [kotlinlang](https://slack-chats.kotlinlang.org/).

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

**Enable K2**

1. Settings
2. Languages & Frameworks
3. Kotlin
4. Enable K2 mode

 
**Enable 3rd party compiler plugins in the IntelliJ registry**

See [KTIJ-29248](https://youtrack.jetbrains.com/issue/KTIJ-29248/K2-IDE-Enable-non-bundled-compiler-plugins-in-IDE-by-default) for details.

1. `Shift-Shift`
2. select `Actions` on top
3. type in `Registry`
4. find `kotlin.k2.only.bundled.compiler.plugins.enabled` and set it OFF
 
Technically you could avoid the registry setting by adding some boilerplate manually, but I think it's just
easier to have it. Also, the issue above is scheduled for 2024.3, so hopefully this setting won't be needed
anymore.

**Import issues**

There are a few import related issues when in K2 mode, see [KTIJ-31100](https://youtrack.jetbrains.com/issue/KTIJ-31100/K2-Incorrect-auto-completion-for-escaped-package-names)
for details. Hopefully as K2 matures these will go away.