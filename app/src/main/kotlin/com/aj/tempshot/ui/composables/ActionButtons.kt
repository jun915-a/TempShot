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
            OutlinedButton(
                onClick = onTemporary,
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(8.dp)),
            ) {
                Text("一時保存 ⏳")
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

@Composable
fun ExpiryDurationButtons(
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "削除までの期間を選択",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onSelect(1) },
                modifier = Modifier.weight(1f)
            ) {
                Text("1日")
            }
            Button(
                onClick = { onSelect(3) },
                modifier = Modifier.weight(1f)
            ) {
                Text("3日")
            }
            Button(
                onClick = { onSelect(7) },
                modifier = Modifier.weight(1f)
            ) {
                Text("1週間")
            }
        }
    }
}
