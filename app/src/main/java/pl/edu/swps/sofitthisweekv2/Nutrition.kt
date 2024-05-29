package pl.edu.swps.sofitthisweekv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.min
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
    private lateinit var barCalories: ProgressBar
    private lateinit var barProtein: ProgressBar
    private lateinit var barFats: ProgressBar
    private lateinit var barCarbs: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)

        // Initialize UI elements
        caloriesMax = findViewById(R.id.caloriesMax)
        proteinMax = findViewById(R.id.proteinMax)
        fatsMax = findViewById(R.id.fatsMax)
        carbsMax = findViewById(R.id.carbsMax)
        caloriesCount = findViewById(R.id.caloriesCount)
        proteinCount = findViewById(R.id.proteinCount)
        fatsCount = findViewById(R.id.fatsCount)
        carbsCount = findViewById(R.id.carbsCount)
        barCalories = findViewById(R.id.barCalories)
        barProtein = findViewById(R.id.barProtein)
        barFats = findViewById(R.id.barFats)
        barCarbs = findViewById(R.id.barCarbs)

        loadGoalData()
        loadCurrentMacro()
        loadNewProduct()    // 0 values if null
        saveCurrentMacro()
        updateProgressBars()

        // Set button listeners
        bAddProduct = findViewById(R.id.bAddProduct)
        bAddProduct.setOnClickListener {
            val intent = Intent(this, AddProduct::class.java)
            startActivity(intent)
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

    private fun loadGoalData() {
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

    private fun loadNewProduct() {
        val extras = intent.extras
        if (extras != null) {
            val newCalories = extras.getInt("calories", 0)
            val newProtein = extras.getInt("protein", 0)
            val newFats = extras.getInt("fats", 0)
            val newCarbs = extras.getInt("carbs", 0)

            val currentCalories = caloriesCount.text.toString().toIntOrNull() ?: 0
            val currentProtein = proteinCount.text.toString().toIntOrNull() ?: 0
            val currentFats = fatsCount.text.toString().toIntOrNull() ?: 0
            val currentCarbs = carbsCount.text.toString().toIntOrNull() ?: 0

            caloriesCount.text = (currentCalories + newCalories).toString()
            proteinCount.text = (currentProtein + newProtein).toString()
            fatsCount.text = (currentFats + newFats).toString()
            carbsCount.text = (currentCarbs + newCarbs).toString()
        }
    }

    private fun saveCurrentMacro() {
        val sharedPreferences = getSharedPreferences("UserMacro", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Calories", caloriesCount.text.toString())
        editor.putString("Protein", proteinCount.text.toString())
        editor.putString("Fats", fatsCount.text.toString())
        editor.putString("Carbs", carbsCount.text.toString())
        editor.apply()
    }

    private fun loadCurrentMacro() {
        val sharedPreferences = getSharedPreferences("UserMacro", Context.MODE_PRIVATE)
        val calories = sharedPreferences.getString("Calories", "")
        val protein = sharedPreferences.getString("Protein", "")
        val fats = sharedPreferences.getString("Fats", "")
        val carbs = sharedPreferences.getString("Carbs", "")

        caloriesCount.text = calories
        proteinCount.text = protein
        fatsCount.text = fats
        carbsCount.text = carbs
    }

    private fun updateProgressBars() {
        val maxCalories = caloriesMax.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 1
        val maxProtein = proteinMax.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 1
        val maxFats = fatsMax.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 1
        val maxCarbs = carbsMax.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 1

        val currentCalories = caloriesCount.text.toString().toIntOrNull() ?: 0
        val currentProtein = proteinCount.text.toString().toIntOrNull() ?: 0
        val currentFats = fatsCount.text.toString().toIntOrNull() ?: 0
        val currentCarbs = carbsCount.text.toString().toIntOrNull() ?: 0

        barCalories.max = maxCalories
        barProtein.max = maxProtein
        barFats.max = maxFats
        barCarbs.max = maxCarbs

        barCalories.progress = min(currentCalories, maxCalories)
        barProtein.progress = min(currentProtein, maxProtein)
        barFats.progress = min(currentFats, maxFats)
        barCarbs.progress = min(currentCarbs, maxCarbs)
    }
}
