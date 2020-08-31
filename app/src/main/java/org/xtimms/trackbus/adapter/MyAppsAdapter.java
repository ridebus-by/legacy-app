package org.xtimms.trackbus.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.model.MyApps;
import org.xtimms.trackbus.util.AppUtils;

import java.util.List;

public class MyAppsAdapter extends RecyclerView.Adapter<MyAppsAdapter.MyAppsHolder> {

    private final Context context;
    private final List<MyApps> items;

    public MyAppsAdapter(Context context, List<MyApps> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyAppsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_apps, parent, false);
        return new MyAppsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAppsHolder holder, final int position) {
        holder.setIsRecyclable(false);

        final MyApps model = items.get(holder.getAdapterPosition());
        int resId = context.getResources().getIdentifier(model.getImageUrl(), "drawable", context.getPackageName());
        Glide.with(context).load(resId).into(holder.image_app);
        holder.tv_app_name.setText(model.getName());
        holder.tv_app_description.setText(model.getDescription());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent();
            if (AppUtils.checkAppInstalled(context, model.getPackageName())) {
                intent = context.getPackageManager().getLaunchIntentForPackage(model.getPackageName());
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                context.startActivity(intent);
            } else {
                intent.setData(Uri.parse(model.getGooglePlayUrl()));
                intent.setAction(Intent.ACTION_VIEW);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class MyAppsHolder extends RecyclerView.ViewHolder {

        private final ImageView image_app;
        private final TextView tv_app_name;
        private final TextView tv_app_description;


        private MyAppsHolder(View itemView) {
            super(itemView);
            image_app = itemView.findViewById(R.id.image_app);
            tv_app_name = itemView.findViewById(R.id.tv_app_name);
            tv_app_description = itemView.findViewById(R.id.tv_app_description);
        }
    }

}