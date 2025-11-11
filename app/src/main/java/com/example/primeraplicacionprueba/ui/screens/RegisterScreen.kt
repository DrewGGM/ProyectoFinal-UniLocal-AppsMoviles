package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.Role
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.components.OperationResultHandler
import com.example.primeraplicacionprueba.ui.theme.Accent
import com.example.primeraplicacionprueba.ui.theme.BgLight
import com.example.primeraplicacionprueba.ui.theme.BorderLight
import com.example.primeraplicacionprueba.ui.theme.FacebookBlue
import com.example.primeraplicacionprueba.ui.theme.GoogleBlue
import com.example.primeraplicacionprueba.ui.theme.Primary
import com.example.primeraplicacionprueba.ui.theme.Secondary
import com.example.primeraplicacionprueba.ui.theme.TextDark
import com.example.primeraplicacionprueba.ui.theme.TextMuted
import java.time.LocalDate
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHome: (user: User) -> Unit = {}
) {
    val mainViewModel = LocalMainViewModel.current
    val usersViewModel = mainViewModel.usersViewModel
    val userResult by usersViewModel.userResult.collectAsState()

    var nombreCompleto by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var ciudadExpanded by remember { mutableStateOf(false) }
    var paisExpanded by remember { mutableStateOf(false) }

    val ciudades = listOf(
        "Bogotá",
        "Medellín",
        "Cali",
        "Barranquilla",
        "Cartagena",
        "Cúcuta",
        "Bucaramanga",
        "Pereira",
        "Santa Marta",
        "Ibagué",
        "Pasto",
        "Manizales",
        "Neiva",
        "Villavicencio",
        "Armenia"
    )
    val paises = listOf(
        "Colombia",
        "México",
        "Argentina",
        "Chile",
        "Perú",
        "Ecuador",
        "Venezuela",
        "Bolivia",
        "Paraguay",
        "Uruguay",
        "Brasil",
        "Estados Unidos",
        "España",
        "Francia",
        "Italia"
    )
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados para validación
    var nombreCompletoError by remember { mutableStateOf("") }
    var nombreUsuarioError by remember { mutableStateOf("") }
    var ciudadError by remember { mutableStateOf("") }
    var paisError by remember { mutableStateOf("") }
    var correoError by remember { mutableStateOf("") }
    var contrasenaError by remember { mutableStateOf("") }

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

        // Validar pais
        if (pais.isBlank()) {
            paisError = context.getString(R.string.txt_country_error)
            isValid = false
        } else {
            paisError = ""
        }

        // Validar correo
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9+_.-]+@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})"
        )
        if (correo.isBlank()) {
            correoError = context.getString(R.string.txt_errorGeneral)
            isValid = false
        } else if (!emailPattern.matcher(correo).matches()) {
            correoError = context.getString(R.string.txt_email_error_invalid)
            isValid = false
        } else {
            correoError = ""
        }

        // Validar contraseña
        if (contrasena.isBlank()) {
            contrasenaError = context.getString(R.string.txt_password_error_required)
            isValid = false
        } else if (contrasena.length < 6) {
            contrasenaError = context.getString(R.string.txt_password_error_short)
            isValid = false
        } else {
            contrasenaError = ""
        }

        return isValid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Card principal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Header con gradiente curvo
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
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
                                    colors = listOf(
                                        Primary,
                                        Secondary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        
                            Image(
                                painter = painterResource(id = R.drawable.unilocal_logo),
                                contentDescription = "UniLocal Logo",
                                modifier = Modifier.size(120.dp)
                            )
                        
                    }

                    // Contenido del formulario
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // Título
                        Text(
                            text = stringResource(R.string.txt_register_title),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            textAlign = TextAlign.Center
                        )

                        // Subtítulo
                        Text(
                            text = stringResource(R.string.txt_register_subtitle),
                            fontSize = 14.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Campos del formulario
                        OutlinedTextField(
                            value = nombreCompleto,
                            onValueChange = {
                                nombreCompleto = it
                                if (nombreCompletoError.isNotEmpty()) nombreCompletoError = ""
                            },
                            label = { Text(stringResource(R.string.txt_name)) },
                            modifier = Modifier.fillMaxWidth(),
                            isError = nombreCompletoError.isNotEmpty(),
                            supportingText = if (nombreCompletoError.isNotEmpty()) {
                                {
                                    Text(
                                        nombreCompletoError,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            } else null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Secondary,
                                focusedLabelColor = Secondary,
                                unfocusedBorderColor = BorderLight
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = nombreUsuario,
                            onValueChange = {
                                nombreUsuario = it
                                if (nombreUsuarioError.isNotEmpty()) nombreUsuarioError = ""
                            },
                            label = { Text(stringResource(R.string.txt_username)) },
                            modifier = Modifier.fillMaxWidth(),
                            isError = nombreUsuarioError.isNotEmpty(),
                            supportingText = if (nombreUsuarioError.isNotEmpty()) {
                                {
                                    Text(
                                        nombreUsuarioError,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            } else null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Secondary,
                                focusedLabelColor = Secondary,
                                unfocusedBorderColor = BorderLight
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Dropdown para Ciudad
                        ExposedDropdownMenuBox(
                            expanded = ciudadExpanded,
                            onExpandedChange = { ciudadExpanded = !ciudadExpanded }
                        ) {
                            OutlinedTextField(
                                value = ciudad,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(stringResource(R.string.txt_city)) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = ciudadExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                                isError = ciudadError.isNotEmpty(),
                                supportingText = if (ciudadError.isNotEmpty()) {
                                    { Text(ciudadError, color = MaterialTheme.colorScheme.error) }
                                } else null,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Secondary,
                                    focusedLabelColor = Secondary
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = ciudadExpanded,
                                onDismissRequest = { ciudadExpanded = false }
                            ) {
                                ciudades.forEach { ciudadOption ->
                                    DropdownMenuItem(
                                        text = { Text(ciudadOption) },
                                        onClick = {
                                            ciudad = ciudadOption
                                            ciudadExpanded = false
                                            if (ciudadError.isNotEmpty()) ciudadError = ""
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Dropdown para País
                        ExposedDropdownMenuBox(
                            expanded = paisExpanded,
                            onExpandedChange = { paisExpanded = !paisExpanded }
                        ) {
                            OutlinedTextField(
                                value = pais,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(stringResource(R.string.txt_country)) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = paisExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                                isError = paisError.isNotEmpty(),
                                supportingText = if (paisError.isNotEmpty()) {
                                    { Text(paisError, color = MaterialTheme.colorScheme.error) }
                                } else null,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Secondary,
                                    focusedLabelColor = Secondary
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = paisExpanded,
                                onDismissRequest = { paisExpanded = false }
                            ) {
                                paises.forEach { paisOption ->
                                    DropdownMenuItem(
                                        text = { Text(paisOption) },
                                        onClick = {
                                            pais = paisOption
                                            paisExpanded = false
                                            if (paisError.isNotEmpty()) paisError = ""
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = correo,
                            onValueChange = {
                                correo = it
                                if (correoError.isNotEmpty()) correoError = ""
                            },
                            label = { Text(stringResource(R.string.txt_email)) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            isError = correoError.isNotEmpty(),
                            supportingText = if (correoError.isNotEmpty()) {
                                { Text(correoError, color = MaterialTheme.colorScheme.error) }
                            } else null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Secondary,
                                focusedLabelColor = Secondary,
                                unfocusedBorderColor = BorderLight
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = contrasena,
                            onValueChange = {
                                contrasena = it
                                if (contrasenaError.isNotEmpty()) contrasenaError = ""
                            },
                            label = { Text(stringResource(R.string.txtPassword)) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            isError = contrasenaError.isNotEmpty(),
                            supportingText = if (contrasenaError.isNotEmpty()) {
                                { Text(contrasenaError, color = MaterialTheme.colorScheme.error) }
                            } else null,
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
                                focusedBorderColor = Secondary,
                                focusedLabelColor = Secondary,
                                unfocusedBorderColor = BorderLight
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OperationResultHandler(
                            result = userResult,
                            onSuccess = {
                                onNavigateToLogin()
                                usersViewModel.resetOperationResult()
                            },
                            onFailure ={
                                usersViewModel.resetOperationResult()
                            }
                        )

                        // Botón de crear cuenta
                        Button(
                            onClick = {
                                if (validarCampos()) {
                                    val user = User(
                                        nombre = nombreCompleto,
                                        username = nombreUsuario,
                                        city = ciudad,
                                        country = pais,
                                        email = correo,
                                        password = contrasena,
                                        id = "",
                                        rol = Role.USER,
                                        joinDate = LocalDate.now()
                                    )
                                    usersViewModel.create(user)
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
                                            colors = listOf(
                                                Primary,
                                                Accent
                                            )
                                        ),
                                        shape = RoundedCornerShape(28.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.btn_Register),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Separador "O continúa con"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = BorderLight
                            )
                            Text(
                                text = " ${stringResource(R.string.txt_or_continue_with)} ",
                                color = TextMuted,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = BorderLight
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Opciones de registro social con iconos mejorados
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Botón de Google
                            Icon(
                                painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = stringResource(R.string.txt_continue_with_google),
                                modifier = Modifier
                                    .size(42.dp)
                                    .clickable { /* TODO: Implementar Google Sign In */ },
                                tint = androidx.compose.ui.graphics.Color.Unspecified
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            // Botón de Facebook
                            Icon(
                                painter = painterResource(id = R.drawable.facebook_logo),
                                contentDescription = stringResource(R.string.txt_continue_with_facebook),
                                modifier = Modifier
                                    .size(42.dp)
                                    .clickable { /* TODO: Implementar Facebook Sign In */ },
                                tint = androidx.compose.ui.graphics.Color.Unspecified
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Link para iniciar sesión
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.txt_already_have_account),
                                color = TextMuted,
                                fontSize = 15.sp
                            )
                            Text(
                                text = stringResource(R.string.txt_login_link),
                                color = Secondary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                modifier = Modifier.clickable {
                                    onNavigateToLogin()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}