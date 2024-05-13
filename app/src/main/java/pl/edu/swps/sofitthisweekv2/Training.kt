package pl.edu.swps.sofitthisweekv2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Training : AppCompatActivity() {

    private lateinit var bStats: Button
    private lateinit var bNutrition: Button
    private lateinit var bCalendar: Button
    private lateinit var bProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        bStats = findViewById(R.id.bStatsMenu)
        bStats.setOnClickListener {
            val intent = Intent(this, Stats::class.java)
            startActivity(intent)
        }

        bCalendar = findViewById<Button>(R.id.bCalendarMenu)
        bCalendar.setOnClickListener {
            val intent = Intent(this, Calendar::class.java)
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
