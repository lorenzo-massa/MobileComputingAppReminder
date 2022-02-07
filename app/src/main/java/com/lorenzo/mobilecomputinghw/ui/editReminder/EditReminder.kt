package com.lorenzo.mobilecomputinghw.ui.editReminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzo.mobilecomputinghw.data.entity.Reminder

@Composable
fun EditReminder(
    onBackPress: () -> Unit,
    viewModel: EditReminderViewModel = viewModel(),
    reminderId: Long
) {
    val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val reminder: Reminder = getReminder(viewState, reminderId)

    val message = rememberSaveable { mutableStateOf(reminder.message) }

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
                    label = { Text(text = "Reminder message")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(50.dp))

                Button(
                    enabled = true,
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveReminder(
                                com.lorenzo.mobilecomputinghw.data.entity.Reminder(
                                    reminderId = reminderId,
                                    message = message.value,
                                    location_x = "",
                                    location_y = "",
                                    reminder_time = "",
                                    creation_time = "",
                                    creator_id = 0,
                                    reminder_seen = 0,
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

fun getReminder(viewState: EditReminderViewState, id: Long): Reminder {

    viewState.reminders.forEach(){
        if (it.reminderId == id )
            return it
    }
    return Reminder(
        reminderId = 0,
        message = "",
        location_x = "",
        location_y = "",
        reminder_time = "",
        creation_time = "",
        creator_id = 0,
        reminder_seen = 0,
    )
}