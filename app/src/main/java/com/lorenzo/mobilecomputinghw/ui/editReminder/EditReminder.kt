package com.lorenzo.mobilecomputinghw.ui.editReminder

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import com.lorenzo.mobilecomputinghw.util.viewModelProviderFactoryOf

@Composable
fun EditReminder(
    onBackPress: () -> Unit,
    reminderId: Long
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

    val appLocationX = viewState.reminder?.location_x ?: ""
    val locationX = rememberSaveable { mutableStateOf("") }
    locationX.value = appLocationX

    val appLocationY = viewState.reminder?.location_y ?: ""
    val locationY = rememberSaveable { mutableStateOf("") }
    locationY.value = appLocationY

    val appReminderTime = viewState.reminder?.reminder_time ?: ""
    val reminderTime = rememberSaveable { mutableStateOf("") }
    reminderTime.value = appReminderTime


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

                OutlinedTextField(
                    value = locationX.value,
                    onValueChange = { locationX.value = it },
                    label = { Text(text = "Location X")},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = locationY.value,
                    onValueChange = { locationY.value = it },
                    label = { Text(text = "Location Y")},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = reminderTime.value,
                    onValueChange = { reminderTime.value = it },
                    label = { Text(text = "Reminder Time")},
                    modifier = Modifier.fillMaxWidth()
                )

                /*Image(
                    painter = rememberImagePainter(
                        data = viewState.reminder?.img_uri ?: ""
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(64.dp)
                )*/

                Spacer(modifier = Modifier.height(50.dp))

                Button(
                    enabled = true,
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveReminder(
                                com.lorenzo.mobilecomputinghw.data.entity.Reminder(
                                    reminderId = reminderId,
                                    message = message.value,
                                    location_x = locationX.value,
                                    location_y = locationY.value,
                                    reminder_time = reminderTime.value,
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

