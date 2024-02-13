package com.example.words1.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.words1.R
import androidx.compose.runtime.livedata.observeAsState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: WordsViewModel,
    navController: NavController,
    context: Context
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(colorResource(id = R.color.p1)) ,

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        val loginState by viewModel.loginState.observeAsState()

        LaunchedEffect(loginState) {
            when (loginState) {
                true -> navController.navigate("CollectionScreen")
                false -> Toast.makeText(context, "Incorrect login or password", Toast.LENGTH_SHORT).show()
                null -> { /* No action if null, could be initial state or loading state */ }
            }
        }


        Spacer(modifier = Modifier.padding(64.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(colorResource(id = R.color.p2)),
            value = login,
            onValueChange = {
                login = it
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                color = colorResource(id = R.color.p3)
            ),
            placeholder = {
                Text(
                    text = "Login",
                    color = colorResource(id = R.color.p3)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.p3),
                unfocusedBorderColor = colorResource(id = R.color.p3)
            )
        )




        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(colorResource(id = R.color.p2)),
            value = password,
            onValueChange = {
                password = it
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                color = colorResource(id = R.color.p3)
            ),
            placeholder = {
                Text(
                    text = "Password",
                    color = colorResource(id = R.color.p3)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.p3),
                unfocusedBorderColor = colorResource(id = R.color.p3)
            )
        )

        Spacer(modifier = Modifier.padding(32.dp))


        FilledTonalButton(
            onClick = {
                viewModel.login(login, password)

                      },
            colors = ButtonDefaults.buttonColors(
                colorResource(id = R.color.p3)
            )
        ) {
            Text(
                text="Login",
                color = colorResource(id = R.color.p2)
            )
        }

        Spacer(modifier = Modifier.padding(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Don't have an account?",
                color = colorResource(id = R.color.p2)

            )


            FilledTonalButton(
                onClick = {
                    navController.navigate("SignupScreen")

                },
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.p1)
                )
            ) {
                Text(
                    "Register",
                    color = colorResource(id = R.color.p3)

                )
            }
        }
    }
}