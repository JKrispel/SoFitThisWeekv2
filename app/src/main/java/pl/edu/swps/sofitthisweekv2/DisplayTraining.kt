package pl.edu.swps.sofitthisweekv2

import TrainingPlan
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DisplayTraining : AppCompatActivity() {

    private lateinit var linearLayoutTrainingPlan: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_training)

        linearLayoutTrainingPlan = findViewById(R.id.linearLayoutTrainingPlan)

        val trainingPlan: TrainingPlan? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("training_plan", TrainingPlan::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("training_plan")
        }

        trainingPlan?.exercises?.forEach {
            addExerciseTextView(it)
        }
    }

    private fun addExerciseTextView(exercise: Exercise) {
        val exerciseDetails = """
            ${exercise.name}
            Serie: ${exercise.sets}
            Powtórzenia: ${exercise.reps}
            Obciążenie: ${exercise.weight}kg
        """.trimIndent()

        val textView = TextView(this).apply {
            text = exerciseDetails
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }

        linearLayoutTrainingPlan.addView(textView)
    }
}