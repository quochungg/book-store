package com.prm.bookstore.UI.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.TextView;
import androidx.fragment.app.FragmentTransaction;

import com.prm.bookstore.R;
import com.prm.bookstore.UI.Book.BookListFragment;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Lấy token từ arguments của HomeFragment
        String token = getArguments() != null ? getArguments().getString("token") : null;
        BookListFragment bookListFragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putString("token", token);
        bookListFragment.setArguments(args);
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_fragment_container, bookListFragment);
        ft.commit();
        return new View(getContext()); // Trả về view rỗng vì fragment con sẽ thay thế
    }
}
