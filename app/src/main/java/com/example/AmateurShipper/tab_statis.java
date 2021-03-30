package com.example.AmateurShipper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.communication.IOnBarClickedListener;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;
import java.util.Random;

import io.grpc.okhttp.internal.Util;

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
    private HorizontalBarChart chart;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_statis, container, false);

        chart = view.findViewById(R.id.fragment_horizontalbarchart_chart);

        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);
        return view;
    }
    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.invalidate();
    }
    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });
    }
    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < MAX_X_VALUE; i++) {
            float x = i;
            int y = new Random().nextInt(300);
            values.add(new BarEntry(x, y));
        }
        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        return data;
    }

}