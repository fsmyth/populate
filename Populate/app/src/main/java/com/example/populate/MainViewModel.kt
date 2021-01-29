package com.example.populate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.populate.data.CharDatabase
import com.example.populate.data.CharacterEntity
import com.example.populate.data.SampleDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = CharDatabase.getInstance(app)
    val charsList = database?.charDao()?.getAll()

    //Add sample items to the database
    fun addSampleChars() {
        //Begin running a co-routine to run "getChars() in the SDP"
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sampleChars = SampleDataProvider.getChars()
                //Insert the retrieved sample items into the database via the DAO.
                database?.charDao()?.insertAll(sampleChars)
            }
        }
    }

    //Delete the selected characters from the database
    fun deleteChars(selectedChars: List<CharacterEntity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //Run the DAO's deleteChars function to remove them from the database
                database?.charDao()?.deleteChars(selectedChars)
            }
        }
    }

    //Deletes all characters from the DB
    fun deleteAllChars() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //Tells the DAO to run a function to delete all characters in the DB.
                database?.charDao()?.deleteAllChars()
            }
        }
    }

}