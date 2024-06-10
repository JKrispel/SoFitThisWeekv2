package pl.edu.swps.sofitthisweekv2

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.time.DateTimeException
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CalendarActivity : AppCompatActivity() {

    private lateinit var bStats: Button
    private lateinit var bNutrition: Button
    private lateinit var bTraining: Button
    private lateinit var bProfile: Button
    private lateinit var bCreateRoutine: Button
    private lateinit var calendarView: MaterialCalendarView
    private val trainingSchedules = mutableListOf<TrainingSchedule>()
    private val trainingDaysMap = mutableMapOf<String, MutableList<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calendarView)

        loadTrainingSchedules()
        highlightTrainingDays()

        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            val selectedDateKey = "${date.year}-${date.month + 1}-${date.day}"
            if (trainingDaysMap.containsKey(selectedDateKey)) {
                val trainingPlansForTheDay = trainingDaysMap[selectedDateKey] ?: emptyList()
                showTrainingPlans(trainingPlansForTheDay)
            }
        })

        bStats = findViewById(R.id.bStatsMenu)
        bStats.setOnClickListener {
            val intent = Intent(this, Stats::class.java)
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
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        bCreateRoutine = findViewById<Button>(R.id.bCreateRoutine)
        bCreateRoutine.setOnClickListener {
            val intent = Intent(this, CreateRoutine::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                finish() // Finish the current activity
            }
        })
    }

    private fun loadTrainingSchedules() {
        val sharedPreferences = getSharedPreferences("Routine", Context.MODE_PRIVATE)
        val gson = Gson()

        // Load existing training days map
        val trainingDaysJson = sharedPreferences.getString("training_days", "{}")
        val trainingDaysType = object : TypeToken<MutableMap<String, MutableList<String>>>() {}.type
        val loadedTrainingDaysMap: MutableMap<String, MutableList<String>> = gson.fromJson(trainingDaysJson, trainingDaysType)
        trainingDaysMap.clear()
        trainingDaysMap.putAll(loadedTrainingDaysMap)

        // Load new training schedules
        val newSchedulesJson = sharedPreferences.getString("new_training_schedules", "[]")
        val newSchedulesType = object : TypeToken<MutableList<TrainingSchedule>>() {}.type
        val loadedTrainingSchedules: MutableList<TrainingSchedule> = gson.fromJson(newSchedulesJson, newSchedulesType)
        trainingSchedules.clear()
        trainingSchedules.addAll(loadedTrainingSchedules)
    }

    private fun highlightTrainingDays() {
        val datesToHighlight = mutableListOf<CalendarDay>()

        // Highlight existing training days
        trainingDaysMap.keys.forEach { dateKey ->
            val parts = dateKey.split("-")
            if (parts.size == 3) {
                val year = parts[0].toInt()
                val month = parts[1].toInt() - 1  // CalendarDay month is 0-based
                val day = parts[2].toInt()
                datesToHighlight.add(CalendarDay.from(year, month, day))
            }
        }

        // Process new training schedules
        trainingSchedules.forEach { schedule ->
            try {
                val startDate = LocalDate.ofEpochDay(schedule.startDate)
                val endDate = LocalDate.ofEpochDay(schedule.endDate)
                var date = startDate
                while (date <= endDate) {
                    if (date.dayOfWeek.value in schedule.daysOfWeek) {
                        val dateKey = "${date.year}-${date.monthValue}-${date.dayOfMonth - 1}"
                        datesToHighlight.add(CalendarDay.from(date.year, date.monthValue - 1, date.dayOfMonth - 1))
                        if (!trainingDaysMap.containsKey(dateKey)) {
                            trainingDaysMap[dateKey] = mutableListOf()
                        }
                        if (!trainingDaysMap[dateKey]!!.contains(schedule.trainingPlanId)) {
                            trainingDaysMap[dateKey]!!.add(schedule.trainingPlanId)
                        }
                    }
                    date = date.plusDays(1)
                }
            } catch (e: DateTimeException) {
                e.printStackTrace()
                // Handle invalid date value gracefully, maybe log the error or notify the user
            }
        }

        calendarView.addDecorator(EventDecorator(datesToHighlight))

        // Save updated training days map and clear new training schedules
        val sharedPreferences = getSharedPreferences("Routine", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val trainingDaysJson = gson.toJson(trainingDaysMap)
        editor.putString("training_days", trainingDaysJson)
        editor.putString("new_training_schedules", "[]")
        editor.apply()
    }

    private fun showTrainingPlans(trainingPlanIds: List<String>) {
        // Inflate the dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_training_plans, null)

        // Get references to dialog views
        val listView = dialogView.findViewById<ListView>(R.id.lvTrainingPlans)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)

        // Set up the ListView with an ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, trainingPlanIds)
        listView.adapter = adapter

        // Create and show the AlertDialog
        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .show()
    }

    inner class EventDecorator(private val dates: Collection<CalendarDay>) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(DotSpan(16f, Color.RED))
        }
    }
}
