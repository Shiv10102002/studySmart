package com.shiv.studysmart.presentation.task

import androidx.lifecycle.ViewModel
import com.shiv.studysmart.domain.repository.TaskRepository
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) :ViewModel() {

}