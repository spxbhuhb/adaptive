---
title: How is the Source Code of an Adaptive Project Organized
tags: [ lib-app, application, project ]
type: conceptual
---

# Summary

This guide shows how the source directory structure of an Adaptive project is organized.

# Objective

Explain the directory structure of an Adaptive project with the following parameters:

- name: `My Project`
- build system: Gradle
- platform: JVM and browser
- workspace-based

# Key Concepts

Adaptive is a Kotlin/Multiplatform application development system. An application built with Adaptive
typically uses Gradle as its build system. And follows the standard directory structure of a
Kotlin/Multiplatform project with some additions following Adaptive conventions.

The following directory structure demonstrates the usual convention of a single-module
Adaptive project focusing on JVM and browser platforms.

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

## Components

This section highlights the directories and files that are specific to Adaptive projects,
excluding standard elements related to Gradle or Kotlin/JS.

- `src/commonMain/adaptiveResources`:
    - `documents`: Structured document content such as markdown help files or license texts used across platforms.
    - `graphics`: Vector graphics like SVGs intended for cross-platform UI rendering.
    - `images`: Bitmap or raster images (e.g., PNG, JPG) used in UI elements.
    - `files`: Miscellaneous static resources, such as raw text or configuration files that are packaged with the app.
    - `fonts`: Custom font files used in the application UI.
    - `strings`: Localized or structured string resources for internationalization.
- `src/commonMain/kotlin/my.project`: Shared application code that targets all supported platforms.
- `src/jsMain/kotlin/main.kt`: JavaScript-specific application entry point.
- `src/jsMain/resources/index.html`: Shell HTML file for the browser platform.
- `src/jvmMain/kotlin/main.kt`: Entry point for the JVM platform.
- `src/jvmMain/resources/logback.xml`: JVM-specific logging configuration.

## Behavior or Flow

- The `adaptiveResources` directory is packaged with both JVM and JS targets by the Adaptive Gradle plugin.
- Platform-specific entry points (`main.kt`) bootstrap the application.

# See also

[What is an Application](what_is_an_application.md)

# Conclusion

This guide outlined the source code structure of an Adaptive project, highlighting how it aligns
with Kotlin Multiplatform conventions while introducing Adaptive-specific directories for shared
resources. By separating UI assets, localized content, and platform entry points, the structure
supports scalable, cross-platform application development.

Understanding this layout enables developers to confidently add features, debug issues, and
extend projects across both JVM and browser targets.