package com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import com.example.primeraplicacionprueba.R
import androidx.compose.ui.res.stringResource
import com.example.primeraplicacionprueba.ui.theme.*
import com.example.primeraplicacionprueba.model.Day
import com.example.primeraplicacionprueba.model.Shedule
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import java.time.LocalTime
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

data class DaySchedule(
    val day: String,
    val openTime: MutableState<String> = mutableStateOf(""),
    val closeTime: MutableState<String> = mutableStateOf(""),
    val isClosed: MutableState<Boolean> = mutableStateOf(false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceStepThree(
    viewModel: PlacesViewModel,
    onNavigateToHome: () -> Unit = {},
    onNavigateToPrevious: () -> Unit = {},
    onNavigateToNext: () -> Unit = {}
) {
    val state by viewModel.createPlaceState.collectAsState()

    val mondayText = stringResource(R.string.txt_monday)
    val tuesdayText = stringResource(R.string.txt_tuesday)
    val wednesdayText = stringResource(R.string.txt_wednesday)
    val thursdayText = stringResource(R.string.txt_thursday)
    val fridayText = stringResource(R.string.txt_friday)
    val saturdayText = stringResource(R.string.txt_saturday)
    val sundayText = stringResource(R.string.txt_sunday)
    
    val schedules = remember {
        mutableStateListOf(
            DaySchedule(mondayText),
            DaySchedule(tuesdayText),
            DaySchedule(wednesdayText),
            DaySchedule(thursdayText),
            DaySchedule(fridayText),
            DaySchedule(saturdayText),
            DaySchedule(sundayText)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(48.dp))
            Text(
                text = stringResource(R.string.txt_add_new_place),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { onNavigateToHome() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.txt_close),
                    tint = TextDark
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = BorderLight)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Barra de progreso - 75%
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BgLight)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.75f)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Primary,
                                    Secondary
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.txt_step_three_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = stringResource(R.string.txt_schedule_description),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            // Grid de horarios - 2 columnas
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                schedules.chunked(2).forEach { rowSchedules ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowSchedules.forEach { schedule ->
                            ScheduleCard(
                                schedule = schedule,
                                onOpenTimeChange = { schedule.openTime.value = it },
                                onCloseTimeChange = { schedule.closeTime.value = it },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Si solo hay un elemento en la fila, agregar spacer
                        if (rowSchedules.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { onNavigateToPrevious() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                        brush = Brush.horizontalGradient(
                            colors = listOf(BorderLight, BorderLight)
                        )
                    )
                ) {
                    Text(
                        text = stringResource(R.string.txt_previous),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                }

                Button(
                    onClick = {
                        // Convertir DaySchedule a Shedule y guardar en el ViewModel
                        // Los horarios son opcionales, si no se llenan se omiten
                        val sheduleList = schedules.mapNotNull { daySchedule ->
                            if (daySchedule.openTime.value.isNotBlank() && daySchedule.closeTime.value.isNotBlank()) {
                                try {
                                    // Agregar :00 si el usuario solo puso HH
                                    val openTimeFormatted = if (daySchedule.openTime.value.contains(":")) {
                                        daySchedule.openTime.value
                                    } else {
                                        "${daySchedule.openTime.value}:00"
                                    }

                                    val closeTimeFormatted = if (daySchedule.closeTime.value.contains(":")) {
                                        daySchedule.closeTime.value
                                    } else {
                                        "${daySchedule.closeTime.value}:00"
                                    }

                                    val day = when (daySchedule.day.lowercase()) {
                                        "lunes", "monday" -> Day.MONDAY
                                        "martes", "tuesday" -> Day.TUESDAY
                                        "mi\u00e9rcoles", "miercoles", "wednesday" -> Day.WEDNESDAY
                                        "jueves", "thursday" -> Day.THURSDAY
                                        "viernes", "friday" -> Day.FRIDAY
                                        "s\u00e1bado", "sabado", "saturday" -> Day.SATURDAY
                                        "domingo", "sunday" -> Day.SUNDAY
                                        else -> null
                                    }

                                    day?.let {
                                        Shedule(
                                            day = it,
                                            openHour = openTimeFormatted,
                                            closeHour = closeTimeFormatted
                                        )
                                    }
                                } catch (e: Exception) {
                                    // Si hay error de formato, ignorar este día
                                    null
                                }
                            } else null
                        }.filterNotNull()

                        // Guardar horarios (puede ser lista vacía si no se configuró ninguno)
                        viewModel.updateCreateState(state.copy(schedule = sheduleList))
                        onNavigateToNext()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Primary,
                                        AccentOrange
                                    )
                                ),
                                shape = RoundedCornerShape(28.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.txt_next),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleCard(
    schedule: DaySchedule,
    onOpenTimeChange: (String) -> Unit,
    onCloseTimeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = BgLight
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = schedule.day,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )

            // Campo de hora de apertura
            val openInvalid = schedule.openTime.value.isNotBlank() && !schedule.openTime.value.matches(Regex("^([01]\\d|2[0-3]):[0-5]\\d$")) && schedule.openTime.value.length >= 4
            OutlinedTextField(
                value = schedule.openTime.value,
                onValueChange = { newValue ->
                    // Filtrar para solo permitir números y :
                    val filtered = newValue.filter { it.isDigit() || it == ':' }
                    // Limitar a formato HH:mm (5 caracteres)
                    if (filtered.length <= 5) {
                        onOpenTimeChange(filtered)
                    }
                },
                placeholder = { Text(stringResource(R.string.placeholder_time_hhmm), fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BorderLight,
                    focusedBorderColor = Secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(8.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                isError = openInvalid,
                supportingText = if (openInvalid) {
                    { Text(text = stringResource(R.string.placeholder_time_hhmm), color = MaterialTheme.colorScheme.error, fontSize = 11.sp) }
                } else null
            )

            // Campo de hora de cierre
            val closeInvalid = schedule.closeTime.value.isNotBlank() && !schedule.closeTime.value.matches(Regex("^([01]\\d|2[0-3]):[0-5]\\d$")) && schedule.closeTime.value.length >= 4
            OutlinedTextField(
                value = schedule.closeTime.value,
                onValueChange = { newValue ->
                    // Filtrar para solo permitir números y :
                    val filtered = newValue.filter { it.isDigit() || it == ':' }
                    // Limitar a formato HH:mm (5 caracteres)
                    if (filtered.length <= 5) {
                        onCloseTimeChange(filtered)
                    }
                },
                placeholder = { Text(stringResource(R.string.placeholder_time_hhmm), fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BorderLight,
                    focusedBorderColor = Secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(8.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                isError = closeInvalid,
                supportingText = if (closeInvalid) {
                    { Text(text = stringResource(R.string.placeholder_time_hhmm), color = MaterialTheme.colorScheme.error, fontSize = 11.sp) }
                } else null
            )
        }
    }
}