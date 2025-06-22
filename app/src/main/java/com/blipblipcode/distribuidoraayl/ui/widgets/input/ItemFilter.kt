package com.blipblipcode.distribuidoraayl.ui.widgets.input

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blipblipcode.distribuidoraayl.domain.models.filters.DataFilter
import com.blipblipcode.distribuidoraayl.domain.models.filters.TypeFilter

@Composable
fun ItemFilter(dataFilter: DataFilter, onDelete: (DataFilter) -> Unit) {
    val transition = updateTransition(dataFilter.type, label = "updateTransition")
    val color = transition.animateColor(label = "color") {
        when (it) {
            TypeFilter.Boolean.Equals -> {
                Color.Green
            }

            TypeFilter.Date.Between, TypeFilter.Date.Equals, TypeFilter.Date.GreaterThan, TypeFilter.Date.LessThan -> {
                Color.Blue
            }

            TypeFilter.Number.Equals, TypeFilter.Number.GreaterThan, TypeFilter.Number.LessThan, TypeFilter.Number.Contains -> {
                Color.Yellow
            }

            TypeFilter.Text.Equals, TypeFilter.Text.Contains -> {
                Color.Red
            }
        }
    }
    val contentColor = transition.animateColor(label = "color") {
        when (it) {
            TypeFilter.Boolean.Equals -> {
                Color.White
            }

            TypeFilter.Date.Between, TypeFilter.Date.Equals, TypeFilter.Date.GreaterThan, TypeFilter.Date.LessThan -> {
                Color.Black
            }

            TypeFilter.Number.Equals, TypeFilter.Number.GreaterThan, TypeFilter.Number.LessThan, TypeFilter.Number.Contains -> {
                Color.Black
            }

            TypeFilter.Text.Equals, TypeFilter.Text.Contains -> {
                Color.White
            }
        }
    }
    Surface(
        color = color.value,
        contentColor = contentColor.value,
        onClick = {
            onDelete.invoke(dataFilter)
        },
        tonalElevation = 8.dp,
        shape = CircleShape
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(dataFilter.toString(), fontSize = 12.sp)
        }
    }
}