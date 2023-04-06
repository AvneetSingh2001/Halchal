package com.avicodes.halchalin.data.db.featured

import androidx.room.Database
import androidx.room.RoomDatabase
import com.avicodes.halchalin.data.models.Featured

@Database(
    entities = [Featured::class],
    version = 1,
    exportSchema = false
)
abstract class FeaturedDb : RoomDatabase() {
    abstract fun featuredDao(): FeaturedDao
}