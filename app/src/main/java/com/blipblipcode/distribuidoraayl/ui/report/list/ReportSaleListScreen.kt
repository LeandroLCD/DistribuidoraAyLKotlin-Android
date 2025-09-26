package com.blipblipcode.distribuidoraayl.ui.report.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.reportSale.ReportSale
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.ReportScreen
import com.blipblipcode.distribuidoraayl.ui.utils.toCurrency
import com.blipblipcode.distribuidoraayl.ui.widgets.input.SearchBar
import com.blipblipcode.distribuidoraayl.ui.widgets.input.stringException
import com.blipblipcode.library.model.FormatType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportSaleListScreen(
    viewModel: ReportSaleListViewModel = hiltViewModel(),
    drawerOpen: () -> Unit,
    onNavigateTo: (ReportScreen) -> Unit
) {
    val lazyState = rememberLazyListState()
    var indexBar by remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.reports))
                },
                navigationIcon = {
                    IconButton(onClick = { drawerOpen() }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {

            BottomAppBar(contentPadding = PaddingValues(0.dp)) {
                TabRow(
                    selectedTabIndex = indexBar,
                    divider = {
                        HorizontalDivider()
                    }
                ) {

                    Button(onClick = {
                        indexBar = 0
                    }, shape = RectangleShape) {
                        Text(text = stringResource(R.string.sales))
                    }

                    Button(onClick = {
                        indexBar = 1
                    }, shape = RectangleShape) {
                        Text(text = stringResource(R.string.products))
                    }
                }
            }
        }) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(8.dp)) {
            val reportSales = viewModel.reportSales.collectAsLazyPagingItems()
            val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle("")

            SearchBar(
                value = searchQuery,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                label = stringResource(R.string.search)
            ) {
                viewModel.onSearchQueryChanged(it)
            }


            LazyColumn(
                state = lazyState,
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(reportSales.itemCount) { index ->

                    when (val state = reportSales.loadState.refresh) {
                        is LoadState.Error -> {
                            Surface(
                                Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.errorContainer,
                                shadowElevation = 8.dp
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.Error,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = stringException(state.error),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        LoadState.Loading -> {
                            LinearProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }

                        is LoadState.NotLoading -> {}
                    }



                    reportSales[index]?.let {
                        ReportSaleItemHolder(reportSale = it) {

                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ReportSaleItemHolder(
    reportSale: ReportSale,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(onClick, modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
        Row(Modifier.fillMaxWidth().padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Column(Modifier.weight(0.7f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val color = when (reportSale.dteType) {
                        DteType.ORDER_NOTE -> Color.Gray
                        DteType.INVOICE -> Color.Green
                        DteType.CREDIT_NOTE -> Color.Red
                    }
                    Box(modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color))
                    Text(text = reportSale.number.toString(), fontWeight = FontWeight.SemiBold)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
                    Text(text = reportSale.date.format(FormatType.Short('-')), fontWeight = FontWeight.SemiBold)
                }
                Text(text = reportSale.client.name,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            }
            Column(Modifier.weight(0.4f), verticalArrangement = Arrangement.Center) {
                Text(text = reportSale.totals.total.toCurrency(), fontWeight = FontWeight.SemiBold)
            }

        }
    }
}
