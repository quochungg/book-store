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
        // Hiển thị BookListFragment thay vì TextView
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_fragment_container, new BookListFragment());
        ft.commit();
        return new View(getContext()); // Trả về view rỗng vì fragment con sẽ thay thế
    }
}

