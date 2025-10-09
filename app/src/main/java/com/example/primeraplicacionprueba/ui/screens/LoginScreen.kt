package com.example.primeraplicacionprueba.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.theme.*

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToHome: (user: User) -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {}
) {
    val mainViewModel = LocalMainViewModel.current
    val usersViewModel = mainViewModel.usersViewModel
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados para validación
    var emailError by remember { mutableStateOf("") }
    var contrasenaError by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Función de validación
    fun validarCampos(): Boolean {
        var isValid = true

        // Validar email
        if (email.isBlank()) {
            emailError = context.getString(R.string.txt_errorGeneral)
            isValid = false
        } else {
            emailError = ""
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
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
                            text = stringResource(R.string.txt_welcome_back),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            textAlign = TextAlign.Center
                        )

                        // Subtítulo
                        Text(
                            text = stringResource(R.string.txt_welcome_subtitle),
                            fontSize = 14.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Campo Email/Usuario
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                if (emailError.isNotEmpty()) emailError = ""
                            },
                            label = { Text(stringResource(R.string.txt_email_hint)) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            isError = emailError.isNotEmpty(),
                            supportingText = if (emailError.isNotEmpty()) {
                                { Text(emailError, color = MaterialTheme.colorScheme.error) }
                            } else null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Secondary,
                                focusedLabelColor = Secondary,
                                unfocusedBorderColor = BorderLight
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Campo Contraseña
                        OutlinedTextField(
                            value = contrasena,
                            onValueChange = {
                                contrasena = it
                                if (contrasenaError.isNotEmpty()) contrasenaError = ""
                            },
                            label = { Text(stringResource(R.string.txt_password_hint)) },
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

                        // Botón de iniciar sesión
                        Button(
                            onClick = {
                                if (validarCampos()) {
                                    val usersLogged = usersViewModel.login(email, contrasena)
                                    if (usersLogged != null) {
                                        onNavigateToHome(usersLogged)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.txt_credentials_incorrect),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

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
                                    text = stringResource(R.string.btn_login),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Opciones de login social
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

                        Spacer(modifier = Modifier.height(16.dp))

                        // Link para registro
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.txt_no_account),
                                color = TextMuted,
                                fontSize = 15.sp
                            )
                            Text(
                                text = stringResource(R.string.txt_register_link),
                                color = Secondary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                modifier = Modifier.clickable {
                                    onNavigateToRegister()
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Link "¿Olvidaste tu contraseña?"
                        Text(
                            text = stringResource(R.string.txt_forgot_password),
                            color = Secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable {
                                onNavigateToForgotPassword()
                            }
                        )
                    }
                }
            }
        }
    }
}