/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.backend.BackendContext
import `fun`.adaptive.auto.backend.PropertyBackend
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.connector.ServiceConnector
import `fun`.adaptive.auto.frontend.AdatClassFrontend
import `fun`.adaptive.auto.worker.AutoWorker
import `fun`.adaptive.server.query.firstImpl
import `fun`.adaptive.service.getService
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Test
import kotlin.test.assertEquals

/**
 * These tests **SHOULD NOT** run parallel, check `junit-platform.properties`.
 */
class PropertyBackendIntegrationTest {

    @Test
    fun basic() {
        autoTest { originAdapter, connectingAdapter ->

            val scope = CoroutineScope(Dispatchers.Default)
            val itemId = LamportTimestamp(0, 0) // does not matter for PropertyBackend

            val autoService = getService<AutoApi>() // service consumer from connecting to origin

            val originWorker = originAdapter.firstImpl<AutoWorker>()
            val connectingWorker = connectingAdapter.firstImpl<AutoWorker>()

            val connectInfo = getService<AutoTestApi>().testInstance()
            val originHandle = connectInfo.originHandle
            val connectingHandle = connectInfo.connectingHandle

            val connectingContext = BackendContext(
                connectingHandle,
                scope,
                ProtoWireFormatProvider(),
                TestData.adatMetadata,
                TestData.adatWireFormat,
                true,
                LamportTimestamp(connectingHandle.clientId, 0),
            )

            val connectingBackend = PropertyBackend(
                connectingContext,
                itemId,
                null,
                null
            )

            val connectingFrontend = AdatClassFrontend(
                connectingBackend,
                TestData,
                null,
                null
            )

            connectingBackend.frontEnd = connectingFrontend

            connectingWorker.register(connectingBackend)

            connectingBackend.addPeer(
                ServiceConnector(originHandle, autoService, scope, 1000),
                connectInfo.originTime
            )

            autoService.addPeer(originHandle, connectingHandle, connectingBackend.context.time)

            waitForSync(originWorker, originHandle, connectingWorker, connectingHandle)

            val originBackend = originWorker[originHandle] as PropertyBackend
            @Suppress("UNCHECKED_CAST")
            val originFrontend = (originBackend.frontEnd as AdatClassFrontend<TestData>)

            connectingFrontend.modify("i", 23)
            waitForSync(originWorker, originHandle, connectingWorker, connectingHandle)

            assertEquals(23, originFrontend.value !!.i)

            originFrontend.modify("i", 34)
            waitForSync(originWorker, originHandle, connectingWorker, connectingHandle)
            assertEquals(34, connectingFrontend.value !!.i)
        }
    }

}