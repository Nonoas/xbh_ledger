package indi.nonoas.xbh.fragment.ui.acclist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.databinding.FragmentAccListBinding;

/**
 * 账户列表: {@link Fragment} 的子类
 * Use the {@link AccListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FragmentAccListBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccListFragment() {
    }


    public static AccListFragment newInstance(String param1, String param2) {
        AccListFragment fragment = new AccListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CollapsingToolbarLayout ctBar = requireActivity().findViewById(R.id.collapsing_bar);
        ctBar.setTitle(getString(R.string.menu_acc));
        ctBar.setExpandedTitleTextAppearance(R.style.actionbar_text_expand);
        ctBar.setCollapsedTitleTextAppearance(R.style.actionbar_text_collapse);
        binding = FragmentAccListBinding.inflate(inflater, container, false);

        ListView lvAcc = binding.lvAcc;
        lvAcc.setEmptyView(binding.tvEmpty);

        lvAcc.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 55;
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = new TextView(requireActivity());
                ((TextView) view).setText(String.valueOf(i));
                view.setPadding(10, 10, 10, 10);
                return view;
            }
        });


        return binding.getRoot();
    }
}