package com.example.primeraplicacionprueba.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.ui.theme.Accent
import com.example.primeraplicacionprueba.ui.theme.BgLight
import com.example.primeraplicacionprueba.ui.theme.BorderLight
import com.example.primeraplicacionprueba.ui.theme.Primary
import com.example.primeraplicacionprueba.ui.theme.Secondary
import com.example.primeraplicacionprueba.ui.theme.TextDark
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
        onItemClick: (T) -> Unit = {},
        modifier: Modifier = Modifier,
        itemContent: @Composable ((T, () -> Unit) -> Unit)? = null
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 18.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.cd_search),
                                tint = Secondary
                            )
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    dividerColor = MaterialTheme.colorScheme.outlineVariant,
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
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(places) { item ->
                            if (itemContent != null) {
                                // Usar contenido personalizado
                                itemContent(item) {
                                    onItemClick(item)
                                    onExpandedChange(false)
                                }
                            } else {
                                // Usar contenido por defecto (solo texto)
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onItemClick(item)
                                            onExpandedChange(false)
                                        }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = itemText(item),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 16.sp
                                    )
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outlineVariant,
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

    }

@Composable
fun PlaceSearchItem(
    place: Place,
    rating: Float,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen del lugar
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                if (place.imagenes.isNotEmpty()) {
                    AsyncImage(
                        model = place.imagenes.first(),
                        contentDescription = place.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.Center),
                        tint = Secondary
                    )
                }
            }

            // Información del lugar
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Título
                Text(
                    text = place.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                // Tipo/Categoría
                Text(
                    text = place.type.name.lowercase().replaceFirstChar { it.titlecase() },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Calificación
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Accent
                    )
                    Text(
                        text = String.format("%.1f", rating),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Ciudad
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = place.city,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}