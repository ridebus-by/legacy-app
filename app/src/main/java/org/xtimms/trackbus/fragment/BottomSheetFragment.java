package org.xtimms.trackbus.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.TimelineActivity;
import org.xtimms.trackbus.adapter.BottomSheetAdapter;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.presenter.BottomSheetPresenter;

import java.util.List;

public class BottomSheetFragment extends BottomSheetDialogFragment implements BottomSheetPresenter.View {

    private String mRouteNumber;
    private BottomSheetFragment mBottomSheetFragment;
    private RecyclerView mRecyclerView;

    public void setArguments(String routeNumber) {
        this.mRouteNumber = routeNumber;
    }

    @Override
    public void setAdapter(List<Route> routesBottomSheet) {
        BottomSheetAdapter bottomSheetAdapter = new BottomSheetAdapter(routesBottomSheet);
        mRecyclerView.setAdapter(bottomSheetAdapter);

        bottomSheetAdapter.setOnItemClickListener((parent, view, position, id) -> {
            mBottomSheetFragment.dismiss();
            Intent intent = TimelineActivity.newIntent(getActivity(), routesBottomSheet.get(position));
            startActivity(intent);

        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBottomSheetFragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycleView_bottomsheet);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (getActivity() != null) {
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }

        BottomSheetPresenter presenter = new BottomSheetPresenter(this, mRouteNumber);
        presenter.setAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

}