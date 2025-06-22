package com.blipblipcode.distribuidoraayl.data.repositiry.reportSale

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.blipblipcode.distribuidoraayl.core.local.entities.RemoteKey
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.ReportSaleEntity
import com.blipblipcode.distribuidoraayl.core.local.room.DataBaseApp
import com.blipblipcode.distribuidoraayl.data.dto.reportSale.ReportSaleDto
import com.blipblipcode.library.DateTime
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPagingApi::class)
class ReportSaleRemoteMediator @AssistedInject constructor(
    @Assisted private val dateTime: DateTime,
    private val firestore: FirebaseFirestore,
    private val database: DataBaseApp,
) : RemoteMediator<Int, ReportSaleEntity>() {
    companion object {
        private const val LAST_SYNC_KEY = "last_sync_report_sales"
    }
    private val pageSize = 50
    private val dateNow = DateTime.now()


        override suspend fun initialize(): InitializeAction {
            val lastSync = database.remoteKeyDao().getByKey(LAST_SYNC_KEY)

            // Verificar si ha pasado un día desde la última sincronización
            if (dateNow.timeSpan(dateTime).days > 1) {
                database.remoteKeyDao().insert(RemoteKey(0, LAST_SYNC_KEY, dateNow.toMillis(),dateNow.format("yyyy-MM-dd")))
                return InitializeAction.LAUNCH_INITIAL_REFRESH
            }

            // Verificar si hay nuevos datos en Firestore
            return try {
                val lastLocalSale = database.reportSaleDao().getLastSynchronizedSale()
                val query = firestore
                    .collection(ReportSaleRepository.REPORT_SALES)
                    .document(dateTime.year.toString())
                    .collection(dateTime.month.toString().padStart(2, '0'))
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                val lastRemoteSale = query.documents.firstOrNull()?.toObject<ReportSaleDto>()

                if (lastLocalSale == null || (lastRemoteSale != null && lastRemoteSale.uid != lastLocalSale.uid)) {
                    InitializeAction.LAUNCH_INITIAL_REFRESH
                } else {
                    InitializeAction.SKIP_INITIAL_REFRESH
                }
            } catch (e: Exception) {
                InitializeAction.SKIP_INITIAL_REFRESH
            }
        }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ReportSaleEntity>
    ): MediatorResult {
        return try {
            val lastSync = database.remoteKeyDao().getByKey(LAST_SYNC_KEY)
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    null
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    lastSync?.offset
                }
            }

            val collectionRef = firestore
                .collection(ReportSaleRepository.REPORT_SALES)
                .document(dateTime.year.toString())
                .collection(dateTime.month.toString().padStart(2, '0'))

            var query = collectionRef
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(pageSize.toLong())

            // Si hay paginación, usar startAfter con el valor adecuado (ajustar según tu lógica)
            // if (page != null) {
            //     query = query.startAfter(page)
            // }

            val documents = query.get().await()
            val sales = documents.mapNotNull { it.toObject<ReportSaleDto>().mapToEntity() }

            database.withTransaction {
                sales.forEach {
                    database.withTransaction {
                        database.reportSaleDao().apply {
                            val id = insert(it.sale)
                            val items = it.items.map { item -> item.copy(id = id) }
                            insert(items)
                        }
                        database.remoteKeyDao().getByKey(LAST_SYNC_KEY)?.apply {
                            database.remoteKeyDao().insert(copy(date = dateNow.toMillis(),
                                offset = page?.toString() ?: dateNow.format("yyyy-MM-dd")))
                        }
                    }
                }

            }

            MediatorResult.Success(endOfPaginationReached = sales.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(dateTime: DateTime): ReportSaleRemoteMediator
    }
}