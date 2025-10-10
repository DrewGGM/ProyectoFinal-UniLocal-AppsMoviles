package com.example.primeraplicacionprueba.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.ui.theme.Primary
import com.example.primeraplicacionprueba.ui.theme.TextSecondary
import com.example.primeraplicacionprueba.ui.theme.TextMuted
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun  <T>Search(
        query: String,
        onQueryChange: (String) -> Unit,
        onSearch: (String) -> List<T>,
        placeholder: String,
        itemText: (T) -> String,
        expanded: Boolean,
        onExpandedChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),

                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { onQueryChange(it) },
                        onSearch = { onExpandedChange(false) },
                        expanded = expanded,
                        onExpandedChange = { onExpandedChange(it) },
                        placeholder = {
                            Text(
                                text = placeholder,
                                color = Color(0xFF9E9E9E), // gris suave
                                fontSize = 18.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Color(0xFFFFC1CC) // color rosado del icono
                            )
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White,
                    dividerColor = Color(0xFFE0E0E0),
                    inputFieldColors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            ) {
                // Si no usamos overlay, podemos no mostrar resultados
                val places = if (expanded && query.isNotEmpty()) onSearch(query) else emptyList<T>()
                if (expanded && places.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 12.dp)
                    ) {
                        items(places) { item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onExpandedChange(false) }
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = itemText(item),
                                    color = Color(0xFF4A4A4A),
                                    fontSize = 16.sp
                                )
                                HorizontalDivider(
                                    color = Color(0xFFF0F0F0),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

    }