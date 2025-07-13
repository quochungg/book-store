package com.prm.bookstore.UI.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.prm.bookstore.R;
import com.prm.bookstore.UI.Book.BookListFragment;

public class HomeFragment extends Fragment {

    private String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lấy token từ arguments
        if (getArguments() != null) {
            token = getArguments().getString("token");
            Log.d("HomeFragment", "Token in HomeFragment: " + token);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Layout rỗng để giữ chỗ cho fragment con
        FrameLayout layout = new FrameLayout(requireContext());
        layout.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBookListFragment();
    }

    private void showBookListFragment() {
        BookListFragment bookListFragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putString("token", token);
        bookListFragment.setArguments(args);

        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_fragment_container, bookListFragment);
        ft.commit();
    }
}
