package com.qat.android.quanlynhasach.admin_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.admin.AddBookActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView mImgScience, mImgText, mImgChildren, mImgForeignLanguage, mImgComic, mImgHistory,
            mImgPolitical, mImgEconomy;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_category, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Category");

        mImgScience = getActivity().findViewById(R.id.img_science_technology_book);
        mImgText = getActivity().findViewById(R.id.img_text_book);
        mImgChildren = getActivity().findViewById(R.id.img_kid_book);
        mImgForeignLanguage = getActivity().findViewById(R.id.img_foreign_language_book);
        mImgComic = getActivity().findViewById(R.id.img_comic_book);
        mImgHistory = getActivity().findViewById(R.id.img_history_book);
        mImgPolitical = getActivity().findViewById(R.id.img_political_book);
        mImgEconomy = getActivity().findViewById(R.id.img_economy_book);

        mImgScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "science");
                startActivity(intent);
            }
        });

        mImgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "text");
                startActivity(intent);
            }
        });

        mImgChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "children");
                startActivity(intent);
            }
        });

        mImgForeignLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "foreign");
                startActivity(intent);
            }
        });

        mImgComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "comic");
                startActivity(intent);
            }
        });

        mImgHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "history");
                startActivity(intent);
            }
        });

        mImgPolitical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "political");
                startActivity(intent);
            }
        });

        mImgEconomy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "economy");
                startActivity(intent);
            }
        });
    }
}
