package com.avicodes.halchalin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.avicodes.halchalin.data.db.featured.FeaturedDao
import com.avicodes.halchalin.data.db.localNews.LocalNewsDao
import com.avicodes.halchalin.data.db.remoteNews.RemoteNewsDao
import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.utils.RoomTypeConverters

@TypeConverters(value = [RoomTypeConverters::class])
@Database(
    entities = [NewsRemote::class, News::class, Featured::class],
    version = 1,
    exportSchema = false
)
abstract class HalchalDatabase : RoomDatabase() {
    abstract fun remoteNewsDao(): RemoteNewsDao
    abstract fun localNewsDao(): LocalNewsDao
    abstract fun featuredDao(): FeaturedDao
}