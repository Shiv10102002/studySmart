package com.shiv.studysmart.presentation.subject

import androidx.lifecycle.ViewModel
import com.shiv.studysmart.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
):ViewModel() {

}