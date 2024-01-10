package com.example.timeronasynctask

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val startBtn: Button by lazy { findViewById(R.id.startBtn) }
    private val stopBtn: Button by lazy { findViewById(R.id.stopBtn) }
    private val resetBtn: Button by lazy { findViewById(R.id.resetBtn) }
    private val timerText: TextView by lazy { findViewById(R.id.timerText) }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private var asyncTimer: TimerAsyncTask? = null
    private var timerCounter = 60

    private inner class TimerAsyncTask: AsyncTask<Int, Int, Void>() {
        private var counter: Int = timerCounter
        override fun doInBackground(vararg p0: Int?): Void? {
            while (true) {
                counter--
                timerCounter = counter
                publishProgress(counter)
                if (counter == 0) {
                    this.cancel(true)
                    break
                }
                Thread.sleep(1000)
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            timerText.text = counter.toString()
            progressBar.progress = counter
        }

        override fun onCancelled() {
            super.onCancelled()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            init()
            setupButtonsListenerHandlers()
        }
    }

    private fun init() {
        progressBar.max = timerCounter
        progressBar.progress = timerCounter
        timerText.text = timerCounter.toString()
    }

    private fun setupButtonsListenerHandlers() {
        startBtn.setOnClickListener { startBtnClick() }
        stopBtn.setOnClickListener { stopBtnClick() }
        resetBtn.setOnClickListener { resetBtnClick() }
    }

    private fun startBtnClick() {
        asyncTimer = TimerAsyncTask()
        asyncTimer?.execute(timerCounter)
    }

    private fun stopBtnClick() {
        asyncTimer?.cancel(true)
    }

    private fun resetBtnClick() {
        timerCounter = 60
        timerText.text = timerCounter.toString()
    }
}