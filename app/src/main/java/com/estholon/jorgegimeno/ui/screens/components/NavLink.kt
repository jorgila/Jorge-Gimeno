package com.estholon.jorgegimeno.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NavLink(
    string : String,
    alignment: Alignment,
    navigation: () -> Unit,
    modifier: Modifier
){
    Box(modifier = modifier, contentAlignment = alignment){

        TextButton(
            onClick = {
                navigation()
            }
        ) {
            Text(
                text = string,
                fontWeight = FontWeight.ExtraBold
            )
        }

    }
}