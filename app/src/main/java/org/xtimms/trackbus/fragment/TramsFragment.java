package org.xtimms.trackbus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.TimelineActivity;
import org.xtimms.trackbus.adapter.TramsAdapter;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.presenter.TramsFragmentPresenter;

import java.util.List;

public class TramsFragment extends Fragment implements TramsFragmentPresenter.View {

    private RecyclerView mRecyclerView;

    public static TramsFragment newInstance() {
        return new TramsFragment();
    }

    @Override
    public void setAdapter(List<Route> routeList) {
        TramsAdapter mTramsAdapter = new TramsAdapter(routeList);
        mRecyclerView.setAdapter(mTramsAdapter);
        mTramsAdapter.setOnItemClickListener((parent, v, position, id) -> {
            Intent intent = TimelineActivity.newIntent(getActivity(), routeList.get(position));
            startActivity(intent);
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trams, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView_trams);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        TramsFragmentPresenter presenter = new TramsFragmentPresenter(this);
        presenter.setAdapter();
    }
}
