package org.xtimms.trackbus.timeline.info;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.chip.Chip;
import com.lucasurbas.listitemview.ListItemView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.timeline.PageHolder;

public class Info extends PageHolder {

    private ListItemView payment_methods;
    private ListItemView fare;
    private ListItemView traffic;
    private ListItemView route;
    private ListItemView company;
    private ListItemView tech;

    private Chip little_chip;
    private Chip middle_chip;
    private Chip big_chip;
    private Chip very_big_chip;

    private TextView large_number;
    private TextView title;

    public Info(@NonNull ViewGroup parent) {
        super(parent, R.layout.fragment_timeline_info);
    }

    @Override
    protected void onViewCreated(@NonNull View view) {

        little_chip = view.findViewById(R.id.little_chip);
        middle_chip = view.findViewById(R.id.middle_chip);
        big_chip = view.findViewById(R.id.big_chip);
        very_big_chip = view.findViewById(R.id.very_big_chip);

        large_number = view.findViewById(R.id.large_number);
        title = view.findViewById(R.id.title);

        payment_methods = view.findViewById(R.id.payment_methods);
        fare = view.findViewById(R.id.fore);
        traffic = view.findViewById(R.id.traffic);
        route = view.findViewById(R.id.route);
        company = view.findViewById(R.id.company);
        tech = view.findViewById(R.id.tech);
    }

    public void updateContent(@NonNull Route mRoute) {
        fare.setSubtitle(mRoute.getFare());
        traffic.setSubtitle(mRoute.getTraffic());
        payment_methods.setSubtitle(mRoute.getPaymentMethods());
//        time.setSubtitle(mRoute.getWorkingTime());
        company.setSubtitle(mRoute.getCompany());
        route.setSubtitle(mRoute.getItinerary());
        tech.setSubtitle(mRoute.getTech());

        large_number.setText(mRoute.getRouteNumber());
        title.setText(mRoute.getRouteTitle());

        if (mRoute.getTransportClassId() == 1) {
            big_chip.setVisibility(View.VISIBLE);
            very_big_chip.setVisibility(View.VISIBLE);
        } else if (mRoute.getTransportClassId() == 2) {
            little_chip.setVisibility(View.VISIBLE);
            big_chip.setVisibility(View.VISIBLE);
            very_big_chip.setVisibility(View.VISIBLE);
        } else if (mRoute.getTransportClassId() == 3) {
            big_chip.setVisibility(View.VISIBLE);
        } else if (mRoute.getTransportClassId() == 4) {
            middle_chip.setVisibility(View.VISIBLE);
        } else if (mRoute.getTransportClassId() == 5) {
            little_chip.setVisibility(View.VISIBLE);
        } else if (mRoute.getTransportClassId() == 6) {
            little_chip.setVisibility(View.VISIBLE);
            big_chip.setVisibility(View.VISIBLE);
        }
    }

}
