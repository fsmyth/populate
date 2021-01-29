package com.example.populate.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CharacterDao {

//    Insert a character into the DB with the given class instance, if there is already an item with this ID replace it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChar(character: CharacterEntity)

//    Insert all of the given characters into the database, ignoring any that are already in the DB.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(characters: List<CharacterEntity>)

//    Retrieve all items from the DB in a list.
    @Query("SELECT * FROM characters ORDER BY name ASC")
    fun getAll(): LiveData<List<CharacterEntity>>

//    Find the item with the corresponding ID.
    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharById(id: Int): CharacterEntity?

//    Count the number of items.
    @Query("SELECT COUNT(*) FROM characters")
    fun getCount(): Int

//    Delete all items in the list of selected items for deletion.
    @Delete
    fun deleteChars(selectedChars: List<CharacterEntity>): Int

//    Delete all characters in the DB.
    @Query("DELETE FROM characters")
    fun deleteAllChars(): Int

//    Delete the given item from the DB.
    @Delete
    fun deleteChar(character: CharacterEntity)
}