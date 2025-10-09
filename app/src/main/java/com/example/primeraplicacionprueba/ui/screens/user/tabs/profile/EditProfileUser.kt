package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit = {},
    onSaveChanges: () -> Unit = {}
) {
    // Estados para los campos editables
    var nombreCompleto by remember { mutableStateOf("Carlos Rodríguez") }
    var nombreUsuario by remember { mutableStateOf("carlos.rodriguez") }
    var ciudad by remember { mutableStateOf("Bogotá") }

    // Estados para campos no editables (solo mostrar)
    val correoElectronico = "carlos.r@ejemplo.com"
    var contrasena by remember { mutableStateOf("******") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados para validación
    var nombreCompletoError by remember { mutableStateOf("") }
    var nombreUsuarioError by remember { mutableStateOf("") }
    var ciudadError by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Función de validación
    fun validarCampos(): Boolean {
        var isValid = true

        // Validar nombre completo
        if (nombreCompleto.isBlank()) {
            nombreCompletoError = context.getString(R.string.txt_errorGeneral)
            isValid = false
        } else if (nombreCompleto.length < 3) {
            nombreCompletoError = context.getString(R.string.txt_name_error_short)
            isValid = false
        } else {
            nombreCompletoError = ""
        }

        // Validar nombre de usuario
        if (nombreUsuario.isBlank()) {
            nombreUsuarioError = context.getString(R.string.txt_username_error)
            isValid = false
        } else if (nombreUsuario.length < 3) {
            nombreUsuarioError = context.getString(R.string.txt_username_error_short)
            isValid = false
        } else {
            nombreUsuarioError = ""
        }

        // Validar ciudad
        if (ciudad.isBlank()) {
            ciudadError = context.getString(R.string.txt_city_error)
            isValid = false
        } else {
            ciudadError = ""
        }

        return isValid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.txt_edit_profile),
                    fontSize = 20.sp,
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
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Foto de perfil circular (placeholder)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = BorderLight,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CR",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CAMPOS EDITABLES
            // Nombre Completo
            Column {
                Text(
                    text = stringResource(R.string.txt_full_name_label),
                    fontSize = 14.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = nombreCompleto,
                    onValueChange = {
                        nombreCompleto = it
                        if (nombreCompletoError.isNotEmpty()) nombreCompletoError = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nombreCompletoError.isNotEmpty(),
                    supportingText = if (nombreCompletoError.isNotEmpty()) {
                        { Text(nombreCompletoError, color = MaterialTheme.colorScheme.error) }
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

            // Nombre de Usuario
            Column {
                Text(
                    text = stringResource(R.string.txt_username_label),
                    fontSize = 14.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = nombreUsuario,
                    onValueChange = {
                        nombreUsuario = it
                        if (nombreUsuarioError.isNotEmpty()) nombreUsuarioError = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nombreUsuarioError.isNotEmpty(),
                    supportingText = if (nombreUsuarioError.isNotEmpty()) {
                        { Text(nombreUsuarioError, color = MaterialTheme.colorScheme.error) }
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

            // Ciudad de Residencia
            Column {
                Text(
                    text = stringResource(R.string.txt_city_residence),
                    fontSize = 14.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = ciudad,
                    onValueChange = {
                        ciudad = it
                        if (ciudadError.isNotEmpty()) ciudadError = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = ciudadError.isNotEmpty(),
                    supportingText = if (ciudadError.isNotEmpty()) {
                        { Text(ciudadError, color = MaterialTheme.colorScheme.error) }
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

            Spacer(modifier = Modifier.height(16.dp))

            // Sección no editable
            Text(
                text = stringResource(R.string.txt_account_info_not_editable),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )

            // Correo Electrónico (no editable)
            Column {
                Text(
                    text = stringResource(R.string.txt_electronic_mail),
                    fontSize = 14.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = correoElectronico,
                    onValueChange = { /* No editable */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = BorderLight,
                        disabledContainerColor = BackgroundDisabled,
                        disabledTextColor = TextMuted
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Contraseña (no editable pero con opción ver)
            Column {
                Text(
                    text = stringResource(R.string.txt_password_label),
                    fontSize = 14.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { /* No editable */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                text = if (passwordVisible)
                                    stringResource(R.string.txt_hide)
                                else
                                    stringResource(R.string.txt_show),
                                color = Secondary,
                                fontSize = 12.sp
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = BorderLight,
                        disabledContainerColor = BackgroundDisabled,
                        disabledTextColor = TextMuted
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Guardar Cambios
            Button(
                onClick = {
                    if (validarCampos()) {
                        onSaveChanges()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_save_changes),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}