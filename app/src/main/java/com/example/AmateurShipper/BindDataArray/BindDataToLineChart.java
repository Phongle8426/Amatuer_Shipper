package com.example.AmateurShipper.BindDataArray;

import android.util.Log;

import com.example.AmateurShipper.Model.DataStatisticObject;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import androidx.constraintlayout.motion.utils.Oscillator;

import static android.content.ContentValues.TAG;

public class BindDataToLineChart {

    public BindDataToLineChart() {
    }


    public void totalCountDayWeek(ArrayList<DataStatisticObject> mlist, ArrayList<Entry> mBeforeBind){
        int mon=0,tus=0,wed=0,thu=0,fri=0,sat=0,sun=0;
        for (int i = 0; i<mlist.size();i++){
            if (mlist.get(i).getDate().equals("Th 2"))
                mon++;
            else if(mlist.get(i).getDate().equals("Th 3"))
                tus++;
            else if(mlist.get(i).getDate().equals("Th 4"))
                wed++;
            else if(mlist.get(i).getDate().equals("Th 5"))
                thu++;
            else if(mlist.get(i).getDate().equals("Th 6"))
                fri++;
            else if(mlist.get(i).getDate().equals("Th 7"))
                sat++;
            else if(mlist.get(i).getDate().equals("CN"))
                sun++;
        }
        mBeforeBind.clear();
        mBeforeBind.add(new Entry(1,mon));
        mBeforeBind.add(new Entry(2,tus));
        mBeforeBind.add(new Entry(3,wed));
        mBeforeBind.add(new Entry(4,thu));
        mBeforeBind.add(new Entry(5,fri));
        mBeforeBind.add(new Entry(6,sat));
        mBeforeBind.add(new Entry(7,sun));
    }

    public void totalCountMonthYear(ArrayList<DataStatisticObject> mlist,ArrayList<Entry> mBeforeBind){
        int jan=0,feb=0,mar=0,apr=0,may=0,jun=0,jul=0,aug=0,sep=0,oct=0,nov=0,dec=0;
       // Log.i(Oscillator.TAG, "totalMOnth: "+mlist.get(0).getDate() + "/"+mlist.get(0).getAmount());
        for (int i = 0; i<mlist.size();i++){
            if (mlist.get(i).getDate().equals("01"))
                jan++;
            else if(mlist.get(i).getDate().equals("02"))
                feb++;
            else if(mlist.get(i).getDate().equals("03"))
                mar++;
            else if(mlist.get(i).getDate().equals("04"))
                apr++;
            else if(mlist.get(i).getDate().equals("05"))
                may++;
            else if(mlist.get(i).getDate().equals("06"))
                jun++;
            else if(mlist.get(i).getDate().equals("07"))
                jul++;
            else if(mlist.get(i).getDate().equals("08"))
                aug++;
            else if(mlist.get(i).getDate().equals("09"))
                sep++;
            else if(mlist.get(i).getDate().equals("10"))
                oct++;
            else if(mlist.get(i).getDate().equals("11"))
                nov++;
            else if(mlist.get(i).getDate().equals("12"))
                dec++;
        }
        mBeforeBind.clear();
        mBeforeBind.add(new Entry(1,jan));
        mBeforeBind.add(new Entry(2,feb));
        mBeforeBind.add(new Entry(3,mar));
        mBeforeBind.add(new Entry(4,apr));
        mBeforeBind.add(new Entry(5,may));
        mBeforeBind.add(new Entry(6,jun));
        mBeforeBind.add(new Entry(7,jul));
        mBeforeBind.add(new Entry(8,aug));
        mBeforeBind.add(new Entry(9,sep));
        mBeforeBind.add(new Entry(10,oct));
        mBeforeBind.add(new Entry(11,nov));
        mBeforeBind.add(new Entry(12,dec));
    }

    public void totalCountDayMonth(ArrayList<DataStatisticObject> mlist,ArrayList<Entry> mBeforeBind){
        int day1=0,day2=0,day3=0,day4=0,day5=0,day6=0,day7=0,day8=0,day9=0,day10=0,day11=0,day12=0,
                day13=0,day14=0,day15=0,day16=0,day17=0,day18=0,day19=0,day20=0,day21=0,day22=0,day23=0,day24=0,
                day25=0,day26=0,day27=0,day28=0,day29=0,day30=0,day31=0;
      //  Log.i(Oscillator.TAG, "totalMOnth: "+mlist.get(0).getDate() + "/"+mlist.get(0).getAmount());
        for (int i = 0; i<mlist.size();i++){
            if (mlist.get(i).getDate().equals("01"))
                day1++;
            else if(mlist.get(i).getDate().equals("02"))
                day2++;
            else if(mlist.get(i).getDate().equals("03"))
                day3++;
            else if(mlist.get(i).getDate().equals("04"))
                day4++;
            else if(mlist.get(i).getDate().equals("05"))
                day5++;
            else if(mlist.get(i).getDate().equals("06"))
                day6++;
            else if(mlist.get(i).getDate().equals("07"))
                day7++;
            else if(mlist.get(i).getDate().equals("08"))
                day8++;
            else if(mlist.get(i).getDate().equals("09"))
                day9++;
            else if(mlist.get(i).getDate().equals("10"))
                day10++;
            else if(mlist.get(i).getDate().equals("11"))
                day11++;
            else if(mlist.get(i).getDate().equals("12"))
                day12++;
            if (mlist.get(i).getDate().equals("13"))
                day13++;
            else if(mlist.get(i).getDate().equals("14"))
                day14++;
            else if(mlist.get(i).getDate().equals("15"))
                day15++;
            else if(mlist.get(i).getDate().equals("16"))
                day16++;
            else if(mlist.get(i).getDate().equals("17"))
                day17++;
            else if(mlist.get(i).getDate().equals("18"))
                day18++;
            else if(mlist.get(i).getDate().equals("19"))
                day19++;
            else if(mlist.get(i).getDate().equals("20"))
                day20++;
            else if(mlist.get(i).getDate().equals("21"))
                day21++;
            else if(mlist.get(i).getDate().equals("22"))
                day22++;
            else if(mlist.get(i).getDate().equals("23"))
                day23++;
            else if(mlist.get(i).getDate().equals("24"))
                day24++;
            else if(mlist.get(i).getDate().equals("25"))
                day25++;
            else if(mlist.get(i).getDate().equals("26"))
                day26++;
            else if(mlist.get(i).getDate().equals("27"))
                day27++;
            else if(mlist.get(i).getDate().equals("28"))
                day28++;
            else if(mlist.get(i).getDate().equals("29"))
                day29++;
            else if(mlist.get(i).getDate().equals("30"))
                day30++;
            else if(mlist.get(i).getDate().equals("31"))
                day31++;
        }
        mBeforeBind.clear();
        mBeforeBind.add(new Entry(1,day1));
        mBeforeBind.add(new Entry(2,day2));
        mBeforeBind.add(new Entry(3,day3));
        mBeforeBind.add(new Entry(4,day4));
        mBeforeBind.add(new Entry(5,day5));
        mBeforeBind.add(new Entry(6,day6));
        mBeforeBind.add(new Entry(7,day7));
        mBeforeBind.add(new Entry(8,day8));
        mBeforeBind.add(new Entry(9,day8));
        mBeforeBind.add(new Entry(10,day10));
        mBeforeBind.add(new Entry(11,day11));
        mBeforeBind.add(new Entry(12,day12));
        mBeforeBind.add(new Entry(13,day13));
        mBeforeBind.add(new Entry(14,day14));
        mBeforeBind.add(new Entry(15,day15));
        mBeforeBind.add(new Entry(16,day16));
        mBeforeBind.add(new Entry(17,day17));
        mBeforeBind.add(new Entry(18,day18));
        mBeforeBind.add(new Entry(19,day19));
        mBeforeBind.add(new Entry(20,day20));
        mBeforeBind.add(new Entry(21,day21));
        mBeforeBind.add(new Entry(22,day22));
        mBeforeBind.add(new Entry(23,day23));
        mBeforeBind.add(new Entry(24,day24));
        mBeforeBind.add(new Entry(25,day25));
        mBeforeBind.add(new Entry(26,day26));
        mBeforeBind.add(new Entry(27,day27));
        mBeforeBind.add(new Entry(28,day28));
        mBeforeBind.add(new Entry(29,day29));
        mBeforeBind.add(new Entry(30,day30));
        mBeforeBind.add(new Entry(31,day31));
    }
}
