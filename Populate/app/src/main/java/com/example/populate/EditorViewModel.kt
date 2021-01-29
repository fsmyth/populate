package com.example.populate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.populate.data.CharDatabase
import com.example.populate.data.CharacterEntity
import com.example.populate.data.NEW_CHAR_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(app: Application) : AndroidViewModel(app) {
    private val database = CharDatabase.getInstance(app)
    val currentChar = MutableLiveData<CharacterEntity>()

    //With the ID sent by the user clicking on the desired item, post the value of the corresponding item in the database.
    fun getCharById(charId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val char =
                    if(charId != NEW_CHAR_ID){
                        //Ensure that the function keeps searching through each item until it finds the correct ID.
                        database?.charDao()?.getCharById(charId)
                    } else {
                        CharacterEntity()
                    }
                currentChar.postValue(char)
            }
        }
    }

    //Update the database entry to reflect the new changes.
    fun updateChar() {
    currentChar.value?.let {
        //Trim each of the strings to remove leading and trailing spaces.
        it.name = it.name.trim()
        it.desc = it.desc.trim()
        it.notes = it.notes.trim()
        //If the fields are empty, return.
        if(it.id == NEW_CHAR_ID && it.name.isEmpty() && it.desc.isEmpty() && it.notes.isEmpty()) {
            return
        }
        //Connect to the database. if the fields are empty, run the delete function to remove the empty item.
        //Otherwise, insert the item with the new values into the database.
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if(it.name.isEmpty() && it.desc.isEmpty() && it.notes.isEmpty()) {
                    database?.charDao()?.deleteChar(it)
                } else {
                    database?.charDao()?.insertChar(it)
                }
            }
        }
    }

    }
}