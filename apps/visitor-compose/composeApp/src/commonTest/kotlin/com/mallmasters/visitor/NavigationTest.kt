package com.mallmasters.visitor

import kotlin.test.*

class NavigationTest {
    @Test fun categorySearchCaseInsensitive() {
        assertEquals("s4", Navigation.search(DemoMall.shops, "VIAJES").single().id)
    }
    @Test fun filtersByFloor() {
        assertTrue(Navigation.search(DemoMall.shops, "", 2).all { it.floor == 2 })
    }
    @Test fun routeBetweenFloors() {
        val route = Navigation.route(DemoMall.nodes, DemoMall.edges, "entry", "l2a", true)
        assertNotNull(route)
        assertEquals(listOf("entry", "lift0", "lift1", "lift2", "l2a"), route.nodeIds)
        assertEquals(51.0, route.distanceMeters)
    }
    @Test fun rejectsMissingNode() {
        assertNull(Navigation.route(DemoMall.nodes, DemoMall.edges, "entry", "invalid"))
    }
    @Test fun privacyRevocationAndExpiry() {
        assertTrue(ShareGrant("friend", 1000).permits("friend", 999))
        assertFalse(ShareGrant("friend", 1000).permits("friend", 1000))
        assertFalse(ShareGrant("friend", 1000, true).permits("friend", 999))
        assertFalse(ShareGrant("friend", 1000).permits("other", 999))
    }
}
