package com.example.words1.presentation


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.words1.R



@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreenB(
    viewModel: WordsViewModel,
    navController: NavController,
) {


    //var word by remember { mutableStateOf("") }



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
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.p2)                )
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {

                if(viewModel.index != viewModel.testSize.toInt() - 1) {
                    navController.navigate("TestScreenA") {

                        popUpTo("TestScreenB") { inclusive = true }
                        launchSingleTop = true

                    }
                    viewModel.f = true

                }
                else {
                    viewModel.learnedWords = 0
                    navController.navigate("WordScreen/${viewModel.resultList[0].collectionId}") {
                        popUpTo("TestScreenB") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
                containerColor = colorResource(id = R.color.p2)
            ) {

                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = "Next word"
                )

            }
        },

        containerColor = colorResource(id = R.color.p1)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {



            Text(
                text = if (viewModel.correct) "Correct" else "Incorrect",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(bottom = 12.dp),
                color = if (viewModel.correct) colorResource(id = R.color.p4) else MaterialTheme.colorScheme.error
            )

            Text(
                text = "Correct word is: ${
                    if(viewModel.langA) {
                        viewModel.resultList[viewModel.index].wordA
                    }
                    else {
                        viewModel.resultList[viewModel.index].wordB  
                    }
                }",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(bottom = 12.dp),
                color = colorResource(id = R.color.p3)
            )

            Text(
                text = "Your answer: ${viewModel.testWord}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(bottom = 12.dp),
                color = colorResource(id = R.color.p3)
            )

            Text(
                text = "Your score: ${viewModel.score}/${viewModel.index + 1}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                color = colorResource(id = R.color.p3)            )


        }
    }
}


