package com.aj.tempshot.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aj.tempshot.ui.theme.DeleteRed
import com.aj.tempshot.ui.theme.SuccessGreen
import com.aj.tempshot.ui.theme.WarningOrange

@Composable
fun ActionButtons(
    onOrganize: () -> Unit,
    onTemporary: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onOrganize,
            modifier = Modifier
                .fillMaxWidth()
                .background(SuccessGreen, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
        ) {
            Text("整理済みにする →")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onTemporary,
                modifier = Modifier
                    .weight(1f)
                    .background(WarningOrange, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = WarningOrange)
            ) {
                Text("3日後に削除 ⏳", color = Color.White)
            }

            Button(
                onClick = onDelete,
                modifier = Modifier
                    .weight(1f)
                    .background(DeleteRed, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = DeleteRed)
            ) {
                Text("削除 ↓", color = Color.White)
            }
        }
    }
}
