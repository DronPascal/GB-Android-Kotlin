package com.test.weather

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var data: TestData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // создайте data class с двумя свойствами, выведите их на экран приложения;
        dataTask()

        // создайте Object, в Object вызывайте copy и выводите значения скопированного класса на экран;
        objectTask()

        // выводите значения из разных циклов в консоль, используя примеры из методических материалов.
        cyclesTask()
    }

    fun dataTask() {
        data = TestData(10, "Hi")
        Toast.makeText(this, "message: ${data.property1}, ${data.property2}", Toast.LENGTH_SHORT)
            .show()
    }

    fun objectTask() {
        val listener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val data = this@MainActivity.data.copy(property2 = "Bye")
                Toast.makeText(
                    this@MainActivity,
                    "message: ${data.property1}, ${data.property2}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        button = findViewById(R.id.button)
        button.setOnClickListener(listener)
    }

    fun cyclesTask() {
        for (i in 1..3) {
            println("First")
        }

        for (i in 4 downTo 1 step 2) {
            println("Second")
        }

        val list = ArrayList<String>(0)
        val arr = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        list.addAll(arr)
        for (i in 0 until list.size) {
            println(list[i])
        }
    }
}