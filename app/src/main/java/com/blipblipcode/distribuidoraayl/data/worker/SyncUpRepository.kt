package com.blipblipcode.distribuidoraayl.data.worker

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.blipblipcode.distribuidoraayl.domain.useCase.worker.ISyncUpRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SyncUpRepository @Inject constructor(
    private val workManager: WorkManager
) : ISyncUpRepository {


    override fun cancelWork() {
        workManager.cancelUniqueWork(SyncUpWorker.TAG)
    }

    override fun workInfoFlow(): Flow<List<WorkInfo>> {
        return workManager.getWorkInfosForUniqueWorkFlow(SyncUpWorker.TAG)
    }


    override fun runPeriodicWork(intervalInMinutes: Long) {
        workManager.enqueueUniquePeriodicWork(
            SyncUpWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            SyncUpWorker.periodicWorkRequest(intervalInMinutes)
        )
    }

}