package com.blipblipcode.distribuidoraayl.domain.useCase.worker

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface ISyncUpRepository {
    fun cancelWork()
    fun workInfoFlow(): Flow<List<WorkInfo>>
    fun runPeriodicWork(intervalInMinutes: Long = 10L)
}