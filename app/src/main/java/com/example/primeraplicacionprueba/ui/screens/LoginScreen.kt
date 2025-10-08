package com.example.primeraplicacionprueba.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.viewmodel.UsersViewModel

@Composable
fun LoginScreen(
    usersViewModel: UsersViewModel,
    onNavigateToRegister: () -> Unit = {},
    onNavigateToHome: (user: User) -> Unit = {}
) {
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
            .background(Color(0xFFF5F5F5))
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
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header con gradiente
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFF6B6B),
                                        Color(0xFF4ECDC4)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Logo circular
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "uL",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Título
                    Text(
                        text = stringResource(R.string.txt_welcome_back),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(R.string.txt_welcome_subtitle),
                        fontSize = 16.sp,
                        color = Color(0xFF7F8C8D),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

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
                            focusedBorderColor = Color(0xFF4ECDC4),
                            focusedLabelColor = Color(0xFF4ECDC4)
                        )
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
                                    color = Color(0xFF4ECDC4),
                                    fontSize = 12.sp
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4ECDC4),
                            focusedLabelColor = Color(0xFF4ECDC4)
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

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
                                        "Credenciales incorrectas",
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
                            containerColor = Color(0xFF4ECDC4)
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.btn_login),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Opciones de login social
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botón de Google
                        Card(
                            modifier = Modifier
                                .size(56.dp)
                                .clickable { /* TODO: Implementar Google Sign In */ },
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                // Logo de Google estilizado
                                Text(
                                    text = "G",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4285F4)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        // Botón de Facebook
                        Card(
                            modifier = Modifier
                                .size(56.dp)
                                .clickable { /* TODO: Implementar Facebook Sign In */ },
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1877F2)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "f",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Link para registro
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.txt_no_account),
                            color = Color(0xFF7F8C8D),
                            fontSize = 15.sp
                        )
                        Text(
                            text = stringResource(R.string.txt_register_link),
                            color = Color(0xFF4ECDC4),
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
                        color = Color(0xFF4ECDC4),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            // TODO: Implementar recuperación de contraseña
                        }
                    )
                }
            }
        }
    }
}