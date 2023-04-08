package com.avicodes.halchalin.data.db.featured

import androidx.room.*
import com.avicodes.halchalin.data.models.Featured
import kotlinx.coroutines.flow.Flow

@Dao
interface FeaturedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(featured: Featured)

    @Query("SELECT * FROM featured_table")
    fun getAllArticles(): Flow<List<Featured>>

    @Delete
    suspend fun deleteArticle(featured: Featured)

    @Query("DELETE FROM featured_table")
    suspend fun deleteAll()
}