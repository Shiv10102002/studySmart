package com.shiv.studysmart.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId:Int,
    val taskSubjectId:Int,
    val title:String,
    val description:String,
    val dueDate:Long,
    val priority:Int,
    val relatedSubject:String,
    val isComplete:Boolean
)
