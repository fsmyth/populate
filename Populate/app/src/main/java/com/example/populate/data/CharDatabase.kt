package com.example.populate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CharacterEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class CharDatabase: RoomDatabase() {

    abstract fun charDao(): CharacterDao?

    companion object {
        //Create an instance to track if the DB connection has been instantiated
        private var INSTANCE: CharDatabase? = null

        fun getInstance(context: Context): CharDatabase? {
            //Only instantiate the connection if it is not already instantiated
            if(INSTANCE == null){
                synchronized(CharDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CharDatabase::class.java,
                        "populate.db"
                    ).build()
                }
            }

            return INSTANCE
        }
    }
}