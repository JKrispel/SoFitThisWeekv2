package pl.edu.swps.sofitthisweekv2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class CreateExercise : AppCompatActivity() {


    private lateinit var editExerciseName: EditText
    private lateinit var editRepsNumber: EditText
    private lateinit var editSetsNumber: EditText
    private lateinit var editExerciseWeight: EditText
    private lateinit var bAddNewExercise: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exercise)


        editExerciseName = findViewById(R.id.editExerciseName)
        editRepsNumber = findViewById(R.id.editRepsNumber)
        editSetsNumber = findViewById(R.id.editSetsNumber)
        editExerciseWeight = findViewById(R.id.editExerciseWeight)


        bAddNewExercise = findViewById(R.id.bAddNewExercise)
        bAddNewExercise.setOnClickListener {

            val name = editExerciseName.text.toString()
            val reps = editRepsNumber.text.toString().toIntOrNull() ?: 0
            val sets = editSetsNumber.text.toString().toIntOrNull() ?: 0
            val weight = editExerciseWeight.text.toString().toFloatOrNull() ?: 0f

            val newExercise = Exercise(name, sets, reps, weight)

            val intent = Intent(this, CreateTraining::class.java)
            intent.putExtra("exercise", newExercise)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                val intent = Intent(this@CreateExercise, CreateTraining::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish() // Finish the current activity
            }
        })
    }
}