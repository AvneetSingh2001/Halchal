package com.avicodes.halchalin.data.db.remoteNews

import androidx.paging.PagingSource
import androidx.room.*
import com.avicodes.halchalin.data.models.NewsRemote

@Dao
interface InternationalNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: List<NewsRemote>)

    @Query("SELECT * FROM remote_news_table")
    suspend fun getAllArticles(): PagingSource<String, NewsRemote>

    @Query("DELETE FROM remote_news_table")
    suspend fun deleteAll()
}