package com.blipblipcode.distribuidoraayl.data.repositiry.reportSale

import android.content.Context
import android.util.Log
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ReportSaleDao
import com.blipblipcode.distribuidoraayl.data.mapper.toDto
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseFireStoreRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.IReportSaleRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReportSaleRepository @Inject constructor(
    dispatcher: CoroutineDispatcher,
    context: Context,
    override val fireStore: FirebaseFirestore,
    private val reportSaleDao: ReportSaleDao
): BaseFireStoreRepository(dispatcher, context, fireStore), IReportSaleRepository{
    companion object{
        const val REPORT_SALES = "report_sales"
    }

    override suspend fun syncReportSales(): ResultType<Unit>{
        return makeCallNetwork {
            val sales = reportSaleDao.getUnSynchronizedSales()
            Log.d("SyncUpWorker", "syncReportSales: $sales")
            if(sales.isEmpty()){
                return@makeCallNetwork
            }
            val dtoList = sales.map { it.toDto() }
            val groupedByYearMonth = dtoList.groupBy { dto ->
                val (year, month) = dto.date.split("-").let { it[0] to it[1] }
                year to month
            }
            fireStore.runBatch { batch->
                groupedByYearMonth.forEach { (yearMonth, dtos) ->
                    val (year, month) = yearMonth
                    val collectionRef = fireStore.collection("${REPORT_SALES}/$year/$month")
                    dtos.forEach { dto ->
                        val docRef = collectionRef.document()
                        batch.set(docRef, dto)
                    }
                }

            }.addOnSuccessListener {
                Log.d("SyncUpWorker", "syncReportSales: addOnCanceledListener")
                repositoryScope.launch {
                    reportSaleDao.updateSaleSync(sales.map { it.sale.copy(isSynchronized = true)})
                }
            }.addOnFailureListener {
                Log.d("SyncUpWorker", "syncReportSales: $it")
            }
        }
    }

}