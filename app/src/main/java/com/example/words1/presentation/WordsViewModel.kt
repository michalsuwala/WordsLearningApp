package com.example.words1.presentation


import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.words1.data.CollectionState
import com.example.words1.data.User
import com.example.words1.data.Word
import com.example.words1.data.WordCollection
import com.example.words1.data.WordState
import com.example.words1.data.WordsEvents
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import deepl.api.v2.DeepLClient
import deepl.api.v2.model.translation.SourceLanguage
import deepl.api.v2.model.translation.TargetLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random


class WordsViewModel : ViewModel() {
    private val database = Firebase.database("https://words-b780c-default-rtdb.europe-west1.firebasedatabase.app/")
    val reference = database.getReference("")

    private val _state = MutableStateFlow(CollectionState(emptyList()))
    val state: StateFlow<CollectionState> = _state

    private val _stateW = MutableStateFlow(WordState(emptyList()))
    val stateW: StateFlow<WordState> = _stateW

    private var user by mutableStateOf("")

    var check by mutableStateOf(false)
    private val _loginState = MutableLiveData<Boolean?>(null)
    val loginState: LiveData<Boolean?> = _loginState

    val resultList = mutableListOf<Word>()
    var testSize by mutableStateOf("")
    var index: Int by mutableIntStateOf(0)
    var score: Int by mutableIntStateOf(0)
    var testWord by mutableStateOf("")
    var correct by mutableStateOf(false)
    var langA by mutableStateOf(false)
    var language by mutableStateOf("")
    var f by mutableStateOf(true)

    var addWordA by mutableStateOf("")
    var addWordB by mutableStateOf("")

    var learnedWords: Int by mutableIntStateOf(0)

    fun onEvent(event: WordsEvents) {
        when(event) {
            is WordsEvents.DeleteCollection -> {
                viewModelScope.launch {
                    deleteCollection(event.collection.key)
                }
            }
            is WordsEvents.AddCollection -> {
                viewModelScope.launch {
                    addCollection(event.title, event.languageA, event.languageB)
                }
            }
            is WordsEvents.AddWord -> {
                addWordToCollection(wordA = event.wordA, wordB = event.wordB, collectionId = event.collectionId)
            }
            is WordsEvents.DeleteWord -> {
                deleteWord(event.word)
            }
            is WordsEvents.ShareCollection -> {
                shareCollection(event.collection.key)
            }
            is WordsEvents.AddSharedCollection -> {
                addSharedCollection(event.collection.key)
            }
            is WordsEvents.ChangeScore -> {
                changeScore(event.word)
            }
            is WordsEvents.Translate -> {
                translate(event.text, event.collection, event.wA)
            }
        }
    }


    fun fetchCollections() {
        val collectionsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedCollections = mutableListOf<WordCollection>()
                for(collectionSnapshot in snapshot.children) {
                    val collectionUser = collectionSnapshot.child("user").getValue(String::class.java)
                    val collectionShared = collectionSnapshot.child("shared").getValue(Boolean::class.java)

                    if (collectionUser == user && !collectionShared!!) {
                        val words = mutableListOf<Word>()
                        for (wordSnapshot in collectionSnapshot.child("words").children) {
                            val word = wordSnapshot.getValue(Word::class.java)
                            word?.let { words.add(it) }
                        }

                        val collectionId = collectionSnapshot.key

                        val collection = WordCollection(
                            title = collectionSnapshot.child("title").getValue(String::class.java) ?: "",
                            words = words,
                            key = collectionId ?: "",
                            languageA = collectionSnapshot.child("languageA").getValue(String::class.java) ?: "",
                            languageB = collectionSnapshot.child("languageB").getValue(String::class.java) ?: "",
                            user = user,
                            shared = collectionSnapshot.child("shared").getValue(Boolean::class.java) ?: false
                        )
                        fetchedCollections.add(collection)
                    }
                }
                _state.value = CollectionState(fetchedCollections)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        reference.child("collections").orderByChild("user").equalTo(user)
            .addListenerForSingleValueEvent(collectionsListener)
    }

    fun fetchSharedCollections() {

        val collectionsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedCollections = mutableListOf<WordCollection>()
                for(collectionSnapshot in snapshot.children) {
                    val collectionShared = collectionSnapshot.child("shared").getValue(Boolean::class.java) ?: false

                    if(collectionShared) {

                        val words = mutableListOf<Word>()
                        for (wordSnapshot in collectionSnapshot.child("words").children) {
                            val word = wordSnapshot.getValue(Word::class.java)
                            word?.let { words.add(it) }
                        }

                        val collectionId = collectionSnapshot.key

                        val collection = WordCollection(
                            title = collectionSnapshot.child("title").getValue(String::class.java) ?: "",
                            words = words,
                            key = collectionId ?: "",
                            languageA = collectionSnapshot.child("languageA").getValue(String::class.java) ?: "",
                            languageB = collectionSnapshot.child("languageB").getValue(String::class.java) ?: "",
                            user = user,
                            shared = true
                        )
                        fetchedCollections.add(collection)
                    }
                }
                _state.value = CollectionState(fetchedCollections)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        reference.child("collections").orderByChild("shared").equalTo(true)
            .addListenerForSingleValueEvent(collectionsListener)
    }


    fun fetchWords(collectionId: String) {
        val collectionsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedWords = mutableListOf<Word>()
                val collectionSnapshot = snapshot.child("collections").child(collectionId)

                for(wordSnapshot in collectionSnapshot.child("words").children) {
                    val word = wordSnapshot.getValue(Word::class.java)
                    if(word != null && (word.score < 10 && learnedWords == -1 ||
                                word.score >= 10 && learnedWords == 1 || learnedWords == 0)) {
                        fetchedWords.add(word)
                    }
                }

                _stateW.value = WordState(fetchedWords)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        reference.addListenerForSingleValueEvent(collectionsListener)
    }


    private fun shareCollection(collectionId: String) {
        val collectionsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val collectionSnapshot = snapshot.child("collections").child(collectionId)

                val title = collectionSnapshot.child("title").getValue(String::class.java) ?: ""
                val languageA = collectionSnapshot.child("languageA").getValue(String::class.java) ?: ""
                val languageB = collectionSnapshot.child("languageB").getValue(String::class.java) ?: ""

                val newCollectionRef = reference.child("collections").push()
                val newCollectionId = newCollectionRef.key

                if (newCollectionId != null) {
                    val newCollection = WordCollection(
                        title = title,
                        key = newCollectionId,
                        languageA = languageA,
                        languageB = languageB,
                        user = user,
                        shared = true
                    )

                    newCollectionRef.setValue(newCollection)

                    val wordsSnapshot = collectionSnapshot.child("words")
                    for(wordSnapshot in wordsSnapshot.children) {
                        val word = wordSnapshot.getValue(Word::class.java)
                        if(word != null) {
                            val newWordRef = newCollectionRef.child("words").push()
                            val newWordId = newWordRef.key
                            if(newWordId != null) {
                                word.key = newWordId
                            }
                            word.score = 0
                            newWordRef.setValue(word)
                        }
                    }

                    //fetchCollections()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        reference.addListenerForSingleValueEvent(collectionsListener)
    }


    private fun addSharedCollection(collectionId: String) {
        val collectionsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val collectionSnapshot = snapshot.child("collections").child(collectionId)

                if(collectionSnapshot.child("user").getValue(String::class.java).equals(user)) return

                val title = collectionSnapshot.child("title").getValue(String::class.java) ?: ""
                val languageA = collectionSnapshot.child("languageA").getValue(String::class.java) ?: ""
                val languageB = collectionSnapshot.child("languageB").getValue(String::class.java) ?: ""

                val newCollectionRef = reference.child("collections").push()
                val newCollectionId = newCollectionRef.key

                if (newCollectionId != null) {
                    val newCollection = WordCollection(
                        title = title,
                        key = newCollectionId,
                        languageA = languageA,
                        languageB = languageB,
                        user = user,
                        shared = false
                    )

                    newCollectionRef.setValue(newCollection)

                    val wordsSnapshot = collectionSnapshot.child("words")
                    for(wordSnapshot in wordsSnapshot.children) {
                        val word = wordSnapshot.getValue(Word::class.java)
                        if(word != null) {
                            val newWordRef = newCollectionRef.child("words").push()
                            val newWordId = newWordRef.key
                            if(newWordId != null) {
                                word.key = newWordId
                            }
                            word.score = 0
                            newWordRef.setValue(word)
                        }
                    }

                    //fetchCollections()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        reference.addListenerForSingleValueEvent(collectionsListener)
    }


    private fun addCollection(title: String, languageA: String, languageB: String) {
        val collectionRef = reference.child("collections").push()
        val collectionId = collectionRef.key
        if (collectionId != null) {
            val collection = WordCollection(title, key = collectionId, languageA = languageA, languageB = languageB, user = user, shared = false)
            collectionRef.setValue(collection)
        }
    }


    private fun addWordToCollection(collectionId: String, wordA: String, wordB: String) {
        val wordRef = reference.child("collections").child(collectionId).child("words").push()
        val wordId = wordRef.key
        if (wordId != null) {
            val word = Word(wordA = wordA, wordB = wordB, score = 0, collectionId = collectionId, key = wordId)
            wordRef.setValue(word)
        }
        fetchCollections()
    }

    private fun deleteCollection(key: String) {
        val collectionRef = database.getReference(FirebaseConstants.COLLECTION_NODE).child(key)
        val ownerListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val owner = snapshot.child("user").getValue(String::class.java)
                if(owner == user || owner == "mm") {
                    collectionRef.removeValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        collectionRef.addListenerForSingleValueEvent(ownerListener)
    }


    private fun deleteWord(word: Word) {
        val wordRef = database.getReference(FirebaseConstants.COLLECTION_NODE).child(word.collectionId).child("words").child(word.key)

        wordRef.removeValue()
        fetchCollections()
    }


    fun createUser(username: String, password: String) {
        val ref = reference.child("users")
        ref.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!dataSnapshot.exists()) {
                    val id = ref.push().key
                    val userData = User(id, username, password)
                    ref.child(id!!).setValue(userData)
                    _loginState.value = true
                    user = username
                }
                else {
                    _loginState.value = false
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    fun login(username: String, password: String) {
        val ref = reference.child("users")
        ref.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val userSnapshot = dataSnapshot.children.first()
                    val psw = userSnapshot.child("password").getValue(String::class.java)
                    _loginState.value = psw == password
                    if(psw == password) {
                        user = username
                    }
                } else {
                    _loginState.value = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _loginState.value = false
            }
        })
    }


    private fun changeScore(word: Word) {
        val wordRef = database.getReference(FirebaseConstants.COLLECTION_NODE)
            .child(word.collectionId)
            .child("words")
            .child(word.key)

        wordRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wordData = dataSnapshot.getValue(Word::class.java)

                if(wordData != null) {

                    if(wordData.score > 0 && !correct) wordData.score -= 1
                    else if(correct) {
                        wordData.score += 1
                    }


                    wordRef.setValue(wordData)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    object FirebaseConstants {
        const val COLLECTION_NODE = "collections"
    }


    fun getWords(wordList: List<Word>, n: Int) {
        resultList.clear()
        val random = Random.Default

        while(resultList.size < n) {
            val randomIndex = random.nextInt(wordList.size)
            val randomWord = wordList[randomIndex]
            if(randomWord !in resultList && (randomWord.score < 10 && learnedWords == -1 ||
                        randomWord.score >= 10 && learnedWords == 1 || learnedWords == 0)) {
                resultList.add(randomWord)
            }
        }
    }


    private fun translate(text: String, collection: WordCollection, wA: Boolean) {
        val apiKey = "736dd3c4-d3e2-eabe-efce-2d7bf1fe3af2:fx"
        val client = DeepLClient(token = apiKey)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        try {
            client.use {
                println(text)
                val translation = client.translate(
                    text = text,
                    targetLang = if(wA) targetLanguage(collection.languageA) else targetLanguage(collection.languageB),
                    sourceLang = if(wA) sourceLanguage(collection.languageB) else sourceLanguage(collection.languageA)
                )
                if(wA) addWordA = translation.translations[0].text
                else addWordB = translation.translations[0].text
            }
        } catch (e: Exception)
        {
            println("An error occurred: ${e.message}")
            e.printStackTrace()
        }
    }


    private fun sourceLanguage(language: String): SourceLanguage {
        return when (language) {
            "English" -> {
                SourceLanguage.English
            }
            "Polish" -> {
                SourceLanguage.Polish
            }
            "French" -> {
                SourceLanguage.French
            }
            "Spanish" -> {
                SourceLanguage.Spanish
            }
            "Italian" -> {
                SourceLanguage.Italian
            }
            else -> {
                SourceLanguage.English
            }
        }
    }


    private fun targetLanguage(language: String): TargetLanguage {
        return when (language) {
            "English" -> {
                TargetLanguage.BritishEnglish
            }
            "Polish" -> {
                TargetLanguage.Polish
            }
            "French" -> {
                TargetLanguage.French
            }
            "Spanish" -> {
                TargetLanguage.Spanish
            }
            "Italian" -> {
                TargetLanguage.Italian
            }
            else -> {
                TargetLanguage.BritishEnglish
            }
        }
    }

    fun levenshteinRecursive(str1: String, str2: String, m: Int, n: Int): Int {

        if(m == 0) {
            return n
        }

        if(n == 0) {
            return m
        }

        if(str1[m - 1] == str2[n - 1]) {
            return levenshteinRecursive(str1, str2, m - 1, n - 1)
        }

        return 1 + minOf(
            levenshteinRecursive(str1, str2, m, n - 1),
            minOf(
                levenshteinRecursive(str1, str2, m - 1, n),

                levenshteinRecursive(str1, str2, m - 1, n - 1)
            )
        )
    }
}
