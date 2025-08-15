package com.focusor.studyplanner.di

import com.focusor.studyplanner.data.repository.EducationRepositoryImpl
import com.focusor.studyplanner.data.repository.FinanceRepositoryImpl
import com.focusor.studyplanner.domain.repository.EducationRepository
import com.focusor.studyplanner.domain.repository.FinanceRepository
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
    
    @Binds
    @Singleton
    abstract fun bindFinanceRepository(
        financeRepositoryImpl: FinanceRepositoryImpl
    ): FinanceRepository
}