package pl.edu.swps.sofitthisweekv2

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class Stats : AppCompatActivity() {

    private lateinit var bCalendar: Button
    private lateinit var bNutrition: Button
    private lateinit var bTraining: Button
    private lateinit var bProfile: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

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


}
