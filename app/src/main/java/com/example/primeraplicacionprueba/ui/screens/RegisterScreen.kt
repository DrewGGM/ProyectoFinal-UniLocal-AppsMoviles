package com.example.primeraplicacionprueba.ui.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.primeraplicacionprueba.ui.components.SearchableDropdown
import com.example.primeraplicacionprueba.viewmodel.LocationViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.primeraplicacionprueba.ui.theme.Accent
import com.example.primeraplicacionprueba.ui.theme.BgLight
import com.example.primeraplicacionprueba.ui.theme.BorderLight
import com.example.primeraplicacionprueba.ui.theme.FacebookBlue
import com.example.primeraplicacionprueba.ui.theme.GoogleBlue
import com.example.primeraplicacionprueba.ui.theme.Primary
import com.example.primeraplicacionprueba.ui.theme.Secondary
import com.example.primeraplicacionprueba.ui.theme.TextDark
import com.example.primeraplicacionprueba.ui.theme.TextMuted
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.time.LocalDate
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHome: (user: User) -> Unit = {},
    onNavigateToCompleteProfile: (user: User) -> Unit = {}
) {
    val mainViewModel = LocalMainViewModel.current
    val usersViewModel = mainViewModel.usersViewModel
    val userResult by usersViewModel.userResult.collectAsState()
    val currentUser by usersViewModel.currentUser.collectAsState()
    val needsProfileCompletion by usersViewModel.needsProfileCompletion.collectAsState()

    // LocationViewModel para países y ciudades
    val locationViewModel: LocationViewModel = viewModel()
    val countries by locationViewModel.countries.collectAsState()
    val cities by locationViewModel.cities.collectAsState()
    val isLoadingCountries by locationViewModel.isLoadingCountries.collectAsState()
    val isLoadingCities by locationViewModel.isLoadingCities.collectAsState()

    var nombreCompleto by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }

    // Efecto para cargar ciudades cuando se selecciona un país
    LaunchedEffect(pais) {
        if (pais.isNotEmpty()) {
            locationViewModel.loadCitiesByCountry(pais)
        } else {
            locationViewModel.resetCities()
            ciudad = "" // Limpiar ciudad cuando se cambia o limpia el país
        }
    }
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

    // Google Sign-In launcher
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

    // Función de Google Sign-In
    fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    // Facebook CallbackManager
    val callbackManager = remember { CallbackManager.Factory.create() }

    // Facebook Sign-In launcher
    val facebookLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        callbackManager.onActivityResult(result.resultCode, result.resultCode, result.data)
    }

    // Función de Facebook Sign-In
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

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                    modifier = Modifier.fillMaxWidth()
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
                            text = stringResource(R.string.txt_register_title),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = stringResource(R.string.txt_register_subtitle),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

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
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
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
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        SearchableDropdown(
                            label = stringResource(R.string.txt_country),
                            selectedValue = pais,
                            options = countries,
                            onValueChange = {
                                pais = it
                                if (paisError.isNotEmpty()) paisError = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isLoading = isLoadingCountries,
                            placeholder = stringResource(R.string.txt_select_country),
                            isError = paisError.isNotEmpty(),
                            errorMessage = paisError.ifEmpty { null }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SearchableDropdown(
                            label = stringResource(R.string.txt_city),
                            selectedValue = ciudad,
                            options = cities,
                            onValueChange = {
                                ciudad = it
                                if (ciudadError.isNotEmpty()) ciudadError = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = pais.isNotEmpty(),
                            isLoading = isLoadingCities,
                            placeholder = if (pais.isEmpty()) stringResource(R.string.txt_select_country_first) else stringResource(R.string.txt_select_city),
                            isError = ciudadError.isNotEmpty(),
                            errorMessage = ciudadError.ifEmpty { null }
                        )

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
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OperationResultHandler(
                            result = userResult,
                            onSuccess = {
                                val result = userResult
                                if (result is com.example.primeraplicacionprueba.utils.RequestResult.Success) {
                                    if (result.message.contains("Google") || result.message.contains("Facebook")) {
                                        // Social sign-in succeeded - check if profile needs completion
                                        val user = currentUser
                                        if (user != null) {
                                            if (needsProfileCompletion) {
                                                onNavigateToCompleteProfile(user)
                                            } else {
                                                onNavigateToHome(user)
                                            }
                                        }
                                    } else {
                                        // Regular registration - navigate to login
                                        onNavigateToLogin()
                                    }
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = BorderLight
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

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

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.txt_already_have_account),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
}