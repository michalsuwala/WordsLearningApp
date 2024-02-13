package com.example.words1.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class WordState(
    val words: List<Word> = emptyList(),
    val wordA: MutableState<String> = mutableStateOf(""),
    val wordB: MutableState<String> = mutableStateOf(""),
    val score: MutableState<Int> = mutableStateOf(0),
    val collectionId: MutableState<String> = mutableStateOf(""),
    val key: MutableState<String> = mutableStateOf("")
)