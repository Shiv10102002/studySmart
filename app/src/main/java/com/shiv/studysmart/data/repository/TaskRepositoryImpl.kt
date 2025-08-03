package com.shiv.studysmart.data.repository

import androidx.room.Insert
import com.shiv.studysmart.data.local.TaskDao
import com.shiv.studysmart.domain.model.Task
import com.shiv.studysmart.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            .map{tasks->tasks.filter { it.isComplete.not() }}
            .map { tasks ->sortTasks(tasks) }
    }

    override fun getCompletedTaskForSubject(subjectId: Int): Flow<List<Task>> {
        return taskDao.getTaskForSubject(subjectId)
            .map{tasks->tasks.filter { it.isComplete }}
            .map { tasks ->sortTasks(tasks) }
    }

    override fun getAllUpcomingTask(): Flow<List<Task>> {
        return taskDao.getAllTasks().map {
            task->task.filter {
                it.isComplete.not()
            }
        }.map { tasks ->sortTasks(tasks) }
    }

    private fun sortTasks(tasks:List<Task>):List<Task>{
        return tasks.sortedWith(compareBy<Task>{it.dueDate}.thenByDescending { it.priority })
    }

}