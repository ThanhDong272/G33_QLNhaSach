package com.qat.android.quanlynhasach.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.admin_fragment.AccountListFragment;
import com.qat.android.quanlynhasach.admin_fragment.CategoryFragment;
import com.qat.android.quanlynhasach.admin_fragment.NewOrdersFragment;
import com.qat.android.quanlynhasach.user.LoginActivity;
import com.qat.android.quanlynhasach.admin_fragment.HomeAdminFragment;

import io.paperdb.Paper;

public class MainAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Open home fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeAdminFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeAdminFragment())
                        .commit();
                break;

            case R.id.nav_category:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new CategoryFragment())
                        .commit();
                break;

            case R.id.nav_new_orders:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new NewOrdersFragment())
                        .commit();
                break;

            case R.id.nav_account_list:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AccountListFragment())
                        .commit();
                break;

            case R.id.nav_logout:
                Paper.book().destroy();
                Intent intent = new Intent(MainAdminActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
