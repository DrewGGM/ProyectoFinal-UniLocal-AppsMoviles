package com.example.primeraplicacionprueba.ui.screens.user.tabs.map.filtro

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.theme.BgLight
import com.example.primeraplicacionprueba.ui.theme.TextDark
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBusqueda(
    onNavigateBack: () -> Unit = {}
){
    var palabraClave by remember { mutableStateOf("") }
    var distancia by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var calificacion by remember { mutableStateOf("Cualquiera") }
    var abiertoAhora by remember { mutableStateOf(false) }

    val categorias = listOf("Restaurante", "Cafetería", "Museo", "Hotel", "Comidas rápidas")
    val categoriasSeleccionadas = remember { mutableStateListOf<String>() }

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF9F9F9)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //  Encabezado
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.txt_filtrosplace),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { onNavigateBack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.txt_back),
                                    tint = TextDark
                                )
                            }
                        },

                    )


            }

            // 🔍 Palabra clave
            Text("Palabra clave", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = palabraClave,
                onValueChange = { palabraClave = it },
                placeholder = { Text( text = stringResource(R.string.txt_find)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            // 🍽️ Categorías
            Text("Categorías", fontWeight = FontWeight.Medium)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { categoria ->
                    val seleccionada = categoria in categoriasSeleccionadas
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (seleccionada) Color(0xFF1976D2) else Color.White)
                            .border(
                                1.dp,
                                if (seleccionada) Color(0xFF1976D2) else Color.LightGray,
                                RoundedCornerShape(50)
                            )
                            .clickable() {
                                if (seleccionada) categoriasSeleccionadas.remove(categoria)
                                else categoriasSeleccionadas.add(categoria)
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = categoria,
                            color = if (seleccionada) Color.White else Color.Black
                        )
                    }
                }
            }

            // 📏 Distancia
            Text("Distancia (km)", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = distancia,
                onValueChange = { distancia = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            // 🏙️ Ciudad
            Text("Ciudad", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = ciudad,
                onValueChange = { ciudad = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            //  Calificación mínima
            Text("Calificación mínima", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = calificacion,
                onValueChange = { calificacion = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            //  Abierto ahora
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = abiertoAhora,
                    onCheckedChange = { abiertoAhora = it }
                )
                Text("Abierto ahora")
            }

            //  Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón "Limpiar"
                Button (
                    onClick = {
                        palabraClave = ""
                        distancia = ""
                        ciudad = ""
                        calificacion = "Cualquiera"
                        abiertoAhora = false
                        categoriasSeleccionadas.clear()
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Limpiar", color = Color.Black, fontWeight = FontWeight.Medium)
                }

                // Botón "Aplicar filtros"
                Button(
                    onClick = { /* aplicar filtros */ },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(65.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFFF6B6B), Color(0xFFFFA726))
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Aplicar filtros", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

}