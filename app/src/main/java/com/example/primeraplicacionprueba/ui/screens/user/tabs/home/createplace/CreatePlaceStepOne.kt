package com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.R
import androidx.compose.ui.res.stringResource
import com.example.primeraplicacionprueba.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceStepOne(
    onNavigateToHome: () -> Unit = {},
    onNavigateToNext: () -> Unit = {}
) {
    var nombreLugar by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<PlaceType?>(null) }

    var nombreError by remember { mutableStateOf("") }
    var descripcionError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }
    var showAllCategories by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.White)
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

            HorizontalDivider(color = BorderLight, thickness = 1.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
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
                        .fillMaxWidth(0.25f)
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
                text = stringResource(R.string.txt_step_one_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Column {
                Text(
                    text = stringResource(R.string.txt_place_name),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = nombreLugar,
                    onValueChange = {
                        nombreLugar = it
                        if (nombreError.isNotEmpty()) nombreError = ""
                    },
                    placeholder = { Text(stringResource(R.string.txt_place_name_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nombreError.isNotEmpty(),
                    supportingText = if (nombreError.isNotEmpty()) {
                        { Text(nombreError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BorderLight,
                        focusedBorderColor = Secondary,
                        unfocusedContainerColor = BgLight,
                        focusedContainerColor = BgLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.txt_detailed_description),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
                        if (descripcionError.isNotEmpty()) descripcionError = ""
                    },
                    placeholder = { Text(stringResource(R.string.txt_description_placeholder)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    minLines = 5,
                    maxLines = 5,
                    isError = descripcionError.isNotEmpty(),
                    supportingText = if (descripcionError.isNotEmpty()) {
                        { Text(descripcionError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BorderLight,
                        focusedBorderColor = Secondary,
                        unfocusedContainerColor = BgLight,
                        focusedContainerColor = BgLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.txt_category),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CategoryCard(
                            icon = Icons.Default.Restaurant,
                            label = stringResource(R.string.place_type_restaurant),
                            type = PlaceType.RESTAURANT,
                            isSelected = selectedCategory == PlaceType.RESTAURANT,
                            onClick = { selectedCategory = PlaceType.RESTAURANT },
                            modifier = Modifier.weight(1f)
                        )
                        CategoryCard(
                            icon = Icons.Default.Coffee,
                            label = stringResource(R.string.place_type_cafe),
                            type = PlaceType.CAFE,
                            isSelected = selectedCategory == PlaceType.CAFE,
                            onClick = { selectedCategory = PlaceType.CAFE },
                            modifier = Modifier.weight(1f)
                        )
                        CategoryCard(
                            icon = Icons.Default.LocalBar,
                            label = stringResource(R.string.place_type_bar),
                            type = PlaceType.BAR,
                            isSelected = selectedCategory == PlaceType.BAR,
                            onClick = { selectedCategory = PlaceType.BAR },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CategoryCard(
                            icon = Icons.Default.Hotel,
                            label = stringResource(R.string.place_type_hotel),
                            type = PlaceType.HOTEL,
                            isSelected = selectedCategory == PlaceType.HOTEL,
                            onClick = { selectedCategory = PlaceType.HOTEL },
                            modifier = Modifier.weight(1f)
                        )
                        CategoryCard(
                            icon = Icons.Default.Museum,
                            label = stringResource(R.string.place_type_museum),
                            type = PlaceType.MUSEUM,
                            isSelected = selectedCategory == PlaceType.MUSEUM,
                            onClick = { selectedCategory = PlaceType.MUSEUM },
                            modifier = Modifier.weight(1f)
                        )
                        CategoryCard(
                            icon = Icons.Default.Fastfood,
                            label = stringResource(R.string.place_type_fast_food),
                            type = PlaceType.FAST_FOOD,
                            isSelected = selectedCategory == PlaceType.FAST_FOOD,
                            onClick = { selectedCategory = PlaceType.FAST_FOOD },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    if (showAllCategories) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CategoryCard(
                                icon = Icons.Default.ShoppingBag,
                                label = stringResource(R.string.place_type_shopping),
                                type = PlaceType.SHOPPING,
                                isSelected = selectedCategory == PlaceType.SHOPPING,
                                onClick = { selectedCategory = PlaceType.SHOPPING },
                                modifier = Modifier.weight(1f)
                            )
                            CategoryCard(
                                icon = Icons.Default.Park,
                                label = stringResource(R.string.place_type_park),
                                type = PlaceType.PARK,
                                isSelected = selectedCategory == PlaceType.PARK,
                                onClick = { selectedCategory = PlaceType.PARK },
                                modifier = Modifier.weight(1f)
                            )
                            CategoryCard(
                                icon = Icons.Default.LocalGasStation,
                                label = stringResource(R.string.place_type_gas_station),
                                type = PlaceType.GAS_STATION,
                                isSelected = selectedCategory == PlaceType.GAS_STATION,
                                onClick = { selectedCategory = PlaceType.GAS_STATION },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CategoryCard(
                                icon = Icons.Default.LocalPharmacy,
                                label = stringResource(R.string.place_type_pharmacy),
                                type = PlaceType.PHARMACY,
                                isSelected = selectedCategory == PlaceType.PHARMACY,
                                onClick = { selectedCategory = PlaceType.PHARMACY },
                                modifier = Modifier.weight(1f)
                            )
                            CategoryCard(
                                icon = Icons.Default.LocalHospital,
                                label = stringResource(R.string.place_type_hospital),
                                type = PlaceType.HOSPITAL,
                                isSelected = selectedCategory == PlaceType.HOSPITAL,
                                onClick = { selectedCategory = PlaceType.HOSPITAL },
                                modifier = Modifier.weight(1f)
                            )
                            CategoryCard(
                                icon = Icons.Default.AccountBalance,
                                label = stringResource(R.string.place_type_bank),
                                type = PlaceType.BANK,
                                isSelected = selectedCategory == PlaceType.BANK,
                                onClick = { selectedCategory = PlaceType.BANK },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CategoryCard(
                                icon = Icons.Default.FitnessCenter,
                                label = stringResource(R.string.place_type_gym),
                                type = PlaceType.GYM,
                                isSelected = selectedCategory == PlaceType.GYM,
                                onClick = { selectedCategory = PlaceType.GYM },
                                modifier = Modifier.weight(1f)
                            )
                            CategoryCard(
                                icon = Icons.Default.Movie,
                                label = stringResource(R.string.place_type_cinema),
                                type = PlaceType.CINEMA,
                                isSelected = selectedCategory == PlaceType.CINEMA,
                                onClick = { selectedCategory = PlaceType.CINEMA },
                                modifier = Modifier.weight(1f)
                            )
                            CategoryCard(
                                icon = Icons.Default.MoreHoriz,
                                label = stringResource(R.string.place_type_other),
                                type = PlaceType.OTHER,
                                isSelected = selectedCategory == PlaceType.OTHER,
                                onClick = { selectedCategory = PlaceType.OTHER },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    
                    Button(
                        onClick = { showAllCategories = !showAllCategories },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (showAllCategories) TextMuted else Secondary
                        )
                    ) {
                        Text(
                            text = if (showAllCategories) 
                                stringResource(R.string.txt_see_less_categories)
                            else 
                                stringResource(R.string.txt_see_more_categories),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                if (categoryError.isNotEmpty()) {
                    Text(
                        text = categoryError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onNavigateToNext() },
                modifier = Modifier
                    .fillMaxWidth()
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

@Composable
fun CategoryCard(
    icon: ImageVector,
    label: String,
    type: PlaceType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Tertiary else BgLight
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Secondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextDark,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}