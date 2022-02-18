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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.lorenzo.mobilecomputinghw.BuildConfig
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.LazyColumn



var imageUriState = mutableStateOf<Uri?>(null)
@ExperimentalPermissionsApi
@Composable
fun Reminder(
    onBackPress: () -> Unit,
    viewModel: ReminderViewModel = viewModel()
) {
    //val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val message = rememberSaveable { mutableStateOf("") }
    val locationX = rememberSaveable { mutableStateOf("") }
    val locationY = rememberSaveable { mutableStateOf("") }
    //val reminderTime = rememberSaveable { mutableStateOf("") }
    val reminderHours = rememberSaveable { mutableStateOf("") }
    val reminderMinutes = rememberSaveable { mutableStateOf("") }
    val reminderSeconds = rememberSaveable { mutableStateOf("") }
    val checkedStateNotification = remember { mutableStateOf(true) }


    message.value = viewModel.textFromSpeech ?: message.value

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
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp
                )
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

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = locationY.value,
                    onValueChange = { locationY.value = it },
                    label = { Text(text = "Location Y")},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Notification: ",
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Switch(
                        checked = checkedStateNotification.value,
                        onCheckedChange = { checkedStateNotification.value = it },
                        modifier = Modifier.padding(
                            start = 50.dp
                        )
                    )
                }


                if (checkedStateNotification.value){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        //horizontalArrangement  =  Arrangement.SpaceEvenly
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.3f)
                        ) {
                            OutlinedTextField(
                                value = reminderHours.value,
                                onValueChange = { reminderHours.value = it },
                                label = { Text(text = "Hours")},
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            OutlinedTextField(
                                value = reminderMinutes.value,
                                onValueChange = { reminderMinutes.value = it },
                                label = { Text(text = "Minutes")},
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            //modifier = Modifier.fillMaxWidth(0.3f)
                        ) {
                            OutlinedTextField(
                                value = reminderSeconds.value,
                                onValueChange = { reminderSeconds.value = it },
                                label = { Text(text = "Seconds")},
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                                )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(10.dp))

                val uri = imageUriState.value

                PhotoSelector()

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    enabled = true,
                    onClick = {
                        coroutineScope.launch {
                            if(reminderHours.value == ""){
                                reminderHours.value = "0"
                            }
                            if(reminderMinutes.value == ""){
                                reminderMinutes.value = "0"
                            }
                            if(reminderSeconds.value == ""){
                                reminderSeconds.value = "0"
                            }
                            val seen: Long = if(checkedStateNotification.value){
                                0
                            }else{
                                1
                            }


                            viewModel.saveReminder(
                                com.lorenzo.mobilecomputinghw.data.entity.Reminder(
                                    message = message.value,
                                    location_x = locationX.value,
                                    location_y = locationY.value,
                                    reminder_time = (((reminderHours.value.toLong()*60)+reminderMinutes.value.toLong())*60+reminderSeconds.value.toLong()).toString(),
                                    creation_time = Date().time.toString(),
                                    creator_id = 0,
                                    reminder_seen = seen,
                                    img_uri = uri.toString()
                                ), checkedStateNotification.value
                            )
                        }
                        onBackPress()
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp)
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
                Log.d("Speech to Text", "onResult: "+result[0])
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

@Composable
fun PhotoSelector() {
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            val selectImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())  { uri ->
                imageUriState.value = uri
            }

            if (imageUriState.value != null) {
                //GlideImage(data = imageUriState.value!!)
                Image(
                    painter = rememberImagePainter(
                        imageUriState.value
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .size(64.dp)
                )
            }

            Button(
                onClick = { selectImageLauncher.launch("image/*") },
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp)
            ) {
                Text("Open Gallery")
            }

        }
    }
}

private fun Date.formatToString(): String {
    return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(this)
}

private fun Long.toDateString(): String {
    return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(this))

}