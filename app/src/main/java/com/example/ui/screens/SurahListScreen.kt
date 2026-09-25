package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.HadithProvider
import com.example.data.model.Surah
import com.example.data.util.BengaliNumberUtil
import com.example.ui.QuranViewModel
import com.example.ui.SurahFilter
import com.example.ui.components.HadithItemCard
import com.example.ui.components.ReciterSelectionDialog
import com.example.ui.components.StickyAudioPlayerBar
import com.example.ui.components.SurahCard
import com.example.ui.components.SurahVirtueCard
import com.example.ui.components.SurahVirtueDetailDialog
import com.example.ui.components.TasbihDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahListScreen(
    viewModel: QuranViewModel,
    onSurahSelected: (Surah) -> Unit,
    modifier: Modifier = Modifier
) {
    val surahs by viewModel.filteredSurahs.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val bookmarkedSurahs by viewModel.bookmarkedSurahs.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()
    val lastRead by viewModel.lastRead.collectAsState()
    val selectedReciter by viewModel.selectedReciter.collectAsState()

    // Virtues & Hadiths
    val virtues by viewModel.filteredVirtues.collectAsState()
    val selectedVirtue by viewModel.selectedVirtue.collectAsState()
    val hadiths by viewModel.filteredHadiths.collectAsState()
    val selectedHadithCategory by viewModel.selectedHadithCategory.collectAsState()
    val hadithSearchQuery by viewModel.hadithSearchQuery.collectAsState()
    val favoriteHadithIds by viewModel.favoriteHadithIds.collectAsState()

    val dailyAyah = viewModel.dailyAyah

    var showTasbihDialog by remember { mutableStateOf(false) }
    var showReciterDialog by remember { mutableStateOf(false) }

    if (showTasbihDialog) {
        TasbihDialog(onDismiss = { showTasbihDialog = false })
    }

    if (showReciterDialog) {
        ReciterSelectionDialog(
            selectedReciterId = selectedReciter.id,
            onReciterSelected = { viewModel.selectReciter(it) },
            onDismiss = { showReciterDialog = false }
        )
    }

    selectedVirtue?.let { virtue ->
        SurahVirtueDetailDialog(
            virtue = virtue,
            onDismiss = { viewModel.selectVirtue(null) }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "আল কুরআনুল কারীম",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "অনুবাদ ও অডিও সহ ১১৪টি সুরা",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                actions = {
                    // Digital Tasbih Button
                    IconButton(
                        onClick = { showTasbihDialog = true },
                        modifier = Modifier.testTag("open_tasbih_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = "তসবিহ কাউন্টার",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Reciter Selector Button
                    IconButton(
                        onClick = { showReciterDialog = true },
                        modifier = Modifier.testTag("open_reciter_dialog_button")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "ক্বারী",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Persistent Audio Player bar (shows when playing full surah or ayah)
            if (playbackState.isPlaying || playbackState.isLoading || playbackState.currentSurahNumber > 0) {
                val playingSurah = surahs.find { it.number == playbackState.currentSurahNumber }
                val surahName = playingSurah?.nameBangla ?: "সুরা"
                val ayahCount = playingSurah?.totalAyat ?: 0

                StickyAudioPlayerBar(
                    playbackState = playbackState,
                    surahNameBangla = surahName,
                    onPlayPauseClick = {
                        if (playbackState.isPlaying) viewModel.pauseAudio() else viewModel.resumeAudio()
                    },
                    onCloseClick = { viewModel.stopAudio() },
                    onSeek = { viewModel.seekAudio(it) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input Field
            if (selectedFilter == SurahFilter.HADITH) {
                OutlinedTextField(
                    value = hadithSearchQuery,
                    onValueChange = { viewModel.setHadithSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("hadith_search_input"),
                    placeholder = {
                        Text(
                            text = "হাদিস, বিষয় বা রাবী খুঁজুন...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "খুঁজুন",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (hadithSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setHadithSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "পরিষ্কার করুন",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            } else {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("surah_search_input"),
                    placeholder = {
                        Text(
                            text = if (selectedFilter == SurahFilter.VIRTUES)
                                "সুরার নাম বা ফজিলত খুঁজুন..."
                            else
                                "সুরা খুঁজুন (যেমন: ফাতিহা, বাক্বারাহ, ১, ২)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "খুঁজুন",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "পরিষ্কার করুন",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            // Horizontally Scrollable Main Filter Chips (সকল, মাক্কী, মাদানী, সুরার ফজিলত, হাদিস, পছন্দ)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == SurahFilter.ALL,
                    onClick = { viewModel.setFilter(SurahFilter.ALL) },
                    label = { Text("সকল (${BengaliNumberUtil.toBengaliNumber(114)})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("filter_all")
                )

                FilterChip(
                    selected = selectedFilter == SurahFilter.MAKKI,
                    onClick = { viewModel.setFilter(SurahFilter.MAKKI) },
                    label = { Text("মাক্কী (${BengaliNumberUtil.toBengaliNumber(86)})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("filter_makki")
                )

                FilterChip(
                    selected = selectedFilter == SurahFilter.MADANI,
                    onClick = { viewModel.setFilter(SurahFilter.MADANI) },
                    label = { Text("মাদানী (${BengaliNumberUtil.toBengaliNumber(28)})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("filter_madani")
                )

                // NEW: সুরার ফজিলত Section
                FilterChip(
                    selected = selectedFilter == SurahFilter.VIRTUES,
                    onClick = { viewModel.setFilter(SurahFilter.VIRTUES) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = { Text("সুরার ফজিলত") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("filter_virtues")
                )

                // NEW: হাদিস (১০০০+) Section
                FilterChip(
                    selected = selectedFilter == SurahFilter.HADITH,
                    onClick = { viewModel.setFilter(SurahFilter.HADITH) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = { Text("হাদিস (১০০০+)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("filter_hadith")
                )

                FilterChip(
                    selected = selectedFilter == SurahFilter.BOOKMARKED,
                    onClick = { viewModel.setFilter(SurahFilter.BOOKMARKED) },
                    label = { Text("পছন্দ (${BengaliNumberUtil.toBengaliNumber(bookmarkedSurahs.size)})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("filter_bookmarked")
                )
            }

            // If HADITH section is active, show category sub-filters
            if (selectedFilter == SurahFilter.HADITH) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val hadithCats = listOf("সকল হাদিস", "পছন্দের হাদিস") + HadithProvider.categories.filter { it != "সকল হাদিস" }
                    hadithCats.forEach { categoryName ->
                        val isSelected = selectedHadithCategory == categoryName
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setHadithCategory(categoryName) },
                            label = { Text(categoryName, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }
                }
            }

            // Main Content Area based on active filter
            when (selectedFilter) {
                SurahFilter.VIRTUES -> {
                    // সুরার ফজিলত ও শানে নুযূল List
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("virtues_lazy_column"),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "যেকোনো সুরার নামের উপর ক্লিক করে তার ইতিহাস, শানে নুযূল ও সহীহ ফজিলত জানুন।",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }

                        if (virtues.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(36.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "কোন সুরার ফজিলত পাওয়া যায়নি।",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            items(
                                items = virtues,
                                key = { it.surahNumber }
                            ) { item ->
                                SurahVirtueCard(
                                    virtue = item,
                                    onClick = { viewModel.selectVirtue(item) }
                                )
                            }
                        }
                    }
                }

                SurahFilter.HADITH -> {
                    // হাদিস সংকলন List (১০০০+)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("hadith_lazy_column"),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "১০০০+ সহীহ হাদিস ভাণ্ডার",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        Text(
                                            text = "আরবি متن ও বাংলা অনুবাদ সহ মোট ${BengaliNumberUtil.toBengaliNumber(hadiths.size)}টি হাদিস প্রদর্শিত হচ্ছে।",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        if (hadiths.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(36.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "কোন হাদিস পাওয়া যায়নি। অন্য শব্দ দিয়ে খুঁজুন।",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            items(
                                items = hadiths,
                                key = { it.id }
                            ) { item ->
                                HadithItemCard(
                                    hadith = item,
                                    isFavorite = favoriteHadithIds.contains(item.id),
                                    onFavoriteToggle = { viewModel.toggleFavoriteHadith(item.id) }
                                )
                            }
                        }
                    }
                }

                else -> {
                    // Standard Quran Surah List (সকল, মাক্কী, মাদানী, পছন্দ)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("surahs_lazy_column"),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 1. Last Read Banner
                        if (lastRead != null && searchQuery.isBlank()) {
                            item(key = "last_read_banner") {
                                val lr = lastRead!!
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val targetSurah = surahs.find { it.number == lr.surahNumber }
                                            if (targetSurah != null) onSurahSelected(targetSurah)
                                        }
                                        .testTag("last_read_card"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.History,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.secondary,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "সর্বশেষ পঠিত",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                                Text(
                                                    text = "সুরা ${lr.surahNameBangla} (আয়াত ${BengaliNumberUtil.toBengaliNumber(lr.ayahNumber)})",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }

                                        Button(
                                            onClick = {
                                                val targetSurah = surahs.find { it.number == lr.surahNumber }
                                                if (targetSurah != null) onSurahSelected(targetSurah)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text("চালিয়ে যান", fontSize = 12.sp)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                                contentDescription = null,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 2. Daily Ayah Card (Featured Quranic verse - ONLY Arabic & Bangla Translation, NO pronunciation)
                        if (searchQuery.isBlank() && selectedFilter == SurahFilter.ALL) {
                            item(key = "daily_ayah_card") {
                                val isDailyPlaying = !playbackState.isFullSurah &&
                                        playbackState.currentSurahNumber == dailyAyah.surahNumber &&
                                        playbackState.currentAyahNumber == dailyAyah.ayahNumber &&
                                        playbackState.isPlaying

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("daily_ayah_card"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "আজকের কুরআনিক বাণী (সুরা ${dailyAyah.surahNameBangla})",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )

                                            FilledIconButton(
                                                onClick = { viewModel.playDailyAyah(dailyAyah) },
                                                modifier = Modifier.size(32.dp),
                                                colors = IconButtonDefaults.filledIconButtonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary,
                                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = if (isDailyPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                    contentDescription = "তিলাওয়াত শুনুন",
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Arabic Text
                                        Text(
                                            text = dailyAyah.arabicText,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif,
                                            textAlign = TextAlign.Right,
                                            modifier = Modifier.fillMaxWidth(),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Bangla Translation ONLY (Pronunciation removed as requested)
                                        Text(
                                            text = "অনুবাদ: \"${dailyAyah.banglaTranslation}\"",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                            }
                        }

                        // 3. Surah List items
                        if (surahs.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(36.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "কোন সুরা খুঁজে পাওয়া যায়নি। অন্য কোনো নাম দিয়ে অনুসন্ধান করুন।",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            items(
                                items = surahs,
                                key = { it.number }
                            ) { surah ->
                                SurahCard(
                                    surah = surah,
                                    isBookmarked = bookmarkedSurahs.contains(surah.number),
                                    onClick = { onSurahSelected(surah) },
                                    onBookmarkToggle = { viewModel.toggleBookmark(surah.number) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
