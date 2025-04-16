package `fun`.adaptive.iot.driver

import `fun`.adaptive.iot.driver.TestSupport.Companion.driverTest
import `fun`.adaptive.iot.driver.announcement.AdaControllerCommissioned
import `fun`.adaptive.iot.driver.request.AdrCommissionController
import `fun`.adaptive.iot.driver.test.TestControllerSpec
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.clearedTestPath
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ControllerCommissionTest {

    @Test
    fun `controller commission with empty driver`() = driverTest(clearedTestPath()) {

        val controller = newController(uuid4()) { TestControllerSpec() }

        assertFails {
            AdrCommissionController(uuid7(), controller.uuid, controller).process()
        }

        val driverController = driverValueWorker.itemOrNull(controller.uuid)
        assertNull(driverController)
        assertTrue { isAnnouncementQueueEmpty }
    }

    @Test
    fun `controller commission with invalid network`() = driverTest(clearedTestPath()) {

        commissionTestNetwork()

        val controller = newController(uuid4()) { TestControllerSpec() }.copy(parentId = uuid4())

        assertFails {
            AdrCommissionController(uuid7(), controller.uuid, controller).process()
        }

        val driverNetwork = driverValueWorker.itemOrNull(controller.uuid)
        assertNull(driverNetwork)
        assertTrue { isAnnouncementQueueEmpty }
    }

    @Test
    fun `controller commission with empty network`() = driverTest(clearedTestPath()) {

        val network = commissionTestNetwork()

        val controller = newController(uuid4()) { TestControllerSpec() }.copy(parentId = network.uuid)

        AdrCommissionController(uuid7(), controller.uuid, controller).process()

        val driverController = driverValueWorker.itemOrNull(controller.uuid)
        assertNotNull(driverController)
        assertEquals(controller, driverController)

        val announcement = getAnnouncement<AdaControllerCommissioned<TestControllerSpec>>()
        assertEquals(network.uuid, announcement.networkId)
        assertEquals(controller.uuid, announcement.item.uuid)
    }

    @Test
    fun `controller recommission`() = driverTest(clearedTestPath()) {

        val network = commissionTestNetwork()
        val controller = newController(uuid4()) { TestControllerSpec() }.copy(parentId = network.uuid)

        AdrCommissionController(uuid7(), controller.uuid, controller).process()
        getAnnouncement<AdaControllerCommissioned<TestControllerSpec>>()

        val modController = controller.copy(name = "renamed")
        AdrCommissionController(uuid7(), modController.uuid, modController).process()

        val driverController = driverValueWorker.itemOrNull(controller.uuid)
        assertNotNull(driverController)
        assertEquals(driverController, modController)

        val announcement = getAnnouncement<AdaControllerCommissioned<TestControllerSpec>>()
        assertEquals(network.uuid, announcement.networkId)
        assertEquals(modController.uuid, announcement.item.uuid)
        assertEquals(modController.name, announcement.item.name)
    }

}