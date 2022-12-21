package com.mateusz.urlot

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.time.Instant
import java.time.ZoneId

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.startPlan)

        startButton.setOnClickListener {
            val drp = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(
                    androidx.core.util.Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

            // show date picker
            startButton.setOnClickListener {
                drp.show(supportFragmentManager, "DATE_PICKER_TAG")
            }

            // get selected dates
            drp.addOnPositiveButtonClickListener {
                val startDate = drp.selection?.first
                val endDate = drp.selection?.second
                // check if start date already passed
                if (startDate!! < System.currentTimeMillis()) {
                    Snackbar.make(
                        findViewById(R.id.root),
                        "Nie można zaplanować podróży w przeszłości. Co ty myślisz, że my cudotwórcy?",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    // convert to local date using Instant
                    val startLocalDate = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate()
                    val endLocalDate = Instant.ofEpochMilli(endDate!!).atZone(ZoneId.systemDefault()).toLocalDate()
                    Snackbar.make(
                        findViewById(R.id.root),
                        "${startLocalDate} - ${endLocalDate}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}