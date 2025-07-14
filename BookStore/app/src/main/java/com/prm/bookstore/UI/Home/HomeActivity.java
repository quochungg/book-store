package com.prm.bookstore.UI.Home;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prm.bookstore.R;
import com.prm.bookstore.UI.Home.CartFragment;
import com.prm.bookstore.UI.Home.ChatFragment;
import com.prm.bookstore.UI.Home.MapFragment;
import com.prm.bookstore.UI.Home.HomeFragment;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_cart) {
                String token = getIntent().getStringExtra("token");
                android.util.Log.d("HomeActivity", "Token in HomeActivity: " + token);
                CartFragment cartFragment = new CartFragment();
                Bundle args = new Bundle();
                args.putString("token", token);
                cartFragment.setArguments(args);
                selectedFragment = cartFragment;
            } else if (itemId == R.id.nav_chat) {
                selectedFragment = new ChatFragment();
            } else if (itemId == R.id.nav_map) {
                selectedFragment = new MapFragment();
            } else {
                String token = getIntent().getStringExtra("token");
                HomeFragment homeFragment = new HomeFragment();
                Bundle args = new Bundle();
                args.putString("token", token);
                homeFragment.setArguments(args);
                selectedFragment = homeFragment;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment_container, selectedFragment)
                    .commit();
            return true;
        });
        // Set default fragment
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
}
