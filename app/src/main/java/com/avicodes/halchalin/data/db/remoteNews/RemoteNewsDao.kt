package com.avicodes.halchalin.data.db.remoteNews

import androidx.room.*
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: NewsRemote)

    @Query("SELECT * FROM remote_news_table")
    fun getAllArticles(): Flow<List<NewsRemote>>

    @Delete
    suspend fun deleteArticle(news: NewsRemote)

    @Query("DELETE FROM remote_news_table")
    suspend fun deleteAll()
}