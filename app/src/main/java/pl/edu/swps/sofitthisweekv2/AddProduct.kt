package pl.edu.swps.sofitthisweekv2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddProduct : AppCompatActivity() {

    private lateinit var editProductWeight: EditText
    private lateinit var editCalories: EditText
    private lateinit var editProtein: EditText
    private lateinit var editFats: EditText
    private lateinit var editCarbs: EditText
    private lateinit var bAddNewProduct: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        editProductWeight = findViewById(R.id.editProductWeight)
        editCalories = findViewById(R.id.editProductCalories)
        editProtein = findViewById(R.id.editProductProtein)
        editFats = findViewById(R.id.editProductFats)
        editCarbs = findViewById(R.id.editProductCarbs)


        bAddNewProduct = findViewById(R.id.bAddNewProduct)
        bAddNewProduct.setOnClickListener {

            val productWeight = editProductWeight.text.toString().toFloatOrNull() ?: 0f
            val caloriesPer100g = editCalories.text.toString().toFloatOrNull() ?: 0f
            val proteinPer100g = editProtein.text.toString().toFloatOrNull() ?: 0f
            val fatsPer100g = editFats.text.toString().toFloatOrNull() ?: 0f
            val carbsPer100g = editCarbs.text.toString().toFloatOrNull() ?: 0f

            val actualCalories = (caloriesPer100g * productWeight) / 100
            val actualProtein = (proteinPer100g * productWeight) / 100
            val actualFats = (fatsPer100g * productWeight) / 100
            val actualCarbs = (carbsPer100g * productWeight) / 100

            val intent = Intent(this, Nutrition::class.java)
            intent.putExtra("calories", actualCalories.toInt())
            intent.putExtra("protein", actualProtein.toInt())
            intent.putExtra("fats", actualFats.toInt())
            intent.putExtra("carbs", actualCarbs.toInt())
            startActivity(intent)
        }
    }
}