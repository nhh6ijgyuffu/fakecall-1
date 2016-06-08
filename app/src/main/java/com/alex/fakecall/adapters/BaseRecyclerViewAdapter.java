package com.alex.fakecall.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerViewAdapter<T, VH extends BaseRecyclerViewVH<T>> extends RecyclerView.Adapter<VH> {
    private List<T> listItem = new ArrayList<>();
    protected OnItemClickListener clickListener;
    protected OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(View v, Object item, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View v, Object item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }

    public void setList(List<T> list) {
        this.listItem = list;
        notifyDataSetChanged();
    }

    public List<T> getListItem() {
        List<T> list = new ArrayList<>();
        for (T item : listItem) {
            if (item != null) list.add(item);
        }
        return list;
    }

    public void clearList() {
        listItem.clear();
        notifyDataSetChanged();
    }

    public void addItem(T item, int pos) {
        listItem.add(pos, item);
        notifyItemInserted(pos);
    }

    public void addItem(T item) {
        addItem(item, listItem.size());
    }

    public void addItems(List<T> list) {
        listItem.addAll(list);
        notifyItemRangeInserted(listItem.size(), list.size());
    }

    public T removeItem(int pos) {
        final T item = listItem.remove(pos);
        notifyItemRemoved(pos);
        return item;
    }

    public void moveItem(int from, int to) {
        final T item = listItem.remove(from);
        listItem.add(to, item);
        notifyItemMoved(from, to);
    }

    public void animateTo(List<T> listItem) {
        applyAndAnimateRemovals(listItem);
        applyAndAnimateAdditions(listItem);
        applyAndAnimateMovedItems(listItem);
    }

    private void applyAndAnimateRemovals(List<T> newList) {
        for (int i = listItem.size() - 1; i >= 0; i--) {
            final T item = listItem.get(i);
            if (!newList.contains(item)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<T> newList) {
        for (int i = 0, count = newList.size(); i < count; i++) {
            final T item = newList.get(i);
            if (!listItem.contains(item)) {
                addItem(item, i);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<T> newList) {
        for (int toPosition = newList.size() - 1; toPosition >= 0; toPosition--) {
            final T item = newList.get(toPosition);
            final int fromPosition = listItem.indexOf(item);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public T getItem(int position) {
        if (position < 0 || position >= listItem.size()) return null;
        return listItem.get(position);
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        T item = listItem.get(position);
        holder.performBind(item, position);
    }
}
