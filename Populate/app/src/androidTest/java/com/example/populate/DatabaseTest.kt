package com.example.populate

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.populate.data.CharDatabase
import com.example.populate.data.CharacterDao
import com.example.populate.data.CharacterEntity
import com.example.populate.data.SampleDataProvider
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var dao: CharacterDao
    private lateinit var database: CharDatabase

    @Before
    fun createDb() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, CharDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.charDao()!!
    }

    @Test
    fun createCharacters() {
        dao.insertAll(SampleDataProvider.getChars())
        val count = dao.getCount()
        assertEquals(count, SampleDataProvider.getChars().size)
    }

    @Test
    fun insertChar() {
        val character = CharacterEntity()
        character.name = "John Doe"
        character.desc = "Insert Description"
        character.notes = "Insert Notes"
        dao.insertChar(character)
        val savedCharacter = dao.getCharById(1)
        assertEquals(savedCharacter?.id ?: 0, 1)
    }

    @After
    fun closeDb() {
        database.close()
    }

}