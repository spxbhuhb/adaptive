/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "adaptive"

includeBuild("adaptive-core")
includeBuild("adaptive-kotlin-plugin")
includeBuild("adaptive-gradle-plugin")
includeBuild("adaptive-grove")
includeBuild("adaptive-grove-runtime")
includeBuild("adaptive-lib-app")
includeBuild("adaptive-lib-auth")
includeBuild("adaptive-lib-auto")
includeBuild("adaptive-lib-chart")
includeBuild("adaptive-lib-cookbook")
includeBuild("adaptive-lib-graphics")
includeBuild("adaptive-lib-exposed")
includeBuild("adaptive-lib-email")
includeBuild("adaptive-lib-ktor")
includeBuild("adaptive-lib-document")
includeBuild("adaptive-lib-process")
includeBuild("adaptive-lib-sandbox")
includeBuild("adaptive-lib-ui")
includeBuild("adaptive-lib-util")
includeBuild("adaptive-lib-value")
includeBuild("adaptive-ui")

includeBuild("cookbook/cookbook-app-echo")

includeBuild("iot/adaptive-iot-app")
includeBuild("iot/adaptive-iot-cli")
includeBuild("iot/adaptive-iot-lib-core")
includeBuild("iot/adaptive-iot-lib-zigbee")

includeBuild("grove")
includeBuild("sandbox")
includeBuild("site")