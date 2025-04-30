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

/**
 * Модуль внедрения зависимостей приложения, отвечающий за предоставление
 * синглтон-экземпляров компонентов, связанных с доступом к базе данных и репозиторием ниток.
 *
 * Используется аннотация [@Module] для регистрации класса как Hilt-модуля.
 * Аннотация [@InstallIn(SingletonComponent::class)] указывает, что зависимости
 * живут на протяжении всего жизненного цикла приложения.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Предоставление экземпляра базы данных [AppDatabase].
     *
     * Использование аннотации [@Singleton] обеспечивает создание единственного экземпляра базы данных.
     *
     * @param context Контекст приложения, автоматически внедряемый Hilt через [@ApplicationContext].
     * @return Экземпляр базы данных.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    /**
     * Предоставление DAO-интерфейса [ThreadsDao] для доступа к таблицам базы данных.
     *
     * Извлечение DAO осуществляется из экземпляра [AppDatabase], предоставленного выше.
     *
     * @param database Экземпляр базы данных, получаемый через Hilt.
     * @return DAO-интерфейс для работы с нитками.
     */
    @Provides
    fun provideThreadsDao(database: AppDatabase): ThreadsDao {
        return database.threadsDao()
    }

    /**
     * Предоставление экземпляра репозитория [ThreadsRepository], инкапсулирующего
     * логику доступа к данным.
     *
     * Использование аннотации [@Singleton] обеспечивает единообразный доступ к данным
     * на протяжении всего приложения.
     *
     * @param threadsDao DAO-интерфейс, необходимый репозиторию для работы с базой данных.
     * @return Экземпляр репозитория ниток.
     */
    @Provides
    @Singleton
    fun provideThreadsRepository(threadsDao: ThreadsDao): ThreadsRepository {
        return ThreadsRepository(threadsDao)
    }
}
