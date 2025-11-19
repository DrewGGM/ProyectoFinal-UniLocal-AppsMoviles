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
import com.example.primeraplicacionprueba.ui.screens.LocalMainViewModel
import com.example.primeraplicacionprueba.ui.components.SingleImageUploader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit = {},
    onSaveChanges: () -> Unit = {}
) {
    val mainViewModel = LocalMainViewModel.current
    val usersViewModel = mainViewModel.usersViewModel
    val currentUser by usersViewModel.currentUser.collectAsState()

    // Estados para los campos editables (con valores reales)
    var nombreCompleto by remember(currentUser) { mutableStateOf(currentUser?.nombre ?: "") }
    var nombreUsuario by remember(currentUser) { mutableStateOf(currentUser?.username ?: "") }
    var ciudad by remember(currentUser) { mutableStateOf(currentUser?.city ?: "") }
    var imageUrl by remember(currentUser) { mutableStateOf(currentUser?.imageUrl) }

    // Estados para campos no editables (solo mostrar)
    val correoElectronico = currentUser?.email ?: ""
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.txt_edit_profile),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            navigationIcon = {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.txt_back),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SingleImageUploader(
                    imageUrl = imageUrl,
                    onImageUploaded = { newUrl ->
                        imageUrl = newUrl
                    },
                    folder = "unilocal/profiles",
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.txt_tap_to_change_photo),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = stringResource(R.string.txt_full_name_label),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.txt_username_label),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.txt_city_residence),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.txt_account_info_not_editable),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Column {
                Text(
                    text = stringResource(R.string.txt_electronic_mail),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = correoElectronico,
                    onValueChange = { /* No editable */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.txt_password_label),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { /* No editable */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (validarCampos()) {
                        usersViewModel.updateCurrentUserProfile(
                            nombre = nombreCompleto,
                            username = nombreUsuario,
                            city = ciudad,
                            imageUrl = imageUrl
                        )
                        onSaveChanges()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
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