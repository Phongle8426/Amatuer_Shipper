package com.example.AmateurShipper.BindDataArray;

import android.util.Log;

import com.example.AmateurShipper.Model.DataStatisticObject;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class BindDataToBarChart {

    public BindDataToBarChart() {
    }

    public void totalAmountDayWeek(ArrayList<DataStatisticObject> mlist,ArrayList<DataStatisticObject> mBeforeBind){
        int mon=0,tus=0,wed=0,thu=0,fri=0,sat=0,sun=0;
        for (int i = 0; i<mlist.size();i++){
            int amount = Integer.parseInt(mlist.get(i).getAmount().replaceAll("\\s",""));
            if (mlist.get(i).getDate().equals("Th 2"))
                mon+=amount;
            else if(mlist.get(i).getDate().equals("Th 3"))
                tus+=amount;
            else if(mlist.get(i).getDate().equals("Th 4"))
                wed+=amount;
            else if(mlist.get(i).getDate().equals("Th 5"))
                thu+=amount;
            else if(mlist.get(i).getDate().equals("Th 6"))
                fri+=amount;
            else if(mlist.get(i).getDate().equals("Th 7"))
                sat+=amount;
            else if(mlist.get(i).getDate().equals("CN"))
                sun+=amount;
        }
        mBeforeBind.clear();
        mBeforeBind.add(new DataStatisticObject("Th2",""+mon));
        mBeforeBind.add(new DataStatisticObject("Th3",""+tus));
        mBeforeBind.add(new DataStatisticObject("Th4",""+wed));
        mBeforeBind.add(new DataStatisticObject("Th5",""+thu));
        mBeforeBind.add(new DataStatisticObject("Th6",""+fri));
        mBeforeBind.add(new DataStatisticObject("Th7",""+sat));
        mBeforeBind.add(new DataStatisticObject("CN",""+sun));

    }

    public void totalAmountMonthYear(ArrayList<DataStatisticObject> mlist,ArrayList<DataStatisticObject> mBeforeBind){
        int jan=0,feb=0,mar=0,apr=0,may=0,jun=0,jul=0,aug=0,sep=0,oct=0,nov=0,dec=0;
       // Log.i(TAG, "totalMOnth: "+mlist.get(0).getDate() + "/"+mlist.get(0).getAmount());
        //Log.i(TAG, "totalMOnth: "+mlist.get(0).getDate() + "/"+mlist.get(0).getAmount());
        for (int i = 0; i<mlist.size();i++){
            int amount = Integer.parseInt(mlist.get(i).getAmount().replaceAll("\\s",""));
           // Log.i(TAG, "totalAmountMonthYear: "+ amount);
            if (mlist.get(i).getDate().equals("01"))
                jan+=amount;
            else if(mlist.get(i).getDate().equals("02"))
                feb+=amount;
            else if(mlist.get(i).getDate().equals("03"))
                mar+=amount;
            else if(mlist.get(i).getDate().equals("04"))
                apr+=amount;
            else if(mlist.get(i).getDate().equals("05"))
                may+=amount;
            else if(mlist.get(i).getDate().equals("06"))
                jun+=amount;
            else if(mlist.get(i).getDate().equals("07"))
                jul+=amount;
            else if(mlist.get(i).getDate().equals("08"))
                aug+=amount;
            else if(mlist.get(i).getDate().equals("09"))
                sep+=amount;
            else if(mlist.get(i).getDate().equals("10"))
                oct+=amount;
            else if(mlist.get(i).getDate().equals("11"))
                nov+=amount;
            else if(mlist.get(i).getDate().equals("12"))
                dec+=amount;
        }
        mBeforeBind.clear();
        mBeforeBind.add(new DataStatisticObject("T1",""+jan));
        mBeforeBind.add(new DataStatisticObject("T2",""+feb));
        mBeforeBind.add(new DataStatisticObject("T3",""+mar));
        mBeforeBind.add(new DataStatisticObject("T4",""+apr));
        mBeforeBind.add(new DataStatisticObject("T5",""+may));
        mBeforeBind.add(new DataStatisticObject("T6",""+jun));
        mBeforeBind.add(new DataStatisticObject("T7",""+jul));
        mBeforeBind.add(new DataStatisticObject("T8",""+aug));
        mBeforeBind.add(new DataStatisticObject("T9",""+sep));
        mBeforeBind.add(new DataStatisticObject("T10",""+oct));
        mBeforeBind.add(new DataStatisticObject("T11",""+nov));
        mBeforeBind.add(new DataStatisticObject("T12",""+dec));

    }


    public void totalMonth(ArrayList<DataStatisticObject> mlist,ArrayList<DataStatisticObject> mBeforeBind){
        int day1=0,day2=0,day3=0,day4=0,day5=0,day6=0,day7=0,day8=0,day9=0,day10=0,day11=0,day12=0,
                day13=0,day14=0,day15=0,day16=0,day17=0,day18=0,day19=0,day20=0,day21=0,day22=0,day23=0,day24=0,
                day25=0,day26=0,day27=0,day28=0,day29=0,day30=0,day31=0;
        //Log.i(TAG, "totalMOnth: "+mlist.get(0).getDate() + "/"+mlist.get(0).getAmount());
        for (int i = 0; i<mlist.size();i++){
            int amount = Integer.parseInt(mlist.get(i).getAmount().replaceAll("\\s",""));
            //Log.i(TAG, "totalAmountMonthYear: "+ amount);
            if (mlist.get(i).getDate().equals("01"))
                day1+=amount;
            else if(mlist.get(i).getDate().equals("02"))
                day2+=amount;
            else if(mlist.get(i).getDate().equals("03"))
                day3+=amount;
            else if(mlist.get(i).getDate().equals("04"))
                day4+=amount;
            else if(mlist.get(i).getDate().equals("05"))
                day5+=amount;
            else if(mlist.get(i).getDate().equals("06"))
                day6+=amount;
            else if(mlist.get(i).getDate().equals("07"))
                day7+=amount;
            else if(mlist.get(i).getDate().equals("08"))
                day8+=amount;
            else if(mlist.get(i).getDate().equals("09"))
                day9+=amount;
            else if(mlist.get(i).getDate().equals("10"))
                day10+=amount;
            else if(mlist.get(i).getDate().equals("11"))
                day11+=amount;
            else if(mlist.get(i).getDate().equals("12"))
                day12+=amount;
            if (mlist.get(i).getDate().equals("13"))
                day13+=amount;
            else if(mlist.get(i).getDate().equals("14"))
                day14+=amount;
            else if(mlist.get(i).getDate().equals("15"))
                day15+=amount;
            else if(mlist.get(i).getDate().equals("16"))
                day16+=amount;
            else if(mlist.get(i).getDate().equals("17"))
                day17+=amount;
            else if(mlist.get(i).getDate().equals("18"))
                day18+=amount;
            else if(mlist.get(i).getDate().equals("19"))
                day19+=amount;
            else if(mlist.get(i).getDate().equals("20"))
                day20+=amount;
            else if(mlist.get(i).getDate().equals("21"))
                day21+=amount;
            else if(mlist.get(i).getDate().equals("22"))
                day22+=amount;
            else if(mlist.get(i).getDate().equals("23"))
                day23+=amount;
            else if(mlist.get(i).getDate().equals("24"))
                day24+=amount;
            else if(mlist.get(i).getDate().equals("25"))
                day25+=amount;
            else if(mlist.get(i).getDate().equals("26"))
                day26+=amount;
            else if(mlist.get(i).getDate().equals("27"))
                day27+=amount;
            else if(mlist.get(i).getDate().equals("28"))
                day28+=amount;
            else if(mlist.get(i).getDate().equals("29"))
                day29+=amount;
            else if(mlist.get(i).getDate().equals("30"))
                day30+=amount;
            else if(mlist.get(i).getDate().equals("31"))
                day31+=amount;
        }
        mBeforeBind.clear();
        mBeforeBind.add(new DataStatisticObject("01",""+day1));
        mBeforeBind.add(new DataStatisticObject("02",""+day2));
        mBeforeBind.add(new DataStatisticObject("03",""+day3));
        mBeforeBind.add(new DataStatisticObject("04",""+day4));
        mBeforeBind.add(new DataStatisticObject("05",""+day5));
        mBeforeBind.add(new DataStatisticObject("06",""+day6));
        mBeforeBind.add(new DataStatisticObject("07",""+day7));
        mBeforeBind.add(new DataStatisticObject("08",""+day8));
        mBeforeBind.add(new DataStatisticObject("09",""+day9));
        mBeforeBind.add(new DataStatisticObject("10",""+day10));
        mBeforeBind.add(new DataStatisticObject("11",""+day11));
        mBeforeBind.add(new DataStatisticObject("12",""+day12));
        mBeforeBind.add(new DataStatisticObject("13",""+day13));
        mBeforeBind.add(new DataStatisticObject("14",""+day14));
        mBeforeBind.add(new DataStatisticObject("15",""+day15));
        mBeforeBind.add(new DataStatisticObject("16",""+day16));
        mBeforeBind.add(new DataStatisticObject("17",""+day17));
        mBeforeBind.add(new DataStatisticObject("18",""+day18));
        mBeforeBind.add(new DataStatisticObject("19",""+day19));
        mBeforeBind.add(new DataStatisticObject("20",""+day20));
        mBeforeBind.add(new DataStatisticObject("21",""+day21));
        mBeforeBind.add(new DataStatisticObject("22",""+day22));
        mBeforeBind.add(new DataStatisticObject("23",""+day23));
        mBeforeBind.add(new DataStatisticObject("24",""+day24));
        mBeforeBind.add(new DataStatisticObject("25",""+day25));
        mBeforeBind.add(new DataStatisticObject("26",""+day26));
        mBeforeBind.add(new DataStatisticObject("27",""+day27));
        mBeforeBind.add(new DataStatisticObject("28",""+day28));
        mBeforeBind.add(new DataStatisticObject("29",""+day29));
        mBeforeBind.add(new DataStatisticObject("30",""+day30));
        mBeforeBind.add(new DataStatisticObject("31",""+day31));
    }
}
