package com.lorenzo.mobilecomputinghw.ui.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding
import com.lorenzo.mobilecomputinghw.data.entity.User
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import com.lorenzo.mobilecomputinghw.ui.login.LoginViewState

@Composable
fun Profile(
    onBackPress: () -> Unit,
    navController: NavController,
    userLogged: String,
    idLogged: Long,
    viewModel: ProfileViewModel = viewModel()
) {
    val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val userName = rememberSaveable { mutableStateOf(userLogged) }
    val password = rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = onBackPress
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(text = "Profile")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = userName.value,
                    onValueChange = { userName.value = it },
                    label = { Text(text = "New Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text(text = "New Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    enabled = true,
                    onClick = {
                        if(checkProfile(userName,password)){
                            coroutineScope.launch {
                                //TODO Check Username
                                viewModel.saveUser(
                                    User(
                                        id = idLogged,
                                        userName = userName.value,
                                        password = password.value
                                    )
                                )
                            }
                            onBackPress()
                        }else{
                            Toast.makeText(
                                context,
                                "Username and Password are mandatory!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Save profile")
                }
            }
        }
    }
}

fun checkProfile(userName: MutableState<String>, password: MutableState<String>): Boolean {

    if(userName.value.isBlank() || password.value.isBlank())
        return false;

    return true
}

