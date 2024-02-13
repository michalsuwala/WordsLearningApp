package com.example.words1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.words1.presentation.AddCollection
import com.example.words1.presentation.AddWord
import com.example.words1.presentation.CollectionScreen
import com.example.words1.presentation.LoginScreen
import com.example.words1.presentation.SharedCollectionScreen
import com.example.words1.presentation.SignupScreen
import com.example.words1.presentation.TestScreen
import com.example.words1.presentation.TestScreenA
import com.example.words1.presentation.TestScreenB
import com.example.words1.presentation.WordsScreen
import com.example.words1.presentation.WordsViewModel
import com.example.words1.ui.theme.Words1Theme


@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<WordsViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return WordsViewModel() as T
                }
            }
        }
    )

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            Words1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.p1)
                ) {

                    val state by viewModel.state.collectAsState()
                    val stateW by viewModel.stateW.collectAsState()
                    val navController = rememberNavController()

                    NavHost(
                        navController= navController,
                        startDestination = "LoginScreen",
                        enterTransition = {
                            fadeIn(animationSpec = tween(500))
                        },
                        exitTransition = {
                            fadeOut(animationSpec = tween(0))
                        }
                    ) {
                        composable("TestScreen/{key}") {
                                backStackEntry ->
                            val collectionKey = backStackEntry.arguments?.getString("key")
                            val selectedCollection = viewModel.state.value.collections.find { it.key == collectionKey}
                            selectedCollection?.let {
                                TestScreen(
                                    navController = navController,
                                    onEvent = viewModel::onEvent,
                                    collection = it,
                                    context = this@MainActivity,
                                    viewModel = viewModel
                                )

                            }
                        }
                        composable("TestScreenA") {
                            TestScreenA(
                                navController = navController,
                                onEvent = viewModel::onEvent,
                                viewModel = viewModel
                            )
                        }
                        composable("TestScreenB") {
                            TestScreenB(
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                        composable("LoginScreen") {
                            LoginScreen(
                                viewModel = viewModel,
                                navController = navController,
                                context = this@MainActivity
                            )
                        }
                            composable("SharedCollectionScreen") {
                                SharedCollectionScreen(
                            viewModel = viewModel,
                            navController = navController,
                            onEvent = viewModel::onEvent)
                    }
                        composable("SignupScreen") {
                            SignupScreen(
                                viewModel = viewModel,
                                navController = navController,
                                onEvent = viewModel::onEvent,
                                context = this@MainActivity
                            )
                        }
                        composable("CollectionScreen") {
                            CollectionScreen(
                                viewModel = viewModel,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                        composable("AddCollection") {
                            AddCollection(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                        composable("WordScreen/{key}") {
                                backStackEntry ->
                            val key = backStackEntry.arguments?.getString("key")
                            val selectedCollection = viewModel.state.value.collections.find { it.key == key }
                            selectedCollection?.let {
                                WordsScreen(
                                    viewModel = viewModel,
                                    navController = navController,
                                    onEvent = viewModel::onEvent,
                                    collection = it
                                )
                            }
                        }
                        composable("AddWord/{key}") {
                                backStackEntry ->
                            val collectionKey = backStackEntry.arguments?.getString("key")
                            val selectedCollection = viewModel.state.value.collections.find { it.key == collectionKey}
                            selectedCollection?.let {
                                AddWord(
                                    navController = navController,
                                    state = stateW,
                                    onEvent = viewModel::onEvent,
                                    collection = it,
                                    viewModel = viewModel
                                )

                            }
                        }


                    }
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Words1Theme {

    }
}

