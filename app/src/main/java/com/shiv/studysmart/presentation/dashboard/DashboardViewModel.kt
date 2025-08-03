package com.shiv.studysmart.presentation.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shiv.studysmart.domain.model.Session
import com.shiv.studysmart.domain.model.Subject
import com.shiv.studysmart.domain.model.Task
import com.shiv.studysmart.domain.repository.SessionRepository
import com.shiv.studysmart.domain.repository.SubjectRepository
import com.shiv.studysmart.domain.repository.TaskRepository
import com.shiv.studysmart.util.SnackBarEvent
import com.shiv.studysmart.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository
):ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalGoalHours(),
        subjectRepository.getAllSubjects(),
        sessionRepository.getTotalSessionsDuration()

    ){state,subjectCount,goalHours,subjects,totalSessionsDuration->
        state.copy(
            totalSubjectCount = subjectCount,
            totalGoalStudyHours = goalHours,
            subjects = subjects,
            totalStudiedHours = totalSessionsDuration.toHours()
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DashboardState()
    )

    val tasks: StateFlow<List<Task>> = taskRepository.getAllUpcomingTask()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val recentSessions:StateFlow<List<Session>> = sessionRepository.getRecentFiveSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarEventFlow  = _snackBarEventFlow.asSharedFlow()


    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(subjectName = event.name)
                }
            }

            is DashboardEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }

            is DashboardEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(subjectCardColors = event.colors)
                }
            }

            is DashboardEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }

            DashboardEvent.SaveSubject -> saveSubject()
            DashboardEvent.DeleteSession -> deleteSession()
            is DashboardEvent.OnTaskIsCompleteChange -> {
                updateTask(event.task)
            }
        }
    }

    private fun updateTask(task:Task){
        viewModelScope.launch {
            try{
                taskRepository.upsertTask(
                    task = task.copy(isComplete = !task.isComplete)
                )
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(message = "Saved in Completed task.")
                )
            }catch (e:Exception){
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Couldn't update task ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }
        }
    }


    private fun saveSubject(){
        viewModelScope.launch {
            try{
                subjectRepository.upsertSubject(
                    subject = Subject(
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull()?:1f,
                        colors = state.value.subjectCardColors.map {
                            it.toArgb()
                        }
                    )
                )
                _state.update{
                    it.copy(
                        subjectName = "",
                        goalStudyHours = "",
                        subjectCardColors = Subject.subjectCardColors.random()
                    )
                }
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Subject saved successfully"
                    )
                )
            }catch (e:Exception){
              _snackBarEventFlow.emit(
                  SnackBarEvent.ShowSnackBar(
                      message ="Couldn't save subject ${e.message}"?:"",
                      SnackbarDuration.Long
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
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "Session deleted successfully")
                    )
                }
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't delete session. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }


}