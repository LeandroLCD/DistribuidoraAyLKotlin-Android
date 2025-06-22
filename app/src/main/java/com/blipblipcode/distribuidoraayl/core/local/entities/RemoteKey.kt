package com.blipblipcode.distribuidoraayl.core.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys",
    indices = [Index(value = ["key"], unique = true)])
data class RemoteKey (
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val key:String,
    val date: Long,
    val offset: String

)