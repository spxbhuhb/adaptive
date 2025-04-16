package `fun`.adaptive.iot.driver

import `fun`.adaptive.iot.driver.TestSupport.Companion.driverTest
import `fun`.adaptive.iot.driver.announcement.AdaNetworkCommissioned
import `fun`.adaptive.iot.driver.request.AdrCommissionNetwork
import `fun`.adaptive.iot.driver.test.TestNetworkSpec
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.clearedTestPath
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NetworkCommissionTest {

    @Test
    fun `network commission with empty driver`() = driverTest(clearedTestPath()) {

        val network = newNetwork { TestNetworkSpec() }

        AdrCommissionNetwork<TestNetworkSpec>(uuid7(), network.uuid, network).process()

        val driverNetwork = driverValueWorker.itemOrNull(network.uuid)
        assertNotNull(driverNetwork)
        assertEquals(network.uuid, driverNetwork.uuid)

        val announcement = getAnnouncement<AdaNetworkCommissioned<*>>()
        assertEquals(network.uuid, announcement.networkId)

    }

    @Test
    fun `network commission with non-empty driver`() = driverTest(clearedTestPath()) {

        val network = newNetwork { TestNetworkSpec() }

        AdrCommissionNetwork<TestNetworkSpec>(uuid7(), network.uuid, network).process()
        getAnnouncement<AdaNetworkCommissioned<*>>()

        val modNetwork = network.copy(name = "renamed")

        AdrCommissionNetwork<TestNetworkSpec>(uuid7(), modNetwork.uuid, modNetwork).process()

        val driverNetwork = driverValueWorker.itemOrNull(network.uuid)
        assertNotNull(driverNetwork)
        assertEquals(modNetwork.name, driverNetwork.name)

        val announcement = getAnnouncement<AdaNetworkCommissioned<*>>()
        assertEquals(network.uuid, announcement.networkId)
        assertEquals(modNetwork.name, announcement.item.name)

    }

    @Test
    fun `network commission with invalid uuid`() = driverTest(clearedTestPath()) {

        val network = newNetwork { TestNetworkSpec() }

        AdrCommissionNetwork<TestNetworkSpec>(uuid7(), network.uuid, network).process()
        getAnnouncement<AdaNetworkCommissioned<*>>()

        val modNetwork = network.copy(uuid = uuid4())

        assertFails { AdrCommissionNetwork<TestNetworkSpec>(uuid7(), modNetwork.uuid, modNetwork).process() }

        val driverNetwork = driverValueWorker.itemOrNull(network.uuid)
        assertNotNull(driverNetwork)
        assertEquals(network.name, driverNetwork.name)
        assertTrue { isAnnouncementQueueEmpty }

    }

}