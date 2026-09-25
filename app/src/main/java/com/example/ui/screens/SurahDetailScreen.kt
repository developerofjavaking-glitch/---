package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Surah
import com.example.data.util.BengaliNumberUtil
import com.example.ui.QuranViewModel
import com.example.ui.VersesUiState
import com.example.ui.components.AyahItemCard
import com.example.ui.components.FontScaleDialog
import com.example.ui.components.FullSurahPlayerCard
import com.example.ui.components.JumpToAyahDialog
import com.example.ui.components.ReciterSelectionDialog
import com.example.ui.components.RevelationBadge
import com.example.ui.components.StickyAudioPlayerBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailScreen(
    surah: Surah,
    viewModel: QuranViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val versesUiState by viewModel.versesUiState.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()
    val bookmarkedSurahs by viewModel.bookmarkedSurahs.collectAsState()
    val selectedReciter by viewModel.selectedReciter.collectAsState()
    val arabicFontSize by viewModel.arabicFontSize.collectAsState()
    val banglaFontSize by viewModel.banglaFontSize.collectAsState()
    val showPronunciation by viewModel.showPronunciation.collectAsState()
    val showTranslation by viewModel.showTranslation.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var showFontDialog by remember { mutableStateOf(false) }
    var showJumpDialog by remember { mutableStateOf(false) }
    var showReciterDialog by remember { mutableStateOf(false) }
    var showDisplayDialog by remember { mutableStateOf(false) }
    var showVirtueDialog by remember { mutableStateOf(false) }

    if (showVirtueDialog) {
        val virtue = com.example.data.datasource.SurahVirtuesProvider.getVirtue(surah.number)
        com.example.ui.components.SurahVirtueDetailDialog(
            virtue = virtue,
            onDismiss = { showVirtueDialog = false }
        )
    }

    if (showFontDialog) {
        FontScaleDialog(
            arabicFontSize = arabicFontSize,
            banglaFontSize = banglaFontSize,
            onArabicFontChange = { viewModel.updateArabicFontSize(it) },
            onBanglaFontChange = { viewModel.updateBanglaFontSize(it) },
            onDismiss = { showFontDialog = false }
        )
    }

    if (showJumpDialog) {
        JumpToAyahDialog(
            totalAyat = surah.totalAyat,
            onJump = { ayahNum ->
                coroutineScope.launch {
                    val offset = if (surah.number != 9) 2 else 1
                    val targetIndex = (ayahNum - 1 + offset).coerceAtLeast(0)
                    listState.animateScrollToItem(targetIndex)
                }
            },
            onDismiss = { showJumpDialog = false }
        )
    }

    if (showReciterDialog) {
        ReciterSelectionDialog(
            selectedReciterId = selectedReciter.id,
            onReciterSelected = { viewModel.selectReciter(it) },
            onDismiss = { showReciterDialog = false }
        )
    }

    if (showDisplayDialog) {
        AlertDialog(
            onDismissRequest = { showDisplayDialog = false },
            title = { Text("পড়ার প্রদর্শন সেটিংস", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = showTranslation,
                            onCheckedChange = { viewModel.toggleShowTranslation() },
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("বাংলা অর্থ ও অনুবাদ দেখান", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDisplayDialog = false }) {
                    Text("ঠিক আছে", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "সুরা ${surah.nameBangla}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            RevelationBadge(revelationType = surah.revelationType)
                        }
                        Text(
                            text = "${surah.nameArabic} • ${BengaliNumberUtil.toBengaliNumber(surah.totalAyat)} আয়াত • অর্থ: ${surah.meaningBangla}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান"
                        )
                    }
                },
                actions = {
                    // Surah Virtue & History button
                    IconButton(
                        onClick = { showVirtueDialog = true },
                        modifier = Modifier.testTag("surah_virtue_info_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "সুরার ইতিহাস ও ফজিলত",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Jump to Ayah button
                    IconButton(
                        onClick = { showJumpDialog = true },
                        modifier = Modifier.testTag("jump_to_ayah_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NearMe,
                            contentDescription = "নির্দিষ্ট আয়াতে যান",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Display Settings Toggle
                    IconButton(
                        onClick = { showDisplayDialog = true },
                        modifier = Modifier.testTag("display_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "পড়ার ভিউ সেটিংস",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Font Size Dialog
                    IconButton(
                        onClick = { showFontDialog = true },
                        modifier = Modifier.testTag("font_size_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "হরফের আকার পরিবর্তন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Bookmark
                    IconButton(
                        onClick = { viewModel.toggleBookmark(surah.number) },
                        modifier = Modifier.testTag("surah_detail_bookmark_button")
                    ) {
                        val isBookmarked = bookmarkedSurahs.contains(surah.number)
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (isBookmarked) "সংরক্ষণ বাতিল" else "সংরক্ষণ করুন",
                            tint = if (isBookmarked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Sticky audio bar for ongoing audio playback (either full surah or single ayah)
            if (playbackState.isPlaying || playbackState.isLoading || playbackState.currentPositionMs > 0) {
                StickyAudioPlayerBar(
                    playbackState = playbackState,
                    surahNameBangla = surah.nameBangla,
                    onPlayPauseClick = {
                        if (playbackState.isPlaying) viewModel.pauseAudio() else viewModel.resumeAudio()
                    },
                    onCloseClick = { viewModel.stopAudio() },
                    onSeek = { viewModel.seekAudio(it) }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("surah_detail_lazy_column"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Full Surah Audio Player Card (Prominent and responsive)
            item(key = "full_surah_audio_card") {
                FullSurahPlayerCard(
                    surah = surah,
                    playbackState = playbackState,
                    onPlayPauseClick = {
                        viewModel.playFullSurah(surah.number)
                    },
                    onSeek = { viewModel.seekAudio(it) },
                    onSkipForward = { viewModel.skipForward10Sec() },
                    onSkipBackward = { viewModel.skipBackward10Sec() },
                    onSpeedChange = { viewModel.setPlaybackSpeed(it) },
                    onReciterClick = { showReciterDialog = true },
                    onRetryClick = { viewModel.retryAudio() }
                )
            }

            // 2. Bismillah Banner (All surahs except Surah At-Tawbah 9)
            if (surah.number != 9) {
                item(key = "bismillah_banner") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                fontSize = (arabicFontSize * 0.95f).sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "বিসমিল্লাহির রাহমানির রাহিম",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "পরম করুণাময় অসীম দয়ালু আল্লাহর নামে শুরু করছি",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // 3. Verses state (Loading, Error, Success)
            when (val state = versesUiState) {
                is VersesUiState.Loading -> {
                    item(key = "loading_indicator") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 3.dp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "আয়াতসমূহ প্রস্তুত করা হচ্ছে...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                is VersesUiState.Error -> {
                    item(key = "error_view") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = state.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { viewModel.loadSurahVerses(surah.number) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("আবার চেষ্টা করুন")
                                }
                            }
                        }
                    }
                }

                is VersesUiState.Success -> {
                    itemsIndexed(
                        items = state.verses,
                        key = { _, ayah -> "${ayah.surahNumber}_${ayah.ayahNumber}" }
                    ) { _, ayah ->
                        val isThisAyahPlaying = !playbackState.isFullSurah &&
                                playbackState.currentSurahNumber == ayah.surahNumber &&
                                playbackState.currentAyahNumber == ayah.ayahNumber &&
                                playbackState.isPlaying

                        val isThisAyahLoading = !playbackState.isFullSurah &&
                                playbackState.currentSurahNumber == ayah.surahNumber &&
                                playbackState.currentAyahNumber == ayah.ayahNumber &&
                                playbackState.isLoading

                        AyahItemCard(
                            ayah = ayah,
                            surahNameBangla = surah.nameBangla,
                            isPlaying = isThisAyahPlaying,
                            isLoading = isThisAyahLoading,
                            arabicFontSize = arabicFontSize,
                            banglaFontSize = banglaFontSize,
                            showPronunciation = showPronunciation,
                            showTranslation = showTranslation,
                            onPlayToggle = {
                                viewModel.recordLastRead(surah.number, ayah.ayahNumber, surah.nameBangla)
                                viewModel.playAyah(ayah)
                            }
                        )
                    }
                }
            }
        }
    }
}
