package com.example.swiftbay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.swiftbay.databinding.ActivityMainBinding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class BlankActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_activity);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.order_now:
                    replaceFragment(new OrderNowFragment());
                    break;
                case R.id.schedule_order:
                    replaceFragment(new SecondFragment());
                    break;
                case R.id.pickup_order:
                    replaceFragment(new ThirdFragment());
                    break;
                case R.id.login_signup_page:
                    replaceFragment(new LoginTabFragment());
                    break;
            }
            return true;
        });
    }


        private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
        }}