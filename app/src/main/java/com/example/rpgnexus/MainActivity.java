package com.example.rpgnexus;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.rpgnexus.databinding.ActivityMainBinding;
import com.example.rpgnexus.fragments.AdminFragment;
import com.example.rpgnexus.fragments.MySheetsFragment;
import com.example.rpgnexus.fragments.ProfileFragment;
import com.example.rpgnexus.fragments.PublicSheetsFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            loadFragment(new PublicSheetsFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_public) {
                selectedFragment = new PublicSheetsFragment();
            } else if (itemId == R.id.nav_my_sheets) {
                selectedFragment = new MySheetsFragment();
            } else if (itemId == R.id.nav_admin) {
                selectedFragment = new AdminFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
