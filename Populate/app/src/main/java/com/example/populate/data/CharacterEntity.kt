package com.example.populate.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

//This class is parcelable, allowing it to be parsed to another component
@Parcelize
@Entity(tableName = "characters")
data class CharacterEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: Date,
    var name: String,
    var desc: String,
    var notes: String
) : Parcelable {

//    Constructors
    constructor(): this(NEW_CHAR_ID, Date(), "", "", "")
    constructor(date: Date, name: String, desc: String, notes: String) : this(NEW_CHAR_ID, date, name, desc, notes)
}