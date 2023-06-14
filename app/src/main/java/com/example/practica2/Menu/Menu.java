package com.example.practica2.Menu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.practica2.Menu.Home.HomeFragment;
import com.example.practica2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Menu extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainerView);

        if (fragment == null){
            fragment = new HomeFragment();
            fm.beginTransaction().add(R.id.fragmentContainerView,fragment).commit();
        }

//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    openFragment(new HomeFragment());
//                    return true;
//                case R.id.navigation_chat:
//                    openFragment(new ChatFragment());
//                    return true;
//                case R.id.navigation_wishlist:
//                    openFragment(new WishListFragment());
//                    return true;
//                case R.id.navigation_account:
//                    openFragment(new AccountFragment());
//                    return true;
//            }
//            return false;
//        });

        // Muestra el fragmento de inicio por defecto
//        openFragment(new HomeFragment());
    }
//    private void openFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
//        fragmentTransaction.commit();
//    }
}
