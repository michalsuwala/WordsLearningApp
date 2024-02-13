package com.example.words1.presentation


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.words1.R
import com.example.words1.data.WordsEvents


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreenA(
    viewModel: WordsViewModel,
    navController: NavController,
    onEvent: (WordsEvents) -> Unit
) {


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
                    text = "${viewModel.index + 1}/${viewModel.testSize}",
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.p2)
                )
            }
        },

        containerColor = colorResource(id = R.color.p1)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {


            if(viewModel.f) {
                viewModel.testWord = ""
                viewModel.index++
                viewModel.f = false
            }
            Text(
                text = "Translate to ${viewModel.language}: ${
                    if(viewModel.langA) {
                        viewModel.resultList[viewModel.index].wordB
                    }
                    else {
                        viewModel.resultList[viewModel.index].wordA
                    }
                }",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .padding(top = 16.dp),
                color = colorResource(id = R.color.p3)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(colorResource(id = R.color.p2)),
                value = viewModel.testWord,
                onValueChange = {
                    viewModel.testWord = it
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = colorResource(id = R.color.p3)
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.p3),
                    unfocusedBorderColor = colorResource(id = R.color.p3)
                )
            )


            FilledTonalButton(
                onClick = {
                    if(viewModel.langA) {
                        val equal = viewModel.levenshteinRecursive(
                            viewModel.testWord.trimEnd().lowercase(),
                            viewModel.resultList[viewModel.index].wordA.trimEnd().lowercase(),
                            viewModel.testWord.trimEnd().length,
                            viewModel.resultList[viewModel.index].wordA.trimEnd().length
                        )
                        viewModel.correct = (equal == 0 || equal == 1)

                            /*viewModel.testWord.trimEnd().lowercase()
                            .equals(
                                viewModel.resultList[viewModel.index].wordA.trimEnd().lowercase()
                            )*/
                    }
                    else {
                        val equal = viewModel.levenshteinRecursive(
                            viewModel.testWord.trimEnd().lowercase(),
                            viewModel.resultList[viewModel.index].wordB.trimEnd().lowercase(),
                            viewModel.testWord.trimEnd().length,
                            viewModel.resultList[viewModel.index].wordB.trimEnd().length
                        )
                        viewModel.correct = (equal == 0 || equal == 1)
                            /*viewModel.testWord.trimEnd().lowercase()
                            .equals(
                                viewModel.resultList[viewModel.index].wordB.trimEnd().lowercase()
                            )*/
                    }
                    if(viewModel.correct) {
                        viewModel.score++
                    }
                    onEvent(WordsEvents.ChangeScore(viewModel.resultList[viewModel.index]))



                    navController.navigate("TestScreenB") {
                        popUpTo("TestScreenA") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.p2)
                )
            ) {
                Text(
                    text = "Confirm",
                    color = colorResource(id = R.color.p3)
                )
            }

        }
    }
}