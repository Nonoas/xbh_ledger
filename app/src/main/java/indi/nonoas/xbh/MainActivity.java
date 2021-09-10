package indi.nonoas.xbh;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import indi.nonoas.xbh.fragment.AccListFragment;
import indi.nonoas.xbh.fragment.MyFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm;
    private TextView tvAcc;
    private TextView tvMy;
    private final Fragment frgAcc = AccListFragment.newInstance("", "");
    private final Fragment frgMy = MyFragment.newInstance("", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        tvAcc = findViewById(R.id.tab_acc);
        tvMy = findViewById(R.id.tab_my);

        fm.beginTransaction().add(R.id.frg_container, frgAcc).commit();

        this.setListener();

    }

    /**
     * 设置一系列监听
     */
    private void setListener() {
        tvAcc.setOnClickListener(view -> fm.beginTransaction()
                .replace(R.id.frg_container, frgAcc).commit());

        tvMy.setOnClickListener(view -> fm.beginTransaction()
                .replace(R.id.frg_container, frgMy).commit());
    }

}