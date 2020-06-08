package com.qat.android.quanlynhasach.user_fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.qat.android.quanlynhasach.CheckConnection;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.models.Books;
import com.qat.android.quanlynhasach.user.BookDetailActivity;
import com.qat.android.quanlynhasach.view_holder.BookAdapter;
import com.qat.android.quanlynhasach.view_holder.BookViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.paperdb.Paper;


public class HomeFragment extends Fragment {

    private DatabaseReference ProductsRef;
    private RecyclerView mRecyclerView;

//    private EditText mEditTextSearch;
//    private ArrayList<Books> mListBooks;
//    private BookAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mEditTextSearch = getActivity().findViewById(R.id.edit_search_field);
//        mListBooks = new ArrayList<>();

        mSwipeRefreshHome = getActivity().findViewById(R.id.swipe_refresh_home);

        Paper.init(getContext());

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        //Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Home");

        mRecyclerView = getActivity().findViewById(R.id.recycler_menu);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        checkInternetConnection();

        mSwipeRefreshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshHome.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshHome.setRefreshing(false);
                        FirebaseRecyclerOptions<Books> options = new FirebaseRecyclerOptions.Builder<Books>()
                                .setQuery(ProductsRef, Books.class)
                                .build();

                        FirebaseRecyclerAdapter<Books, BookViewHolder> adapter = new FirebaseRecyclerAdapter<Books, BookViewHolder>(options) {
                            @SuppressLint("SetTextI18n")
                            @Override
                            protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull final Books model) {
                                holder.mTxtBookName.setText(model.getPname());
                                holder.mTxtBookPrice.setText(model.getPrice() + "₫");
                                Picasso.get().load(model.getImage()).into(holder.imageView);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getContext(), BookDetailActivity.class);
                                        intent.putExtra("pid", model.getPid());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_layout, parent, false);
                                BookViewHolder holder = new BookViewHolder(view);
                                return holder;
                            }
                        };
                        mRecyclerView.setAdapter(adapter);
                        adapter.startListening();
                    }
                }, 2000);
            }
        });
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//
//    }

    private void checkInternetConnection() {
        if (CheckConnection.isOnline(getContext())) {
            FirebaseRecyclerOptions<Books> options = new FirebaseRecyclerOptions.Builder<Books>()
                    .setQuery(ProductsRef, Books.class)
                    .build();

            FirebaseRecyclerAdapter<Books, BookViewHolder> adapter = new FirebaseRecyclerAdapter<Books, BookViewHolder>(options) {
                @SuppressLint("SetTextI18n")
                @Override
                protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull final Books model) {
                    holder.mTxtBookName.setText(model.getPname());
                    holder.mTxtBookPrice.setText(model.getPrice() + "₫");
                    Picasso.get().load(model.getImage()).into(holder.imageView);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), BookDetailActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_layout, parent, false);
                    BookViewHolder holder = new BookViewHolder(view);
                    return holder;
                }
            };
            mRecyclerView.setAdapter(adapter);
            adapter.startListening();
        } else {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    //    @Override
//    public void onStart() {
//        super.onStart();
//
//        if (ProductsRef != null) {
//            ProductsRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            mListBooks.add(ds.getValue(Books.class));
//                        }
//                        BookAdapter bookAdapter = new BookAdapter(mListBooks);
//                        mRecyclerView.setAdapter(bookAdapter);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//
//        if (mSearchView != null) {
//            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String s) {
//                    search(s);
//                    return true;
//                }
//            });
//        }
//    }
//
//    private void search(String str) {
//
//        ArrayList<Books> mArrayListBook = new ArrayList<>();
//        for (Books books : mListBooks) {
//            if (books.getPname().toLowerCase().contains(str.toLowerCase())) {
//                mArrayListBook.add(books);
//            }
//        }
//        BookAdapter bookAdapter = new BookAdapter(mArrayListBook);
//        mRecyclerView.setAdapter(bookAdapter);
//    }

//    @Override
//    public void onItemClick(int position) {
//        Intent intent = new Intent(getContext(), BookDetailActivity.class);
//        intent.putExtra("pid", model.getPid());
//        startActivity(intent);
//    }
}
