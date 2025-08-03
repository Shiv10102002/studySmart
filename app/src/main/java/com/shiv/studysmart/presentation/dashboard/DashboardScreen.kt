package com.shiv.studysmart.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.shiv.studysmart.R
import com.shiv.studysmart.domain.model.Session
import com.shiv.studysmart.domain.model.Subject
import com.shiv.studysmart.domain.model.Task
import com.shiv.studysmart.presentation.components.AddSubjectDialog
import com.shiv.studysmart.presentation.components.CountCard
import com.shiv.studysmart.presentation.components.DeleteDialog
import com.shiv.studysmart.presentation.components.SubjectCard
import com.shiv.studysmart.presentation.components.studySessionList
import com.shiv.studysmart.presentation.components.tasksList
import com.shiv.studysmart.presentation.destinations.SessionScreenRouteDestination
import com.shiv.studysmart.presentation.destinations.SubjectScreenRouteDestination
import com.shiv.studysmart.presentation.destinations.TaskScreenRouteDestination
import com.shiv.studysmart.presentation.subject.SubjectScreenNavArgs
import com.shiv.studysmart.presentation.task.TaskScreenNavArgs
import com.shiv.studysmart.sessions
import com.shiv.studysmart.subjects
import com.shiv.studysmart.tasks
import com.shiv.studysmart.util.SnackBarEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreenRoute(
    navigator: DestinationsNavigator
){

    val viewModel:DashboardViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val task by viewModel.tasks.collectAsStateWithLifecycle()
    val recentSession by viewModel.recentSessions.collectAsStateWithLifecycle()

    DashboardScreen(
        snackBarEvent = viewModel.snackBarEventFlow,
        tasks = task,
        recentSessions = recentSession,
        onEvent = viewModel::onEvent,
        state = state,
        onSubjectCardClick = {subjectId->
            subjectId?.let{
                val navArg = SubjectScreenNavArgs(subjectId = subjectId)
                navigator.navigate(SubjectScreenRouteDestination(navArgs = navArg))
            }
        },
        onTaskCardClick = {taskId->
            taskId?.let{
                val navArg = TaskScreenNavArgs(taskId = taskId,subjectId = null)
                navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))
            }

        },
        onStartSessionButtonClick = {
                navigator.navigate(SessionScreenRouteDestination())
        }
    )
}

@Composable
private fun DashboardScreen(
    snackBarEvent: SharedFlow<SnackBarEvent>,
    tasks:List<Task>,
    recentSessions:List<Session>,
    onEvent:(DashboardEvent)->Unit,
    state:DashboardState,
    onSubjectCardClick:(Int?)->Unit,
    onTaskCardClick:(Int?)->Unit,
    onStartSessionButtonClick:()->Unit
){

    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        snackBarEvent.collectLatest { event ->
            when (event) {
                is SnackBarEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackBarEvent.NavigateUp -> {}
            }
        }
    }
    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        onDismissRequest = {isAddSubjectDialogOpen = false},
        onConfirmButtonClick = {
            onEvent(DashboardEvent.SaveSubject)
            isAddSubjectDialogOpen = false
        },
        selectColors = state.subjectCardColors,
        onColorChange = {
            onEvent(DashboardEvent.OnSubjectCardColorChange(it))
        },
        subjectName = state.subjectName,
        goalHours = state.goalStudyHours,
        onSubjectNameChange = {
            onEvent(DashboardEvent.OnSubjectNameChange(it))
        },
        onGoalHoursChange = {
            onEvent(DashboardEvent.OnGoalStudyHoursChange(it))
        }
    )
    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure , you want to delete this session? your studied hours will be reduced by this session time. this action can not be undone",
        onDismissRequest = {isDeleteSessionDialogOpen = false},
        onConfirmButtonClick = {
            onEvent(DashboardEvent.DeleteSession)
            isDeleteSessionDialogOpen = false}
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar ={ DashboardScreenTopBar() }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            item{
                CountCardSection(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    studiedHours = state.goalStudyHours,
                    goalStudyHours = state.totalGoalStudyHours.toString(),
                    subjectCount = state.totalSubjectCount
                )
            }
            item{
                SubjectCardsSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = state.subjects,
                    onAddIconClick = {isAddSubjectDialogOpen = true},
                    onSubjectCardClick = onSubjectCardClick

                )
            }
            item{
                Button(
                    onClick = onStartSessionButtonClick,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                    Text(text = "Start Study Session")
                }
            }
            tasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyListTask = "You don't have any upcoming tasks.\n " + "Click the + button in subject screen to add new task",
                tasks = tasks,
                onCheckBoxClicked = {
                    onEvent(DashboardEvent.OnTaskIsCompleteChange(it))
                },
                onTaskCardClicked = onTaskCardClick
            )
            item{
                Spacer(modifier = Modifier.height(12.dp))
            }
            studySessionList(
                sectionTitle = "RECENT STUDY SESSIONS",
                emptyListText = "You don't have any recent sessions.\n " + "Start a study session to begin recording your progress",
                sessions = recentSessions,
                onDeleteIconClick = {
                    onEvent(DashboardEvent.OnDeleteSessionButtonClick(it))
                    isDeleteSessionDialogOpen = true}
            )

        }

    }
}

@Composable
private fun CountCardSection(
    modifier: Modifier = Modifier,
    subjectCount:Int,
    studiedHours:String,
    goalStudyHours:String

){
    Row(modifier = modifier ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Subject Count",
            count = "$subjectCount"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalStudyHours
        )
    }
}

@Composable
private fun SubjectCardsSection(
    onSubjectCardClick:(Int?)->Unit,
    onAddIconClick:()->Unit,
    modifier: Modifier,
    subjectList:List<Subject>,
    emptyListText:String = "you don't have any subjects.\n Click the + button t o add new subjects"
){
    Column(modifier = modifier){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(
                onClick = onAddIconClick
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }
        if(subjectList.isEmpty()){
            Image(
            modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.img_books),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList){subject->
                SubjectCard(
                    subjectName = subject.name,
                    onClick = { onSubjectCardClick(subject.subjectId)},
                    gradientColor = subject.colors.map{
                        Color(it)
                    }
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar(){
    CenterAlignedTopAppBar(
        title = {
            Text(text = "StudySmart",
            style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

