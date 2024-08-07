/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auto.integration

import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.api.AutoApi
import hu.simplexion.adaptive.auto.backend.BackendContext
import hu.simplexion.adaptive.auto.backend.PropertyBackend
import hu.simplexion.adaptive.auto.backend.TestData
import hu.simplexion.adaptive.auto.connector.ServiceConnector
import hu.simplexion.adaptive.auto.frontend.AdatClassFrontend
import hu.simplexion.adaptive.auto.worker.AutoWorker
import hu.simplexion.adaptive.server.query.firstImpl
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Test
import kotlin.test.assertEquals

/**
 * These tests **SHOULD NOT** run parallel, check `junit-platform.properties`.
 */
class PropertyBackendTest {

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
                true,
                LamportTimestamp(connectingHandle.clientId, 0),
            )

            val connectingBackend = PropertyBackend(
                connectingContext,
                itemId,
                TestData.adatWireFormat.propertyWireFormats
            )

            val connectingFrontend = AdatClassFrontend(
                connectingBackend,
                TestData,
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