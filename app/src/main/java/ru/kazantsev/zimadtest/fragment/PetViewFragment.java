package ru.kazantsev.zimadtest.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.kazantsev.zimadtest.R;
import ru.kazantsev.zimadtest.model.Pet;

public class PetViewFragment extends Fragment {

    public static final String PET_ITEM = "pet_item";


    @BindView(R.id.pet_image)
    ImageView petView;
    @BindView(R.id.pet_title)
    TextView textView;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pet_item, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Bundle args = getArguments();
        if (args != null && args.containsKey(PET_ITEM)) {
            Pet pet = (Pet) getArguments().getSerializable(PET_ITEM);
            Picasso.get().load(pet.getUrl()).into(petView);
            textView.setText(pet.getTitle());
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
