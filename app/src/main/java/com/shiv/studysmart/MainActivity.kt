package com.shiv.studysmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.ramcosta.composedestinations.DestinationsNavHost
import com.shiv.studysmart.domain.model.Session
import com.shiv.studysmart.domain.model.Subject
import com.shiv.studysmart.domain.model.Task
import com.shiv.studysmart.presentation.NavGraphs
import com.shiv.studysmart.presentation.theme.StudySmartTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudySmartTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

val subjects = listOf(
    Subject(name = "English", goalHours = 10f, colors = Subject.subjectCardColors[0].map { it.toArgb() }, subjectId = 0),
    Subject(name = "Physics", goalHours = 10f, colors = Subject.subjectCardColors[1].map { it.toArgb() }, subjectId = 1),
    Subject(name = "Maths", goalHours = 10f, colors = Subject.subjectCardColors[2].map { it.toArgb() }, subjectId = 2),
    Subject(name="Chemistry", goalHours = 10f, colors = Subject.subjectCardColors[3].map { it.toArgb() }, subjectId = 3),
    Subject(name = "Fine Arts", goalHours = 10f, colors = Subject.subjectCardColors[4].map { it.toArgb() }, subjectId = 4)
)

val tasks = listOf(
    Task(title = "Prepare notes",
        description = "",
        priority = 1,
        dueDate = 0L,
        relatedSubject = "English",
        isComplete = false,
        taskId = 0,
        taskSubjectId = 0
    ),
    Task(title = "Do Homeworks",
        description = "",
        priority = 0,
        dueDate = 0L,
        relatedSubject = "Math",
        isComplete = true,
        taskId = 1,
        taskSubjectId = 2
    ),
    Task(title = "Prepare notes",
        description = "",
        priority = 1,
        dueDate = 0L,
        relatedSubject = "Chemistry",
        isComplete = false,
        taskId = 2,
        taskSubjectId = 3
    ),
    Task(title = "Go Coaching",
        description = "",
        priority = 3,
        dueDate = 0L,
        relatedSubject = "English",
        isComplete = false,
        taskId = 3,
        taskSubjectId = 0
    ),
)

val sessions = listOf(
    Session(
        sessionSubjectId = 0,
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionId = 1,
        sessionSubjectId = 0
    ),
    Session(
        relatedToSubject = "Physics",
        date = 0L,
        duration = 2,
        sessionId = 0,
        sessionSubjectId = 1
    ),
    Session(
        relatedToSubject = "Maths",
        date = 0L,
        duration = 2,
        sessionId = 0,
        sessionSubjectId = 2
    ),


    )

