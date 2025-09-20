package com.example.primeraplicacionprueba.ui.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.components.InputText

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit
) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current;
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier
            .fillMaxSize(),

        content = {
            Image(
                modifier = Modifier
                    .width(153.dp),
                painter = painterResource(R.drawable.instagram),
                contentDescription = stringResource(R.string.img_imagen)
            )

            InputText(
                value = email,
                label = stringResource(R.string.txt_email),
                supportingText = stringResource(R.string.txt_email_error),
                onValueChange = {
                    email = it
                },
                onValidate = {
                                            //para validar si tiene el cuerpo de un email
                    email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                },
                icon = Icons.Outlined.Email

            )
            InputText(
                value = password,
                isPassword = true,
                label = stringResource(R.string.txtPassword),
                supportingText = stringResource(R.string.txt_password_error),
                onValueChange = {
                    password = it
                },
                onValidate = {
                    password.isBlank()
                },
                icon = Icons.Outlined.Lock

            )




            Button(

                onClick = {
                    if (email == "val" && password == "123") {
                           onNavigateToHome()
                    } else {
                        Toast.makeText(context, "INCORRECTO", Toast.LENGTH_SHORT).show()

                    }
                },
                content = {
                    Text(
                        text = stringResource(R.string.btn_login)
                    )
                }
            )
            Button(

                onClick = {
                   onNavigateToRegister()
                },
                content = {
                    Text(
                        text = stringResource(R.string.txt_Register)
                    )
                }
            )


        }
    )

}

