package com.mateusz.urlot

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.startPlan)
        val dayCounter = findViewById<TextView>(R.id.dayCounter)

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
                    dayCounter.text = "0 dni"
                    Snackbar.make(
                        findViewById(R.id.root),
                        "Nie można zaplanować podróży w przeszłości. Co ty myślisz, że my cudotwórcy?",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    // convert to local date using Instant
                    // count how many days between start and end date
                    val start = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate()
                    val end = Instant.ofEpochMilli(endDate!!).atZone(ZoneId.systemDefault()).toLocalDate()
                    val days = start.until(end).days
                    dayCounter.text = "${days.toString()} dni"
                    val startLocalDate = Instant.ofEpochMilli(startDate).atZone(ZoneId.of("Europe/Warsaw")).toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    val endLocalDate = Instant.ofEpochMilli(endDate!!).atZone(ZoneId.of("Europe/Warsaw")).toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    Snackbar.make(
                        findViewById(R.id.root),
                        "Poczekaj chwile, nasze gnomy właśnie obliczają twoją podróż w terminie od ${startLocalDate} do ${endLocalDate}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}