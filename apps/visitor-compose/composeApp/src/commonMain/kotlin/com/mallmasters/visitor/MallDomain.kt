package com.mallmasters.visitor

data class Mall(val id: String, val name: String, val demo: Boolean, val floors: List<Int>)
data class Shop(val id: String, val name: String, val category: String, val floor: Int, val nodeId: String)
data class Node(val id: String, val floor: Int, val label: String)
data class Edge(val from: String, val to: String, val meters: Double, val accessible: Boolean = true)
data class Route(val nodeIds: List<String>, val distanceMeters: Double)
data class LocationEstimate(val nodeId: String?, val floor: Int?, val confidence: Double, val timestampMillis: Long)
data class ShareGrant(val recipientId: String, val expiresAtMillis: Long, val revoked: Boolean = false) {
    fun permits(recipient: String, now: Long): Boolean = !revoked && recipient == recipientId && now < expiresAtMillis
}

object DemoMall {
    val mall = Mall("ion-concept", "ION Orchard · Concepto", true, listOf(0, 1, 2))
    val shops = listOf(
        Shop("s1", "Moda Central", "Moda", 2, "l2a"),
        Shop("s2", "Tecnología", "Electrónica", 2, "l2b"),
        Shop("s3", "Café Atrio", "Gastronomía", 1, "l1a"),
        Shop("s4", "Equipaje", "Viajes", 1, "l1b"),
        Shop("s5", "Servicios", "Información", 0, "l0a"),
        Shop("s6", "Belleza", "Belleza", 0, "l0b")
    )
    val nodes = listOf(
        Node("entry", 0, "Entrada"), Node("lift0", 0, "Ascensor L0"),
        Node("lift1", 1, "Ascensor L1"), Node("lift2", 2, "Ascensor L2"),
        Node("l0a", 0, "Servicios"), Node("l0b", 0, "Belleza"),
        Node("l1a", 1, "Café Atrio"), Node("l1b", 1, "Equipaje"),
        Node("l2a", 2, "Moda Central"), Node("l2b", 2, "Tecnología")
    )
    val edges = listOf(
        Edge("entry", "lift0", 20.0),
        Edge("lift0", "lift1", 8.0), Edge("lift1", "lift2", 8.0),
        Edge("lift0", "l0a", 12.0), Edge("lift0", "l0b", 25.0),
        Edge("lift1", "l1a", 14.0), Edge("lift1", "l1b", 26.0),
        Edge("lift2", "l2a", 15.0), Edge("lift2", "l2b", 24.0)
    )
}

object Navigation {
    fun search(shops: List<Shop>, query: String, floor: Int? = null): List<Shop> {
        val q = query.trim()
        return shops.filter { (floor == null || it.floor == floor) &&
            (q.isBlank() || it.name.contains(q, true) || it.category.contains(q, true)) }
    }

    fun route(nodes: List<Node>, edges: List<Edge>, from: String, to: String, accessibleOnly: Boolean = false): Route? {
        if (from !in nodes.map { it.id } || to !in nodes.map { it.id }) return null
        val distances = mutableMapOf(from to 0.0)
        val previous = mutableMapOf<String, String>()
        val pending = nodes.map { it.id }.toMutableSet()
        while (pending.isNotEmpty()) {
            val current = pending.minByOrNull { distances[it] ?: Double.POSITIVE_INFINITY } ?: break
            val best = distances[current] ?: break
            if (current == to) break
            pending.remove(current)
            edges.filter { !accessibleOnly || it.accessible }.forEach { edge ->
                val next = when (current) { edge.from -> edge.to; edge.to -> edge.from; else -> null }
                if (next != null && next in pending && edge.meters >= 0.0 && edge.meters.isFinite()) {
                    val value = best + edge.meters
                    if (value < (distances[next] ?: Double.POSITIVE_INFINITY)) {
                        distances[next] = value; previous[next] = current
                    }
                }
            }
        }
        val total = distances[to] ?: return null
        val path = mutableListOf(to)
        while (path.last() != from) { path.add(previous[path.last()] ?: return null) }
        return Route(path.reversed(), total)
    }
}
