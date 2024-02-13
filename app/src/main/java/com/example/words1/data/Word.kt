package com.example.words1.data

data class Word(
    val wordA: String = "",
    val wordB: String = "",
    var score: Int = 0,
    val collectionId: String = "",
    var key: String = ""
) {
    constructor() : this("", "", 0)
}
