package com.bit.spring.blink.Tab2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 어댑터 클래스 정의
 *
 * @author Mike
 *
 */
public class IconTextListAdapter extends BaseAdapter {

    private Context mContext;

    private List<IconTextItem> mItems = new ArrayList<IconTextItem>();

    public IconTextListAdapter(Context context) {
        mContext = context;
    }

    public void remove(int position){
        mItems.remove(position);
    }

    public void addItem(IconTextItem it) {
        mItems.add(it);
    }

    public void setListItems(List<IconTextItem> lit) {
        mItems = lit;
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        com.bit.spring.blink.Tab2.IconTextView itemView;

        if (convertView == null) {
            itemView = new com.bit.spring.blink.Tab2.IconTextView(mContext, mItems.get(position));
        } else {
            itemView = (com.bit.spring.blink.Tab2.IconTextView) convertView;

            itemView.setIcon(mItems.get(position).getIcon());
            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));
            itemView.setText(2, mItems.get(position).getData(2));
            itemView.setText(3, mItems.get(position).getData(3));
            itemView.setText(4, mItems.get(position).getData(4));

        }

        return itemView;
    }



}
