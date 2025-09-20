package com.example.primeraplicacionprueba.ui.screens
import android.util.Log
import android.util.Patterns
import com.example.primeraplicacionprueba.R

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.primeraplicacionprueba.ui.components.Slect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.primeraplicacionprueba.ui.components.InputText

@Composable
fun RegisterScreem() {
    var cities = listOf("Bogota", "Armenia", "Bucaramanga")
    var countries = listOf("Colombia", "EEUU", "Venezuela")
    var city by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var phoneN by rememberSaveable { mutableStateOf("") }
    var CPassword by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    //SURFACE ES UN CONTENEDOR QUE SIRVE PARA QUE SE ACOMPATIBLE CON LOS MATERIAL IU
    // QUIERE DECIR QUE ADAPTA AL COLOR DEL CEL EJ MODO NOCHE
    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {

            InputText(
                value = name,
                label = stringResource(R.string.txt_name),
                supportingText = stringResource(R.string.txt_errorGeneral),
                onValueChange = {
                    name = it
                },
                onValidate = {

                    name.isBlank()
                },
                icon = Icons.Outlined.Person

            )
            Slect(
                label = stringResource(R.string.txt_city),
                cities,
                onValueChange = {
                    city = it
                }
            )
            Slect(
                label = stringResource(R.string.txt_country),
                countries,
                onValueChange = {
                    country = it
                }
            )

            InputText(
                value = phoneN,
                label = stringResource(R.string.txt_phone_number),
                supportingText = stringResource(R.string.txt_errorGeneral),
                onValueChange = {
                    phoneN = it
                },
                onValidate = {

                    phoneN.isBlank()
                },
                icon = Icons.Outlined.Call

            )
            InputText(
                value = phoneN,
                label = stringResource(R.string.txt_phone_number),
                supportingText = stringResource(R.string.txt_errorGeneral),
                onValueChange = {
                    phoneN = it
                },
                onValidate = {

                    phoneN.isBlank() || Patterns.PHONE.matcher(phoneN).matches()
                },
                icon = Icons.Outlined.Call

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
            InputText(
                value = CPassword,
                label = stringResource(R.string.txtPassword),
                supportingText = stringResource(R.string.txt_errorGeneral),
                onValueChange = {
                    CPassword = it
                },
                onValidate = {

                    CPassword.isBlank() || CPassword.length < 5
                },
                icon = Icons.Outlined.Lock

            )

            Button(
                onClick = {
                    Log.d("RegisterScrreem", "name: $city, name:$name,phone:$phoneN")
                },
                content = {
                    Text(
                        text = stringResource(R.string.btn_login)
                    )
                }
            )
        }
    }

}