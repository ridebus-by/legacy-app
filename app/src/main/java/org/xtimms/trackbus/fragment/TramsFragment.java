package org.xtimms.trackbus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.TimelineActivity;
import org.xtimms.trackbus.adapter.TramsAdapter;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.presenter.TramsFragmentPresenter;

import java.util.ArrayList;

public class TramsFragment extends AppBaseFragment implements TramsFragmentPresenter.View {

    private RecyclerView mRecyclerView;
    private TramsAdapter mTramsAdapter;
    private ProgressBar mProgressBar;
    private boolean mAdapterIsSet = false;

    public static TramsFragment newInstance() {
        return new TramsFragment();
    }

    @Override
    public void setAdapter(ArrayList<Route> routeList) {
        if (!mAdapterIsSet) {
            mProgressBar.setVisibility(View.GONE);
            mTramsAdapter = new TramsAdapter(routeList);
            mRecyclerView.setAdapter(mTramsAdapter);
            mTramsAdapter.setOnItemClickListener((parent, v, position, id) -> {
                Intent intent = TimelineActivity.newIntent(getActivity(), routeList.get(position));
                startActivity(intent);
            });
        } else {
            mTramsAdapter.dataChange(routeList);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_routes, container, false);
        mProgressBar = root.findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.column_count)));
        mRecyclerView.setHasFixedSize(true);
        TramsFragmentPresenter presenter = new TramsFragmentPresenter(this);
        presenter.setAdapter();
    }
}
