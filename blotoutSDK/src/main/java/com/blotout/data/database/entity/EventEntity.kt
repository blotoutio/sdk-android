package com.blotout.data.database.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(

        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        @ColumnInfo(name = "eventData")
        val eventData: String
){
    constructor(event: String) : this(0, event)
}