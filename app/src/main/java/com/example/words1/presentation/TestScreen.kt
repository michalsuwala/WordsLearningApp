package com.example.words1.presentation


import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.words1.R
import com.example.words1.data.WordCollection
import com.example.words1.data.WordState
import com.example.words1.data.WordsEvents


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    viewModel: WordsViewModel,
    navController: NavController,
    onEvent: (WordsEvents) -> Unit,
    collection: WordCollection,
    context: Context
) {
    val state by viewModel.stateW.collectAsState()


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                var size = 0
                try {
                    size = viewModel.testSize.toInt()
                } catch (_: NumberFormatException){
                }
                 if(size <= 0) {
                    Toast.makeText(context ,"Add test size", Toast.LENGTH_SHORT).show()
                }
                else if(size > state.words.size) {
                    Toast.makeText(context ,"Test size is too big", Toast.LENGTH_SHORT).show()
                }
                else {
                    viewModel.getWords(collection.words, size)
                    viewModel.index = 0
                     viewModel.f = false
                     viewModel.score = 0
                     viewModel.testWord = ""
                    navController.navigate("TestScreenA") {
                        popUpTo("WordScreen/${viewModel.resultList[0].collectionId}") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
                containerColor = colorResource(id = R.color.p2)
            ) {

                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Start Test"
                )

            }
        },
        containerColor = colorResource(id = R.color.p1)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var textFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
            var expanded by remember { mutableStateOf(false) }
            var expanded2 by remember { mutableStateOf(false) }
            var lang by remember { mutableStateOf("") }
            var learned by remember { mutableStateOf("") }

            val icon = if(expanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown


            Text(
                text = "Configure test",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 30.dp),
                color = colorResource(id = R.color.p3),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.p2)),
                value = viewModel.testSize,
                onValueChange = {
                    viewModel.testSize = it
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = colorResource(id = R.color.p3)
                ),
                placeholder = {
                    Text(
                        text = "Number of words: max ${state.words.size}",
                        color = colorResource(id = R.color.p3)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.p3),
                    unfocusedBorderColor = colorResource(id = R.color.p3)
                )
            )

            Spacer(modifier = Modifier.padding(16.dp))

            OutlinedTextField(
                value = lang,
                onValueChange = {},
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = colorResource(id = R.color.p3)
                ),
                placeholder = { Text(
                    text = "Answer in",
                    color = colorResource(id = R.color.p3)
                ) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.p2))
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                trailingIcon = {
                    Icon(icon, "contentDescription",
                        Modifier.clickable { expanded = !expanded })
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.p3),
                    unfocusedBorderColor = colorResource(id = R.color.p3)
                )
            )
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(id = R.color.p2))
                    .zIndex(4f),

                ) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.p2))
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = collection.languageA,
                                color = colorResource(id = R.color.p3)
                            )
                        },
                        onClick = {
                            lang = collection.languageA
                            viewModel.language = collection.languageA
                            viewModel.langA = true
                            expanded = false
                        })
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = collection.languageB,
                                color = colorResource(id = R.color.p3)
                            )
                        },
                        onClick = {
                            lang = collection.languageB
                            viewModel.language = collection.languageB
                            viewModel.langA = false
                            expanded = false
                        })
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            OutlinedTextField(
                value = learned,
                onValueChange = {},
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = colorResource(id = R.color.p3)
                ),
                placeholder = { Text(
                    text = "All words",
                    color = colorResource(id = R.color.p3)
                ) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.p2))
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                trailingIcon = {
                    Icon(icon, "contentDescription",
                        Modifier.clickable { expanded2 = !expanded2 })
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.p3),
                    unfocusedBorderColor = colorResource(id = R.color.p3)
                )
            )
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(id = R.color.p2))
                    .zIndex(4f),

                ) {
                DropdownMenu(
                    expanded = expanded2,
                    onDismissRequest = { expanded2 = false },
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.p2))
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "All words",
                                color = colorResource(id = R.color.p3)
                            )
                        },
                        onClick = {
                            viewModel.learnedWords = 0
                            viewModel.fetchWords(collection.key)
                            learned = "All words"
                            expanded2 = false
                        })
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Words learned",
                                color = colorResource(id = R.color.p3)
                            )
                        },
                        onClick = {
                            viewModel.learnedWords = 1
                            viewModel.fetchWords(collection.key)
                            learned = "Words learned"
                            expanded2 = false
                        })
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Words to learn",
                                color = colorResource(id = R.color.p3)
                            )
                        },
                        onClick = {
                            viewModel.learnedWords = -1
                            viewModel.fetchWords(collection.key)
                            learned = "Words to learn"
                            expanded2 = false
                        })
                }
            }
        }
    }
}

