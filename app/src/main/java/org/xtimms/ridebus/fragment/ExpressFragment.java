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
import org.xtimms.ridebus.activity.StopsTimeLineActivity;
import org.xtimms.ridebus.adapter.ExpressAdapter;
import org.xtimms.ridebus.model.Route;
import org.xtimms.ridebus.presenter.ExpressFragmentPresenter;

import java.util.List;

public class ExpressFragment extends Fragment implements ExpressFragmentPresenter.View {

    private RecyclerView mRecyclerView;

    public static ExpressFragment newInstance() {
        return new ExpressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_express, container, false);
    }

    @Override
    public void setAdapter(List<Route> routeList) {
        ExpressAdapter mExpressAdapter = new ExpressAdapter(routeList);
        mRecyclerView.setAdapter(mExpressAdapter);
        mExpressAdapter.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = StopsTimeLineActivity.newIntent(getActivity(), routeList.get(position));
            startActivity(intent);
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView_express);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        ExpressFragmentPresenter presenter = new ExpressFragmentPresenter(this);
        presenter.setAdapter();
    }
}
