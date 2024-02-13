package com.example.words1.data

data class WordCollection(
    val title: String,
    val words: List<Word> = emptyList(),
    val key: String,
    val languageA: String,
    val languageB: String,
    val user: String,
    val shared: Boolean
)
