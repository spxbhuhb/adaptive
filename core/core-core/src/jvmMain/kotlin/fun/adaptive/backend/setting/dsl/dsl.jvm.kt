/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend.setting.dsl

import `fun`.adaptive.backend.setting.provider.EnvironmentSettingProvider
import `fun`.adaptive.backend.setting.provider.PropertyFileSettingProvider
import `fun`.adaptive.backend.setting.provider.DelegatingSettingProvider
import java.nio.file.Paths

fun DelegatingSettingProvider.environment(prefix: () -> String) {
    this += EnvironmentSettingProvider(prefix())
}


fun DelegatingSettingProvider.propertyFile(optional: Boolean = true, path: () -> String) {
    this += PropertyFileSettingProvider(Paths.get(path()), optional)
}