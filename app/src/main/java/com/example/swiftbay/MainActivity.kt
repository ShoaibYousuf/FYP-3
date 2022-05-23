package com.example.swiftbay

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.fazalmapbox.utils.PrefsHelper
import com.example.swiftbay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.order_now -> replaceFragment(OrderNowFragment())
                R.id.login_signup_page -> {
                    val isLogin = PrefsHelper.read(PrefsHelper.LOGIN, false)

                        val i = Intent(this,LoginActivity::class.java)
                        startActivityForResult(i, 1)


                }
                R.id.schedule_order -> {
                    val isLogin = PrefsHelper.read(PrefsHelper.LOGIN, false)
                    if(isLogin){
                        replaceFragment(SecondFragment())
                    }else{
                        val i = Intent(this,LoginActivity::class.java)
                        startActivityForResult(i, 1)
                    }

                }
                R.id.pickup_order -> {
                    val isLogin = PrefsHelper.read(PrefsHelper.LOGIN, false)
                    if(isLogin){
                        replaceFragment(ThirdFragment())
                    }else{
                        val i = Intent(this,LoginActivity::class.java)
                        startActivityForResult(i, 2)
                    }
                }
            }
            true
        }
        replaceFragment(OrderNowFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {

        callBack()
    }

    fun callBack() {
        val builder1: AlertDialog.Builder = AlertDialog.Builder(this)
        builder1.setMessage("Are you sure! you want to exit?")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "Yes",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                finishAffinity()
            })

        builder1.setNeutralButton(
            "LogOut",
            DialogInterface.OnClickListener { dialog, id ->
                PrefsHelper.clear(PrefsHelper.TOKEN)
                PrefsHelper.clear(PrefsHelper.ROLE)
                PrefsHelper.clear(PrefsHelper.LOGIN)
                dialog.cancel()
                finishAffinity()
            })

        builder1.setNegativeButton(
            "No",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11: AlertDialog = builder1.create()
        alert11.show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val isLogin = PrefsHelper.read(PrefsHelper.LOGIN, false)
                if(isLogin){
                    replaceFragment(SecondFragment())
                }
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                val isLogin = PrefsHelper.read(PrefsHelper.LOGIN, false)
                if(isLogin){
                    replaceFragment(ThirdFragment())
                }
            }
        }

    }
}
