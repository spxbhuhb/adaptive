/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "adaptive"

includeBuild("core/core-core")
includeBuild("core/core-kotlin-plugin")
includeBuild("core/core-gradle-plugin")
includeBuild("core/core-ui")

includeBuild("doc/doc-example")
includeBuild("doc/doc-main")

includeBuild("grove/grove-app")
includeBuild("grove/grove-doc")
includeBuild("grove/grove-lib")
includeBuild("grove/grove-runtime")

includeBuild("lib/lib-app")
includeBuild("lib/lib-auth")
includeBuild("lib/lib-chart")
includeBuild("lib/lib-graphics")
includeBuild("lib/lib-ktor")
includeBuild("lib/lib-document")
includeBuild("lib/lib-process")
includeBuild("lib/lib-test")
includeBuild("lib/lib-ui")
includeBuild("lib/lib-util")
includeBuild("lib/lib-value")

includeBuild("sandbox/sandbox-app-echo")
includeBuild("sandbox/sandbox-app")

includeBuild("site/site-app")
includeBuild("site/site-lib-cookbook")

includeBuild("supervisor/supervisor-app")