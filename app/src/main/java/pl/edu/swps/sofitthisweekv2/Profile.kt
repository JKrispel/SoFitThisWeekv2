package pl.edu.swps.sofitthisweekv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO okreslic dzialanie dla przycisku cofania w kazdym Activity (obecnie cofa zmiany)
// TODO Calendar (11 + podwidoki)
// TODO opcjonalnie: Statystyki
// TODO opcjonalnie: usuwanie cwiczen i treningow
class Profile : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userHeight: TextView
    private lateinit var userWeight: TextView
    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputHeight: EditText
    private lateinit var inputWeight: EditText
    private lateinit var editProfile: Button
    private lateinit var bStats: Button
    private lateinit var bNutrition: Button
    private lateinit var bTraining: Button
    private lateinit var bCalendar: Button
    private var isEditEnabled = false // To track whether edit mode is on or off



    private fun saveData() {
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Name", inputName.text.toString())
        editor.putString("Email", inputEmail.text.toString())
        editor.putString("Height", inputHeight.text.toString())
        editor.putString("Weight", inputWeight.text.toString())
        // Save weight history
        val gson = Gson()
        val json = sharedPreferences.getString("WeightHistory", "{}")
        val type = object : TypeToken<MutableMap<String, Float>>() {}.type
        val weightHistory: MutableMap<String, Float> = gson.fromJson(json, type)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val newWeight = inputWeight.text.toString().toFloatOrNull() ?: 0f

        weightHistory[currentDate] = newWeight

        val updatedJson = gson.toJson(weightHistory)
        editor.putString("WeightHistory", updatedJson)
        editor.apply() // or editor.commit() for synchronous saving
    }


    private fun loadData() {
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("Name", "")
        val email = sharedPreferences.getString("Email", "")
        val height = sharedPreferences.getString("Height", "")
        val weight = sharedPreferences.getString("Weight", "")

        userName.text = name
        userEmail.text = email
        userHeight.text = height
        userWeight.text = weight
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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

        bCalendar = findViewById<Button>(R.id.bCalendarMenu)
        bCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
        // Initialize the views
        userName = findViewById(R.id.textUserName)
        userEmail = findViewById(R.id.textUserEmail)
        userHeight = findViewById(R.id.textUserHeight)
        userWeight = findViewById(R.id.textUserWeight)
        inputName = findViewById(R.id.editName)
        inputEmail = findViewById(R.id.editEmail)
        inputHeight = findViewById(R.id.editHeight)
        inputWeight = findViewById(R.id.editWeight)
        editProfile = findViewById(R.id.bEditProfile)

        // Set initial visibility
        inputName.visibility = View.GONE
        inputEmail.visibility = View.GONE
        inputHeight.visibility = View.GONE
        inputWeight.visibility = View.GONE

        loadData()

        // Set the button click listener
        editProfile.setOnClickListener {
            toggleEdit()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                finish() // Finish the current activity
            }
        })
    }

    private fun toggleEdit() {
        if (!isEditEnabled) {
            // Switch to edit mode
            userName.visibility = View.GONE
            userEmail.visibility = View.GONE
            userHeight.visibility = View.GONE
            userWeight.visibility = View.GONE

            inputName.visibility = View.VISIBLE
            inputEmail.visibility = View.VISIBLE
            inputHeight.visibility = View.VISIBLE
            inputWeight.visibility = View.VISIBLE

            inputName.setText(userName.text)
            inputEmail.setText(userEmail.text)
            inputHeight.setText(userHeight.text)
            inputWeight.setText(userWeight.text)

            editProfile.text = getString(R.string.saveChanges) // Change button text to indicate saving action
            isEditEnabled = true
        } else {
            // Apply changes and switch back to view mode
            userName.text = inputName.text
            userEmail.text = inputEmail.text
            userHeight.text = inputHeight.text
            userWeight.text = inputWeight.text

            saveData()

            userName.visibility = View.VISIBLE
            userEmail.visibility = View.VISIBLE
            userHeight.visibility = View.VISIBLE
            userWeight.visibility = View.VISIBLE

            inputName.visibility = View.GONE
            inputEmail.visibility = View.GONE
            inputHeight.visibility = View.GONE
            inputWeight.visibility = View.GONE

            editProfile.text = getString(R.string.editProfile) // Change button text back to edit
            isEditEnabled = false
        }
    }
}
