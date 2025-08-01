package com.shiv.studysmart.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.shiv.studysmart.domain.repository.SubjectRepository
import com.shiv.studysmart.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
):ViewModel() {

}