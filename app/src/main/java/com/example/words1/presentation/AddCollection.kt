package com.example.words1.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.words1.R
import com.example.words1.data.CollectionState
import com.example.words1.data.WordsEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCollection(
    state: CollectionState,
    navController: NavController,
    onEvent: (WordsEvents) -> Unit
) {

    var selectedLanguageA by remember { mutableStateOf("Language A") }
    var selectedLanguageB by remember { mutableStateOf("Language B") }
    var expanded by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    val icon = if(expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    onEvent(
                        WordsEvents.AddCollection(
                            title = state.title.value,
                            languageA = state.languageA.value,
                            languageB = state.languageB.value
                        )
                    )
                    navController.popBackStack()
                },
                containerColor = colorResource(id = R.color.p2)
            ) {

                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Add Collection"
                )

            }
        },
        containerColor = colorResource(id = R.color.p1)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()

        ) {
            Spacer(modifier = Modifier.padding(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.p2)),
                value = state.title.value,
                onValueChange = {
                    state.title.value = it
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = colorResource(id = R.color.p3)
                ),
                placeholder = {
                    Text(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        text = "Title",
                        color = colorResource(id = R.color.p3)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.p3),
                    unfocusedBorderColor = colorResource(id = R.color.p3)
                )
            )

            Spacer(modifier = Modifier.padding(32.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                OutlinedTextField(
                    value = selectedLanguageA,
                    onValueChange = {},
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = colorResource(id = R.color.p3)
                    ),
                    placeholder = { Text(text = "Language A") },
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
                                    text = "English",
                                    color = colorResource(id = R.color.p3)
                                )
                            },
                            onClick = {
                                state.languageA.value = "English"
                                selectedLanguageA = "English"
                                expanded = false
                            })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Polish",
                                    color = colorResource(id = R.color.p3)
                                )
                            },
                            onClick = {
                                state.languageA.value = "Polish"
                                selectedLanguageA = "Polish"
                                expanded = false
                            })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Spanish",
                                    color = colorResource(id = R.color.p3)
                                )
                            },
                            onClick = {
                                state.languageA.value = "Spanish"
                                selectedLanguageA = "Spanish"
                                expanded = false
                            })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "French",
                                    color = colorResource(id = R.color.p3)
                                )
                            },
                            onClick = {
                                state.languageA.value = "French"
                                selectedLanguageA = "French"
                                expanded = false
                            })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Italian",
                                    color = colorResource(id = R.color.p3)
                                )
                            },
                            onClick = {
                                state.languageA.value = "Italian"
                                selectedLanguageA = "Italian"
                                expanded = false
                            })
                    }
                }

                Spacer(modifier = Modifier.padding(32.dp))

                    OutlinedTextField(
                        value = selectedLanguageB,
                        onValueChange = {},
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp,
                            color = colorResource(id = R.color.p3)
                        ),
                        placeholder = { Text(text = "Language B") },
                        modifier = Modifier
                            .fillMaxWidth()
                            //.padding(16.dp)
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
                    DropdownMenu(
                        expanded = expanded2,
                        onDismissRequest = { expanded2 = false },
                        modifier = Modifier
                            .background(color = colorResource(id = R.color.p2))
                            .zIndex(1f)
                            //.padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() }
                            )

                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "English",
                                    color = colorResource(id = R.color.p3)
                                )
                            },
                            onClick = {
                                state.languageB.value = "English"
                                selectedLanguageB = "English"
                                expanded2 = false
                            })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Polish",
                                    color = colorResource(id = R.color.p3)
                                )
                            },
                            onClick = {
                                state.languageB.value = "Polish"
                                selectedLanguageB = "Polish"
                                expanded2 = false
                            })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Spanish",
                                    color = colorResource(id = R.color.p3)
                                )
                            },
                            onClick = {
                                state.languageB.value = "Spanish"
                                selectedLanguageB = "Spanish"
                                expanded2 = false
                            })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "French",
                                    color = colorResource(id = R.color.p3)
                                )
                            },
                            onClick = {
                                state.languageB.value = "French"
                                selectedLanguageB = "French"
                                expanded2 = false
                            })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Italian",
                                    color = colorResource(id = R.color.p3)
                                )
                            },
                            onClick = {
                                state.languageB.value = "Italian"
                                selectedLanguageB = "Italian"
                                expanded2 = false
                            })
                    }


            }
        }
    }
}








