package pl.edu.swps.sofitthisweekv2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Exercise(
   val name: String,
   val sets: Int,
   val reps: Int,
   val weight: Float
) : Parcelable