package indi.nonoas.xbh;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import indi.nonoas.xbh.activity.SignUpActivity;
import indi.nonoas.xbh.common.AppStore;
import indi.nonoas.xbh.databinding.ActivityMainBinding;
import indi.nonoas.xbh.greendao.DaoSession;
import indi.nonoas.xbh.greendao.UserDao;
import indi.nonoas.xbh.pojo.User;
import indi.nonoas.xbh.utils.GreenDaoUtil;

public class MainActivity extends AppCompatActivity {

    private int statusBarHeight = -1;

    private AppBarConfiguration appBarConfig;
    private ActivityMainBinding binding;

    /**
     * fragment的id数组
     */
    private final int[] frgIds = {R.id.nav_home, R.id.nav_setting};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断是否登录
        if (!isLogin()) {
            System.out.println("未登录");
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Window window = getWindow();
        AppStore.setCurrWindow(window);

        WindowInsetsControllerCompat compat = new WindowInsetsControllerCompat(window, window.getDecorView());
        AppStore.setWinInsetCtrlCompat(compat);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        window.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        binding.appBarMain.appbar.setPadding(0, statusBarHeight, 0, 0);

    }

	/**
	 * 判断是否已经登录
	 *
	 * @return 已登录: true
	 */
	private boolean isLogin() {
		DaoSession session = GreenDaoUtil.getDaoSession(this);
		UserDao userDao = session.getUserDao();
		List<User> list = userDao.queryBuilder().list();
		if (list == null || list.isEmpty()) {
			return false;
		}
		AppStore.setUser(list.get(0));
		return true;
	}

    @Override
    protected void onStart() {
        super.onStart();
        // 抽屉导航
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navView = binding.navView;

        TextView tfUser = navView.getHeaderView(0).findViewById(R.id.tf_user);
        User user = AppStore.getUser();
        tfUser.setText(user != null ? user.getName() : "未登录");

        navView.setPadding(0, statusBarHeight, 0, 0);

        NavController navController = Navigation.findNavController(this, R.id.fragment_content);

        appBarConfig = new AppBarConfiguration
                .Builder(frgIds)
                .setOpenableLayout(drawer)
                .build();

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