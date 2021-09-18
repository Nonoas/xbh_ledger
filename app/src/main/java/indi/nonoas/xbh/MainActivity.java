package indi.nonoas.xbh;

import android.content.Intent;
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

import indi.nonoas.xbh.activity.SignUpActivity;
import indi.nonoas.xbh.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private int snackBarFlag = 10;

    private AppBarConfiguration appBarConfig;
    private ActivityMainBinding binding;

    Toast toast;

    /**
     * fragment的id数组
     */
    private final int[] frgIds = {R.id.nav_home, R.id.nav_setting};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        判断是否登录
        if (!isLogin()) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.fab.setOnClickListener(view -> {
            if (toast != null) {
                toast.cancel();
            }
            if (snackBarFlag > 0) {
                toast = Toast.makeText(MainActivity.this, "再点" + snackBarFlag + "次可触发事件", Toast.LENGTH_SHORT);
                snackBarFlag--;
            } else {
                toast = Toast.makeText(MainActivity.this, "行啦行啦！别点了！还没开发呢！", Toast.LENGTH_SHORT);
            }
            toast.show();
        });


    }

    private boolean isLogin() {
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 抽屉导航
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navView = binding.navView;

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