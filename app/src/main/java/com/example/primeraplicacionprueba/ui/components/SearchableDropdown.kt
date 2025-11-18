package com.example.primeraplicacionprueba.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.primeraplicacionprueba.R
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableDropdown(
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Mostrar el valor seleccionado o el texto de búsqueda
    val displayText = if (expanded) searchQuery else selectedValue

    // Filtrar opciones basado en la búsqueda
    val filteredOptions = remember(searchQuery, options) {
        if (searchQuery.isBlank()) {
            options
        } else {
            options.filter { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    // Actualizar displayText cuando cambia selectedValue
    LaunchedEffect(selectedValue) {
        if (!expanded) {
            searchQuery = ""
        }
    }

    Box(modifier = modifier) {
        Column {
            OutlinedTextField(
                value = displayText,
                onValueChange = { newValue ->
                    searchQuery = newValue
                    expanded = true
                },
                label = { Text(label) },
                placeholder = { Text(placeholder) },
                enabled = enabled && !isLoading,
                isError = isError,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && !expanded) {
                            expanded = true
                            searchQuery = ""
                        }
                    },
                trailingIcon = {
                    Row {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            if (selectedValue.isNotEmpty() && enabled && !expanded) {
                                IconButton(
                                    onClick = {
                                        onValueChange("")
                                        searchQuery = ""
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = stringResource(R.string.cd_clear)
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = stringResource(R.string.cd_dropdown)
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.outline,
                    disabledLabelColor = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Mensaje de error
            if (isError && errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        // Dropdown menu con lista filtrada
        DropdownMenu(
            expanded = expanded && filteredOptions.isNotEmpty(),
            onDismissRequest = {
                expanded = false
                searchQuery = ""
            },
            properties = PopupProperties(focusable = false),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (filteredOptions.isEmpty() && searchQuery.isNotEmpty()) {
                    // Mensaje si no hay resultados
                    Text(
                        text = "No se encontraron resultados",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    filteredOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onValueChange(option)
                                expanded = false
                                searchQuery = ""
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = if (option == selectedValue) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        )
                    }
                }
            }
        }
    }
}
