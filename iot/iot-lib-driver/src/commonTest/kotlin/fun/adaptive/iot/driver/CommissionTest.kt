package `fun`.adaptive.iot.driver

import `fun`.adaptive.iot.driver.TestSupport.Companion.driverTest
import `fun`.adaptive.iot.driver.request.AdrCommissionNetwork
import `fun`.adaptive.iot.driver.test.TestNetworkSpec
import `fun`.adaptive.utility.clearedTestPath
import kotlin.test.Test
import kotlin.test.assertTrue

class CommissionTest {

    @Test
    fun `network commission with empty driver`() = driverTest(clearedTestPath()) {

        val network = newNetwork { TestNetworkSpec() }

        AdrCommissionNetwork<TestNetworkSpec>(network.uuid, network).process()

        val driverNetwork = driverValueWorker.itemOrNull(network.uuid)

        assertTrue { driverNetwork != null }
    }

}