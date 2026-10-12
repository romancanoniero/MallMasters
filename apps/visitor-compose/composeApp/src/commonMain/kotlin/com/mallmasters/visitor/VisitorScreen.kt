package com.mallmasters.visitor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VisitorScreen() {
    var query by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf<Int?>(null) }
    var selected by remember { mutableStateOf<Shop?>(null) }
    val results = Navigation.search(DemoMall.shops, query, floor)
    val route = selected?.let { Navigation.route(DemoMall.nodes, DemoMall.edges, "entry", it.nodeId, true) }
    MaterialTheme {
        Scaffold(topBar = { Surface { Text("MALL MASTERS · VISITOR", modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.titleLarge) } }) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                Text("ION Orchard · Concepto no cartográfico", style = MaterialTheme.typography.titleMedium)
                Text("Los pisos y comercios son ficticios; no navegar usando este prototipo.", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Buscar local o categoría") })
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.padding(vertical = 12.dp)) {
                    FilterChip(selected = floor == null, onClick = { floor = null }, label = { Text("Todas") })
                    DemoMall.mall.floors.forEach { f ->
                        FilterChip(selected = floor == f, onClick = { floor = if (floor == f) null else f }, label = { Text("L$f") })
                    }
                }
                if (selected != null) {
                    Text("Destino: ${selected!!.name} · L${selected!!.floor}")
                    Text(route?.let { "Ruta simulada ${it.distanceMeters.toInt()} m, ${it.nodeIds.size} puntos" } ?: "Ruta no disponible")
                    TextButton(onClick = { selected = null }) { Text("Quitar ruta") }
                }
                HorizontalDivider()
                LazyColumn {
                    items(results, key = { it.id }) { shop ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column { Text(shop.name); Text("L${shop.floor} · ${shop.category}", style = MaterialTheme.typography.bodySmall) }
                            TextButton(onClick = { selected = shop }) { Text("Ruta") }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
