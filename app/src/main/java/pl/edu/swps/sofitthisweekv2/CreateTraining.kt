package pl.edu.swps.sofitthisweekv2

import android.content.Context
import android.os.Build
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CreateTraining : AppCompatActivity() {

    private lateinit var bAddExercise: Button
    private lateinit var bMergeIntoTraining: Button
    private lateinit var linearLayoutContainer: LinearLayout
    private val exercises = mutableListOf<Exercise>()
    private val sharedPreferences by lazy { getSharedPreferences("exercises_prefs", Context.MODE_PRIVATE) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_training)


        linearLayoutContainer = findViewById(R.id.linearLayoutExercises)
        loadExercises()

        val exercise: Exercise? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("exercise", Exercise::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("exercise")
        }

        exercise?.let {
            addExercise(it)
        }


        bAddExercise = findViewById(R.id.bAddExercise)
        bAddExercise.setOnClickListener {
            val intent = Intent(this, CreateExercise::class.java)
            startActivity(intent)
        }

        bMergeIntoTraining = findViewById(R.id.bMergeIntoTraining)
        bMergeIntoTraining.setOnClickListener {



        }

    }


    override fun onPause() {
        super.onPause()
        saveExercises()
    }

    private fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
        addExerciseTextView(exercise)
    }

    private fun addExerciseTextView(exercise: Exercise) {
        val exerciseDetails = """
            ${exercise.name}
            Serie: ${exercise.sets}
            Powtórzenia: ${exercise.reps}
            Obciążenie: ${exercise.weight}
        """.trimIndent()

        val textView = TextView(this).apply {
            text = exerciseDetails
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }

        linearLayoutContainer.addView(textView)
    }

    private fun saveExercises() {
        val gson = Gson()
        val json = gson.toJson(exercises)
        sharedPreferences.edit().putString("exercises", json).apply()
    }

    private fun loadExercises() {
        val gson = Gson()
        val json = sharedPreferences.getString("exercises", null)
        if (json != null) {
            val type = object : TypeToken<List<Exercise>>() {}.type
            val savedExercises: List<Exercise> = gson.fromJson(json, type)
            exercises.addAll(savedExercises)
            exercises.forEach { addExerciseTextView(it) }
        }
    }
}