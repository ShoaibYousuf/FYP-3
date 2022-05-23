package com.example.swiftbay

import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import android.os.Bundle
import com.example.swiftbay.R
import android.content.Intent
import android.os.Handler
import android.widget.ImageView
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.OnBoardingActivity
import com.example.swiftbay.helper.dismissDialoog
import com.example.swiftbay.helper.isEmptyField

class SplashScreen : AppCompatActivity() {
    var logo: ImageView? = null
    var lottieAnimationView: LottieAnimationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        logo = findViewById(R.id.splash_logo)
        lottieAnimationView = findViewById(R.id.lottie)
        PrefsHelper.init(this)

        Handler().postDelayed({
            openAct()
        }, 3000)

    }

    fun openAct(){
        val role = PrefsHelper.read(PrefsHelper.ROLE, "")
        if(isEmptyField(role)){
            /*val i = Intent(applicationContext, OnBoardingActivity::class.java)
            startActivity(i)
            finish()*/
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            finish()
        } else if(role.equals("customer")){
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            finish()
        } else if(role.equals("seller")){
            val i = Intent(this,restaurant_home_page::class.java)
            startActivity(i)
            finish()
        }else if(role.equals("rider")){

            val i = Intent(this,RiderMainActivty::class.java)
            startActivity(i)
            finish()
        }

    }
}