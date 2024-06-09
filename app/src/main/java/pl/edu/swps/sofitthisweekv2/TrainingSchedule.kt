package pl.edu.swps.sofitthisweekv2

// TrainingSchedule.kt
data class TrainingSchedule(
    val startDate: Long,
    val endDate: Long,
    val daysOfWeek: List<Int>,
    val trainingPlanId: String
)
