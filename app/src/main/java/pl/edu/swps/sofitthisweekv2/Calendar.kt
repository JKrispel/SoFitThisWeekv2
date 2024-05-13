package pl.edu.swps.sofitthisweekv2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Calendar : AppCompatActivity() {

    private lateinit var bStats: Button
    private lateinit var bNutrition: Button
    private lateinit var bTraining: Button
    private lateinit var bProfile: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}