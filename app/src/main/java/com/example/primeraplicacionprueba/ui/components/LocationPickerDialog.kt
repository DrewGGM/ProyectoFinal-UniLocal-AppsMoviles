package com.example.primeraplicacionprueba.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.primeraplicacionprueba.ui.theme.SuccessGreen
import com.example.primeraplicacionprueba.ui.theme.TextDark
import com.example.primeraplicacionprueba.ui.theme.Secondary
import com.mapbox.geojson.Point
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.components.Map as PlacesMap

@Composable
fun LocationPickerDialog(
    onDismiss: () -> Unit,
    onLocationSelected: (Point) -> Unit,
    initialLocation: Point? = null
) {
    var selectedLocation by remember { mutableStateOf(initialLocation) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Secondary)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.txt_select_location),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.cd_close),
                                tint = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        PlacesMap(
                            modifierr = Modifier.fillMaxSize(),
                            activateClick = true,
                            onMapClickListener = { point ->
                                selectedLocation = point
                            },
                            centerPoint = selectedLocation ?: Point.fromLngLat(-75.6811, 4.5339),
                            centerZoom = if (selectedLocation != null) 15.0 else 13.0
                        )

                        if (selectedLocation != null) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp)
                                    .background(SuccessGreen, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.txt_location_selected),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(R.string.btn_cancel))
                        }

                        Button(
                            onClick = {
                                selectedLocation?.let {
                                    onLocationSelected(it)
                                    onDismiss()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = selectedLocation != null,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Secondary,
                                contentColor = TextDark
                            )
                        ) {
                            Text(stringResource(R.string.btn_confirm))
                        }
                    }
                }
            }
        }
    }
}

