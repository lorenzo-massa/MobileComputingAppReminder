package com.lorenzo.mobilecomputinghw.ui.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.lorenzo.mobilecomputinghw.Graph
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import com.lorenzo.mobilecomputinghw.data.repository.ReminderRepository
import com.lorenzo.mobilecomputinghw.util.NotificationWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import com.lorenzo.mobilecomputinghw.R

class ReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
): ViewModel() {
    private val _state = MutableStateFlow(ReminderViewState())
    var textFromSpeech: String? by mutableStateOf(null)

    val state: StateFlow<ReminderViewState>
        get() = _state

    suspend fun saveReminder(reminder: Reminder): Long {
        createReminderNotification(reminder)
        return reminderRepository.addReminder(reminder)
    }

    init {
        createNotificationChannel(context = Graph.appContext)
        setOneTimeNotification()
        viewModelScope.launch {
            reminderRepository.reminders().collect { reminders ->
                _state.value = ReminderViewState(reminders)
            }
        }
    }
}

data class ReminderViewState(
    val reminders: List<Reminder> = emptyList()
)

private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescriptionText"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }
        // register the channel with the system
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

private fun setOneTimeNotification() {
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(10, TimeUnit.SECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    //Monitoring for state of work
    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createSuccessNotification()
            } else {
                //createErrorNotification()
            }
        }
}

private fun createSuccessNotification() {
    val notificationId = 1
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("Success! Download complete")
        .setContentText("Your countdown completed successfully")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(Graph.appContext)) {
        //notificationId is unique for each notification that you define
        notify(notificationId, builder.build())
    }
}

private fun createReminderNotification(reminder: Reminder) {
    val notificationId = 2
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("New reminder made")
        .setContentText("Message: ${reminder.message} Date: ${reminder.reminder_time}")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(Graph.appContext)) {
        notify(notificationId, builder.build())
    }
}