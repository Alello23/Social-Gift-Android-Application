package com.example.practica2.Menu;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.practica2.Menu.Account.*;
import com.example.practica2.Menu.Chats.ChatFragment;
import com.example.practica2.Menu.Home.HomeFragment;
import com.example.practica2.Menu.WishList.NewWishlistFragment;
import com.example.practica2.Menu.WishList.WishListFragment;
import com.example.practica2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Menu extends AppCompatActivity {
    RequestQueue requestQueue;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        saveToSharedPrefs(getIntent().getStringExtra("User"));

        requestQueue = Volley.newRequestQueue(Menu.this);
        bottomNavigationView = findViewById(R.id.ME_bottomNavigationView);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.ME_fragmentContainerView);

        if (fragment == null){
            fragment = new HomeFragment();
            fm.beginTransaction().add(R.id.ME_fragmentContainerView,fragment).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home){
                openFragment(new HomeFragment());
                return true;
            }
            if (item.getItemId() == R.id.navigation_chat){
                openFragment(new ChatFragment(requestQueue));
                return true;
            }
            if (item.getItemId() == R.id.navigation_wishlist){
//                openFragment(new WishListFragment());
                openFragment(new NewWishlistFragment());
                return true;
            }
            if (item.getItemId() == R.id.navigation_account){
                openFragment(new AccountFragment(requestQueue));
                return true;
            }
            return false;
        });

        openFragment(new HomeFragment());
    }
    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ME_fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
    public void saveToSharedPrefs(String token) {
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        prefsEditor.putString("token", token);
        prefsEditor.apply();
    }

    @Override
    public void onBackPressed() {
    }
}
