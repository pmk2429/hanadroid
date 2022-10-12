package com.example.hanadroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hanadroid.model.RickMortyCharacter


@Database(
    entities = [RickMortyCharacter::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocationConverter::class)
abstract class RickMortyDatabase : RoomDatabase() {

    abstract fun charactersDao(): RickMortyCharacterDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: RickMortyDatabase? = null

        fun getInstance(context: Context): RickMortyDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RickMortyDatabase::class.java, "RickAndMorty.db"
            ).build()
    }

}