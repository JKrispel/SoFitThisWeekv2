package pl.edu.swps.sofitthisweekv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class NutritionChangeGoal : AppCompatActivity() {

    private lateinit var bApplyGoal: Button
    private lateinit var editCalories: EditText
    private lateinit var editProtein: EditText
    private lateinit var editFats: EditText
    private lateinit var editCarbs: EditText
    private lateinit var macroError: TextView
    private lateinit var nullError: TextView


    private fun saveData() {
        val sharedPreferences = getSharedPreferences("UserGoal", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Calories", editCalories.text.toString())
        editor.putString("Protein", editProtein.text.toString())
        editor.putString("Fats", editFats.text.toString())
        editor.putString("Carbs", editCarbs.text.toString())
        editor.apply() // or editor.commit() for synchronous saving
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition_change_goal)

        editCalories = findViewById(R.id.editCalories)
        editProtein = findViewById(R.id.editProtein)
        editFats = findViewById(R.id.editFats)
        editCarbs = findViewById(R.id.editCarbs)
        bApplyGoal = findViewById(R.id.bApplyGoal)
        macroError = findViewById(R.id.textMacroError)
        nullError = findViewById(R.id.nullError)

        bApplyGoal.setOnClickListener {

            val calories = editCalories.text.toString().toIntOrNull()
            val protein = editProtein.text.toString().toIntOrNull()
            val fats = editFats.text.toString().toIntOrNull()
            val carbs = editCarbs.text.toString().toIntOrNull()

            if (calories != null && protein != null && fats != null && carbs != null)
            {
                val sum = protein + fats + carbs

                if (sum == 100) {

                    saveData()
                    val intent = Intent(this, Nutrition::class.java)
                    startActivity(intent)
                }
                else {
                    macroError.visibility = View.VISIBLE
                }
            }
            else {
                nullError.visibility = View.VISIBLE
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                val intent = Intent(this@NutritionChangeGoal, Nutrition::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish() // Finish the current activity
            }
        })

    }
}