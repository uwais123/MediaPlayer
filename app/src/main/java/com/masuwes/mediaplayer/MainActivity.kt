package com.masuwes.mediaplayer

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var isReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        btn_stop.setOnClickListener(this)
        btn_play.setOnClickListener(this)

    }

    private fun init() {
        mMediaPlayer = MediaPlayer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val attribute = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            mMediaPlayer?.setAudioAttributes(attribute)
        } else {
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }

        val afd = applicationContext.resources.openRawResourceFd(R.raw.bass_solo)
        try {
            mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        /*
        Alur logika proses play sangatlah sederhana. Tombol play ditekan → baca audio menggunakan prepareAsync() → tunggu sampai proses baca selesai → metode start() dijalankan pada onPrepared().
        Setelah MediaPlayer sudah siap untuk dijalankan, apakah sedang menjalankan musik atau tidak? Jika sedang menjalankan musik maka perintah yang digunakan adalah pause(),
        yang berfungsi untuk memberikan jeda atau memberhentikan sementara kepada MediaPlayer.
        Jika tidak dikondisi itu, maka menggunakan start() untuk melanjutkan musik dari MediaPlayer.
         */

        mMediaPlayer?.setOnPreparedListener {
            isReady = true
            mMediaPlayer?.start()
        }
        mMediaPlayer?.setOnErrorListener { mp, what, extra -> false }
    }

    override fun onClick(view: View) {
        val id = view.id
        when(id) {
            R.id.btn_play -> if (!isReady) {
                mMediaPlayer?.prepareAsync()
            } else {
                if (mMediaPlayer?.isPlaying() as Boolean) {
                    mMediaPlayer?.pause()
                } else {
                    mMediaPlayer?.start()
                }
            }
            R.id.btn_stop -> if (mMediaPlayer?.isPlaying() as Boolean || isReady) {
                mMediaPlayer?.stop()
                isReady = false
            }
            else -> {

            }
        }
    }
}


































// end