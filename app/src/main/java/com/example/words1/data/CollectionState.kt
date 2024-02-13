package com.example.words1.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class CollectionState(
    val collections: List<WordCollection> = emptyList(),
    val words: List<WordState> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val key: MutableState<String> = mutableStateOf(""),
    val languageA: MutableState<String> = mutableStateOf(""),
    val languageB: MutableState<String> = mutableStateOf(""),
    val user: MutableState<String> = mutableStateOf(""),
    val shared: MutableState<Boolean> = mutableStateOf(false)
)
