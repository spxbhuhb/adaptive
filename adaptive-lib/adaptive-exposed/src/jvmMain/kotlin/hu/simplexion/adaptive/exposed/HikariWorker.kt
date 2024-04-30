/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.exposed

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import hu.simplexion.adaptive.server.component.WorkerImpl
import hu.simplexion.adaptive.settings.dsl.setting
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.exposed.sql.Database

class HikariWorker : WorkerImpl<HikariWorker> {

    val driver by setting<String> { "JDBC_DRIVER" }
    val url by setting<String> { "JDBC_URL"}
    val username by setting<String> { "JDBC_USERNAME" }
    val password by setting<String> { "JDBC_PASSWORD"} // TODO secret

    override suspend fun run(scope: CoroutineScope) {
        val config = HikariConfig().also {
            it.driverClassName = driver
            it.jdbcUrl = url
            it.username = username
            it.password = password
            it.maximumPoolSize = 10
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }
}