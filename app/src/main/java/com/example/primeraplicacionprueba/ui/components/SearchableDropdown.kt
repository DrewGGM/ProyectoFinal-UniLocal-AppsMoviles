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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var textFieldValue by rememberSaveable { mutableStateOf(selectedValue) }

    LaunchedEffect(selectedValue) {
        if (!expanded) {
            textFieldValue = selectedValue
            searchQuery = ""
        }
    }

    val filteredOptions by derivedStateOf {
        if (searchQuery.isBlank()) {
            options
        } else {
            options.filter { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    Box(modifier = modifier) {
        Column {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    searchQuery = newValue
                    if (!expanded) {
                        expanded = true
                    }
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
                                        textFieldValue = ""
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

            if (isError && errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
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
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    }
                    filteredOptions.isEmpty() && searchQuery.isNotEmpty() -> {
                        Text(
                            text = stringResource(R.string.txt_no_search_results),
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    filteredOptions.isEmpty() -> {
                        Text(
                            text = stringResource(R.string.txt_no_options_available),
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    else -> {
                        filteredOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    onValueChange(option)
                                    expanded = false
                                    searchQuery = ""
                                    textFieldValue = option
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
}
