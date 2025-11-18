package com.example.primeraplicacionprueba.ui.screens.user.tabs.map.filtro

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.theme.BgLight
import com.example.primeraplicacionprueba.ui.theme.BorderLight
import com.example.primeraplicacionprueba.ui.theme.Primary
import com.example.primeraplicacionprueba.ui.theme.Secondary
import com.example.primeraplicacionprueba.ui.theme.TextDark
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.primeraplicacionprueba.model.MapFilters
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.ui.screens.LocalMainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBusqueda(
    onNavigateBack: () -> Unit = {}
){
    val placesViewModel = LocalMainViewModel.current.placesViewModel
    var palabraClave by remember { mutableStateOf("") }
    var distancia by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var calificacion by remember { mutableStateOf("Cualquiera") }
    var abiertoAhora by remember { mutableStateOf(false) }

    val categorias = listOf("Restaurante", "Cafetería", "Museo", "Hotel", "Comidas rápidas")
    val categoriasSeleccionadas = remember { mutableStateListOf<String>() }

    Dialog(
        onDismissRequest = { onNavigateBack() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxSize(0.9f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header fijo
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Primary, Secondary)
                            )
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.txt_filtrosplace),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.txt_close),
                            tint = Color.White
                        )
                    }
                }

                // Contenido con scroll
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // 🔍 Palabra clave
                    Text(
                        "Palabra clave",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TextDark
                    )
                    OutlinedTextField(
                        value = palabraClave,
                        onValueChange = { palabraClave = it },
                        placeholder = { Text(text = stringResource(R.string.txt_find)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = BorderLight,
                            focusedBorderColor = Secondary,
                            unfocusedContainerColor = BgLight,
                            focusedContainerColor = BgLight
                        )
                    )

                    // 🍽️ Categorías
                    Text(
                        "Categorías",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TextDark
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categorias.forEach { categoria ->
                            val seleccionada = categoria in categoriasSeleccionadas
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (seleccionada) Primary else Color.White)
                                    .border(
                                        2.dp,
                                        if (seleccionada) Primary else BorderLight,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        if (seleccionada) categoriasSeleccionadas.remove(categoria)
                                        else categoriasSeleccionadas.add(categoria)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = categoria,
                                    color = if (seleccionada) Color.White else TextDark,
                                    fontSize = 14.sp,
                                    fontWeight = if (seleccionada) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    // 📏 Distancia
                    Text(
                        "Distancia (km)",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TextDark
                    )
                    OutlinedTextField(
                        value = distancia,
                        onValueChange = { distancia = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = BorderLight,
                            focusedBorderColor = Secondary,
                            unfocusedContainerColor = BgLight,
                            focusedContainerColor = BgLight
                        )
                    )

                    // 🏙️ Ciudad
                    Text(
                        "Ciudad",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TextDark
                    )
                    OutlinedTextField(
                        value = ciudad,
                        onValueChange = { ciudad = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = BorderLight,
                            focusedBorderColor = Secondary,
                            unfocusedContainerColor = BgLight,
                            focusedContainerColor = BgLight
                        )
                    )

                    //  Calificación mínima
                    Text(
                        "Calificación mínima",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TextDark
                    )
                    OutlinedTextField(
                        value = calificacion,
                        onValueChange = { calificacion = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = BorderLight,
                            focusedBorderColor = Secondary,
                            unfocusedContainerColor = BgLight,
                            focusedContainerColor = BgLight
                        )
                    )

                    //  Abierto ahora
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = abiertoAhora,
                            onCheckedChange = { abiertoAhora = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Abierto ahora",
                            fontSize = 15.sp,
                            color = TextDark
                        )
                    }
                }

                // Botones fijos en la parte inferior
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón "Limpiar"
                    OutlinedButton(
                        onClick = {
                            palabraClave = ""
                            distancia = ""
                            ciudad = ""
                            calificacion = "Cualquiera"
                            abiertoAhora = false
                            categoriasSeleccionadas.clear()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(
                                colors = listOf(BorderLight, BorderLight)
                            )
                        )
                    ) {
                        Text(
                            "Limpiar",
                            color = TextDark,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }

                    // Botón "Aplicar filtros"
                    Button(
                        onClick = {
                            val selectedTypes = categoriasSeleccionadas.mapNotNull { nombre ->
                                when (nombre) {
                                    "Restaurante" -> PlaceType.RESTAURANT
                                    "Cafetería" -> PlaceType.CAFE
                                    "Museo" -> PlaceType.MUSEUM
                                    "Hotel" -> PlaceType.HOTEL
                                    "Comidas rápidas" -> PlaceType.FAST_FOOD
                                    else -> null
                                }
                            }.toSet()

                            val minRating = calificacion.trim()
                                .takeIf { it.isNotEmpty() && it != "Cualquiera" }?.toFloatOrNull()
                            val maxDist = distancia.trim().takeIf { it.isNotEmpty() }?.toFloatOrNull()

                            val filters = MapFilters(
                                keyword = palabraClave.ifBlank { null },
                                categories = selectedTypes,
                                city = ciudad.ifBlank { null },
                                minRating = minRating,
                                openNow = abiertoAhora,
                                maxDistanceKm = maxDist
                            )
                            placesViewModel.applyFilters(filters)
                            onNavigateBack()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Primary, Secondary)
                                    ),
                                    shape = RoundedCornerShape(28.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Aplicar filtros",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}