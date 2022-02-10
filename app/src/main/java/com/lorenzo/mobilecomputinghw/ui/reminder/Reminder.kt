package com.lorenzo.mobilecomputinghw.ui.reminder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.lorenzo.mobilecomputinghw.BuildConfig

@ExperimentalPermissionsApi
@Composable
fun Reminder(
    onBackPress: () -> Unit,
    viewModel: ReminderViewModel = viewModel()
) {
    val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val message = rememberSaveable { mutableStateOf("") }
    val locationX = rememberSaveable { mutableStateOf("") }
    val locationY = rememberSaveable { mutableStateOf("") }
    val reminderTime = rememberSaveable { mutableStateOf("") }
    message.value = viewModel.textFromSpeech ?: ""

    var clickToShowPermission by rememberSaveable { mutableStateOf("F") }


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
                Text(text = "New reminder")
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
                Spacer(modifier = Modifier.height(10.dp))

                //Speech To Text ----------------------------------------

                if (clickToShowPermission == "T") {
                    OpenVoiceWithPermission(
                        onDismiss = { clickToShowPermission = "F" },
                        vm = viewModel,
                        ctxFromScreen = context
                    ){
                        // Do anything you want when the voice has finished and do
                        // not forget to return clickToShowPermission to false!!
                        clickToShowPermission = "F"

                    }
                }

                Button(
                    enabled = true,
                    onClick = {clickToShowPermission = "T"},
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Speech to Text", color = Color.White)
                }
                //------------------------------------------------

                Spacer(modifier = Modifier.height(10.dp))


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

                Spacer(modifier = Modifier.height(10.dp))


                Spacer(modifier = Modifier.height(50.dp))



                Button(
                    enabled = true,
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveReminder(
                                com.lorenzo.mobilecomputinghw.data.entity.Reminder(
                                    message = message.value,
                                    location_x = locationX.value,
                                    location_y = locationY.value,
                                    reminder_time = reminderTime.value,
                                    creation_time = "",
                                    creator_id = 0,
                                    reminder_seen = 0,
                                    img_uri = ""
                                )
                            )
                        }
                        onBackPress()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Save reminder")
                }

            }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun  OpenVoiceWithPermission(
    onDismiss: () -> Unit,
    vm: ReminderViewModel,
    ctxFromScreen: Context,
    finished: () -> Unit
) {

    //val voicePermissionState = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    fun newIntent(ctx: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package",
            BuildConfig.APPLICATION_ID, null
        )
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    startSpeechToText(vm, ctxFromScreen, finished = finished)

}

fun startSpeechToText(vm: ReminderViewModel, ctx: Context, finished: ()-> Unit) {
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(ctx)
    val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    speechRecognizerIntent.putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
    )

    // Optionally I have added my mother language
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el_EN")

    speechRecognizer.setRecognitionListener(object : RecognitionListener {

        override fun onReadyForSpeech(bundle: Bundle?) {
            Log.d("Speech to Text", "onReadyForSpeech")

        }
        override fun onBeginningOfSpeech() {
            Log.d("Speech to Text", "onBeginningOfSpeech")

        }
        override fun onRmsChanged(v: Float) {
            Log.d("Speech to Text", "onRmsChanged")
        }
        override fun onBufferReceived(bytes: ByteArray?) {}
        override fun onEndOfSpeech() {
            finished()
            Log.d("Speech to Text", "End of the Speech")
            // changing the color of your mic icon to
            // gray to indicate it is not listening or do something you want
        }

        override fun onError(i: Int) {
            Log.d("Speech to Text", "onError: $i")
        }

        override fun onResults(bundle: Bundle) {
            val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (result != null) {
                vm.textFromSpeech = result[0]
                Log.d("Speech to Text", "onResults")
                Log.d("Speech to Text", result[0])
            }
        }

        override fun onPartialResults(bundle: Bundle) {
            Log.d("Speech to Text", "onPartialResults")

        }
        override fun onEvent(i: Int, bundle: Bundle?) {
            Log.d("Speech to Text", "onEvent")

        }

    })
    speechRecognizer.startListening(speechRecognizerIntent)
    Log.d("Speech to Text", "startListening")

}
