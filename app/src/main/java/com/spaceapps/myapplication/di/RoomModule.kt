package com.spaceapps.myapplication.di

import android.content.Context
import androidx.room.Room
import com.spaceapps.myapplication.local.SpaceAppsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "space_apps_db"

    @Provides
    @Singleton
    fun provideRoom(
        @ApplicationContext context: Context
    ): SpaceAppsDatabase {
        val executor = Dispatchers.IO.asExecutor()
        return Room
            .databaseBuilder(context, SpaceAppsDatabase::class.java, DATABASE_NAME)
            .setTransactionExecutor(executor)
            .setQueryExecutor(executor)
            .build()
    }

    @Provides
    @Singleton
    fun providePostsDao(db: SpaceAppsDatabase) = db.getPostsDao()

    @Provides
    @Singleton
    fun provideCommentsDao(db: SpaceAppsDatabase) = db.getCommentsDao()
}