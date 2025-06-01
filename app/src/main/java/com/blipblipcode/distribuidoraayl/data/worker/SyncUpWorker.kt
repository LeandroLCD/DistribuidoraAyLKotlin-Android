package com.blipblipcode.distribuidoraayl.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.IReportSaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncUpWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: IReportSaleRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val TAG = "report_sale_sync_up"

        fun periodicWorkRequest(intervalInMinutes: Long = 10L): PeriodicWorkRequest {
            val constrains = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            return PeriodicWorkRequestBuilder<SyncUpWorker>(
                intervalInMinutes,
                TimeUnit.MINUTES
            ).setConstraints(constrains)
                .build()
        }

    }

    override suspend fun doWork(): Result {

        val result = repository.syncReportSales()

        return when (result) {
            is ResultType.Success -> {
                val outputData = Data.Builder()
                    .putBoolean("success", true)
                Result.success(outputData.build())
            }

            is ResultType.Error -> {
                val outputData = Data.Builder()
                    .putBoolean("success", false)
                Result.failure(outputData.build())
            }
        }
    }
}