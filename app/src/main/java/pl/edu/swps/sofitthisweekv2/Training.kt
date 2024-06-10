package pl.edu.swps.sofitthisweekv2

import TrainingPlan
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Training : AppCompatActivity() {

    private lateinit var bStats: Button
    private lateinit var bNutrition: Button
    private lateinit var bCalendar: Button
    private lateinit var bProfile: Button
    private lateinit var bCreateTraining: Button
    private lateinit var linearLayoutTrainings: LinearLayout
    private val sharedPreferences by lazy { getSharedPreferences("training_plans_prefs", Context.MODE_PRIVATE) }
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        linearLayoutTrainings = findViewById(R.id.linearLayoutTrainings)

        loadTrainingPlans()

        bCreateTraining = findViewById(R.id.bCreateTraining)
        bCreateTraining.setOnClickListener {
            val intent = Intent(this, CreateTraining::class.java)
            startActivity(intent)
        }

        bStats = findViewById(R.id.bStatsMenu)
        bStats.setOnClickListener {
            val intent = Intent(this, Stats::class.java)
            startActivity(intent)
        }

        bCalendar = findViewById(R.id.bCalendarMenu)
        bCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        bNutrition = findViewById(R.id.bNutritionMenu)
        bNutrition.setOnClickListener {
            val intent = Intent(this, Nutrition::class.java)
            startActivity(intent)
        }

        bProfile = findViewById(R.id.bProfileMenu)
        bProfile.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                finish() // Finish the current activity
            }
        })
    }

    private fun loadTrainingPlans() {
        val json = sharedPreferences.getString("training_plans", "[]")
        val type = object : TypeToken<List<TrainingPlan>>() {}.type
        val trainingPlans: List<TrainingPlan> = gson.fromJson(json, type)

        trainingPlans.forEach { trainingPlan ->
            addTrainingButton(trainingPlan)
        }
    }

    private fun addTrainingButton(trainingPlan: TrainingPlan) {
        val button = Button(this).apply {
            text = trainingPlan.name
            setOnClickListener {
                val intent = Intent(this@Training, DisplayTraining::class.java)
                intent.putExtra("training_plan", trainingPlan)
                startActivity(intent)
            }
        }

        linearLayoutTrainings.addView(button)
    }
}
