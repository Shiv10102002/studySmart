package com.shiv.studysmart.presentation.session

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.shiv.studysmart.presentation.components.DeleteDialog
import com.shiv.studysmart.presentation.components.SubjectListBottomSheet
import com.shiv.studysmart.presentation.components.studySessionList
import com.shiv.studysmart.sessions
import kotlinx.coroutines.launch

@Destination
@Composable
fun SessionScreenRoute(
    navigator: DestinationsNavigator
){
    SessionScreen(onBackButtonClick = {
           navigator.navigateUp()
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(
    onBackButtonClick : ()->Unit
){
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope= rememberCoroutineScope()

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = listOf(),
        onSubjectClicked = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if(!sheetState.isVisible) {
                    isBottomSheetOpen = false
                }
            }
        },
        onDismissRequest = {
            isBottomSheetOpen = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "Delete Task?",
        bodyText = "Are you sure ,you want to delete this task?"+
                "This action can not be undone",
        onDismissRequest = {
            isDeleteDialogOpen = false
        },
        onConfirmButtonClick = {
            isDeleteDialogOpen =false
        }
    )

    Scaffold (
        topBar = {
            SessionScreenTopBar(onBackClicked = onBackButtonClick)
        }
    ){
        paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            item{
                TimerSection(modifier = Modifier.fillMaxWidth().aspectRatio(1f))
            }
            item{
                RelatedToSubjectSection(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    relatedToSubject = "English",
                    selectSubjectButtonClick = {
                        isBottomSheetOpen = true
                    }
                )
            }
            item{
                ButtonSection(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    startButtonClicked = {},
                    cancelButtonClicked = {},
                    finishButtonClicked = {}
                )
            }

            studySessionList(
                sectionTitle = "STUDY SESSION HISTORY",
                emptyListText = "You don't have any recent study sessions.\n" + "Start a study session to begin recording your progress",
                sessions = sessions,
                onDeleteIconClick = {
                    isDeleteDialogOpen = true
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreenTopBar(
    onBackClicked:()->Unit
){
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClicked
            ) {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Navigate to Back Screen"
              )
            }
        },
        title = {
            Text(text = "Study Session", style = MaterialTheme.typography.headlineSmall)
        },
    )
}

@Composable
private fun TimerSection(
    modifier: Modifier
){
    Box (modifier = modifier,
        contentAlignment = Alignment.Center
        ){
        Box(
            modifier = Modifier.size(250.dp).border(5.dp,MaterialTheme.colorScheme.surfaceVariant,
                CircleShape)
        )

        Text(
            text = "00:05:32",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
        )
    }
}


@Composable
private fun RelatedToSubjectSection(
    modifier: Modifier,
    relatedToSubject:String,
    selectSubjectButtonClick:()->Unit
){
   Column(
       modifier = modifier
   ) {
       Text(
           text = "Related to subject",
           style = MaterialTheme.typography.bodySmall
       )
       Spacer(modifier = Modifier.height(10.dp))
       Row(
           modifier = Modifier.fillMaxWidth(),
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically
       ){
           Text(
               text = "English",
               style = MaterialTheme.typography.bodyLarge
           )
           IconButton(onClick = {
                 selectSubjectButtonClick()
           }) {
               Icon(
                   imageVector = Icons.Default.ArrowDropDown,
                   contentDescription = "Select Subject"
               )
           }
       }
   }
}

@Composable
private fun ButtonSection(
    modifier: Modifier,
    startButtonClicked:()->Unit,
    cancelButtonClicked:()->Unit,
    finishButtonClicked:()->Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Button(
            onClick = cancelButtonClicked
        ){
            Text(
                modifier = Modifier.padding(horizontal = 10.dp,vertical = 5.dp),
                text = "Cancel",
            )
        }
        Button(
            onClick = startButtonClicked
        ){
            Text(
                modifier = Modifier.padding(horizontal = 10.dp,vertical = 5.dp),
                text = "Start",
            )
        }
        Button(
            onClick = finishButtonClicked
        ){
            Text(
                modifier = Modifier.padding(horizontal = 10.dp,vertical = 5.dp),
                text = "Finish",
            )
        }
    }
}