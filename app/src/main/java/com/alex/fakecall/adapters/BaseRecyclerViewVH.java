package com.alex.fakecall.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseRecyclerViewVH<T> extends RecyclerView.ViewHolder {

    private T item;

    public BaseRecyclerViewVH(ViewGroup parent, int resId) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
        ButterKnife.bind(this,itemView);
    }

    protected final void performBind(T item, int position) {
        this.item = item;
        onBind(item, position);
    }

    protected abstract void onBind(T item, int pos);

    protected T getItem() {
        return item;
    }
}
