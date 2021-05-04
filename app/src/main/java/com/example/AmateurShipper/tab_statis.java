package com.example.AmateurShipper;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.AmateurShipper.Callback.DataStatisticCallback;
import com.example.AmateurShipper.Model.DataStatisticObject;
import com.example.AmateurShipper.Util.fecthDataStatistic;
import com.example.AmateurShipper.Util.formatTimeStampToDate;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab_statis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab_statis extends Fragment {
    private static final int MAX_X_VALUE = 7;
    private static final int MAX_Y_VALUE = 50;
    private static final int MIN_Y_VALUE = 5;
    private static final String SET_LABEL = "App Downloads";
    private static final String[] DAYS = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    BarChart barchat;
    ArrayList<BarEntry> entryArrayList;
    ArrayList<String> labelName;
    ArrayList<DataStatisticObject> listWeekAmount = new ArrayList<>();

    Button nexxt,preevious;
    TextView showw;
    fecthDataStatistic fecthDataStatistic;
    String uId;
    int k;
    ArrayList<DataStatisticObject> mListData = new ArrayList<>();
    ArrayList<DataStatisticObject> mListWeek = new ArrayList<>();
    formatTimeStampToDate fmDate = new formatTimeStampToDate();
    int currentWeek  = fmDate.getCurrentWeek(System.currentTimeMillis()/1000);
    int currentMonth = fmDate.getCurrentMonth(System.currentTimeMillis()/1000);


    public tab_statis() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab_statis.
     */
    // TODO: Rename and change types and number of parameters
    public static tab_statis newInstance(String param1, String param2) {
        tab_statis fragment = new tab_statis();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_statis, container, false);
      //  nexxt = view.findViewById(R.id.next);
      //  preevious= view.findViewById(R.id.previous);
      //  showw= view.findViewById(R.id.show);
        barchat = view.findViewById(R.id.barChart);
        fecthDataStatistic = new fecthDataStatistic(getActivity());
        getUid();
      //  loadData(currentWeek);

        entryArrayList = new ArrayList<>();
        labelName = new ArrayList<>();
        filldata();
        for (int i = 0 ; i< listWeekAmount.size();i++){
            String day = listWeekAmount.get(i).getDate();
            int amount = Integer.parseInt(listWeekAmount.get(i).getAmount());
            entryArrayList.add(new BarEntry(i,amount));
            labelName.add(day);
        }

        BarDataSet barDataSet = new BarDataSet(entryArrayList,"Amount Week");
        barDataSet.setColors(Color.CYAN);
        Description des = new Description();
        des.setText("");
        barchat.setDescription(des);
        BarData barData = new BarData(barDataSet);
        barchat.setData(barData);
        YAxis rightAxis = barchat.getAxisRight();
        rightAxis.setEnabled(false);
        XAxis xAxis = barchat.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawGridLinesBehindData(false);
        //xAxis.setGranularity(float);
        xAxis.setLabelCount(labelName.size());
        //xAxis.setLabelRotationAngle(270);
        barchat.isDoubleTapToZoomEnabled();
        barchat.setDrawBarShadow(false);
        barchat.setDrawGridBackground(false);
        barchat.setDrawValueAboveBar(false);
        barchat.animateY(2000);
        barchat.invalidate();

        //Log.i(TAG, "onSuccess: "+ mListData.get(0).getAmount());
//        nexxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nextWeek();
//            }
//        });
//        preevious.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                previuosWeek();
//            }
//        });
        return view;
    }

    public void filldata(){
        listWeekAmount.clear();
        listWeekAmount.add(new DataStatisticObject("Mon","200000"));
        listWeekAmount.add(new DataStatisticObject("Tus","150000"));
        listWeekAmount.add(new DataStatisticObject("Web","175000"));
        listWeekAmount.add(new DataStatisticObject("Thu","300000"));
        listWeekAmount.add(new DataStatisticObject("Fri","100000"));
        listWeekAmount.add(new DataStatisticObject("Sta","400000"));
        listWeekAmount.add(new DataStatisticObject("Sun","140000"));

    }

    public void previuosWeek(){
        --currentWeek;
        loadData(currentWeek);
    }
    public void nextWeek(){
        ++currentWeek;
        loadData(currentWeek);
    }
    public void loadData(int filter){
        mListData.clear();
        fecthDataStatistic.fecthData(new DataStatisticCallback() {
            @Override
            public void onSuccess(ArrayList<DataStatisticObject> lists) {
                switch ( k ){
                    case 1: for (int i = 0; i< lists.size();i++){
                        if (fmDate.getCurrentWeek(Long.parseLong(lists.get(i).getDate()))==filter){
                            mListData.add(lists.get(i));
                        }
                    } break;

                    case 2: for (int i = 0; i< lists.size();i++){
                        if (fmDate.getCurrentMonth(Long.parseLong(lists.get(i).getDate()))==filter){
                            mListData.add(lists.get(i));
                        }
                    } break;
                }
               // Log.i(TAG, "mListWeek: "+mListWeek.size() +"/"+ mListWeek.get(0).getDate()+"/"+fmDate.getCurrentMonth(Long.parseLong(lists.get(0).getDate())));
                showw.setText(mListData.size() +"/"+ mListData.get(0).getDate());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Loi rooi", Toast.LENGTH_LONG).show();
            }
        },uId);
    }
    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uId = user.getUid();
    }
}