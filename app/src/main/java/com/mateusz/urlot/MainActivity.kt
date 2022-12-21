package com.mateusz.urlot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
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
                // check if person is not traveling in reverse, ie end date already passed
                if (endDate!! < startDate!!) {
                    Snackbar.make(
                        findViewById(R.id.root),
                        "End date cannot be before start date",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    Snackbar.make(
                        findViewById(R.id.root),
                        "Start date: $startDate, End date: $endDate",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
}