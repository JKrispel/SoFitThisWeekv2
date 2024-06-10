package pl.edu.swps.sofitthisweekv2

import TrainingPlan
import android.content.Context
import android.os.Build
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CreateTraining : AppCompatActivity() {

    private lateinit var bAddExercise: Button
    private lateinit var bMergeIntoTraining: Button
    private lateinit var linearLayoutContainer: LinearLayout
    private val exercises = mutableListOf<Exercise>()
    private val sharedPreferences by lazy { getSharedPreferences("training_plans_prefs", Context.MODE_PRIVATE) }


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

            showTrainingNameDialog()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                val intent = Intent(this@CreateTraining, Training::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish() // Finish the current activity
            }
        })
    }


    override fun onPause() {
        super.onPause()
        saveExercises()
    }

    private fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
        addExerciseCheckBox(exercise)
    }


    private fun addExerciseCheckBox(exercise: Exercise) {
        val exerciseDetails = """
            ${exercise.name}
            Serie: ${exercise.sets}
            Powtórzenia: ${exercise.reps}
            Obciążenie: ${exercise.weight}
        """.trimIndent()

        val checkBox = CheckBox(this).apply {
            text = exerciseDetails
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }

        linearLayoutContainer.addView(checkBox)
    }

    private fun showTrainingNameDialog() {
        val editText = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Nazwij swój trening")
            .setView(editText)
            .setPositiveButton("Zapisz") { _, _ ->
                val trainingName = editText.text.toString()
                combineExercises(trainingName)
            }
            .setNegativeButton("Anuluj", null)
            .create()
        dialog.show()
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
            exercises.forEach { addExerciseCheckBox(it) }
        }
    }

    private fun combineExercises(trainingName: String) {
        val selectedExercises = mutableListOf<Exercise>()

        for (i in 0 until linearLayoutContainer.childCount) {
            val checkBox = linearLayoutContainer.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
                selectedExercises.add(exercises[i])
            }
        }

        val trainingPlan = TrainingPlan(trainingName, selectedExercises)
        saveTrainingPlan(trainingPlan)

        val intent = Intent(this, Training::class.java)
        startActivity(intent)
    }

    private fun saveTrainingPlan(trainingPlan: TrainingPlan) {
        val gson = Gson()
        val json = sharedPreferences.getString("training_plans", "[]")
        val type = object : TypeToken<MutableList<TrainingPlan>>() {}.type
        val trainingPlans: MutableList<TrainingPlan> = gson.fromJson(json, type)

        trainingPlans.add(trainingPlan)

        val updatedJson = gson.toJson(trainingPlans)
        sharedPreferences.edit().putString("training_plans", updatedJson).apply()
    }


}