# Source code organization

Applications built with Adaptive typically use Gradle as its build system. Adaptive 
projects use the standard directory structure of a Kotlin/Multiplatform project with some additions.

The following directory structure demonstrates the usual convention of a single-module
Adaptive project focusing on JVM and browser [platforms](def://).

```text
my.project
├── etc
│   └── my.project.properties
├── kotlin-js-store
│   └── skip-yarn-lock
├── gradle
│   ├── wrapper
│   │   ├── gradle-wrapper.jar
│   │   └── gradle-wrapper.properties
│   └── lib.versions.toml
├── doc
│   ├── definitions
│   ├── guides
│   └── internals
├── src
│   └── commonMain
│   │   ├── adaptiveResources
│   │   │   ├── documents
│   │   │   ├── graphics
│   │   │   ├── images
│   │   │   ├── files
│   │   │   ├── fonts
│   │   │   └── strings
│   │   │       └── strings.xml
│   │   └── kotlin
│   │       └── my.project
│   ├── jsMain
│   │   ├── kotlin
│   │   │   └── main.kt
│   │   └── resources
│   │       └── index.html
│   └── jvmMain
│       ├── kotlin
│       │   └── main.kt
│       └── resources
│           └── logback.xml
├── webpack.config.d
│   └── config.js
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle.kts
```

- `doc` - The documentation of the project.
  - `definitions` - Short [definitions](def://) that define the vocabulary of the project.
  - `guides` - Longer [guides](def://) that explain concepts, provide examples etc.
  - `internals` - Internal documents useful for development but not necessary for the users of the project.
- `src/commonMain/adaptiveResources`:
    - `documents`: Structured document content such as markdown help files or license texts.
    - `graphics`: Vector graphics like SVGs intended for cross-platform UI rendering.
    - `images`: Bitmap or raster images (e.g., PNG, JPG) used in UI elements.
    - `files`: Miscellaneous static resources, such as raw text or configuration files that are packaged with the app.
    - `fonts`: Custom font files used in the application UI.
    - `strings`: Localized or structured string resources for internationalization.
- `src/commonMain/kotlin/my.project`: Shared application code that targets all supported [platforms](def://).
- `src/jsMain/kotlin/main.kt`: JavaScript-specific application entry point.
- `src/jsMain/resources/index.html`: Shell HTML file for the browser [platform](def://).
- `src/jvmMain/kotlin/main.kt`: Entry point for the JVM [platform](def://).
- `src/jvmMain/resources/logback.xml`: JVM-specific logging configuration.

## Behavior

- The `adaptiveResources` directory is packaged with both JVM and JS targets by the Adaptive Gradle plugin.
- [Platform-dependent](def://) entry points (`main.kt`) bootstrap the [applications](def://).

# See also

- [Documentation system](guide://)
- [Application?](guide://)