package com.shiv.studysmart.presentation.subject

import SubjectState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shiv.studysmart.domain.model.Subject
import com.shiv.studysmart.domain.model.Task
import com.shiv.studysmart.domain.repository.SessionRepository
import com.shiv.studysmart.domain.repository.SubjectRepository
import com.shiv.studysmart.domain.repository.TaskRepository
import com.shiv.studysmart.presentation.navArgs
import com.shiv.studysmart.util.SnackBarEvent
import com.shiv.studysmart.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository:TaskRepository,
    private val sessionRepository:SessionRepository,
    savedStateHandle: SavedStateHandle

):ViewModel() {



    private val navArgs:SubjectScreenNavArgs = savedStateHandle.navArgs()
    private val _state = MutableStateFlow(SubjectState())

    val state = combine(
        _state,
        taskRepository.getUpcomingTaskForSubject(navArgs.subjectId),
        taskRepository.getCompletedTaskForSubject(navArgs.subjectId),
        sessionRepository.getRecentTenSessionsForSubject(navArgs.subjectId),
        sessionRepository.getTotalSessionsDurationBySubjectId(navArgs.subjectId)
    ){
        state,upcomingTask,completedTask,recentSession,totalSessionDuration->
        state.copy(
            upcomingTasks = upcomingTask,
            completedTask = completedTask,
            recentSession = recentSession,
            studiedHours = totalSessionDuration.toHours()

        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SubjectState()
    )
    private val _snackbarEventFlow = MutableSharedFlow<SnackBarEvent>()
    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()

    init {
        fetchSubject()
    }


    fun onEvent(event: SubjectEvent) {
        when (event) {
            is SubjectEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(subjectCardColors = event.color)
                }
            }

            is SubjectEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(subjectName = event.name)
                }
            }

            is SubjectEvent.OnGoalStudyHourChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }

            is SubjectEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is SubjectEvent.OnTaskIsCompleteChange -> {
                updateTask(event.task)
            }

            SubjectEvent.UpdateSubject -> updateSubject()
            SubjectEvent.DeleteSubject -> deleteSubject()
            SubjectEvent.DeleteSession -> deleteSession()

            SubjectEvent.UpdateProgress -> {
                val goalStudyHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f
                _state.update {
                    it.copy(
                        progress = (state.value.studiedHours / goalStudyHours).coerceIn(0f, 1f)
                    )
                }
            }

        }
    }

    private fun updateSubject() {
        viewModelScope.launch {
            try {
                subjectRepository.upsertSubject(
                    subject = Subject(
                        subjectId = state.value.currentSubjectId,
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.subjectCardColors.map { it.toArgb() }
                    )
                )
                _snackbarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(message = "Subject updated successfully.")
                )
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't update subject. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }
    private fun fetchSubject() {
        viewModelScope.launch {
            subjectRepository
                .getSubjectById(navArgs.subjectId)?.let { subject ->
                    _state.update {
                        it.copy(
                            subjectName = subject.name,
                            goalStudyHours = subject.goalHours.toString(),
                            subjectCardColors = subject.colors.map { colors -> Color(colors) },
                            currentSubjectId = subject.subjectId
                        )
                    }
                }
        }
    }

    private fun deleteSubject() {
        viewModelScope.launch {
            try {
                val currentSubjectId = state.value.currentSubjectId
                if (currentSubjectId != null) {
                    withContext(Dispatchers.IO) {
                        subjectRepository.deleteSubject(subjectId = currentSubjectId)
                    }
                    _snackbarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "Subject deleted successfully")
                    )
                    _snackbarEventFlow.emit(SnackBarEvent.NavigateUp)
                } else {
                    _snackbarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "No Subject to delete")
                    )
                }
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't delete subject. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.upsertTask(
                    task = task.copy(isComplete = !task.isComplete)
                )
                if (task.isComplete) {
                    _snackbarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "Saved in upcoming tasks.")
                    )
                } else {
                    _snackbarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "Saved in completed tasks.")
                    )
                }
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't update task. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            try {
                state.value.session?.let {
                    sessionRepository.deleteSession(it)
                    _snackbarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "Session deleted successfully")
                    )
                }
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't delete session. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }


}