package com.avicodes.halchalin.data.db.localNews

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.avicodes.halchalin.data.db.featured.FeaturedDao
import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.RoomTypeConverters

@TypeConverters(value = [RoomTypeConverters::class])
@Database(
    entities = [News::class],
    version = 1,
    exportSchema = false
)
abstract class LocalNewsDb : RoomDatabase() {
    abstract fun localNewsDao(): LocalNewsDao
}