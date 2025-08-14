package com.focusor.studyplanner.data.dao

import androidx.room.*
import com.focusor.studyplanner.data.entity.AIProviderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AIProviderDao {
    
    @Insert
    suspend fun insertProvider(provider: AIProviderEntity): Long
    
    @Update
    suspend fun updateProvider(provider: AIProviderEntity)
    
    @Delete
    suspend fun deleteProvider(provider: AIProviderEntity)
    
    @Query("SELECT * FROM ai_providers ORDER BY name ASC")
    fun getAllProviders(): Flow<List<AIProviderEntity>>
    
    @Query("SELECT * FROM ai_providers WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveProviders(): Flow<List<AIProviderEntity>>
    
    @Query("SELECT * FROM ai_providers WHERE id = :providerId")
    suspend fun getProviderById(providerId: Long): AIProviderEntity?
    
    @Query("UPDATE ai_providers SET totalTokensUsed = totalTokensUsed + :tokens, totalCost = totalCost + :cost WHERE id = :providerId")
    suspend fun updateUsage(providerId: Long, tokens: Long, cost: Double)
    
    @Query("UPDATE ai_providers SET isActive = :isActive WHERE id = :providerId")
    suspend fun updateActiveStatus(providerId: Long, isActive: Boolean)
    
    @Query("SELECT SUM(totalTokensUsed) FROM ai_providers")
    suspend fun getTotalTokensUsed(): Long?
    
    @Query("SELECT SUM(totalCost) FROM ai_providers")
    suspend fun getTotalCost(): Double?
}