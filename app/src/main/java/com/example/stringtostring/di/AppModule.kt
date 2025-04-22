package com.example.stringtostring.di

import android.content.Context
import com.example.stringtostring.database.AppDatabase
import com.example.stringtostring.database.ThreadsDao
import com.example.stringtostring.repository.ThreadsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideThreadsDao(database: AppDatabase): ThreadsDao {
        return database.threadsDao()
    }

    @Provides
    @Singleton
    fun provideThreadsRepository(threadsDao: ThreadsDao): ThreadsRepository {
        return ThreadsRepository(threadsDao)
    }
}