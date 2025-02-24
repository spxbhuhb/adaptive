[![Maven Central](https://img.shields.io/maven-central/v/fun.adaptive/adaptive-core)](https://mvnrepository.com/artifact/fun.adaptive/adaptive-core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

**Maven Central version is _VERY_ outdated. I'll make new release when someone asks for it.**

> [!CAUTION]
>
> Project status: **preview - stage 1**
>
> While the features below are all working, they are NOT complete. I typically add only what I need, 
> at some places the documentation is lacking, etc.
> 
> For now, Adaptive is a kind of personal project. With a scope this big it is not a realistic
> expectation that people drop mature solutions in favor of something in preview state.
> 
> That said,
> 
> - it is realistic to go on and reach the point where the project is actually useful for others,
> - I do use Adaptive in production environment, it works.
> 
> If you're interested, feel free to explore, contribute, or provide feedback—your input is valuable
> in shaping Adaptive’s future!
> 

Adaptive is a low-code application development system for Kotlin Multiplatform, featuring:

- fully reactive UI
- manually coded and no-code UI fragments
- manually coded and no-code data classes
- seamless client-server communication with simple function calls
- automatic data replication between nodes (client-client, client-server, server-server)
- multiplatform resource management
- very clean, easy to read UI code syntax
- UI fragment library, editors etc.

Upcoming features (near future, already in development):

- General purpose lexer/parser generation from ANTLR grammars
- Kotlin lexer/parser to build Kotlin AST
- Kotlin syntax coloring
- UI fragments:
  - split pane and workspace with multiple, resizable panes 
  - general purpose drawing sheet for graphical editors
  - date, time, date time editor

For support and news, please join the `#fun-adaptive` channel on [kotlinlang](https://slack-chats.kotlinlang.org/).

Here are some Medium articles I've written about Adaptive, it is best to start with these I think:

* [A life without Compose](https://medium.com/@tiz_26128/a-life-without-compose-5b77a9a8129f)
* [I'm done with SVG](https://medium.com/@tiz_26128/im-done-with-svg-f3339118dcde)
* [The state of Adaptive - 2024](https://medium.com/@tiz_26128/the-state-of-adaptive-2024-6a6fca3d632b)

These documentation topics are a bit outdated but still they provide good information:

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
> Currently only IntelliJ 2024.3.1 and Kotlin 2.1.0 is supported. As of now I do not support any older versions
> because the library is still in the initial development phase.
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
4. find `kotlin.k2.only.bundled.compiler.plugins.enabled` and set it **OFF**
 
Technically you could avoid the registry setting by adding some boilerplate manually, but I think it's just
easier to have it. Also, the issue above is scheduled for 2024.3, so hopefully this setting won't be needed
anymore.

**Import issues**

There are a few import related issues when in K2 mode, see [KTIJ-31100](https://youtrack.jetbrains.com/issue/KTIJ-31100/K2-Incorrect-auto-completion-for-escaped-package-names)
for details. Hopefully as K2 matures these will go away.
