package com.example.words1.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.words1.R
import com.example.words1.data.CollectionState
import com.example.words1.data.WordCollection
import com.example.words1.data.WordState
import com.example.words1.data.WordsEvents


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWord(
    state: WordState,
    navController: NavController,
    onEvent: (WordsEvents) -> Unit,
    collection: WordCollection,
    viewModel: WordsViewModel
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(
                    WordsEvents.AddWord(
                        wordA = viewModel.addWordA,
                        wordB = viewModel.addWordB,
                        collectionId = collection.key
                    )
                )
                navController.navigate("WordScreen/${collection.key}")
                viewModel.addWordA = ""
                viewModel.addWordB = ""
            },
                containerColor = colorResource(id = R.color.p2)
                ) {

                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Add Word"
                )

            }
        },
        containerColor = colorResource(id = R.color.p1)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(colorResource(id = R.color.p2)),
                value = viewModel.addWordA,
                onValueChange = {
                    viewModel.addWordA = it
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = colorResource(id = R.color.p3)
                ),
                placeholder = {
                    Text(
                        text = collection.languageA,
                        color = colorResource(id = R.color.p3)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.p3),
                    unfocusedBorderColor = colorResource(id = R.color.p3)
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(colorResource(id = R.color.p2)),
                value = viewModel.addWordB,
                onValueChange = {
                    viewModel.addWordB = it
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = colorResource(id = R.color.p3)
                ),
                placeholder = {
                    Text(
                        text = collection.languageB,
                        color = colorResource(id = R.color.p3)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.p3),
                    unfocusedBorderColor = colorResource(id = R.color.p3)
                )
            )
            FilledTonalButton(
                onClick = {
                    if(viewModel.addWordA != "" || viewModel.addWordB != "") {
                        onEvent(
                            WordsEvents.Translate(
                                text = if(viewModel.addWordA != "") viewModel.addWordA else viewModel.addWordB,
                                collection = collection,
                                wA = viewModel.addWordA == ""
                            )
                        )
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.p1)
                )
            ) {
                Text(
                    text = "Translate",
                    color = colorResource(id = R.color.p3)
                )
            }

        }
    }
}