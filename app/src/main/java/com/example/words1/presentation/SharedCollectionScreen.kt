package com.example.words1.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.words1.R
import com.example.words1.data.WordCollection
import com.example.words1.data.WordsEvents


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedCollectionScreen(
    viewModel: WordsViewModel,
    navController: NavController,
    onEvent: (WordsEvents) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchSharedCollections()
    }

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
                    text = "Shared collections",
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.p2)
                )
            }
        },




        containerColor = colorResource(id = R.color.p1)

    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(colorResource(id = R.color.p1)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            this.items(state.collections) { collection ->
                SharedCollectionItem(
                    viewModel = viewModel,
                    collection = collection,
                    onEvent = onEvent,
                    navController = navController
                )
            }

        }
    }
}

@Composable
fun SharedCollectionItem(
    viewModel: WordsViewModel,
    collection: WordCollection,
    onEvent: (WordsEvents) -> Unit,
    navController: NavController
) {

    var expanded by remember { mutableStateOf(false) }
    var menuPosition by remember { mutableStateOf(DpOffset.Zero) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.p4))
            .padding(12.dp)
            .pointerInput(true) {
                detectTapGestures(
                    onPress = {
                        expanded = true
                        menuPosition = DpOffset(it.x.toDp(), it.y.toDp())
                    }
                )
            },


        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = collection.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.p2),
            modifier = Modifier.padding(30.dp)
        )

        DropdownMenu(
            expanded = expanded,
            offset = menuPosition.copy(
                y = menuPosition.y - 80.dp
            ),
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(color = colorResource(id = R.color.p2))
                .zIndex(1f)
                .width(with(LocalDensity.current) { 80.dp }

                )

        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Enter",
                        color = colorResource(id = R.color.p3)
                    )
                },
                onClick = {
                    navController.navigate("WordScreen/${collection.key}")
                    expanded = false
                })
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Add",
                        color = colorResource(id = R.color.p3)
                    )
                },
                onClick = {
                    onEvent(WordsEvents.AddSharedCollection(collection))
                    expanded = false
                })
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Delete",
                        color = colorResource(id = R.color.p3)
                    )
                },
                onClick = {
                    onEvent(WordsEvents.DeleteCollection(collection))
                    viewModel.fetchSharedCollections()
                    expanded = false
                })



        }
    }
}

