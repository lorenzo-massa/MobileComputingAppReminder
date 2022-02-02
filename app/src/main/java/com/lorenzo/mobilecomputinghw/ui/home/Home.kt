package com.lorenzo.mobilecomputinghw.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzo.mobilecomputinghw.data.entity.Category
import com.google.accompanist.insets.systemBarsPadding
import com.lorenzo.mobilecomputinghw.R
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
import com.lorenzo.mobilecomputinghw.data.entity.User
import com.lorenzo.mobilecomputinghw.ui.home.categoryPayment.CategoryReminder

@Composable
fun Home(
    viewModel: HomeViewModel = viewModel(),
    navController: NavController,
    userLogged: String?
) {
    val viewState by viewModel.state.collectAsState()

    //val selectedCategory = viewState.selectedCategory

    //if (viewState.categories.isNotEmpty() && selectedCategory != null) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(
                //selectedCategory = selectedCategory,
                //categories = viewState.categories,
                //onCategorySelected = viewModel::onCategorySelected,
                reminders = viewState.reminders,
                navController = navController,
                userLogged = userLogged
            )
        }
    //}
}


@Composable
fun HomeContent(
    //selectedCategory: Category,
    //categories: List<Category>,
    //onCategorySelected: (Category) -> Unit,
    reminders: List<Reminder>,
    navController: NavController,
    userLogged: String?
) {
    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(route = "reminder") },
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

            HomeAppBar(
                backgroundColor = appBarColor,
                navController = navController,
                userLogged = userLogged
            )

            /*CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
            )
            */
            CategoryReminder(
                viewModel(),
                modifier = Modifier.fillMaxSize(),
                //categoryId = selectedCategory.id
            )
        }
    }
}

@Composable
private fun HomeAppBar(
    backgroundColor: Color,
    navController: NavController,
    userLogged: String?
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
                    if (userLogged != null) {
                        Box (
                            contentAlignment = Alignment.CenterEnd
                                ){
                            Text(
                                text = userLogged, textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = stringResource(R.string.account),
                                tint = MaterialTheme.colors.primary,
                            )
                        }
                    }
                }

                DropdownMenuItem(onClick = {
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

@Composable
private fun CategoryTabs(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 24.dp,
        indicator = emptyTabIndicator,
        modifier = Modifier.fillMaxWidth(),
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategorySelected(category) }
            ) {
                ChoiceChipContent(
                    text = category.name,
                    selected = index == selectedIndex,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ChoiceChipContent(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.secondary.copy(alpha = 0.87f)
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> Color.White
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}
