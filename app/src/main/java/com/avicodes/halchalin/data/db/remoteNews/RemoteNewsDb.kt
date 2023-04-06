package com.avicodes.halchalin.data.db.remoteNews

import androidx.room.Database
import androidx.room.RoomDatabase
import com.avicodes.halchalin.data.db.featured.FeaturedDao
import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.models.NewsRemote

@Database(
    entities = [NewsRemote::class],
    version = 1,
    exportSchema = false
)
abstract class RemoteNewsDb : RoomDatabase() {
    abstract fun remoteNewsDao(): RemoteNewsDao
}