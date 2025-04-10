/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend.setting.provider

import `fun`.adaptive.backend.setting.model.Setting
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use


/**
 * Delegates setting operations to other providers
 */
class DelegatingSettingProvider : SettingProvider {

    private val lock = getLock()

    private var providers = emptyList<SettingProvider>()

    override var isReadOnly: Boolean = true

    /**
     * Add a setting provider. The last provider added is called first for all
     * API calls.
     */
    operator fun plusAssign(provider: SettingProvider) {
        lock.use {
            providers = listOf(provider) + providers
            if (!provider.isReadOnly) isReadOnly = false
        }
    }

    override fun put(path: String, value: String?) {
        check(!isReadOnly) { "settings in this application are read-only" }

        for (provider in lock.use { providers }) {
            if (provider.isReadOnly) continue
            provider.put(path, value)
            return
        }
    }

    override fun get(path: String, children: Boolean): List<Setting> {
        for (provider in lock.use { providers }) {
            val result = provider.get(path, children)
            if (result.isNotEmpty()) return result
        }
        return emptyList()
    }
}