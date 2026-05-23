package com.aj.tempshot.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aj.tempshot.data.local.dao.ImageMemoDao
import com.aj.tempshot.data.local.entity.ImageMemoEntity

@Database(
    entities = [ImageMemoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TempShotDatabase : RoomDatabase() {
    abstract fun imageMemoDao(): ImageMemoDao

    companion object {
        @Volatile
        private var instance: TempShotDatabase? = null

        fun getInstance(context: Context): TempShotDatabase {
            return instance ?: synchronized(this) {
                instance ?: createDatabase(context).also { instance = it }
            }
        }

        private fun createDatabase(context: Context): TempShotDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                TempShotDatabase::class.java,
                "tempshot_db"
            ).build()
        }
    }
}
