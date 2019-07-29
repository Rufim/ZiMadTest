package ru.kazantsev.zimadtest.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import ru.kazantsev.zimadtest.App;
import ru.kazantsev.zimadtest.R;
import ru.kazantsev.zimadtest.model.Pet;
import ru.kazantsev.zimadtest.net.RestApi;
import ru.kazantsev.zimadtest.service.RestService;
import ru.kazantsev.zimadtest.utils.RxUtils;
import ru.kazantsev.zimadtest.view.AdvancedRecyclerView;

public class PetTabFragment extends Fragment {

    public static final String PET_ARG = "pet_arg";
    public static final String INSTANCE_STATE = "instanceOf{s}";
    public static final String TAG = PetTabFragment.class.getSimpleName();

    private String instanceName;
    private String instanceNameList;

    @BindView(R.id.list)
    AdvancedRecyclerView recyclerView;

    @Inject
    RestService restService;

    private Disposable disposable;
    private PetListAdapter adapter;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pet_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        App.getInstance().getComponent().inject(this);
        Bundle bundle = getArguments();
        RestApi.PetQuery query = null;
        if (bundle != null && bundle.containsKey(PET_ARG)) {
            query = (RestApi.PetQuery) bundle.getSerializable(PET_ARG);
        }
        if (query != null) {
            instanceName = INSTANCE_STATE.replace("{s}", query.name());
            instanceNameList = instanceName + "List";
            if (adapter == null) {
                adapter = new PetListAdapter(this);
                if (getParentFragment() != null) {
                    Bundle parentArgs = getParentFragment().getArguments();
                    if (parentArgs != null && parentArgs.containsKey(instanceNameList)) {
                        adapter.setPets(parentArgs.getParcelableArrayList(instanceNameList));
                    }
                }
                if (adapter.pets.isEmpty()) {
                    disposable = restService.getPets(query).compose(RxUtils.applySchedulers())
                            .subscribe(message -> {
                                adapter.setPets(new ArrayList<>(message.getData()));
                            }, ex -> {
                                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG);
                                Log.e(TAG, ex.getMessage(), ex);
                            });
                }

            }
            recyclerView.setAdapter(adapter);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getParentFragment() != null) {
            Bundle parentArgs = getParentFragment().getArguments();
            if (parentArgs != null && instanceName != null && parentArgs.containsKey(instanceName)) {
                recyclerView.onRestoreInstanceState(parentArgs.getParcelable(instanceName));
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (getParentFragment() != null) {
            Bundle parentArgs = getParentFragment().getArguments();
            if (parentArgs != null && instanceName != null) {
                parentArgs.putParcelable(instanceName, recyclerView.onSaveInstanceState());
                parentArgs.putParcelableArrayList(instanceNameList, adapter.pets);
            }
        }
        unbinder.unbind();
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroyView();
    }

    public static class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.ViewHolder> {

        private Picasso thumbnailPicasso;
        private PetTabFragment fragment;
        private ArrayList<Pet> pets = new ArrayList<>();

        public PetListAdapter(PetTabFragment fragment) {
            this.fragment = fragment;
        }

        public void setPets(ArrayList<Pet> pets) {
            this.pets = pets;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_pet_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.initItem(pets.get(i), i);
        }

        @Override
        public int getItemCount() {
            return pets.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.pet_item_thumbnail)
            ImageView thumbnail;
            @BindView(R.id.pet_item_title)
            TextView title;
            @BindView(R.id.pet_item_number)
            TextView number;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @SuppressLint("SetTextI18n")
            public void initItem(Pet pet, int position) {
                number.setText("" + (position + 1));
                title.setText(pet.getTitle());
                Picasso.get().load(pet.getUrl())
                        .placeholder(R.drawable.ic_img_placeholder)
                        .into(thumbnail);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment partent  = fragment.getParentFragment();
                        if (partent != null) {
                            FragmentTransaction tr = partent.getFragmentManager().beginTransaction();
                            PetViewFragment viewFragment = new PetViewFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(PetViewFragment.PET_ITEM, pet);
                            viewFragment.setArguments(bundle);
                            tr.replace(R.id.container, viewFragment);
                            tr.addToBackStack(partent.getClass().getSimpleName());
                            tr.commit();
                        }
                    }
                });
            }


        }
    }
}
