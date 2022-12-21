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
        val fromPlaceholder = findViewById<TextView>(R.id.fromPlaceholder)
        val tillPlaceholder = findViewById<TextView>(R.id.tillPlaceholder)
        val pricePlaceholder = findViewById<TextView>(R.id.pricePlaceholder)


        startButton.setOnClickListener {
            val drp = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                // limit only to 2 years from today
                .setSelection(androidx.core.util.Pair(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds() + 63113904000))
                    // disable days that already passed
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
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
                    dayCounter.text = "- dni"
                    fromPlaceholder.text = ""
                    tillPlaceholder.text = ""

                    Snackbar.make(
                        findViewById(R.id.root),
                        "Nie można zaplanować podróży w przeszłości. Co ty myślisz, że my cudotwórcy?",
                        Snackbar.LENGTH_SHORT
                    ).show()
                // else if from start or end date is more than 2 years away, cancel and send snackbar
                } else if (startDate > System.currentTimeMillis() + 63113904000 || endDate!! > System.currentTimeMillis() + 63113904000) {
                    dayCounter.text = "- dni"
                    fromPlaceholder.text = ""
                    tillPlaceholder.text = ""
                    Snackbar.make(
                        findViewById(R.id.root),
                        "Nie możesz zaplanować podróży na więcej niż 2 lata w przyszłość",
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
                    fromPlaceholder.text = "Rozpoczęcie podróży w dniu $startLocalDate"
                    tillPlaceholder.text = "Zakończenie podróży w dniu $endLocalDate"
                    pricePlaceholder.text = "Przewidywana Cena: ${days * 529} zł"
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