/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "adaptive"

includeBuild("core/core-build") {
    // This block is crucial for making the plugins from core-build available.
    // By default, composite builds do not expose their plugins to the main build unless specified.
    // The `dependencySubstitution` ensures that any request for the plugin ID
    // provided by `core-build` is satisfied by this included build.
    // This is especially important if you were to define a `plugins { id("...") version "..." }`
    // block in your main settings.gradle.kts or root build.gradle.kts for your own plugins.
    //
    // For simple cases, `pluginManagement` + `includeBuild` is often enough without explicit
    // `dependencySubstitution` for direct plugin application in subprojects.
    // However, it's good practice for clarity or if you're substituting other plugins.
    //
    // For direct plugin application, the `includeBuild` call itself is
    // sufficient to make the plugins visible. No explicit substitution is required here unless
    // you are trying to *override* a plugin from a repository.
}

includeBuild("core/core-core")
includeBuild("core/core-gradle-plugin")
includeBuild("core/core-kotlin-plugin")
includeBuild("core/core-ui")

includeBuild("doc/doc-example")
includeBuild("doc/doc-main")

includeBuild("grove/grove-app")
includeBuild("grove/grove-doc")
includeBuild("grove/grove-host")
includeBuild("grove/grove-lib")
includeBuild("grove/grove-runtime")

includeBuild("lib/lib-app")
includeBuild("lib/lib-auth")
includeBuild("lib/lib-chart")
includeBuild("lib/lib-document")
includeBuild("lib/lib-graphics")
includeBuild("lib/lib-ktor")
includeBuild("lib/lib-process")
includeBuild("lib/lib-test")
includeBuild("lib/lib-ui")
includeBuild("lib/lib-ui-mpw")
includeBuild("lib/lib-util")
includeBuild("lib/lib-value")

includeBuild("sandbox/sandbox-app")
includeBuild("sandbox/sandbox-app-echo")
includeBuild("sandbox/sandbox-app-mpw")

includeBuild("site/site-app")
includeBuild("site/site-lib-cookbook")