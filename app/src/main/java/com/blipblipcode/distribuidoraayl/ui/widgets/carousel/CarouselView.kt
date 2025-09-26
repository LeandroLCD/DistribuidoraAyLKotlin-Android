package com.blipblipcode.distribuidoraayl.ui.widgets.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun <T> CarouselView(
    ini:Int = 0,
    label: String,
    items:List<T>,
    content: @Composable (T) -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = key(items) {
        rememberPagerState(
            ini
        ) {
            items.size
        }
    }
    Column {
        Text(label, fontSize = 12.sp)
        HorizontalPager(state = pagerState,
            pageSpacing = 16.dp
        ) {page->
            val item = items[page]
            content(item)
        }

        if(items.size > 1){
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(items.size) {
                    val color = if (pagerState.currentPage == it) MaterialTheme.colorScheme.primary else Color.Gray
                    Box(modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .size(if (pagerState.currentPage == it) 13.dp else 10.dp)
                        .background(color)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        }) {

                    }
                }
            }
        }
    }
}