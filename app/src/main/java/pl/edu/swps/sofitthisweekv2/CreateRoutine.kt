package pl.edu.swps.sofitthisweekv2

import TrainingPlan
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.util.*

class CreateRoutine : AppCompatActivity() {

    private lateinit var btnPickStartDate: Button
    private lateinit var btnPickEndDate: Button
    private lateinit var checkMonday: CheckBox
    private lateinit var checkTuesday: CheckBox
    private lateinit var checkWednesday: CheckBox
    private lateinit var checkThursday: CheckBox
    private lateinit var checkFriday: CheckBox
    private lateinit var checkSaturday: CheckBox
    private lateinit var checkSunday: CheckBox

    private lateinit var spinnerTrainingPlans: Spinner
    private lateinit var btnSaveTraining: Button
    private lateinit var displayStartDate: TextView
    private lateinit var displayEndDate: TextView

    private var startDate: Long = 0
    private var endDate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        btnPickStartDate = findViewById(R.id.btnPickStartDate)
        btnPickEndDate = findViewById(R.id.btnPickEndDate)
        checkMonday = findViewById(R.id.checkMonday)
        checkTuesday = findViewById(R.id.checkTuesday)
        checkWednesday = findViewById(R.id.checkWednesday)
        checkThursday = findViewById(R.id.checkThursday)
        checkFriday = findViewById(R.id.checkFriday)
        checkSaturday = findViewById(R.id.checkSaturday)
        checkSunday = findViewById(R.id.checkSunday)
        spinnerTrainingPlans = findViewById(R.id.spinnerTrainingPlans)
        btnSaveTraining = findViewById(R.id.btnSaveTraining)
        displayStartDate = findViewById(R.id.displayStartDate)
        displayEndDate = findViewById(R.id.displayEndDate)

        btnPickStartDate.setOnClickListener {
            pickDate { date ->
                startDate = date
                displayStartDate.text = dateToString(startDate)
            }
        }
        btnPickEndDate.setOnClickListener {
            pickDate { date ->
                endDate = date
                displayEndDate.text = dateToString(endDate)
            }
        }

        btnSaveTraining.setOnClickListener { saveTrainingSchedule() }

        // Load training plans and populate the spinner
        loadTrainingPlans()


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                val intent = Intent(this@CreateRoutine, CalendarActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish() // Finish the current activity
            }
        })
    }

    private fun pickDate(onDateSelected: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            onDateSelected(calendar.timeInMillis / (1000 * 60 * 60 * 24))  // convert to days since epoch
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun dateToString(daysSinceEpoch: Long): String {
        val localDate = LocalDate.ofEpochDay(daysSinceEpoch)
        return localDate.toString() // Format as desired
    }

    private fun loadTrainingPlans() {
        val sharedPreferences = getSharedPreferences("training_plans_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("training_plans", "[]")
        val type = object : TypeToken<MutableList<TrainingPlan>>() {}.type
        val trainingPlans: MutableList<TrainingPlan> = gson.fromJson(json, type)

        val trainingPlanNames = trainingPlans.map { it.name }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, trainingPlanNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTrainingPlans.adapter = adapter
    }

    private fun saveTrainingSchedule() {
        val daysOfWeek = mutableListOf<Int>()
        if (checkMonday.isChecked) daysOfWeek.add(Calendar.MONDAY)
        if (checkTuesday.isChecked) daysOfWeek.add(Calendar.TUESDAY)
        if (checkWednesday.isChecked) daysOfWeek.add(Calendar.WEDNESDAY)
        if (checkThursday.isChecked) daysOfWeek.add(Calendar.THURSDAY)
        if (checkFriday.isChecked) daysOfWeek.add(Calendar.FRIDAY)
        if (checkSaturday.isChecked) daysOfWeek.add(Calendar.SATURDAY)
        if (checkSunday.isChecked) daysOfWeek.add(Calendar.SUNDAY)

        val selectedTrainingPlanName = spinnerTrainingPlans.selectedItem.toString()

        val trainingSchedule = TrainingSchedule(
            startDate = startDate,  // already in days since epoch
            endDate = endDate,      // already in days since epoch
            daysOfWeek = daysOfWeek,
            trainingPlanId = selectedTrainingPlanName
        )

        val sharedPreferences = getSharedPreferences("Routine", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("new_training_schedules", "[]")
        val type = object : TypeToken<MutableList<TrainingSchedule>>() {}.type
        val trainingSchedules: MutableList<TrainingSchedule> = gson.fromJson(json, type)

        trainingSchedules.add(trainingSchedule)

        val updatedJson = gson.toJson(trainingSchedules)
        sharedPreferences.edit().putString("new_training_schedules", updatedJson).apply()

        val intent = Intent(this, CalendarActivity::class.java)
        startActivity(intent)
    }
}
