package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import android.os.PowerManager
import android.util.Log
import com.example.data.model.PlaybackState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class QuranAudioPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private var focusRequest: AudioFocusRequest? = null

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val playerScope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    // Fallback URLs queue for resilient playback
    private var candidateUrls: List<String> = emptyList()
    private var currentUrlIndex: Int = 0
    private var isPlayingFullSurah: Boolean = false
    private var targetSurahNumber: Int = 0
    private var targetAyahNumber: Int = 0
    private var activeReciterName: String = "শায়খ মিশারী রশীদ আল-আফাসী"

    var isRepeatEnabled: Boolean = false
    var onAyahCompletedListener: ((surah: Int, ayah: Int) -> Unit)? = null

    init {
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
            Log.e("QuranAudioPlayer", "Error releasing player", e)
        }

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            // Try wake lock for screen-off playback
            try {
                setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            } catch (e: Exception) {
                // Ignore if permission denied
            }

            setOnPreparedListener { mp ->
                Log.d("QuranAudioPlayer", "MediaPlayer prepared successfully. Duration: ${mp.duration}")
                _playbackState.value = _playbackState.value.copy(
                    isLoading = false,
                    isPlaying = true,
                    durationMs = mp.duration,
                    errorMessage = null
                )

                // Apply saved speed
                applyCurrentSpeed()

                mp.start()
                startProgressTracker()
            }

            setOnCompletionListener { mp ->
                if (isRepeatEnabled) {
                    mp.seekTo(0)
                    mp.start()
                } else {
                    val state = _playbackState.value
                    _playbackState.value = state.copy(
                        isPlaying = false,
                        currentPositionMs = 0
                    )
                    stopProgressTracker()
                    abandonAudioFocus()

                    if (!state.isFullSurah && state.currentAyahNumber > 0) {
                        onAyahCompletedListener?.invoke(state.currentSurahNumber, state.currentAyahNumber)
                    }
                }
            }

            setOnErrorListener { _, what, extra ->
                Log.e("QuranAudioPlayer", "MediaPlayer error: what=$what, extra=$extra. URL index: $currentUrlIndex")
                // Attempt next fallback URL if available
                if (currentUrlIndex + 1 < candidateUrls.size) {
                    currentUrlIndex++
                    Log.i("QuranAudioPlayer", "Trying fallback audio URL: ${candidateUrls[currentUrlIndex]}")
                    tryPlayCurrentCandidate()
                } else {
                    _playbackState.value = _playbackState.value.copy(
                        isLoading = false,
                        isPlaying = false,
                        errorMessage = "অডিও প্লে করা সম্ভব হয়নি। ইন্টারনেট সংযোগ ও স্পিকার ভলিউম পরীক্ষা করুন।"
                    )
                    stopProgressTracker()
                    abandonAudioFocus()
                }
                true
            }
        }
    }

    fun playFullSurah(surahNumber: Int, urls: List<String>, reciterName: String) {
        val currentState = _playbackState.value
        // If already playing this full surah from same reciter, toggle pause/resume
        if (currentState.isFullSurah &&
            currentState.currentSurahNumber == surahNumber &&
            currentState.reciterName == reciterName
        ) {
            if (currentState.isPlaying) {
                pause()
            } else {
                resume()
            }
            return
        }

        candidateUrls = urls
        currentUrlIndex = 0
        isPlayingFullSurah = true
        targetSurahNumber = surahNumber
        targetAyahNumber = 0
        activeReciterName = reciterName

        _playbackState.value = PlaybackState(
            isLoading = true,
            isPlaying = false,
            isFullSurah = true,
            currentSurahNumber = surahNumber,
            currentAyahNumber = 0,
            currentPositionMs = 0,
            durationMs = 0,
            reciterName = reciterName,
            speed = _playbackState.value.speed,
            errorMessage = null
        )

        tryPlayCurrentCandidate()
    }

    fun playAyah(surahNumber: Int, ayahNumber: Int, primaryUrl: String, fallbackUrl: String = "") {
        val currentState = _playbackState.value
        // If already playing this ayah, toggle pause/play
        if (!currentState.isFullSurah &&
            currentState.currentSurahNumber == surahNumber &&
            currentState.currentAyahNumber == ayahNumber
        ) {
            if (currentState.isPlaying) {
                pause()
            } else {
                resume()
            }
            return
        }

        val urls = if (fallbackUrl.isNotBlank()) listOf(primaryUrl, fallbackUrl) else listOf(primaryUrl)
        candidateUrls = urls
        currentUrlIndex = 0
        isPlayingFullSurah = false
        targetSurahNumber = surahNumber
        targetAyahNumber = ayahNumber

        _playbackState.value = PlaybackState(
            isLoading = true,
            isPlaying = false,
            isFullSurah = false,
            currentSurahNumber = surahNumber,
            currentAyahNumber = ayahNumber,
            currentPositionMs = 0,
            durationMs = 0,
            reciterName = "শায়খ মিশারী রশীদ আল-আফাসী",
            speed = _playbackState.value.speed,
            errorMessage = null
        )

        tryPlayCurrentCandidate()
    }

    private fun tryPlayCurrentCandidate() {
        if (candidateUrls.isEmpty() || currentUrlIndex >= candidateUrls.size) {
            _playbackState.value = _playbackState.value.copy(
                isLoading = false,
                isPlaying = false,
                errorMessage = "কোন কার্যকর অডিও লিংক পাওয়া যায়নি"
            )
            return
        }

        val url = candidateUrls[currentUrlIndex]
        Log.d("QuranAudioPlayer", "Preparing to stream audio: $url")

        stopProgressTracker()
        requestAudioFocus()

        try {
            initMediaPlayer()
            mediaPlayer?.apply {
                reset()
                setDataSource(url)
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("QuranAudioPlayer", "Exception in prepareAsync for URL: $url", e)
            if (currentUrlIndex + 1 < candidateUrls.size) {
                currentUrlIndex++
                tryPlayCurrentCandidate()
            } else {
                _playbackState.value = _playbackState.value.copy(
                    isLoading = false,
                    isPlaying = false,
                    errorMessage = "অডিও স্ট্রিম লোড করতে ত্রুটি হয়েছে"
                )
            }
        }
    }

    fun retryPlayback() {
        currentUrlIndex = 0
        tryPlayCurrentCandidate()
    }

    fun pause() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    _playbackState.value = _playbackState.value.copy(isPlaying = false)
                    stopProgressTracker()
                }
            }
        } catch (e: Exception) {
            Log.e("QuranAudioPlayer", "Error pausing", e)
        }
    }

    fun resume() {
        requestAudioFocus()
        try {
            mediaPlayer?.let {
                it.start()
                _playbackState.value = _playbackState.value.copy(isPlaying = true)
                applyCurrentSpeed()
                startProgressTracker()
            }
        } catch (e: Exception) {
            Log.e("QuranAudioPlayer", "Error resuming", e)
        }
    }

    fun seekTo(positionMs: Int) {
        try {
            mediaPlayer?.let {
                it.seekTo(positionMs.coerceIn(0, it.duration))
                _playbackState.value = _playbackState.value.copy(currentPositionMs = positionMs)
            }
        } catch (e: Exception) {
            Log.e("QuranAudioPlayer", "Error seeking", e)
        }
    }

    fun skipForward10Sec() {
        mediaPlayer?.let {
            val target = (it.currentPosition + 10000).coerceAtMost(it.duration)
            seekTo(target)
        }
    }

    fun skipBackward10Sec() {
        mediaPlayer?.let {
            val target = (it.currentPosition - 10000).coerceAtLeast(0)
            seekTo(target)
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackState.value = _playbackState.value.copy(speed = speed)
        applyCurrentSpeed()
    }

    private fun applyCurrentSpeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mediaPlayer?.let { mp ->
                    if (mp.isPlaying) {
                        val params = mp.playbackParams ?: PlaybackParams()
                        params.speed = _playbackState.value.speed
                        mp.playbackParams = params
                    }
                }
            } catch (e: Exception) {
                Log.e("QuranAudioPlayer", "Failed to set playback speed", e)
            }
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
            }
        } catch (e: Exception) {
            Log.e("QuranAudioPlayer", "Error stopping", e)
        }
        stopProgressTracker()
        abandonAudioFocus()
        _playbackState.value = PlaybackState(speed = _playbackState.value.speed)
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressJob = playerScope.launch {
            while (isActive) {
                try {
                    mediaPlayer?.let { mp ->
                        if (mp.isPlaying) {
                            _playbackState.value = _playbackState.value.copy(
                                currentPositionMs = mp.currentPosition,
                                durationMs = mp.duration
                            )
                        }
                    }
                } catch (e: Exception) {
                    // Ignore state errors during fast tracking
                }
                delay(400)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun requestAudioFocus() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val attributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(attributes)
                    .setOnAudioFocusChangeListener { focusChange ->
                        when (focusChange) {
                            AudioManager.AUDIOFOCUS_LOSS,
                            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
                            AudioManager.AUDIOFOCUS_GAIN -> resume()
                        }
                    }
                    .build()
                focusRequest?.let { audioManager?.requestAudioFocus(it) }
            } else {
                @Suppress("DEPRECATION")
                audioManager?.requestAudioFocus(
                    { focusChange ->
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                            pause()
                        }
                    },
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            }
        } catch (e: Exception) {
            Log.e("QuranAudioPlayer", "Audio focus request failed", e)
        }
    }

    private fun abandonAudioFocus() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                focusRequest?.let { audioManager?.abandonAudioFocusRequest(it) }
            }
        } catch (e: Exception) {
            Log.e("QuranAudioPlayer", "Audio focus abandon failed", e)
        }
    }

    fun release() {
        stopProgressTracker()
        abandonAudioFocus()
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
            Log.e("QuranAudioPlayer", "Error releasing player", e)
        }
        mediaPlayer = null
    }
}
