package com.lorenzo.mobilecomputinghw.ui.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch

@Composable
fun Login(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()

) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val username = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }

        val context = LocalContext.current
        val viewState by viewModel.state.collectAsState()
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { data -> username.value = data },
                label = { Text("Username")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { data -> password.value = data },
                label = { Text("Password")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick ={
                    if(checkLogin(viewState,username,password)) {
                        var id: Long
                        id = getId(viewState, username)
                        navController.navigate(route = "home/${id}")
                    }
                    else
                        Toast.makeText(
                            context,
                            "Incorrect Username/Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Login")
            }
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick ={


                    if(checkRegistration(viewState,username,password)){
                        val user = com.lorenzo.mobilecomputinghw.data.entity.User(
                            userName = username.value,
                            password = password.value
                        )
                        coroutineScope.launch {
                            viewModel.saveUser(user)
                        }
                        Toast.makeText(
                            context,
                            "User registered",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else
                        Toast.makeText(
                            context,
                            "Username and Password are mandatory or Username not available!",
                            Toast.LENGTH_SHORT
                        ).show()
                         },
                enabled = true,
                modifier = Modifier.fillMaxWidth().size(55.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.87f)),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Register", color = Color.White)
            }
        }
    }


}

@Composable
fun toastDemo() {
    val context = LocalContext.current
    Column(
        content = {
            Button(onClick = {
                Toast.makeText(
                    context,
                    "Showing toast....",
                    Toast.LENGTH_LONG
                ).show()
            }, content = {
                Text(text = "Show Toast")
            })
        }, modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
}

fun checkLogin(viewState: LoginViewState, userName: MutableState<String>, password: MutableState<String>): Boolean {
    var found = false;

    if(userName.value.isBlank() || password.value.isBlank())
        return false;

    viewState.users.forEach(){
        if (it.userName == userName.value && it.password == password.value)
            found = true;
    }

    return found
}

fun checkRegistration(viewState: LoginViewState, userName: MutableState<String>, password: MutableState<String>): Boolean {
    var found = false;

    if(userName.value.isBlank() || password.value.isBlank())
        return false;

    viewState.users.forEach(){
        if (it.userName == userName.value)
            found = true;
    }

    if(found)
        return false;

    return true;
}

fun getId(viewState: LoginViewState, userName: MutableState<String>) : Long {

    viewState.users.forEach(){
        if (it.userName == userName.value )
            return it.id
    }
    return 0
}