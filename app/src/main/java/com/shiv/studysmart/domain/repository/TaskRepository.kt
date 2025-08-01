package com.shiv.studysmart.domain.repository

import com.shiv.studysmart.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun upsertTask(task: Task)

    suspend fun deleteTask(taskId:Int)

    suspend fun getTaskById(taskId:Int):Task?

    fun getAllTasks():Flow<List<Task>>

    fun getUpcomingTaskForSubject(subjectId:Int):Flow<List<Task>>

    fun getCompletedTaskForSubject(subjectId: Int):Flow<List<Task>>

    fun getAllUpcomingTask():Flow<List<Task>>

}