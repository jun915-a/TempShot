package com.aj.tempshot.di

import android.content.Context
import androidx.room.Room
import com.aj.tempshot.data.local.ImageProvider
import com.aj.tempshot.data.local.dao.ImageMemoDao
import com.aj.tempshot.data.local.db.TempShotDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideTempShotDatabase(
        @ApplicationContext context: Context
    ): TempShotDatabase {
        return Room.databaseBuilder(
            context,
            TempShotDatabase::class.java,
            "tempshot_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideImageMemoDao(database: TempShotDatabase): ImageMemoDao {
        return database.imageMemoDao()
    }

    @Singleton
    @Provides
    fun provideImageProvider(
        @ApplicationContext context: Context
    ): ImageProvider {
        return ImageProvider(context)
    }
}
