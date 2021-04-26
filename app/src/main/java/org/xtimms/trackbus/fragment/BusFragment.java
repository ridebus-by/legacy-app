package org.xtimms.trackbus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.TimelineActivity;
import org.xtimms.trackbus.adapter.BusAdapter;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.presenter.BusFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

public class BusFragment extends AppBaseFragment implements BusFragmentPresenter.View {

    private RecyclerView mRecyclerView;

    public static BusFragment newInstance() {
        return new BusFragment();
    }

    @Override
    public void setAdapter(ArrayList<Route> routeList) {
        BusAdapter mBusAdapter = new BusAdapter(routeList);
        mRecyclerView.setAdapter(mBusAdapter);
        mBusAdapter.setOnItemClickListener((parent, view, position, id) -> {
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
        return inflater.inflate(R.layout.fragment_buses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView_buses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        BusFragmentPresenter presenter = new BusFragmentPresenter(this);
        presenter.setAdapter();

    }

}
