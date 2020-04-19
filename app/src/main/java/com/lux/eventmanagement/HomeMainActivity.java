package com.lux.eventmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lux.eventmanagement.fragments.ProfileUserData;


public class HomeMainActivity extends AppCompatActivity implements Utils.OnActionComplete {

    private AppBarConfiguration mAppBarConfiguration;
    private String[] activityTitles;
    private FirebaseFirestore db;
    NavigationView navigationView;
    public static Utils.OnActionComplete mOnActionComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homemain);
        Toolbar toolbar = findViewById(R.id.toolbar);
        db = FirebaseFirestore.getInstance();
        mOnActionComplete = this;
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","email@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mail From App");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_myractivities,
                R.id.nav_addnewentry, R.id.nav_aboutuspage, R.id.nav_contact_uspage)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(HomeMainActivity.this, R.id.nav_host_homefragment);
        NavigationUI.setupActionBarWithNavController(HomeMainActivity.this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
      /*  navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.nav_home) {

               //   Toast.makeText(HomeMainActivity.this,"fff",Toast.LENGTH_LONG).show();
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(HomeMainActivity.this, "Hello"+menuItem.toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/

        Utils.getUserDetailsFromServer(HomeMainActivity.this,db,mOnActionComplete);

        PackageManager pm = HomeMainActivity.this.getPackageManager();
        int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, HomeMainActivity.this.getPackageName());
        if (!(hasPerm == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(HomeMainActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.LOCATION_HARDWARE},
                    Constants.PERMISSION_CAMERA);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logoutt:
                // do stuff, like showing settings fragment

                Utils.logoutUser(getApplicationContext());
                Intent intent = new Intent(HomeMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

                return true;

        }

        return super.onOptionsItemSelected(item); // important line
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_homefragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void updateUserData(){
       ProfileUserData profileUserData = Utils.userDetailsFromPreference(getApplicationContext());
        View headerview = navigationView.getHeaderView(0);
        if(profileUserData!= null) {
            TextView testname = (TextView) headerview.findViewById(R.id.testname);
            testname.setText(profileUserData.getName() != null ? profileUserData.getName() : "");
            TextView testemail = (TextView) headerview.findViewById(R.id.testemail);
            testemail.setText(profileUserData.getEmail() != null ? profileUserData.getEmail() : "");
        }
    }




    @Override
    public void onAction(boolean actionDone) {
        Log.e("actionDone", "actionDone.");
        if(actionDone){
            updateUserData();
        }
    }


}
