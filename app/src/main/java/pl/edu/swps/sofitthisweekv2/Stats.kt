package pl.edu.swps.sofitthisweekv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class Stats : AppCompatActivity() {

    private lateinit var bCalendar: Button
    private lateinit var bNutrition: Button
    private lateinit var bTraining: Button
    private lateinit var bProfile: Button
    private lateinit var bmi: TextView
    private lateinit var bmiDesc: TextView
    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        bmi = findViewById(R.id.bmi)
        bmiDesc = findViewById(R.id.bmiDesc)
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        var height = sharedPreferences.getString("Height", "")?.toFloatOrNull()
        val weight = sharedPreferences.getString("Weight", "")?.toFloatOrNull()

        if(height != null && weight != null) {

            height /= 100   // zamiana na metry
            val bmiVal = weight / (height * height)
            bmi.text = String.format("%.2f", bmiVal)

            if (bmiVal < 18.5) {

                bmiDesc.text = "(niedowaga)"
            }
            else if (bmiVal < 25) {

                bmiDesc.text = "(prawidłowe)"
            }
            else if (bmiVal < 30) {

                bmiDesc.text = "(nadwaga)"
            }
            else {
                bmiDesc.text = "(otyłość)"
            }
        }

        lineChart = findViewById(R.id.lineChart)

        val entries = ArrayList<Entry>()
        val gson = Gson()
        val json = sharedPreferences.getString("WeightHistory", "{}")
        val type = object : TypeToken<Map<String, Float>>() {}.type
        val weightHistory: Map<String, Float> = gson.fromJson(json, type)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()

        // Get the last 30 days
        for (i in 0..29) {
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val dateKey = dateFormat.format(calendar.time)
            val weight = weightHistory[dateKey] ?: 0f

            // Ensure only valid (non-zero) entries are added
            if (weight > 0f) {
                entries.add(Entry(calendar.timeInMillis.toFloat(), weight))
            }
        }

        val dataSet = LineDataSet(entries, "Waga (kg)")
        dataSet.color = resources.getColor(R.color.purple, theme)
        dataSet.valueTextColor = resources.getColor(R.color.black, theme)

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Format the X axis to show dates
        lineChart.xAxis.valueFormatter = object : ValueFormatter() {
            private val format = SimpleDateFormat("dd/MM", Locale.getDefault())

            override fun getFormattedValue(value: Float): String {
                return format.format(Date(value.toLong()))
            }
        }

        // Additional customizations
        lineChart.axisRight.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description = Description().apply { text = "Weight over Time" }
        lineChart.invalidate() // refresh


        bCalendar = findViewById(R.id.bCalendarMenu)
        bCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        bTraining = findViewById<Button>(R.id.bTrainingMenu)
        bTraining.setOnClickListener {
            val intent = Intent(this, Training::class.java)
            startActivity(intent)
        }

        bNutrition = findViewById<Button>(R.id.bNutritionMenu)
        bNutrition.setOnClickListener {
            val intent = Intent(this, Nutrition::class.java)
            startActivity(intent)
        }

        bProfile = findViewById<Button>(R.id.bProfileMenu)
        bProfile.setOnClickListener {
            val intent = Intent(this, pl.edu.swps.sofitthisweekv2.Profile::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                finish() // Finish the current activity
            }
        })
    }


}
