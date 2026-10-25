package com.example.capucareapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.capucareapp.data.model.Insumo
import com.example.capucareapp.data.model.Movimiento

@Database(entities = [Insumo::class, Movimiento::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun insumoDao(): InsumoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cappucare_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}