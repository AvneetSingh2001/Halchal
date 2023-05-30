package com.avicodes.halchalin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.models.NewsLocal
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.utils.RoomTypeConverters

@TypeConverters(value = [RoomTypeConverters::class])
@Database(
    entities = [NewsRemote::class, NewsLocal::class],
    version = 1,
    exportSchema = false
)
abstract class HalchalDatabase : RoomDatabase() {
//    abstract fun nationalNewsDao(): NationalNewsDao
//    abstract fun localNewsDao(): LocalNewsDao
//    abstract fun featuredDao(): FeaturedDao
//    abstract fun internationalNewsDao(): InternationalNewsDao
//
//    abstract fun getRepoDao(): RemoteKeysDao
}