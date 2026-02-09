package com.ivor.openanime.presentation.player

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    tmdbId: Int,
    season: Int,
    episode: Int,
    onBackClick: () -> Unit
) {
    val embedUrl = "https://vidking.me/embed?type=series&tmdb=$tmdbId&season=$season&episode=$episode"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.5f)
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
            // innerPadding is ignored for full-screen immersive video, but useful for controls overlay if native
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        
                        // Set clients to handle navigation and full-screen if needed
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()
                        
                        loadUrl(embedUrl)
                    }
                },
                update = { webView ->
                    // Avoid reloading on recomposition unless URL changed
                    if (webView.url != embedUrl) {
                        webView.loadUrl(embedUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
