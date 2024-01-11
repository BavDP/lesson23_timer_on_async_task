package com.example.timeronasynctask

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.progressindicator.CircularProgressIndicator

class MainActivity : AppCompatActivity() {
    private val startBtn: Button by lazy { findViewById(R.id.startBtn) }
    private val stopBtn: Button by lazy { findViewById(R.id.stopBtn) }
    private val resetBtn: Button by lazy { findViewById(R.id.resetBtn) }
    private val timerText: TextView by lazy { findViewById(R.id.timerText) }
    private val progressBar: CircularProgressIndicator by lazy { findViewById(R.id.progressBar) }
    private var asyncTimer: TimerAsyncTask? = null
    private var timerCounter = CONST.TIMER_MAX

    object CONST {
        const val TIMER_MAX = 60
    }

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
            setTimerProgress(counter)
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
        setTimerProgress(CONST.TIMER_MAX)
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
        asyncTimer?.cancel(true)
        setTimerProgress(CONST.TIMER_MAX)
    }

    private fun setTimerProgress(value: Int) {
        timerCounter = value
        timerText.text = timerCounter.toString()
        progressBar.progress = timerCounter
    }
}