package com.example.words1.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.words1.R
import com.example.words1.data.WordCollection
import com.example.words1.data.WordsEvents


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    viewModel: WordsViewModel,
    navController: NavController,
    onEvent: (WordsEvents) -> Unit,
) {
    val state by viewModel.state.collectAsState()


    LaunchedEffect(true) {
        viewModel.fetchCollections()
    }



    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(colorResource(id = R.color.p3))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Collections",
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.p2)
                )
            }
        },


        floatingActionButton = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        state.title.value = ""
                        navController.navigate("AddCollection")
                    },
                    containerColor = colorResource(id = R.color.p2)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add new collection"
                    )
                }

                FloatingActionButton(
                    onClick = {
                        state.title.value = ""
                        navController.navigate("SharedCollectionScreen")
                    },
                    containerColor = colorResource(id = R.color.p2)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Share collection"
                    )
                }
            }
        },

        containerColor = colorResource(id = R.color.p1)

    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(colorResource(id = R.color.p1)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            this.items(state.collections) { collection ->
                CollectionItem(
                    viewModel = viewModel,
                    collection = collection,
                    onEvent = onEvent,
                    navController = navController
                )
            }

        }
    }
}

@Composable
fun CollectionItem(
    viewModel: WordsViewModel,
    collection: WordCollection,
    onEvent: (WordsEvents) -> Unit,
    navController: NavController
) {

    var expanded by remember { mutableStateOf(false) }
    var menuPosition by remember { mutableStateOf(DpOffset.Zero) }
    var dialog by remember { mutableStateOf(false) }

    val dismissDialog: () -> Unit = {
        dialog = false
    }

    if (dialog) {
        ConfirmationDialog(
            collection = collection,
            onEvent = onEvent,
            onDismiss = dismissDialog,
            viewModel = viewModel
        )
    }


    Row(

        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.p4))
            .padding(12.dp)
            .pointerInput(true) {
                detectTapGestures(
                    onPress = {
                        expanded = true
                        menuPosition = DpOffset(it.x.toDp(), it.y.toDp())
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = collection.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.p2),
            modifier = Modifier.padding(30.dp)
        )

        DropdownMenu(
            expanded = expanded,
            offset = menuPosition.copy(
                y = menuPosition.y - 80.dp
            ),
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(color = colorResource(id = R.color.p2))
                .zIndex(1f)
                .width(with(LocalDensity.current) { 80.dp }

                )

        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Enter",
                        color = colorResource(id = R.color.p3)
                    )
                },
                onClick = {
                    navController.navigate("WordScreen/${collection.key}")
                    expanded = false
                })
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Share",
                        color = colorResource(id = R.color.p3)
                    )
                },
                onClick = {


                    onEvent(WordsEvents.ShareCollection(collection))
                    expanded = false
                })
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Delete",
                        color = colorResource(id = R.color.p3)
                    )
                },
                onClick = {
                    dialog = true
                    viewModel.fetchCollections()
                    expanded = false
                })
        }
    }
}
@Composable
fun CustomAlertDialogTheme(content: @Composable () -> Unit) {
    val customColors = lightColorScheme(
        surface = colorResource(id = R.color.p2),
    )

    MaterialTheme(
        colorScheme = customColors,
        typography = MaterialTheme.typography,
        content = content
    )
}


@Composable
fun ConfirmationDialog(
    onDismiss: () -> Unit,
    collection: WordCollection,
    onEvent: (WordsEvents) -> Unit,
    viewModel: WordsViewModel
) {

    CustomAlertDialogTheme {
        AlertDialog(
            title = {

                Text(
                    text = "Are you sure?",
                    color = colorResource(id = R.color.p3)
                )
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = {
                        //onConfirm()
                        onEvent(WordsEvents.DeleteCollection(collection))
                        viewModel.fetchCollections()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        colorResource(id = R.color.p2)
                    )
                ) {
                    Text(
                        text = "Yes",
                        color = colorResource(id = R.color.p3)
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        colorResource(id = R.color.p2)
                    )
                ) {
                    Text(
                        text = "No",
                        color = colorResource(id = R.color.p3)
                    )
                }
            },
        )
    }
}




