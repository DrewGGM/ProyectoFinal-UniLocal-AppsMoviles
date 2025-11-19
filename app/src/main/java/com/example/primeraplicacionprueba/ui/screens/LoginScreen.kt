package com.example.primeraplicacionprueba.ui.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.components.OperationResultHandler
import com.example.primeraplicacionprueba.ui.theme.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

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

    var emailError by remember { mutableStateOf("") }
    var contrasenaError by remember { mutableStateOf("") }

    val userResult by usersViewModel.userResult.collectAsState()
    val contextU = LocalContext.current

    val context = LocalContext.current

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let {
                usersViewModel.signInWithGoogle(it, context)
            }
        } catch (e: ApiException) {
            Toast.makeText(context, context.getString(R.string.toast_error_google_signin, e.message ?: ""), Toast.LENGTH_LONG).show()
        }
    }

    fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    val callbackManager = remember { CallbackManager.Factory.create() }

    val facebookLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        callbackManager.onActivityResult(result.resultCode, result.resultCode, result.data)
    }

    fun signInWithFacebook() {
        val loginManager = LoginManager.getInstance()
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val accessToken = result.accessToken
                usersViewModel.signInWithFacebook(accessToken, context)
            }

            override fun onCancel() {
                Toast.makeText(context, context.getString(R.string.toast_facebook_signin_cancelled), Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(context, context.getString(R.string.toast_error_facebook_signin, error.message ?: ""), Toast.LENGTH_LONG).show()
            }
        })
        loginManager.logInWithReadPermissions(
            context as ComponentActivity,
            callbackManager,
            listOf("email", "public_profile")
        )
    }

    fun validarCampos(): Boolean {
        var isValid = true

        if (email.isBlank()) {
            emailError = context.getString(R.string.txt_errorGeneral)
            isValid = false
        } else {
            emailError = ""
        }

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
                                contentDescription = stringResource(R.string.cd_unilocal_logo),
                                modifier = Modifier.size(120.dp)
                            )

                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.txt_welcome_back),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = stringResource(R.string.txt_welcome_subtitle),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

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
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
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
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OperationResultHandler(
                            result = userResult,
                            onSuccess = {
                                usersViewModel.currentUser.value?.let { user ->
                                    onNavigateToHome(user)
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
                                    usersViewModel.login(email, contrasena, context)
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

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = stringResource(R.string.txt_continue_with_google),
                                modifier = Modifier
                                    .size(42.dp)
                                    .clickable { signInWithGoogle() },
                                tint = Color.Unspecified
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            Icon(
                                painter = painterResource(id = R.drawable.facebook_logo),
                                contentDescription = stringResource(R.string.txt_continue_with_facebook),
                                modifier = Modifier
                                    .size(42.dp)
                                    .clickable { signInWithFacebook() },
                                tint = Color.Unspecified
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.txt_no_account),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
}