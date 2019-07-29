package ru.kazantsev.zimadtest.adapter;


import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.WeakHashMap;



/**
 * Created by Dmitry on 26.10.2015.
 */
public abstract class ExtendedRecyclerAdapter<I,F extends Fragment> extends FragmentStatePagerAdapter {

    private static final String TAG = ExtendedRecyclerAdapter.class.getSimpleName();
    protected WeakHashMap<Integer, F> registeredFragments = new WeakHashMap<>();
    protected List<I> items = new ArrayList<>();

    public ExtendedRecyclerAdapter(FragmentManager fm) {
        this(fm, new ArrayList<>());
    }

    public ExtendedRecyclerAdapter(FragmentManager fm, List<I> items) {
        super(fm);
        this.items = items;
    }

    public List<I> getItems(){
        return items;
    }

    public I getItemTag(int position) {
        if(position >= 0 && position < items.size()) {
            return items.get(position);
        } else {
            return null;
        }
    }


    public void addItems(Collection<I> newItems) {
        addItems(newItems, true);
    }


    public void addItems(Collection<I> newItems, boolean notify) {
        items.addAll(newItems);
        if(notify) notifyDataSetChanged();
    }

    public void addItem(I item) {
        addItem(item, true);
    }

    public void addItem(I item, boolean notify) {
        items.add(item);
        if(notify) notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        F fragment = (F) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        try {
            super.restoreState(arg0, arg1);
        } catch (IllegalStateException ex) {
            Log.e(TAG, ex.getMessage() , ex);
        }
    }

    public F getRegisteredFragment(int position) {
        if(position < 0) {
            return null;
        }
        return registeredFragments.get(position);
    }

    @Override
    public F getItem(int position) {
        F fragment = getRegisteredFragment(position);
        if (fragment == null) {
            fragment = getNewItem(position);
            registeredFragments.put(position, fragment);
        }
        return fragment;
    }

    public abstract F getNewItem(int position);

}
