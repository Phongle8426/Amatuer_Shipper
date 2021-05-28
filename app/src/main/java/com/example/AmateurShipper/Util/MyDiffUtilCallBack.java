package com.example.AmateurShipper.Util;

import androidx.recyclerview.widget.DiffUtil;

import com.example.AmateurShipper.Model.PostObject;

import java.util.List;

public class MyDiffUtilCallBack extends DiffUtil.Callback {

    private List<PostObject> oldList;
    private List<PostObject> newList;

    public MyDiffUtilCallBack(List<PostObject> oldList, List<PostObject> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }
    public MyDiffUtilCallBack() {
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }
}
