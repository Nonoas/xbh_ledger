package indi.nonoas.xbh;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;
import java.util.List;

import indi.nonoas.xbh.activity.LoginActivity;
import indi.nonoas.xbh.common.AppStore;
import indi.nonoas.xbh.databinding.ActivityMainBinding;
import indi.nonoas.xbh.fragment.acclist.AccListFragment;
import indi.nonoas.xbh.fragment.home.HomeFragment;
import indi.nonoas.xbh.fragment.setting.SettingFragment;
import indi.nonoas.xbh.fragment.stats.StatsFragment;
import indi.nonoas.xbh.greendao.DaoSession;
import indi.nonoas.xbh.greendao.UserDao;
import indi.nonoas.xbh.pojo.User;
import indi.nonoas.xbh.utils.GreenDaoUtil;
import indi.nonoas.xbh.utils.StatusBarUtil;
import indi.nonoas.xbh.view.DrawerAdapter;
import indi.nonoas.xbh.view.DrawerItem;
import indi.nonoas.xbh.view.SimpleItem;
import indi.nonoas.xbh.view.SpaceItem;
import indi.nonoas.xbh.view.toast.CoverableToast;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private AppBarConfiguration appBarConfig;
    private ActivityMainBinding binding;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    private final int POS_HOME = 0;
    private final int POS_ABOUT = 1;
    private final int POS_LOGOUT = 3;

    HomeFragment homeFragment = HomeFragment.newInstance();
    SettingFragment settingFragment = SettingFragment.newInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.setStatusBar(this, StatusBarUtil.StatusBarType.LIGHT);

        // 判断是否登录
        if (!isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.getRoot().findViewById(R.id.user_name);

        Toolbar toolbar = binding.toolbar;

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.8f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_ABOUT),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)
        ));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_HOME);
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray typedArray = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            int id = typedArray.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        typedArray.recycle();
        return icons;
    }


    private DrawerItem createItemFor(int pos) {
        return new SimpleItem(screenIcons[pos], screenTitles[pos])
                .withIconTint(color(R.color.secondary_text_color))
                .withTextTint(color(R.color.primary_text_color))
                .withSelectedIconTint(color(R.color.soft_green))
                .withSelectedTextTint(color(R.color.soft_green));
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
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


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (position == POS_HOME) {
            transaction.replace(R.id.container, homeFragment);
        }

        if (position == POS_ABOUT) {
            transaction.replace(R.id.container, settingFragment);
        }

        if (position == POS_LOGOUT) {
            finish();
        }

        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
    }

}