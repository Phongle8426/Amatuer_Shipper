package com.example.AmateurShipper.Util;

import com.example.AmateurShipper.Model.PostObject;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class PostDiffUtilCallback extends DiffUtil.Callback {

    private List<PostObject> oldList;
    private List<PostObject> newList;

    public PostDiffUtilCallback(List<PostObject> oldList, List<PostObject> newList) {
        this.oldList = oldList;
        this.newList = newList;
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
