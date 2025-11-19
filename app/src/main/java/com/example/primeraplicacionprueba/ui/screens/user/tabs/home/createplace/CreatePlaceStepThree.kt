package com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
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

    // Cargar horarios existentes del estado (para modo edición)
    val schedules = remember(state.schedule) {
        val dayTexts = listOf(mondayText, tuesdayText, wednesdayText, thursdayText, fridayText, saturdayText, sundayText)
        val days = listOf(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY, Day.SUNDAY)

        mutableStateListOf<DaySchedule>().apply {
            dayTexts.forEachIndexed { index, dayText ->
                val existingSchedule = state.schedule.find { it.day == days[index] }
                add(
                    DaySchedule(
                        day = dayText,
                        openTime = mutableStateOf(existingSchedule?.openHour ?: ""),
                        closeTime = mutableStateOf(existingSchedule?.closeHour ?: "")
                    )
                )
            }
        }
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
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { onNavigateToHome() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.txt_close),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)

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
                    .background(MaterialTheme.colorScheme.surfaceVariant)
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
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(R.string.txt_schedule_description),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                            colors = listOf(MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.outline)
                        )
                    )
                ) {
                    Text(
                        text = stringResource(R.string.txt_previous),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCard(
    schedule: DaySchedule,
    onOpenTimeChange: (String) -> Unit,
    onCloseTimeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showOpenTimePicker by remember { mutableStateOf(false) }
    var showCloseTimePicker by remember { mutableStateOf(false) }

    // Parse existing time or use current time
    val parseTime = { timeStr: String ->
        if (timeStr.isNotBlank() && timeStr.contains(":")) {
            val parts = timeStr.split(":")
            Pair(parts[0].toIntOrNull() ?: 9, parts[1].toIntOrNull() ?: 0)
        } else {
            Pair(9, 0)
        }
    }

    val (openHour, openMinute) = parseTime(schedule.openTime.value)
    val (closeHour, closeMinute) = parseTime(schedule.closeTime.value)

    val openTimePickerState = rememberTimePickerState(
        initialHour = openHour,
        initialMinute = openMinute,
        is24Hour = true
    )

    val closeTimePickerState = rememberTimePickerState(
        initialHour = closeHour,
        initialMinute = closeMinute,
        is24Hour = true
    )

    // Diálogo para hora de apertura
    if (showOpenTimePicker) {
        AlertDialog(
            onDismissRequest = { showOpenTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val time = String.format("%02d:%02d", openTimePickerState.hour, openTimePickerState.minute)
                    onOpenTimeChange(time)
                    showOpenTimePicker = false
                }) {
                    Text(stringResource(R.string.txt_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showOpenTimePicker = false }) {
                    Text(stringResource(R.string.txt_cancel))
                }
            },
            text = {
                TimePicker(state = openTimePickerState)
            }
        )
    }

    // Diálogo para hora de cierre
    if (showCloseTimePicker) {
        AlertDialog(
            onDismissRequest = { showCloseTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val time = String.format("%02d:%02d", closeTimePickerState.hour, closeTimePickerState.minute)
                    onCloseTimeChange(time)
                    showCloseTimePicker = false
                }) {
                    Text(stringResource(R.string.txt_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCloseTimePicker = false }) {
                    Text(stringResource(R.string.txt_cancel))
                }
            },
            text = {
                TimePicker(state = closeTimePickerState)
            }
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = schedule.day,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Botón para hora de apertura
            OutlinedButton(
                onClick = { showOpenTimePicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (schedule.openTime.value.isBlank())
                            stringResource(R.string.txt_opening_time)
                        else schedule.openTime.value,
                        fontSize = 13.sp,
                        color = if (schedule.openTime.value.isBlank())
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Secondary
                    )
                }
            }

            // Botón para hora de cierre
            OutlinedButton(
                onClick = { showCloseTimePicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (schedule.closeTime.value.isBlank())
                            stringResource(R.string.txt_closing_time)
                        else schedule.closeTime.value,
                        fontSize = 13.sp,
                        color = if (schedule.closeTime.value.isBlank())
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Secondary
                    )
                }
            }
        }
    }
}