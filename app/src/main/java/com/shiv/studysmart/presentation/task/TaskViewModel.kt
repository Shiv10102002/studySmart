package com.shiv.studysmart.presentation.task

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shiv.studysmart.domain.model.Task
import com.shiv.studysmart.domain.repository.SubjectRepository
import com.shiv.studysmart.domain.repository.TaskRepository
import com.shiv.studysmart.presentation.navArgs
import com.shiv.studysmart.util.Priority
import com.shiv.studysmart.util.SnackBarEvent
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
import java.time.Instant
import javax.inject.Inject
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) :ViewModel() {
    private val navArgs:TaskScreenNavArgs = savedStateHandle.navArgs()
     private val _state = MutableStateFlow(TaskState())
    val state = combine(
        _state,
        subjectRepository.getAllSubjects()

    ){state,subjects->
        state.copy(subjects = subjects)

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = TaskState()
    )
     private val _snackbarEventFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarEventFlow = _snackbarEventFlow.asSharedFlow()
    init {
        fetchTask()
        fetchSubject()
    }

    fun onEvent(event:TaskEvent){
        when(event){
            TaskEvent.DeleteTask -> {
                deleteTask()
            }
            is TaskEvent.OnDateChange -> {
                _state.update {
                    it.copy(dueDate = event.date)
                }
            }
            is TaskEvent.OnDescriptionChange -> {
                _state.update {
                    it.copy(description = event.description)
                }

            }
            TaskEvent.OnIsCompleteChange -> {
                _state.update {
                    it.copy(isTaskComplete = !_state.value.isTaskComplete)
                }
            }
            is TaskEvent.OnPriorityChange -> {
                _state.update {
                    it.copy(priority = event.priority)
                }
            }
            is TaskEvent.OnRelatedSubjectSelect -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }
            is TaskEvent.OnTitleChange -> {
                _state.update {
                    it.copy(title = event.title)
                }
            }
            TaskEvent.SaveTask -> saveTask()
        }


    }
    private fun saveTask(){
       viewModelScope.launch {
           val state = _state.value
           if(state.subjectId ==null || state.relatedToSubject == null){
               _snackbarEventFlow.emit(
                   SnackBarEvent.ShowSnackBar(
                        "Please select subject related to the task",
                       SnackbarDuration.Long
                   )
               )
               return@launch
           }
          try {
              taskRepository.upsertTask(
                  task = Task(
                      title = state.title,
                      description = state.description,
                      dueDate = state.dueDate?: Instant.now().toEpochMilli(),
                      priority = state.priority.value,
                      isComplete = state.isTaskComplete,
                      relatedSubject = state.relatedToSubject,
                      taskSubjectId = state.subjectId,
                      taskId = state.currentTaskId
                  )
              )
              _snackbarEventFlow.emit(
                  SnackBarEvent.ShowSnackBar(
                      message = "Task saved successfully"
                  )
              )

              _snackbarEventFlow.emit(
                  SnackBarEvent.NavigateUp
              )


          }catch (e:Exception){
              _snackbarEventFlow.emit(
                  SnackBarEvent.ShowSnackBar(
                      message = "Couldn't save subject ${e.message}",
                      duration = SnackbarDuration.Long
                  )
              )
          }
       }
    }

    private fun fetchTask(){
        viewModelScope.launch {
               navArgs.taskId?.let { id ->
                   taskRepository.getTaskById(id)?.let { task ->
                       _state.update {
                           it.copy(
                               title = task.title,
                               description = task.description,
                               dueDate = task.dueDate,
                               isTaskComplete = task.isComplete,
                               relatedToSubject = task.relatedSubject,
                               priority = Priority.fromInt(task.priority),
                               subjectId = task.taskSubjectId,
                               currentTaskId = task.taskId
                           )

                       }
                   }
               }
        }
    }

    private fun fetchSubject(){
        viewModelScope.launch {
            navArgs.subjectId?.let{
                id->
                subjectRepository.getSubjectById(id)?.let{
                    subject->
                    _state.update {
                        it.copy(
                            subjectId = subject.subjectId,
                            relatedToSubject = subject.name
                        )
                    }
                }

            }
        }
    }

    private fun deleteTask(){
        viewModelScope.launch {
            try {
                val currentTaskId = state.value.currentTaskId
                if(currentTaskId != null){
                    withContext(Dispatchers.IO){
                        taskRepository.deleteTask(taskId = currentTaskId)
                    }
                    _snackbarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(
                            message = "Task deleted successfully"
                        )
                    )
                    _snackbarEventFlow.emit(
                        SnackBarEvent.NavigateUp
                    )
                }
            }catch (e:Exception){
                _snackbarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't delete task ${e.message}"
                    )
                )
            }
        }
    }
}