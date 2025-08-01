package com.shiv.studysmart.data.repository

import androidx.room.Insert
import com.shiv.studysmart.data.local.TaskDao
import com.shiv.studysmart.domain.model.Task
import com.shiv.studysmart.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
): TaskRepository {
    override suspend fun upsertTask(task: Task) {
       taskDao.upsertTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        return taskDao.getTaskById(taskId)
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    override fun getUpcomingTaskForSubject(subjectId: Int): Flow<List<Task>> {
        return taskDao.getTaskForSubject(subjectId)
    }

    override fun getCompletedTaskForSubject(subjectId: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getAllUpcomingTask(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }
}