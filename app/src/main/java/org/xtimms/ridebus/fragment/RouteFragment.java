package org.xtimms.ridebus.fragment;

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

import org.xtimms.ridebus.R;
import org.xtimms.ridebus.adapter.RouteAdapter;
import org.xtimms.ridebus.model.Route;
import org.xtimms.ridebus.presenter.RouteFragmentPresenter;

import java.util.List;

public class RouteFragment extends Fragment implements RouteFragmentPresenter.View {

    private RecyclerView mRecyclerView;

    public static RouteFragment newInstance() {
        return new RouteFragment();
    }

    @Override
    public void setAdapter(List<Route> routeList) {
        RouteAdapter mRouteAdapter = new RouteAdapter(routeList);
        mRecyclerView.setAdapter(mRouteAdapter);
        mRouteAdapter.setOnItemClickListener((parent, v, position, id) -> {
            //TODO
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView_routes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        RouteFragmentPresenter presenter = new RouteFragmentPresenter(this);
        presenter.setAdapter();
    }

}
