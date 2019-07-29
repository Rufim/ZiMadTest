package ru.kazantsev.zimadtest.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.kazantsev.zimadtest.R;
import ru.kazantsev.zimadtest.adapter.ExtendedRecyclerAdapter;
import ru.kazantsev.zimadtest.net.RestApi;

public class PetTabsFragment extends Fragment {

    public static final String POSITION = "curChoice";
    public static final String CHILD_DATA = "childData";

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    protected ExtendedRecyclerAdapter<Integer, Fragment> adapter;
    protected Integer[] tabIds = {R.string.cat_tab, R.string.dog_tab};

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pet_tabs, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (adapter == null) {
            adapter =  new ExtendedRecyclerAdapter<Integer, Fragment>(getChildFragmentManager()) {
                @Override
                public Fragment getNewItem(int position) {
                    Bundle bundle = new Bundle();
                    PetTabFragment fragment = new PetTabFragment();
                    switch (tabIds[position]) {
                        case R.string.cat_tab:
                            bundle.putSerializable(PetTabFragment.PET_ARG, RestApi.PetQuery.cat);
                            break;
                        case R.string.dog_tab:
                            bundle.putSerializable(PetTabFragment.PET_ARG, RestApi.PetQuery.dog);
                            break;
                    }
                    fragment.setArguments(bundle);
                    return fragment;
                }

                @Nullable
                @Override
                public CharSequence getPageTitle(int position) {
                    return getString(tabIds[position]);
                }
            };
            adapter.addItems(Arrays.asList(tabIds));
        }
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(pager);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            setArguments(savedInstanceState.getParcelable(CHILD_DATA));
            pager.onRestoreInstanceState(savedInstanceState.getParcelable(POSITION));
        } else if(getArguments() == null) {
            setArguments(new Bundle());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(POSITION, pager.onSaveInstanceState());
        outState.putParcelable(CHILD_DATA, getArguments());
    }


    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
