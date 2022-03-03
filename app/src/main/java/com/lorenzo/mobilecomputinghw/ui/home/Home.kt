package com.lorenzo.mobilecomputinghw.ui.home

import android.annotation.SuppressLint
import android.inputmethodservice.Keyboard
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.systemBarsPadding
import com.google.android.gms.maps.model.LatLng
import com.lorenzo.mobilecomputinghw.R
import com.lorenzo.mobilecomputinghw.data.entity.User
import com.lorenzo.mobilecomputinghw.util.viewModelProviderFactoryOf

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Home(
    navController: NavController,
    idLogged: Long
) {
    val viewModel: HomeViewModel = viewModel(
        key = "id_$idLogged",
        factory = viewModelProviderFactoryOf { HomeViewModel(idLogged) }
    )

    val viewState by viewModel.state.collectAsState()
    //val coroutineScope = rememberCoroutineScope()

    viewModel.updateUser()

    val user = viewState.user

    Surface(modifier = Modifier.fillMaxSize()) {
        if (user != null) {
            HomeContent(
                viewModel,
                navController = navController,
                user = user
            )
        }
    }
}


@Composable
fun HomeContent(
    //selectedCategory: Category,
    //categories: List<Category>,
    //onCategorySelected: (Category) -> Unit,
    viewModel: HomeViewModel,
    navController: NavController,
    user: User
) {
    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(route = "reminder")
                          },
                contentColor = Color.White,
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) {

        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
        ) {
            val appBarColor = MaterialTheme.colors.secondary.copy(alpha = 0.87f)

            val latlng = navController
                .currentBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<LatLng>("location_data")
                ?.value

            HomeAppBar(
                backgroundColor = appBarColor,
                navController = navController,
                user = user
            )

            PositionInfo(navController = navController, latlng = latlng)

            CategoryReminder(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                latlng = latlng
            )
        }
    }
}

@Composable
private fun PositionInfo(
    navController: NavController,
    latlng: LatLng?
){
    Spacer(modifier = Modifier.height(20.dp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (latlng == null) {
            Button(
                onClick = { navController.navigate("map") },
                modifier = Modifier.height(55.dp)
            ) {
                Text(text = "Virtual Location")
            }
        }
        else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(

                    text = "Lat: ${"%.4f".format(latlng.latitude)}\nLng: ${"%.4f".format(latlng.longitude)}"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { navController.navigate("map") },
                    modifier = Modifier.height(35.dp)
                ) {
                    Text(text = "Change")
                }
            }

        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun HomeAppBar(
    backgroundColor: Color,
    navController: NavController,
    user: User
) {
    val expanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
            IconButton( onClick = {} ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.search), tint = MaterialTheme.colors.primary)
            }
            IconButton( onClick = { expanded.value = true } ) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.account),tint = MaterialTheme.colors.primary)
            }

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                DropdownMenuItem(onClick = {
                    expanded.value = false
                }) {
                    Box (
                        contentAlignment = Alignment.CenterEnd
                            ){
                        Text(
                            text = user.userName, textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = stringResource(R.string.account),
                            tint = MaterialTheme.colors.primary,
                        )
                    }

                }

                DropdownMenuItem(onClick = {
                    navController.navigate(route = "profile/${user.userName},${user.id}")
                    expanded.value = false
                }) {
                    Text("My profile")
                }

                DropdownMenuItem(onClick = {
                    navController.navigate(route = "login")
                }) {
                    Text("Log out")
                }
            }
        }
    )
}