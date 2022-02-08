package com.lorenzo.mobilecomputinghw.ui.editReminder

import android.util.Log
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
import com.lorenzo.mobilecomputinghw.util.viewModelProviderFactoryOf

@Composable
fun EditReminder(
    onBackPress: () -> Unit,
    reminderId: Long
) {
    val coroutineScope = rememberCoroutineScope()

    Log.d("Debug", reminderId.toString())

    val viewModel: EditReminderViewModel = viewModel(
        key = "reminder_id_$reminderId",
        factory = viewModelProviderFactoryOf { EditReminderViewModel(reminderId) }
    )
    val viewState by viewModel.state.collectAsState()

    //val reminder: Reminder = viewModel.getReminder(reminderId)


    //val message = rememberSaveable { mutableStateOf(viewState.reminder?.message) }
    var message = viewState.reminder?.message ?: ""

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
                        value = message,
                        onValueChange = { message = it },
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
                                    message = message,
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

