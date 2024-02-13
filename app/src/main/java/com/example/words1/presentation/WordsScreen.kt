package com.example.words1.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.words1.R
import com.example.words1.data.Word
import com.example.words1.data.WordCollection
import com.example.words1.data.WordsEvents


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordsScreen(
    viewModel: WordsViewModel,
    navController: NavController,
    onEvent: (WordsEvents) -> Unit,
    collection: WordCollection
) {
    val state by viewModel.stateW.collectAsState()
    var isFabVisible by remember { mutableStateOf(true) }


    LaunchedEffect(true) {
        viewModel.fetchWords(collection.key)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(colorResource(id = R.color.p3))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Word List",
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.p2)
                )
                IconButton(
                    onClick = {
                        isFabVisible = !isFabVisible
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "Show icons",
                        tint = colorResource(id = R.color.p2)
                    )
                }


            }
        },

        floatingActionButton = {
            if (isFabVisible) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FloatingActionButton(
                        onClick = {
                            viewModel.learnedWords = 0
                            viewModel.fetchWords(collection.key)
                            navController.navigate("TestScreen/${collection.key}") {
                                popUpTo("TestScreen") { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        containerColor = colorResource(id = R.color.p2)
                    ) {
                        /*Text(
                            text = "Test",
                            color = colorResource(id = R.color.black)
                        )*/
                        Icon(imageVector = Icons.Rounded.Done, contentDescription = "Test")
                    }

                    FloatingActionButton(
                        onClick = {
                            navController.navigate("AddWord/${collection.key}") {
                                launchSingleTop = true
                            }
                        },
                        containerColor = colorResource(id = R.color.p2)
                    ) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add new word")
                    }
                    FloatingActionButton(
                        onClick = {
                            if (viewModel.learnedWords < 1) viewModel.learnedWords++
                            else viewModel.learnedWords = -1
                            viewModel.fetchWords(collection.key)
                        },
                        containerColor = colorResource(id = R.color.p2)
                    ) {
                        Icon(imageVector = Icons.Rounded.List, contentDescription = "Change words")
                    }

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

            items(state.words) { word ->
                WordItem(
                    viewModel = viewModel,
                    word = word,
                    onEvent = onEvent
                )
            }

        }
    }
}

@Composable
fun WordItem(
    viewModel: WordsViewModel,
    word: Word,
    onEvent: (WordsEvents) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.p4))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var dialog by remember { mutableStateOf(false) }

        val dismissDialog: () -> Unit = {
            dialog = false
        }

        if (dialog) {
            ConfirmationWordDialog(
                word = word,
                onEvent = onEvent,
                onDismiss = dismissDialog,
                viewModel = viewModel
            )
        }

        Column() {
            Text(
                text = word.wordA,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.p2)
            )
            Text(
                text = word.wordB,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.p2)
            )
            Text(
                text = "Score: ${word.score}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.p2)
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = {
                dialog = true
            }
        ) {

            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete Collection",
                modifier = Modifier.size(35.dp),
                tint = colorResource(id = R.color.p2)
            )

        }

    }
}




@Composable
fun ConfirmationWordDialog(
    onDismiss: () -> Unit,
    word: Word,
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
                        onEvent(WordsEvents.DeleteWord(word))
                        viewModel.fetchWords(collectionId = word.collectionId)
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
