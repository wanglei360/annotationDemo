package com.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lib.BindView

class MainActivity : AppCompatActivity() {

    var tv1: TextView? = null
    var tv2: TextView? = null
    var tv3: TextView? = null
    var tv4: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv1 = findViewById(R.id.tv1)
        tv2 = findViewById(R.id.tv2)
        tv3 = findViewById(R.id.tv3)
        tv4 = findViewById(R.id.tv4)
        tv1?.visibility = View.VISIBLE
        tv2?.visibility = View.VISIBLE
        tv3?.visibility = View.VISIBLE
        tv4?.visibility = View.VISIBLE

    }

    /**
     * 反射的
     */
    fun btnClick1(view: View) {
        Intent(this, ReflexActivity::class.java).apply {
            startActivity(this)
        }
    }

    /**
     * java apt
     */
    fun btnClick2(view: View) {
        Intent(this, JavaAnnotationActivity::class.java).apply {
            startActivity(this)
        }
    }

    fun btnClick3(view: View) {
        Intent(this, KotlinAnnotationActivity::class.java).apply {
            startActivity(this)
        }
    }

    fun btnClick4(view: View) {
        Intent(this, KspKotlinAnnotationActivity::class.java).apply {
            startActivity(this)
        }
    }
}