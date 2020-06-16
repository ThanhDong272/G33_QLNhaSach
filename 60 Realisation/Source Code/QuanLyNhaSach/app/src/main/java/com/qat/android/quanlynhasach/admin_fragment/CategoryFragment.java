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

public class CategoryFragment extends Fragment {

    private ImageView mImgScience, mImgText, mImgChildren, mImgForeignLanguage,
            mImgComic, mImgHistory, mImgPolitical, mImgEconomy;

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
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mImgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "text");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mImgChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "children");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mImgForeignLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "foreign");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mImgComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "comic");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mImgHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "history");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mImgPolitical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "political");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mImgEconomy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                intent.putExtra("category", "economy");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }
}
