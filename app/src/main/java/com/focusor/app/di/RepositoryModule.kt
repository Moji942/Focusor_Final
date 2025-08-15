package com.focusor.app.di

import com.focusor.app.data.repository.EducationRepositoryImpl
import com.focusor.app.domain.repository.EducationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindEducationRepository(
        educationRepositoryImpl: EducationRepositoryImpl
    ): EducationRepository
}