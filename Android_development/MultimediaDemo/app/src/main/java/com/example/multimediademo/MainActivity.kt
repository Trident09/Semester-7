package com.example.multimediademo

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var btnPlay: Button
    private lateinit var btnPause: Button
    private lateinit var btnStop: Button
    private lateinit var btnStartRecording: Button
    private lateinit var btnStopRecording: Button
    private lateinit var btnPlayRecording: Button

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioFilePath: String? = null
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        checkPermissions()
        setupVideoPlayer()
        setupAudioRecorder()
    }

    private fun initializeViews() {
        videoView = findViewById(R.id.videoView)
        btnPlay = findViewById(R.id.btnPlay)
        btnPause = findViewById(R.id.btnPause)
        btnStop = findViewById(R.id.btnStop)
        btnStartRecording = findViewById(R.id.btnStartRecording)
        btnStopRecording = findViewById(R.id.btnStopRecording)
        btnPlayRecording = findViewById(R.id.btnPlayRecording)

        setupVideoButtons()
        setupAudioButtons()
    }

    private fun setupVideoButtons() {
        btnPlay.setOnClickListener { videoView.start() }
        btnPause.setOnClickListener { videoView.pause() }
        btnStop.setOnClickListener {
            videoView.stopPlayback()
            setupVideoPlayer()
        }
    }

    private fun setupAudioButtons() {
        btnStartRecording.setOnClickListener { startRecording() }
        btnStopRecording.setOnClickListener { stopRecording() }
        btnPlayRecording.setOnClickListener { playRecording() }
    }

    private fun setupVideoPlayer() {
        val videoPath = "android.resource://$packageName/raw/sample_video"
        videoView.setVideoURI(Uri.parse(videoPath))
        videoView.setMediaController(MediaController(this))
    }

    private fun setupAudioRecorder() {
        audioFilePath = "${externalCacheDir?.absolutePath}/audio_record.mp3"
    }

    private fun startRecording() {
        if (checkAudioPermissions()) {
            try {
                mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    MediaRecorder(this)
                } else {
                    @Suppress("DEPRECATION")
                    MediaRecorder()
                }.apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(audioFilePath)
                    prepare()
                    start()
                }
                isRecording = true
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error starting recording: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder?.apply {
                    stop()
                    release()
                }
                mediaRecorder = null
                isRecording = false
                Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error stopping recording: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playRecording() {
        val file = File(audioFilePath ?: "")
        if (file.exists()) {
            mediaPlayer = MediaPlayer().apply {
                try {
                    setDataSource(audioFilePath)
                    prepare()
                    start()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Error playing recording: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
        } else {
            Toast.makeText(this, "No recording found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>().apply {
            add(Manifest.permission.RECORD_AUDIO)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_VIDEO)
            }
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, PERMISSION_REQUEST_CODE)
        }
    }

    private fun checkAudioPermissions(): Boolean {
        val permissions = mutableListOf(Manifest.permission.RECORD_AUDIO).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, AUDIO_PERMISSION_REQUEST_CODE)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    setupVideoPlayer()
                    Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
                }
            }
            AUDIO_PERMISSION_REQUEST_CODE -> {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    Toast.makeText(this, "Audio permissions granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Audio permissions denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.apply {
            if (isRecording) {
                stop()
            }
            release()
        }
        mediaPlayer?.release()
        mediaRecorder = null
        mediaPlayer = null
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
        private const val AUDIO_PERMISSION_REQUEST_CODE = 1002
    }
}