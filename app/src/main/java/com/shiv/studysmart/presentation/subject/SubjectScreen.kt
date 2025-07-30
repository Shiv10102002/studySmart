package com.shiv.studysmart.presentation.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shiv.studysmart.domain.model.Subject
import com.shiv.studysmart.presentation.components.AddSubjectDialog
import com.shiv.studysmart.presentation.components.CountCard
import com.shiv.studysmart.presentation.components.DeleteDialog
import com.shiv.studysmart.presentation.components.studySessionList
import com.shiv.studysmart.presentation.components.tasksList
import com.shiv.studysmart.sessions
import com.shiv.studysmart.tasks

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isEditSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Subject.subjectCardColors.random()) }
    var subjectName by remember { mutableStateOf("") }
    var goalHours by remember { mutableStateOf("") }
    AddSubjectDialog(
        isOpen = isEditSubjectDialogOpen,
        onDismissRequest = {isEditSubjectDialogOpen = false},
        onConfirmButtonClick = {
            isEditSubjectDialogOpen = false
        },
        selectColors = selectedColor,
        onColorChange = {selectedColor = it},
        subjectName = subjectName,
        goalHours = goalHours,
        onSubjectNameChange = {subjectName = it},
        onGoalHoursChange = {goalHours = it}
    )
    DeleteDialog(
        isOpen = isDeleteSubjectDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure , you want to delete this session? your studied hours will be reduced by this session time. this action can not be undone",
        onDismissRequest = {isDeleteSubjectDialogOpen = false},
        onConfirmButtonClick = {isDeleteSubjectDialogOpen = false}
    )
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SubjectScreenTopBar(
                onBackClick = {},
                onEditClick = {
                    isEditSubjectDialogOpen = true
                },
                onDeleteClick = {
                    isDeleteSubjectDialogOpen = true
                },
                title = "English",
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {},
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                },
                text = {
                    Text(text = "Add Task")
                },
                expanded = isFABExpanded
            )
        }
    ){ paddingValues ->
        LazyColumn (modifier =  Modifier.fillMaxWidth().padding(paddingValues)){
            item{
                SubjectOverViewSection(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    studiedHours = "10",
                    goalHours = "15",
                    progress = 0.75f
                )
            }
            tasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyListTask = "You don't have any upcoming tasks.\n " + "Click the + button to add new task",
                tasks = tasks,
                onCheckBoxClicked = {},
                onTaskCardClicked = {}
            )
            item{
                Spacer(modifier = Modifier.height(20.dp))
            }
            tasksList(
                sectionTitle = "COMPLETED TASKS",
                emptyListTask = "You don't have any completed tasks.\n " + "Click the check box on completion of task.",
                tasks = tasks,
                onCheckBoxClicked = {},
                onTaskCardClicked = {}
            )
            item{
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionList(
                sectionTitle = "RECENT STUDY SESSIONS",
                emptyListText = "You don't have any recent study sessions.\n " + "Start a study session to begin recording your progress",
                sessions = sessions,
                onDeleteIconClick = {
                    isDeleteSubjectDialogOpen = true
                }
            )

        }

    }
}

@Composable
private fun SubjectOverViewSection(
    modifier: Modifier = Modifier,
    studiedHours:String,
    goalHours:String,
    progress:Float
){
    val percentageProgress = remember(progress) {
        (progress * 100).toInt().coerceIn(0,100) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
          CountCard(
              modifier = Modifier.weight(1f),
              headingText = "Goal Study Hours",
              count = goalHours
          )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Study Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,

            )
            Text(
                text = "$percentageProgress%",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreenTopBar(
    title:String,
    onBackClick:()->Unit,
    onEditClick:()->Unit,
    onDeleteClick:()->Unit,
    scrollBehavior: TopAppBarScrollBehavior
){
    LargeTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Text(
                text =title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Subject"
                )
            }
            IconButton(
                onClick = onEditClick
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Subject"
                )
            }
        }
    )
}


@Preview
@Composable
fun SubjectScreenPreview(){
    SubjectScreen()
}