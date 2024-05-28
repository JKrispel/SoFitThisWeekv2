package pl.edu.swps.sofitthisweekv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class Nutrition : AppCompatActivity() {

    private lateinit var bStats: Button
    private lateinit var bCalendar: Button
    private lateinit var bTraining: Button
    private lateinit var bProfile: Button
    private lateinit var bAddProduct: Button
    private lateinit var bChangeGoal: Button
    private lateinit var caloriesCount: TextView
    private lateinit var proteinCount: TextView
    private lateinit var fatsCount: TextView
    private lateinit var carbsCount: TextView
    private lateinit var caloriesMax: TextView
    private lateinit var proteinMax: TextView
    private lateinit var fatsMax: TextView
    private lateinit var carbsMax: TextView

private fun loadData() {
    val sharedPreferences = getSharedPreferences("UserGoal", Context.MODE_PRIVATE)
    val calories = sharedPreferences.getString("Calories", "")
    val protein = sharedPreferences.getString("Protein", "")
    val fats = sharedPreferences.getString("Fats", "")
    val carbs = sharedPreferences.getString("Carbs", "")
    val caloriesValue = calories?.toIntOrNull() ?: 1

    caloriesMax.text = "/${caloriesValue}"
    proteinMax.text = run {
        val proteinValue = protein?.toIntOrNull() ?: 1
        "/${(proteinValue.toFloat() * caloriesValue / 400).roundToInt()}g"
    }
    fatsMax.text = run {
        val fatsValue = fats?.toIntOrNull() ?: 1
        "/${(fatsValue.toFloat() * caloriesValue / 900).roundToInt()}g"
    }
    carbsMax.text = run {
        val carbsValue = carbs?.toIntOrNull() ?: 1
        "/${(carbsValue.toFloat() * caloriesValue / 400).roundToInt()}g"
    }
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)

        caloriesMax = findViewById(R.id.caloriesMax)
        proteinMax = findViewById(R.id.proteinMax)
        fatsMax = findViewById(R.id.fatsMax)
        carbsMax = findViewById(R.id.carbsMax)

        loadData()

        bAddProduct = findViewById(R.id.bAddProduct)
        bAddProduct.setOnClickListener {

        }

        bChangeGoal = findViewById(R.id.bChangeGoal)
        bChangeGoal.setOnClickListener {

            val intent = Intent(this, NutritionChangeGoal::class.java)
            startActivity(intent)
        }

        bStats = findViewById(R.id.bStatsMenu)
        bStats.setOnClickListener {
            val intent = Intent(this, Stats::class.java)
            startActivity(intent)
        }

        bTraining = findViewById(R.id.bTrainingMenu)
        bTraining.setOnClickListener {
            val intent = Intent(this, Training::class.java)
            startActivity(intent)
        }

        bCalendar = findViewById(R.id.bCalendarMenu)
        bCalendar.setOnClickListener {
            val intent = Intent(this, Calendar::class.java)
            startActivity(intent)
        }

        bProfile = findViewById(R.id.bProfileMenu)
        bProfile.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}