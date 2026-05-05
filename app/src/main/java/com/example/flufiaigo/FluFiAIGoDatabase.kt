package com.example.flufiaigo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [IngresoModel::class, GastoModel::class, NominaModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FluFiAIGoDatabase : RoomDatabase() {

    abstract val ingresoDao: IngresoDao
    abstract val gastoDao: GastoDao
    abstract val nominaDao: NominaDao

    companion object {
        @Volatile
        private var INSTANCE: FluFiAIGoDatabase? = null

        fun getInstance(context: Context): FluFiAIGoDatabase {
            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    FluFiAIGoDatabase::class.java,
                    "flufi_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
            }
            return instance
        }
    }
}
