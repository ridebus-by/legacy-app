package org.xtimms.trackbus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.StopsActivity;
import org.xtimms.trackbus.adapter.StopAdapter;
import org.xtimms.trackbus.model.Stop;
import org.xtimms.trackbus.presenter.StopFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

public class StopFragment extends AppBaseFragment implements StopFragmentPresenter.View{
    //private final String TAG = BusStopFragment.class.getSimpleName();
    private StopAdapter mStopAdapter;
    private FastScrollRecyclerView mRecyclerView;

    public static StopFragment newInstance() {
        return new StopFragment();
    }

    @Override
    public void setAdapter(List<Stop> stopList) {
        mStopAdapter = new StopAdapter(stopList);
        mRecyclerView.setAdapter(mStopAdapter);

        mStopAdapter.setOnItemClickListener((parent, v, position, id) -> {
            //Snackbar.make(v, mStopAdapter.getStopsListFiltered().get(position).getStopTitle(), Snackbar.LENGTH_SHORT).show();
            Intent intent = StopsActivity.newIntent(getActivity(), mStopAdapter.getStopsListFiltered().get(position));
            startActivity(intent);
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //mStopList = getStops();
        List<Stop> stops = new ArrayList<>();
        mStopAdapter = new StopAdapter(stops);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stop, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_stop_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        mSearch.setVisible(true);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mStopAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerView_stops);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        StopFragmentPresenter presenter = new StopFragmentPresenter(this);
        presenter.setAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
