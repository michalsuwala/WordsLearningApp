package com.example.words1.data

sealed interface WordsEvents {

    data class DeleteCollection(val collection: WordCollection): WordsEvents

    data class ShareCollection(val collection: WordCollection): WordsEvents

    data class AddSharedCollection(val collection: WordCollection): WordsEvents

    data class ChangeScore(val word: Word): WordsEvents

    data class Translate(val collection: WordCollection, val text: String, val wA: Boolean): WordsEvents

    data class AddCollection(
        val title: String,
        val languageA: String,
        val languageB:String
    ): WordsEvents

    data class  AddWord(val wordA: String, val wordB: String, val collectionId: String): WordsEvents

    data class DeleteWord(val word: Word): WordsEvents
}