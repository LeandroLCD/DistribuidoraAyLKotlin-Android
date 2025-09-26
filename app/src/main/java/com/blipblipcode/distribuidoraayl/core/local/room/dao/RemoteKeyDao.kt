package com.blipblipcode.distribuidoraayl.core.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blipblipcode.distribuidoraayl.core.local.entities.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Query("DELETE FROM remote_keys WHERE `key` = :key")
    suspend fun deleteByKey(key: String)

    @Query("SELECT * FROM remote_keys WHERE `key` = :key")
    suspend fun getByKey(key: String): RemoteKey?
}