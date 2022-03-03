package com.lorenzo.mobilecomputinghw.ui.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.model.LatLng
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import com.lorenzo.mobilecomputinghw.R
import kotlinx.coroutines.launch
import java.lang.Math.sqrt
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

@Composable
fun CategoryReminder(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    latlng: LatLng?
) {
    val viewState by viewModel.state.collectAsState()

    Column(modifier = modifier) {
        ReminderList(
            list = viewState.reminders,
            viewmodel = viewModel,
            navController = navController,
            latlng = latlng
        )
    }
}

@Composable
private fun ReminderList(
    list: List<Reminder>,
    viewmodel: HomeViewModel,
    navController: NavController,
    latlng: LatLng?
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            if(item.reminder_seen.toInt() == 1 && positionIsNear(item,latlng)){
                ReminderListItem(
                    reminder = item,
                    viewModel = viewmodel,
                    onClick = {},
                    modifier = Modifier.fillParentMaxWidth(),
                    navController = navController,
                )
            }
        }
    }
}

private fun positionIsNear(r: Reminder, latlng: LatLng?): Boolean {
    val x = ((latlng?.latitude ?: r.location_x) - r.location_x).pow(2)
    val y = ((latlng?.longitude ?: r.location_y) - r.location_y).pow(2)
    val distance = kotlin.math.sqrt(x + y)

    if(r.location_x == 0.0 && r.location_y == 0.0)
        return true
    return distance < 0.05
}


@Composable
private fun ReminderListItem(
    reminder: Reminder,
    viewModel: HomeViewModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (divider, reminderImage, remainderMessage, locationX, locationY, iconEdit, iconDelete, date) = createRefs()

        val viewState by viewModel.state.collectAsState()

        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        val coroutineScope = rememberCoroutineScope()

        Image(painter = rememberImagePainter(Uri.parse(reminder.img_uri)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .fillMaxSize()
                .constrainAs(reminderImage) {
                linkTo(
                    start = parent.start,
                    end = remainderMessage.start,
                    startMargin = 10.dp,
                    endMargin = 10.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )
                top.linkTo(parent.top, margin = 10.dp)
                width = Dimension.preferredWrapContent
            })

        // message
        Text(
            text = reminder.message,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(remainderMessage) {
                /*linkTo(
                    start = reminderImage.start,
                    end = parent.end,
                    startMargin = 24.dp,
                    endMargin = 2.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )*/
                top.linkTo(parent.top, margin = 10.dp)
                start.linkTo(reminderImage.end, margin = 30.dp)

                width = Dimension.preferredWrapContent
            }
        )

        val t: String = if(reminder.reminder_time == "0")
            ""
        else
            "Time: " +reminder.reminder_time

        Text(
            text = t,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = 24.dp,
                    endMargin = 24.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )
                //centerVerticallyTo(paymentCategory)
                top.linkTo(reminderImage.bottom, 6.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            }
        )

        val x: String = if(reminder.location_x == 0.0)
            ""
        else
            "X: ${"%.4f".format(reminder.location_x)}"

        Text(
            text = x,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(locationX) {
                linkTo(
                    start = date.start,
                    end = iconEdit.start,
                    startMargin = 80.dp,
                    endMargin = 24.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )
                //centerVerticallyTo(paymentCategory)
                top.linkTo(reminderImage.bottom, 6.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            }
        )

        val y: String = if(reminder.location_y == 0.0)
            ""
        else
            "Y: ${"%.4f".format(reminder.location_y)}"

        Text(
            text = y,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(locationY) {
                linkTo(
                    start = locationX.start,
                    end = iconEdit.start,
                    startMargin = 80.dp,
                    endMargin = 24.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )
                //centerVerticallyTo(paymentCategory)
                top.linkTo(reminderImage.bottom, 6.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            }
        )

        // Icon edit
        IconButton(
            onClick = {
                navController.navigate(route = "editReminder/${reminder.reminderId}")
                      },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(iconEdit) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(iconDelete.start, 5.dp)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.edit)
            )
        }

        //Icon Delete
        IconButton(
            onClick = {
                coroutineScope.launch {
                    viewModel.deleteReminder(reminder.reminderId)
                }
            },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(iconDelete) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end, 5.dp)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.delete)
            )
        }


    }
}

private fun Date.formatToString(): String {
    return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(this)
}

private fun Long.toDateString(): String {
    return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(this))
}