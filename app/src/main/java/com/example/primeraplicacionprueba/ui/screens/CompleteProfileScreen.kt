package com.example.primeraplicacionprueba.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.components.OperationResultHandler
import com.example.primeraplicacionprueba.ui.components.SearchableDropdown
import com.example.primeraplicacionprueba.viewmodel.LocationViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.primeraplicacionprueba.ui.theme.*

@Composable
fun CompleteProfileScreen(
    user: User,
    onNavigateToHome: (User) -> Unit = {}
) {
    val mainViewModel = LocalMainViewModel.current
    val usersViewModel = mainViewModel.usersViewModel
    val context = LocalContext.current

    // LocationViewModel para países y ciudades
    val locationViewModel: LocationViewModel = viewModel()
    val countries by locationViewModel.countries.collectAsState()
    val cities by locationViewModel.cities.collectAsState()
    val isLoadingCountries by locationViewModel.isLoadingCountries.collectAsState()
    val isLoadingCities by locationViewModel.isLoadingCities.collectAsState()

    // Estados para los campos
    var email by remember { mutableStateOf(user.email) }
    var username by remember { mutableStateOf(user.username) }
    var city by remember { mutableStateOf(user.city) }
    var country by remember { mutableStateOf(user.country) }

    // Efecto para cargar ciudades cuando se selecciona un país
    LaunchedEffect(country) {
        if (country.isNotEmpty()) {
            locationViewModel.loadCitiesByCountry(country)
        } else {
            locationViewModel.resetCities()
            city = "" // Limpiar ciudad cuando se cambia o limpia el país
        }
    }

    // Estados para validación
    var emailError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var cityError by remember { mutableStateOf("") }
    var countryError by remember { mutableStateOf("") }

    val userResult by usersViewModel.userResult.collectAsState()

    // Función de validación
    fun validarCampos(): Boolean {
        var isValid = true

        // Validar email (solo si el usuario original tenía email vacío)
        if (user.email.isBlank()) {
            if (email.isBlank()) {
                emailError = context.getString(R.string.txt_email_required)
                isValid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailError = context.getString(R.string.txt_email_invalid)
                isValid = false
            } else {
                emailError = ""
            }
        }

        // Validar username
        if (username.isBlank()) {
            usernameError = context.getString(R.string.txt_errorGeneral)
            isValid = false
        } else if (username.length < 3) {
            usernameError = context.getString(R.string.txt_username_error_short)
            isValid = false
        } else {
            usernameError = ""
        }

        // Validar ciudad
        if (city.isBlank()) {
            cityError = context.getString(R.string.txt_errorGeneral)
            isValid = false
        } else {
            cityError = ""
        }

        // Validar país
        if (country.isBlank()) {
            countryError = context.getString(R.string.txt_errorGeneral)
            isValid = false
        } else {
            countryError = ""
        }

        return isValid
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 32.dp,
                                    topEnd = 32.dp,
                                    bottomStart = 50.dp,
                                    bottomEnd = 50.dp
                                )
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Primary, Secondary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.txt_complete_profile_title),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.txt_complete_profile_subtitle),
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.txt_welcome) + ", ${user.nombre}!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = stringResource(R.string.txt_complete_profile_message),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (user.email.isBlank()) {
                            OutlinedTextField(
                                value = email,
                                onValueChange = {
                                    email = it
                                    if (emailError.isNotEmpty()) emailError = ""
                                },
                                label = { Text(stringResource(R.string.txt_email_label)) },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                isError = emailError.isNotEmpty(),
                                supportingText = if (emailError.isNotEmpty()) {
                                    { Text(emailError, color = MaterialTheme.colorScheme.error) }
                                } else null,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Secondary,
                                    focusedLabelColor = Secondary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        OutlinedTextField(
                            value = username,
                            onValueChange = {
                                username = it
                                if (usernameError.isNotEmpty()) usernameError = ""
                            },
                            label = { Text(stringResource(R.string.txt_username_hint)) },
                            modifier = Modifier.fillMaxWidth(),
                            isError = usernameError.isNotEmpty(),
                            supportingText = if (usernameError.isNotEmpty()) {
                                { Text(usernameError, color = MaterialTheme.colorScheme.error) }
                            } else null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Secondary,
                                focusedLabelColor = Secondary,
                                unfocusedBorderColor = BorderLight
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        SearchableDropdown(
                            label = stringResource(R.string.txt_country),
                            selectedValue = country,
                            options = countries,
                            onValueChange = {
                                country = it
                                if (countryError.isNotEmpty()) countryError = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isLoading = isLoadingCountries,
                            placeholder = stringResource(R.string.txt_select_country_placeholder),
                            isError = countryError.isNotEmpty(),
                            errorMessage = countryError.ifEmpty { null }
                        )

                        SearchableDropdown(
                            label = stringResource(R.string.txt_city),
                            selectedValue = city,
                            options = cities,
                            onValueChange = {
                                city = it
                                if (cityError.isNotEmpty()) cityError = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = country.isNotEmpty(),
                            isLoading = isLoadingCities,
                            placeholder = if (country.isEmpty()) stringResource(R.string.txt_select_country_first) else stringResource(R.string.txt_select_city_placeholder),
                            isError = cityError.isNotEmpty(),
                            errorMessage = cityError.ifEmpty { null }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OperationResultHandler(
                            result = userResult,
                            onSuccess = {
                                usersViewModel.currentUser.value?.let { updatedUser ->
                                    onNavigateToHome(updatedUser)
                                }
                                usersViewModel.resetOperationResult()
                            },
                            onFailure = {
                                usersViewModel.resetOperationResult()
                            }
                        )

                        Button(
                            onClick = {
                                if (validarCampos()) {
                                    usersViewModel.completeProfile(
                                        email = email,
                                        username = username,
                                        city = city,
                                        country = country,
                                        context = context
                                    )
                                }
                            },
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
                                            colors = listOf(Primary, Accent)
                                        ),
                                        shape = RoundedCornerShape(28.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.btn_complete_profile),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        }
    }
}
