package indi.nonoas.xbh;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import indi.nonoas.xbh.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private int snackBarFlag = 10;

    private AppBarConfiguration appBarConfig;
    private ActivityMainBinding binding;

    /**
     * fragment的id数组
     */
    private final int[] frgIds = {R.id.nav_home, R.id.nav_stats, R.id.nav_setting, R.id.nav_acc};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        Toast toast = Toast.makeText(MainActivity.this, null, Toast.LENGTH_SHORT);
        binding.appBarMain.fab.setOnClickListener(view -> {
            if (snackBarFlag > 0) {
                toast.setText("再点" + snackBarFlag + "次可触发事件");
                snackBarFlag--;
            } else {
                toast.setText("行了！别点了，还没开发好呢");
            }
            toast.show();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        // 抽屉导航
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navView = binding.navView;
        navView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.fragment_content);

        appBarConfig = new AppBarConfiguration.Builder(frgIds)
                .setOpenableLayout(drawer).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_content);
        return NavigationUI.navigateUp(navController, appBarConfig)
                || super.onSupportNavigateUp();
    }

}