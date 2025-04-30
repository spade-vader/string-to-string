package com.example.stringtostring.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ShelfThreadEntity
import com.example.stringtostring.model.ThreadEntity

/**
 * Основной класс базы данных приложения, реализующий RoomDatabase.
 *
 * Используется для доступа к локальному хранилищу данных, определённому через сущности Room.
 * В данной базе хранятся:
 * - Производители ниток [Manufacturer]
 * - Нитки с их цветовым кодом [ThreadEntity]
 * - Пользовательская "Полка" с сохранёнными нитками [ShelfThreadEntity]
 *
 * Версия базы данных: 1
 * Внимание: [exportSchema = false] отключает экспорт схемы базы — уместно для pet-проекта, но
 * для продакшена лучше включить и хранить схему в git.
 */
@Database(
    entities = [
        Manufacturer::class,
        ThreadEntity::class,
        ShelfThreadEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Предоставляет доступ к DAO, содержащему методы работы с нитками.
     */
    abstract fun threadsDao(): ThreadsDao

    companion object {
        // volatile используется, чтобы изменения экземпляра были видны во всех потоках
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Получает единственный экземпляр базы данных.
         *
         * Применяется паттерн Singleton с синхронизацией для потокобезопасного lazy-инстанциирования.
         * Используется метод [createFromAsset] для предварительно загруженной базы данных, что
         * удобно при поставке предзаполненной БД с приложением.
         *
         * @param context Контекст приложения
         * @return Экземпляр [AppDatabase]
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "threads.db" // Имя локального файла базы данных
                )
                    .createFromAsset("threads.db") // Импорт готовой базы из ассетов
                    .fallbackToDestructiveMigration(false) // Отключение авто-сброса при несовпадении схемы
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
