package com.example.primeraplicacionprueba.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.theme.*

@Composable
fun ForgotPasswordScreen(
    onNavigateToLogin: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var emailSent by remember { mutableStateOf(false) }

    val context = LocalContext.current
    
    // Strings para validación
    val emailRequiredText = stringResource(R.string.txt_email_required)
    val emailInvalidText = stringResource(R.string.txt_email_invalid)
    val emailSentText = stringResource(R.string.txt_email_sent, email)

    // Función de validación
    fun validateEmail(): Boolean {
        return if (email.isBlank()) {
            emailError = emailRequiredText
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = emailInvalidText
            false
        } else {
            emailError = ""
            true
        }
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
                        // Logo circular con backdrop blur effect
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "uL",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Contenido del formulario
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Título
                        Text(
                            text = stringResource(R.string.txt_forgot_password_title),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            textAlign = TextAlign.Center
                        )

                        // Subtítulo
                        Text(
                            text = stringResource(R.string.txt_forgot_password_subtitle),
                            fontSize = 14.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Campo de email
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
                                unfocusedBorderColor = BorderLight
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón de enviar
                        Button(
                            onClick = {
                                if (validateEmail()) {
                                    // TODO: Implementar lógica de recuperación
                                    emailSent = true
                                    Toast.makeText(
                                        context,
                                        emailSentText,
                                        Toast.LENGTH_LONG
                                    ).show()
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
                                    text = stringResource(R.string.txt_send),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Link para volver al login
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.txt_back_to_login),
                                color = Secondary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable {
                                    onNavigateToLogin()
                                }
                            )
                        }
                    }
                }
            }

            // Mensaje de confirmación (opcional)
            if (emailSent) {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Tertiary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.txt_check_inbox),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = SuccessGreen,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}