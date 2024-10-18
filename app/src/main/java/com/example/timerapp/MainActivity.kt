package com.example.timerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var isTimerRunning = false
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.start_button)
        val stopButton = findViewById<Button>(R.id.stop_button)

        startButton.setOnClickListener {
            if (!isTimerRunning) {
                isTimerRunning = true
                startTime = System.currentTimeMillis() // Record start time when timer starts
                println("Timer started at: $startTime")
            }
        }

        stopButton.setOnClickListener {
            if (isTimerRunning) {
                isTimerRunning = false
                val endTime = System.currentTimeMillis()  // Record end time when timer stops
                println("Timer stopped at: $endTime")

                // Call function to fetch projects and submit both start and end time
                ClockifyAPI.fetchProjectsAndSubmitTime(this, startTime, endTime)
            }
        }
    }
}
