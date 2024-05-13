package pl.edu.swps.sofitthisweekv2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Nutrition : AppCompatActivity() {

    private lateinit var bStats: Button
    private lateinit var bCalendar: Button
    private lateinit var bTraining: Button
    private lateinit var bProfile: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)

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

        bCalendar = findViewById<Button>(R.id.bCalendarMenu)
        bCalendar.setOnClickListener {
            val intent = Intent(this, Calendar::class.java)
            startActivity(intent)
        }

        bProfile = findViewById<Button>(R.id.bProfileMenu)
        bProfile.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}