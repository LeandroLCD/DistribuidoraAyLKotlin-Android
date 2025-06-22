package com.blipblipcode.distribuidoraayl.data.repositiry.reportSale

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.sqlite.db.SimpleSQLiteQuery
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ReportSaleDao
import com.blipblipcode.distribuidoraayl.data.mapper.toDto
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseFireStoreRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.filters.DataFilter
import com.blipblipcode.distribuidoraayl.domain.models.filters.TypeFilter
import com.blipblipcode.distribuidoraayl.domain.models.reportSale.ReportSale
import com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.IReportSaleRepository
import com.blipblipcode.library.DateTime
import com.blipblipcode.library.model.DateTimeRange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReportSaleRepository @Inject constructor(
    dispatcher: CoroutineDispatcher,
    context: Context,
    override val fireStore: FirebaseFirestore,
    private val reportSaleDao: ReportSaleDao,
    private val mediatorFactory: ReportSaleRemoteMediator.Factory
): BaseFireStoreRepository(dispatcher, context, fireStore), IReportSaleRepository{

    companion object{
        const val REPORT_SALES = "report_sales"
    }

    override suspend fun syncReportSales(): ResultType<Unit>{
        return makeCallNetwork {
            val sales = reportSaleDao.getUnSynchronizedSales()
            if(sales.isEmpty()){
                return@makeCallNetwork
            }
            val dtoList = sales.map { it.toDto() }
            val groupedByYearMonth = dtoList.groupBy { dto ->
                val date = DateTime.fromMillis(dto.date)
                date.year to date.month
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
                repositoryScope.launch {
                    reportSaleDao.update(sales.map { it.sale.copy(isSynchronized = true)})
                }
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getReportSales(
        rangue: DateTimeRange,
        dataFilter: List<DataFilter>
    ): Flow<PagingData<ReportSale>> {
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                prefetchDistance = 10,
                enablePlaceholders = false
            ),
            remoteMediator = mediatorFactory.create(rangue.start),
            pagingSourceFactory = {
                val query = buildSaleQuery(rangue, dataFilter)
                Log.d("ReportSaleRepository",query.sql)
                reportSaleDao.getReportSalePaging(query)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.mapToDomain() }
        }
    }

    private fun buildSaleQuery(date: DateTimeRange, filters: List<DataFilter>): SimpleSQLiteQuery {
        val baseQuery = StringBuilder("""
        SELECT sale_data.*, 
        client_receiver.*
        FROM sale_data 
        LEFT JOIN client_receiver ON sale_data.clientRut = client_receiver.rut 
        WHERE sale_data.date >= '${date.start.toMillis()}' 
        AND sale_data.date <= '${date.end.toMillis()}'
    """.trimIndent())

        filters.forEach { filter ->
            val field = when (filter.field) {
                // Campos de cliente
                "commune", "name", "address" -> "client_receiver.${filter.field}"
                // Campos de venta
                else -> "sale_data.${filter.field}"
            }

            val condition = when(filter.type) {
                is TypeFilter.Text.Equals -> "$field = '${filter.value}'"
                is TypeFilter.Text.Contains -> "$field LIKE '%${filter.value}%'"
                is TypeFilter.Number.Equals -> "$field = ${filter.value}"
                is TypeFilter.Number.GreaterThan -> "$field > ${filter.value}"
                is TypeFilter.Number.LessThan -> "$field < ${filter.value}"
                is TypeFilter.Date.Equals -> "date($field) = date('${filter.value}')"
                is TypeFilter.Date.GreaterThan -> "date($field) > date('${filter.value}')"
                is TypeFilter.Date.LessThan -> "date($field) < date('${filter.value}')"
                is TypeFilter.Date.Between -> {
                    val dates = filter.value.split(",")
                    if (dates.size == 2) {
                        "date($field) BETWEEN date('${dates[0]}') AND date('${dates[1]}')"
                    } else null
                }
                is TypeFilter.Boolean.Equals -> "$field = ${filter.value}"
                TypeFilter.Number.Contains -> "$field LIKE '%${filter.value}%'"
            }

            condition?.let {
                baseQuery.append(" AND $it")
            }
        }

        baseQuery.append(" ORDER BY sale_data.date DESC")

        return SimpleSQLiteQuery(baseQuery.toString())
    }

}