// TrainingPlan.kt
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pl.edu.swps.sofitthisweekv2.Exercise

@Parcelize
data class TrainingPlan(
    val name: String,
    val exercises: List<Exercise>
) : Parcelable
