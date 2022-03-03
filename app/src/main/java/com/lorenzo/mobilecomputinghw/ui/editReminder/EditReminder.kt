package com.lorenzo.mobilecomputinghw.ui.editReminder

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.lorenzo.mobilecomputinghw.util.viewModelProviderFactoryOf

@Composable
fun EditReminder(
    onBackPress: () -> Unit,
    reminderId: Long,
    navController: NavController
) {
    val viewModel: EditReminderViewModel = viewModel(
        key = "reminder_id_$reminderId",
        factory = viewModelProviderFactoryOf { EditReminderViewModel(reminderId) }
    )
    val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    //val message = rememberSaveable { mutableStateOf(viewState.reminder?.message) }
    val appMessage = viewState.reminder?.message ?: ""
    val message = rememberSaveable { mutableStateOf("") }
    message.value = appMessage

    var appLocationX: Double = (viewState.reminder?.location_x ?: 0.0)
    var appLocationY: Double = (viewState.reminder?.location_y ?: 0.0)

    val latlng = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<LatLng>("location_data")
        ?.value

    if(latlng != null){
        appLocationX = latlng.latitude
        appLocationY = latlng.longitude
    }


    /*val appReminderTime = viewState.reminder?.reminder_time ?: ""
    val reminderTime = rememberSaveable { mutableStateOf("") }
    reminderTime.value = appReminderTime*/


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
                Text(text = "Edit reminder")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = message.value,
                    onValueChange = { message.value = it },
                    label = { Text(text = "Message")},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (appLocationX == 0.0 && appLocationY == 0.0) {
                        Button(
                            onClick = { navController.navigate("map") },
                            modifier = Modifier.height(35.dp)
                        ) {
                            Text(text = "Edit Location")
                        }
                    }
                    else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "Lat: ${"%.4f".format(appLocationX)}\nLng: ${"%.4f".format(appLocationY)}"
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

                Spacer(modifier = Modifier.height(100.dp))


                Button(
                    enabled = true,
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveReminder(
                                com.lorenzo.mobilecomputinghw.data.entity.Reminder(
                                    reminderId = reminderId,
                                    message = message.value,
                                    location_x = appLocationX,
                                    location_y = appLocationY,
                                    reminder_time = viewState.reminder?.reminder_time ?: "",
                                    creation_time = viewState.reminder?.creation_time ?: "",
                                    creator_id = viewState.reminder?.creator_id ?: 0,
                                    reminder_seen = viewState.reminder?.reminder_seen ?: 0,
                                    img_uri = viewState.reminder?.img_uri ?: ""
                                )
                            )
                        }
                        onBackPress()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Edit reminder")
                }
            }
        }
    }
}

