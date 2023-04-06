package com.avicodes.halchalin.data.db.localNews


import androidx.room.*
import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.models.News
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: News)

    @Query("SELECT * FROM local_news_table")
    fun getAllArticles(): Flow<List<News>>

    @Delete
    suspend fun deleteArticle(news: News)
}