package com.avicodes.halchalin.data.db.localNews


import androidx.room.*
import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: NewsLocal)

    @Query("SELECT * FROM local_news_table")
    suspend fun getAllArticles(): List<NewsLocal>

    @Delete
    suspend fun deleteArticle(news: NewsLocal)

    @Query("DELETE FROM local_news_table")
    suspend fun deleteAll()
}