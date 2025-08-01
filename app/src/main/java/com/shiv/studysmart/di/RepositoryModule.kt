package com.shiv.studysmart.di

import com.shiv.studysmart.data.repository.SessionRepositoryImpl
import com.shiv.studysmart.data.repository.SubjectRepositoryImpl
import com.shiv.studysmart.data.repository.TaskRepositoryImpl
import com.shiv.studysmart.domain.repository.SessionRepository
import com.shiv.studysmart.domain.repository.SubjectRepository
import com.shiv.studysmart.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSubjectRepository(
        impl: SubjectRepositoryImpl
    ):SubjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ):TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(
        impl: SessionRepositoryImpl
    ): SessionRepository
}