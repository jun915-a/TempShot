package com.aj.tempshot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.aj.tempshot.ui.screen.MainScreen
import com.aj.tempshot.ui.theme.TempShotTheme
import com.aj.tempshot.viewmodel.MainViewModel
import com.aj.tempshot.worker.CleanupWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scheduleCleanupWorker()

        setContent {
            TempShotTheme {
                MainScreenWrapper()
            }
        }
    }

    private fun scheduleCleanupWorker() {
        val cleanupRequest = PeriodicWorkRequestBuilder<CleanupWorker>(
            1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "image_cleanup",
            ExistingPeriodicWorkPolicy.KEEP,
            cleanupRequest
        )
    }
}

@Composable
fun MainScreenWrapper() {
    val viewModel: MainViewModel = hiltViewModel()
    MainScreen(viewModel)
}
