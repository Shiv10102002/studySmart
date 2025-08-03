package com.shiv.studysmart.presentation.subject

import androidx.compose.ui.graphics.Color
import com.shiv.studysmart.domain.model.Session
import com.shiv.studysmart.domain.model.Subject
import com.shiv.studysmart.domain.model.Task

sealed class SubjectEvent {
    data object UpdateSubject : SubjectEvent()

    data object DeleteSubject : SubjectEvent()

    data object DeleteSession : SubjectEvent()

    data object UpdateProgress: SubjectEvent()

    data class OnTaskIsCompleteChange(val task: Task):SubjectEvent()

    data class OnSubjectCardColorChange(val color:List<Color>):SubjectEvent()

    data class OnSubjectNameChange(val name:String):SubjectEvent()

    data class OnGoalStudyHourChange(val hours:String):SubjectEvent()

    data class OnDeleteSessionButtonClick(val session: Session):SubjectEvent()

}