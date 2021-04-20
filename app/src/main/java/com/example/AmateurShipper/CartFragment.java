package com.example.AmateurShipper;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab_nhan,tab_chua_nhan,tab_lichsu;
    public PageAdapter pagerAdapter;
   // Button daNhan,dangGiao,lichSu;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart,container,false);
        tabLayout = view.findViewById(R.id.tablayout);
        tab_nhan = view.findViewById(R.id.tab_nhan);
        tab_chua_nhan = view.findViewById(R.id.tab_chua_nhan);
        tab_lichsu = view.findViewById(R.id.tab_lich_su);
        viewPager = view.findViewById(R.id.viewpage);

//        //loadFragment(new tab_nhan());
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.content_cart, new tab_nhan())
//                .commit();
//        onTabSelected();

//        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Đã Nhận", R.color.whiteTextColor);
//        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Đang Giao", R.color.whiteTextColor);
//        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Lịch Sử", R.color.whiteTextColor);
       // topNavigationView.setCurrentItem(0);
        pagerAdapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 ){
                    pagerAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1 ){
                    pagerAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 2 ){
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // Inflate the layout for this fragment
        return view;
    }
//    private void loadFragment(Fragment fragment) {
//        //switching fragment
//        if (fragment != null) {
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.content_cart, fragment)
//                    .commit();
//            //return true;
//        }
//       // return false;
//    }
//
//    public void onTabSelected(){
//        daNhan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tab_nhan tab_nhan1 = new tab_nhan();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_cart, tab_nhan1).commit();
//               // getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_cart, tab_nhan1).commit();
//            }
//        });
//        dangGiao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tab_dang_giao tab_dang_giao1 = new tab_dang_giao();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_cart, tab_dang_giao1).commit();
//            }
//        });
//        lichSu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tab_lich_su tab_lich_su1 = new tab_lich_su();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_cart, tab_lich_su1).commit();
//            }
//        });
//    }
//    @Override
//    public boolean onTabSelected(int position, boolean wasSelected) {
//        if (position == 0) {
//            tab_nhan tab_nhan1 = new tab_nhan();
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_cart, tab_nhan1).commit();
//            return true;
//        } else if (position == 1) {
//            tab_dang_giao tab_dang_giao1 = new tab_dang_giao();
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_cart, tab_dang_giao1).commit();
//            return true;
//        } else if (position == 2) {
//            tab_lich_su tab_lich_su1 = new tab_lich_su();
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_cart, tab_lich_su1).commit();
//            return true;
//        }
//        return false;
//    }
}