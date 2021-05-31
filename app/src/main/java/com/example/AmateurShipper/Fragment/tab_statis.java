package com.example.AmateurShipper.Fragment;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.example.AmateurShipper.BindDataArray.BindDataToBarChart;
import com.example.AmateurShipper.BindDataArray.BindDataToLineChart;
import com.example.AmateurShipper.Callback.DataStatisticCallback;
import com.example.AmateurShipper.Model.DataStatisticObject;
import com.example.AmateurShipper.R;
import com.example.AmateurShipper.Util.FormatName;
import com.example.AmateurShipper.Util.fecthDataStatistic;
import com.example.AmateurShipper.Util.formatTimeStampToDate;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab_statis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab_statis extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private static final int MAX_X_VALUE = 7;
    private static final int MAX_Y_VALUE = 50;
    private static final int MIN_Y_VALUE = 5;
    private static final String SET_LABEL = "App Downloads";
    private static final String[] DAYS = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    devs.mulham.horizontalcalendar.HorizontalCalendarView horizontalCalendarView;
    BarChart barchat;
    LineChart lineChart;
    TextView filter_barChart, filter_lineChart, soLuong, thuNhap;
    ArrayList<BarEntry> entryArrayList = new ArrayList<>();
    ArrayList<String> labelName = new ArrayList<>();
    ArrayList<DataStatisticObject> listWeekAmount = new ArrayList<>();
    ArrayList<DataStatisticObject> listMonthAmount = new ArrayList<>();
    ArrayList<DataStatisticObject> listYearAmount = new ArrayList<>();


    ArrayList<Entry> lineEntryList = new ArrayList<>();
    ArrayList<String> labelLineName = new ArrayList<>();
    ArrayList<Entry> listWeekCount = new ArrayList<>();
    ArrayList<Entry> listMonthCount = new ArrayList<>();
    ArrayList<Entry> listYearCount = new ArrayList<>();
    ArrayList<Integer> myColors = new ArrayList<>();
    ImageView next, previous, nextLine, previousLine;
    TextView currentStatistic, currentStatisticLine;
    fecthDataStatistic fecthDataStatistic;
    String uId;
    int k = 0,check_b=0, check_l = 0;
    int checkChart = 0;
    long current_timeStamp = System.currentTimeMillis() / 1000;
    ArrayList<DataStatisticObject> mListData = new ArrayList<>();
    ArrayList<DataStatisticObject> mListDataDay = new ArrayList<>();
    formatTimeStampToDate fmDate = new formatTimeStampToDate();
    int currentWeek = fmDate.getCurrentWeek(current_timeStamp);
    int currentMonth = fmDate.getCurrentMonth(current_timeStamp);
    int currenYear = fmDate.getCurrentYear(current_timeStamp);

    int currentWeekLine = fmDate.getCurrentWeek(current_timeStamp);
    int currentMonthLine = fmDate.getCurrentMonth(current_timeStamp);
    int currenYearLine = fmDate.getCurrentYear(current_timeStamp);


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
        view = inflater.inflate(R.layout.fragment_tab_statis, container, false);
        next = view.findViewById(R.id.btn_next);
        previous = view.findViewById(R.id.btn_previous);
        currentStatistic = view.findViewById(R.id.tv_current_statistic);
        nextLine = view.findViewById(R.id.btn_next_line);
        previousLine = view.findViewById(R.id.btn_previous_line);
        currentStatisticLine = view.findViewById(R.id.tv_current_statistic_line);
        barchat = view.findViewById(R.id.barChart);
        lineChart = view.findViewById(R.id.lineChart);
        filter_barChart = view.findViewById(R.id.btn_filter_barchart);
        filter_lineChart = view.findViewById(R.id.btn_filter_linechart);
        soLuong = view.findViewById(R.id.tv_so_luong);
        thuNhap = view.findViewById(R.id.tv_thu_nhap);

        myColors.add(Color.BLACK);
        myColors.add(Color.YELLOW);
        myColors.add(Color.BLUE);
        myColors.add(Color.DKGRAY);
        myColors.add(Color.GREEN);
        myColors.add(Color.GRAY);
        fecthDataStatistic = new fecthDataStatistic(getActivity());
        /* starts before 1 month from now */
        getUid();
        loadDataDay(current_timeStamp);
        //Log.i(TAG, "onCreateView: "+ currenYear);
        filter_barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChart = 1;
                openFilter(v);
            }
        });
        filter_lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChart = 2;
                openFilter(v);
            }
        });

        loadData(currentWeek, 0);
        //Log.i(TAG, "onSuccess: "+ mListData.get(0).getAmount());
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBar();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousBar();
            }
        });

        nextLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextLine();
            }
        });
        previousLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousLine();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCalender();
    }

    private void initCalender() {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        View calView = view.findViewById(R.id.calendarView);
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(getActivity(),calView.getId())
                .range(startDate, endDate)
                .datesNumberOnScreen(5).configure()
                .sizeTopText(10)
                .sizeMiddleText(12)
                .formatTopText("dd/MM")
                .formatMiddleText("EEE")
                .showBottomText(false).end()
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                loadDataDay(date.getTimeInMillis() / 1000);
            }
        });
    }


    public void renderDataBarChart() {
        BarDataSet barDataSet = new BarDataSet(entryArrayList, "Tổng thu nhập");
        barchat.notifyDataSetChanged();
        barDataSet.setColors(Color.CYAN);
        Description des = new Description();
        des.setText("");
        barchat.setDescription(des);
        BarData barData = new BarData(barDataSet);
        barchat.setData(barData);
        barchat.setTouchEnabled(true);
        barchat.resetViewPortOffsets();
        if (labelName.size() > 7) {
            barchat.setVisibleXRange(0, 7);
        }
        YAxis rightAxis = barchat.getAxisRight();
        rightAxis.setEnabled(false);
        XAxis xAxis = barchat.getXAxis();
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
     //   xAxis.setDrawGridLinesBehindData(false);
        //xAxis.setGranularity(float);
        //xAxis.setLabelRotationAngle(270);
        barchat.isDoubleTapToZoomEnabled();
        barchat.setDrawBarShadow(false);
        barchat.setDrawGridBackground(false);
        barchat.setDrawValueAboveBar(false);
        barchat.animateY(1000);
        barchat.setDrawValueAboveBar(true);
        barchat.invalidate();


    }

    public void renderDataLineChart() {
        XAxis xAxis = lineChart.getXAxis();
        //xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setGranularity(1.0f);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelLineName));
        xAxis.setAxisMinimum(0.0f);
        xAxis.setAxisMaximum(7.0f);
        xAxis.setGridLineWidth(0f);
        xAxis.setDrawAxisLine(true);//Drawing axis
        xAxis.setDrawGridLines(true);//Set the line corresponding to each point on the x-axis
        xAxis.setDrawLabels(true);//Draw a label to refer to the corresponding value on the x-axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//Set the display position of the x-axis
        xAxis.setGridLineWidth(0.5f);
        xAxis.setGridColor(Color.parseColor("#9E9E9E"));
        xAxis.isDrawGridLinesEnabled();
        xAxis.setDrawGridLines(true);
        YAxis leftAxis = lineChart.getAxisLeft();

        leftAxis.setAxisMaximum(50f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setSpaceBottom(0);
        leftAxis.setSpaceTop(0);
        xAxis.setCenterAxisLabels(false);

        showLineChart();
    }


    public void showLineChart() {
        LineDataSet lineDataSet;
        lineDataSet = new LineDataSet(lineEntryList, "Tổng số đơn");
        Description des = new Description();
        des.setText("");
        lineChart.setDescription(des);
        lineDataSet.setDrawIcons(false);
        lineDataSet.enableDashedLine(10f, 5f, 0f);
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);
        lineDataSet.setColor(Color.CYAN);
        lineDataSet.setLineWidth(1.0f);
        lineDataSet.setValueTextSize(9f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        lineDataSet.setFormSize(15.f);
        lineDataSet.setFillColor(Color.CYAN);

        ArrayList<ILineDataSet> lineDataSetsArray = new ArrayList<>();
        lineDataSetsArray.add(lineDataSet);
        LineData data = new LineData(lineDataSetsArray);
      //  lineChart.setVisibleXRangeMaximum(7);
       // lineChart.setDragEnabled(true);
       // lineChart.setScaleEnabled(false);
        //lineChart.setDrawGridBackground(false);

        lineChart.moveViewToX(10.0f);
        lineChart.getAxisRight().setEnabled(false);
        if (labelLineName.size() > 7) {
            //lineChart.setVisibleXRangeMaximum(7);
        }
        lineChart.getXAxis().setAxisMinimum(0.0f);
        lineChart.getAxisLeft().setAxisMinimum(0.0f);
        lineChart.fitScreen();
        lineChart.notifyDataSetChanged();
        lineChart.animateY(1500);
        lineChart.setData(data);
        lineChart.invalidate();
        //  }
    }

    public void fecthDataForLineChartWeek() {
        labelLineName.clear();
        lineEntryList.clear();
        for (int i = 0; i < listWeekCount.size(); i++) {
            float dayy = listWeekCount.get(i).getX()-1;
            String day = String.valueOf(listWeekCount.get(i).getX());
            //Toast.makeText(getActivity(), "line entry list" + day, Toast.LENGTH_SHORT).show();
            if (day.equals("0.0")) day = "Mon";
            else if (day.equals("1.0")) day = "Tus";
            else if (day.equals("2.0")) day = "Web";
            else if (day.equals("3.0")) day = "Thu";
            else if (day.equals("4.0")) day = "Fri";
            else if (day.equals("5.0")) day = "Sat";
            else if (day.equals("6.0")) day = "Sun";
            labelLineName.add(day);
            lineEntryList.add(listWeekCount.get(i));
           // Log.i(TAG, "fecthDataForLineChartWeek: " + day);
        }
    }

    public void fecthDataForLineChartMonth() {
        labelLineName.clear();
        lineEntryList.clear();
        for (int i = 0; i < listMonthCount.size(); i++) {
            String day = String.valueOf(listMonthCount.get(i).getX());
            if (day.equals("1.0")) day = "1";
            else if (day.equals("2.0")) day = "2";
            else if (day.equals("3.0")) day = "3";
            else if (day.equals("4.0")) day = "4";
            else if (day.equals("5.0")) day = "5";
            else if (day.equals("6.0")) day = "6";
            else if (day.equals("7.0")) day = "7";
            else if (day.equals("8.0")) day = "8";
            else if (day.equals("9.0")) day = "9";
            else if (day.equals("10.0")) day = "10";
            else if (day.equals("11.0")) day = "11";
            else if (day.equals("12.0")) day = "12";
            else if (day.equals("13.0")) day = "13";
            else if (day.equals("14.0")) day = "14";
            else if (day.equals("15.0")) day = "15";
            else if (day.equals("16.0")) day = "16";
            else if (day.equals("17.0")) day = "17";
            else if (day.equals("18.0")) day = "18";
            else if (day.equals("19.0")) day = "19";
            else if (day.equals("20.0")) day = "20";
            else if (day.equals("21.0")) day = "21";
            else if (day.equals("22.0")) day = "22";
            else if (day.equals("23.0")) day = "23";
            else if (day.equals("24.0")) day = "24";
            else if (day.equals("25.0")) day = "25";
            else if (day.equals("26.0")) day = "26";
            else if (day.equals("27.0")) day = "27";
            else if (day.equals("28.0")) day = "28";
            else if (day.equals("29.0")) day = "29";
            else if (day.equals("30.0")) day = "30";
            else if (day.equals("31.0")) day = "31";
            labelLineName.add(day);
            lineEntryList.add(listMonthCount.get(i));
            Log.i(TAG, "fecthDataForLineChartMonth: " + day + "/" + lineEntryList.size());
        }

    }

    public void fecthDataForLineChartYear() {
        labelLineName.clear();
        lineEntryList.clear();
        for (int i = 0; i < listYearCount.size(); i++) {
            String day = String.valueOf(listYearCount.get(i).getX());
            if (day.equals("1.0")) day = "T1";
            else if (day.equals("2.0")) day = "T2";
            else if (day.equals("3.0")) day = "T3";
            else if (day.equals("4.0")) day = "T4";
            else if (day.equals("5.0")) day = "T5";
            else if (day.equals("6.0")) day = "T6";
            else if (day.equals("7.0")) day = "T7";
            else if (day.equals("3.0")) day = "T8";
            else if (day.equals("4.0")) day = "T9";
            else if (day.equals("5.0")) day = "T10";
            else if (day.equals("6.0")) day = "T11";
            else if (day.equals("7.0")) day = "T12";
            labelLineName.add(day);
            lineEntryList.add(listYearCount.get(i));
        }
    }

    public void fecthDataForBarChartWeek() {
        entryArrayList.clear();
        barchat.clear();
        labelName.clear();
        for (int i = 0; i < listWeekAmount.size(); i++) {
            String day = listWeekAmount.get(i).getDate();
            int amount = Integer.parseInt(listWeekAmount.get(i).getAmount());
            entryArrayList.add(new BarEntry(i, amount));
            labelName.add(day);
            Log.i(TAG, "fecthDataForBarChartDay: " + labelName.get(i));
        }
    }

    public void fecthDataForBarChartYear() {
        entryArrayList.clear();
        barchat.setFitBars(true);
        barchat.clear();
        labelName.clear();
        for (int i = 0; i < listYearAmount.size(); i++) {
            String day = listYearAmount.get(i).getDate();
            int amount = Integer.parseInt(listYearAmount.get(i).getAmount());
            entryArrayList.add(new BarEntry(i, amount));
            labelName.add(day);
        }

    }

    public void fecthDataForBarChartMonth() {
        entryArrayList.clear();
        barchat.setFitBars(true);
        barchat.clear();
        labelName.clear();
        //fillDataMonth();
        for (int i = 0; i < listMonthAmount.size(); i++) {
            String day = listMonthAmount.get(i).getDate();
            int amount = Integer.parseInt(listMonthAmount.get(i).getAmount());
            entryArrayList.add(new BarEntry(i, amount));
            labelName.add(day);
            Log.i(TAG, "fecthDataForBarChartMonth: " + labelName.get(i));
        }

    }

    public void previousBar() {
        Log.i(TAG, "previousBar:"+check_b);
        if (check_b == 0) {
            if (currentWeek > 1) {
                --currentWeek;
                currentStatistic.setText("Tuần " + currentWeek);
                loadData(currentWeek, 1);
            }
        } else if (check_b == 1) {
            if (currentMonth > 1) {
                --currentMonth;
                currentStatistic.setText("Tháng " + currentMonth);
                loadData(currentMonth, 1);
            }
        } else if (check_b == 2) {
            if (currenYear > 1) {
                --currenYear;
                currentStatistic.setText("Năm " + currenYear);
                loadData(currenYear, 1);
            }
        }
    }

    public void nextBar() {
        if (check_b == 0) {
            int crtWeek = fmDate.getCurrentWeek(current_timeStamp);
            if (currentWeek < crtWeek) {
                ++currentWeek;
                if (currentWeek == crtWeek) {
                    currentStatistic.setText("Tuần này");
                } else currentStatistic.setText("Tuần " + currentWeek);
                loadData(currentWeek, 1);
            }
        } else if (check_b == 1) {
            int crtMonth = fmDate.getCurrentMonth(current_timeStamp);
            if (currentMonth < crtMonth) {
                ++currentMonth;
                if (currentMonth == crtMonth) {
                    currentStatistic.setText("Tháng này");
                } else currentStatistic.setText("Tháng " + currentMonth);
                loadData(currentMonth, 1);
            }
        } else if (check_b == 2) {
            int crtYear = fmDate.getCurrentYear(current_timeStamp);
            if (currenYear < crtYear) {
                ++currenYear;
                if (currenYear == crtYear) {
                    currentStatistic.setText("Năm này");
                } else currentStatistic.setText("Năm " + currenYear);
                loadData(currenYear, 1);
            }
        }
    }

    public void nextLine() {
        if (check_l == 0) {
            int crtWeek = fmDate.getCurrentWeek(current_timeStamp);
            if (currentWeekLine < crtWeek) {
                ++currentWeekLine;
                if (currentWeekLine == crtWeek) {
                    currentStatisticLine.setText("Tuần này");
                } else currentStatisticLine.setText("Tuần " + currentWeekLine);
                loadData(currentWeekLine, 2);
            }
        } else if (check_l == 1) {
            int crtMonth = fmDate.getCurrentMonth(current_timeStamp);
            if (currentMonthLine < crtMonth) {
                ++currentMonthLine;
                if (currentMonthLine == crtMonth) {
                    currentStatisticLine.setText("Tháng này");
                } else currentStatisticLine.setText("Tháng " + currentMonthLine);
                loadData(currentMonthLine, 2);
            }
        }else if (check_l==2){
            int crtYear = fmDate.getCurrentYear(current_timeStamp);
            if (currenYearLine < crtYear){
                ++currenYearLine;
                if (currenYearLine == crtYear){
                    currentStatisticLine.setText("Năm này");
                }else currentStatisticLine.setText("Năm " + currenYearLine);
                loadData(currenYearLine,2);
            }
        }
    }
    public void previousLine(){
        if (check_l==0){
            if (currentWeekLine > 1){
                --currentWeekLine;
                currentStatisticLine.setText("Tuần " + currentWeekLine);
                loadData(currentWeekLine,2);
            }
        }else if (check_l==1){
            if (currentMonthLine > 1){
                --currentMonthLine;
                currentStatisticLine.setText("Tháng " + currentMonthLine);
                loadData(currentMonthLine,2);
            }
        }else if (check_l==2){
            if (currenYearLine > 1){
                --currenYearLine;
                currentStatisticLine.setText("Năm " + currenYearLine);
                loadData(currenYearLine,2);
            }
        }
    }


    public void loadDataDay(long filter){
        FormatName fmn = new FormatName();
        String fill  = fmDate.convertDayMonthYear(filter);
        fecthDataStatistic.fecthData(new DataStatisticCallback() {
            @Override
            public void onSuccess(ArrayList<DataStatisticObject> lists) {
               // Log.i(TAG, "onSIZE: "+ lists.size());
                int count=0;
                long amount=0;
                for (int i = 0; i< lists.size();i++) {
                    if (fmDate.convertDayMonthYear(Long.parseLong(lists.get(i).getDate())).equals(fill)) {
                         amount += Long.parseLong(lists.get(i).getAmount().replaceAll("\\s",""));
                         count++;
                    }
                }
                soLuong.setText(count+"");
                thuNhap.setText(fmn.formatMoney(amount));
            }

            @Override
            public void onError(String message) {

            }
        },uId);
    }

    public void loadData(int filter, int type){
        mListData.clear();
        barchat.fitScreen();
        barchat.clear();
        barchat.invalidate();
        fecthDataStatistic.fecthData(new DataStatisticCallback() {
            @Override
            public void onSuccess(ArrayList<DataStatisticObject> lists) {
                if (k== 0){
                    for (int i = 0; i< lists.size();i++){
                        if (fmDate.getCurrentWeek(Long.parseLong(lists.get(i).getDate()))==filter){
                            String dayofweek = fmDate.convertToDayOfWeek(Long.parseLong(lists.get(i).getDate()));
                            mListData.add(new DataStatisticObject(dayofweek,lists.get(i).getAmount()));
                        }
                    }
                    if (type==1){
                        BindDataToBarChart mbind = new BindDataToBarChart();
                        mbind.totalAmountDayWeek(mListData,listWeekAmount);
                        fecthDataForBarChartWeek();
                    }else if (type==2){
                        BindDataToLineChart lbind = new BindDataToLineChart();
                        lbind.totalCountDayWeek(mListData,listWeekCount);
                        fecthDataForLineChartWeek();

                    }else{
                        BindDataToBarChart mbind = new BindDataToBarChart();
                        mbind.totalAmountDayWeek(mListData,listWeekAmount);
                        fecthDataForBarChartWeek();

                        BindDataToLineChart lbind = new BindDataToLineChart();
                        lbind.totalCountDayWeek(mListData,listWeekCount);
                        fecthDataForLineChartWeek();
                    }
                }else if(k==1){
                    for (int i = 0; i< lists.size();i++){
                        if (fmDate.getCurrentMonth(Long.parseLong(lists.get(i).getDate()))==filter){
                            String monthofyear = fmDate.convertDayMonth(Long.parseLong(lists.get(i).getDate()));
                            mListData.add(new DataStatisticObject(monthofyear,lists.get(i).getAmount()));
                        }
                    }

                    if (type==1){
                        BindDataToBarChart mbind = new BindDataToBarChart();
                        mbind.totalMonth(mListData,listMonthAmount);
                        fecthDataForBarChartMonth();
                    }else if (type==2){
                        BindDataToLineChart lbind = new BindDataToLineChart();
                        lbind.totalCountDayMonth(mListData,listMonthCount);
                        fecthDataForLineChartMonth();

                    }else{
                        BindDataToBarChart mbind = new BindDataToBarChart();
                        mbind.totalMonth(mListData,listMonthAmount);
                        fecthDataForBarChartMonth();

                        BindDataToLineChart lbind = new BindDataToLineChart();
                        lbind.totalCountDayMonth(mListData,listMonthCount);
                        fecthDataForLineChartMonth();
                    }

                }else if(k==2){
                    for (int i = 0; i< lists.size();i++){
                        if (fmDate.getCurrentYear(Long.parseLong(lists.get(i).getDate()))==filter){
                            String dayofmonth = fmDate.convertToMonth(Long.parseLong(lists.get(i).getDate()));
                            mListData.add(new DataStatisticObject(dayofmonth,lists.get(i).getAmount()));
                        }
                    }

                    if (type==1){
                        BindDataToBarChart mbind = new BindDataToBarChart();
                        mbind.totalAmountMonthYear(mListData,listYearAmount);
                        fecthDataForBarChartYear();
                    }else if (type==2){
                        BindDataToLineChart lbind = new BindDataToLineChart();
                        lbind.totalCountMonthYear(mListData,listYearCount);
                        fecthDataForLineChartYear();

                    }else{
                        BindDataToBarChart mbind = new BindDataToBarChart();
                        mbind.totalAmountMonthYear(mListData,listYearAmount);
                        fecthDataForBarChartYear();

                        BindDataToLineChart lbind = new BindDataToLineChart();
                        lbind.totalCountMonthYear(mListData,listYearCount);
                        fecthDataForLineChartYear();
                    }
                }
                renderDataBarChart();
                renderDataLineChart();
               // Log.i(TAG, "mListWeek: "+mListWeek.size() +"/"+ mListWeek.get(0).getDate()+"/"+fmDate.getCurrentMonth(Long.parseLong(lists.get(0).getDate())));
               // showw.setText(mListData.size() +"/"+ mListData.get(0).getDate());
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

    public void openFilter(View v){
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.filter_menu);
        popupMenu.show();
    }

    public class MyFormatter implements IValueFormatter {

        String[] text;

        public MyFormatter(String[] text) {
            this.text = text;
        }



        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return null;
        }
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_week:
                k=0;
                if (checkChart==1){
                    check_b=0;
                    filter_barChart.setText("Tuần");
                    currentStatistic.setText("Tuần này");
                    loadData(fmDate.getCurrentWeek(current_timeStamp),1);
                    currentWeek = fmDate.getCurrentWeek(current_timeStamp);
                    barchat.fitScreen();
                }else{
                    check_l=0;
                    filter_lineChart.setText("Tuần");
                    currentStatisticLine.setText("Tuần này");
                    loadData(fmDate.getCurrentWeek(current_timeStamp),2);
                    currentWeekLine = fmDate.getCurrentWeek(current_timeStamp);
                    barchat.fitScreen();
                }
                return true;
            case R.id.item_month:
                k=1;
                if (checkChart==1){
                    check_b=1;
                    filter_barChart.setText("Tháng");
                    currentStatistic.setText("Tháng này");
                    loadData(fmDate.getCurrentMonth(current_timeStamp),1);
                    currentMonth = fmDate.getCurrentMonth(current_timeStamp);
                    barchat.fitScreen();
//                    barchat.setVisibleXRangeMaximum(7);
                }else{
                    check_l=1;
                    filter_lineChart.setText("Tháng");
                    currentStatisticLine.setText("Tháng này");
                    loadData(fmDate.getCurrentMonth(current_timeStamp),2);
                    currentMonthLine = fmDate.getCurrentMonth(current_timeStamp);
                    barchat.fitScreen();
//                    barchat.setVisibleXRangeMaximum(7);
                }
                return true;
            case R.id.item_year:
                k=2;
                if(checkChart==1){
                    check_b=2;
                    filter_barChart.setText("Năm");
                    currentStatistic.setText("Năm này");
                    loadData(fmDate.getCurrentYear(current_timeStamp),1);
                    currenYear = fmDate.getCurrentYear(current_timeStamp);
                    barchat.fitScreen();
//                    barchat.setVisibleXRangeMaximum(7);
                }else{
                    check_l=2;
                    filter_lineChart.setText("Năm");
                    currentStatisticLine.setText("Năm này");
                    loadData(fmDate.getCurrentYear(current_timeStamp),2);
                    currenYearLine = fmDate.getCurrentYear(current_timeStamp);
                    barchat.fitScreen();
//                    barchat.setVisibleXRangeMaximum(7);
                }
                return true;
            default: return false;
        }
    }
}