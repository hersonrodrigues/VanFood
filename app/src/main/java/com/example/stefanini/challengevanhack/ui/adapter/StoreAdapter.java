package com.example.stefanini.challengevanhack.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stefanini.challengevanhack.MainActivity;
import com.example.stefanini.challengevanhack.R;
import com.example.stefanini.challengevanhack.model.Product;
import com.example.stefanini.challengevanhack.model.Store;
import com.example.stefanini.challengevanhack.util.ItemClick;
import com.example.stefanini.challengevanhack.util.SharedPrefs;

import java.util.List;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = StoreAdapter.class.getSimpleName();
    private final ItemClick mOnclick;

    private Context mContext;
    private final MainActivity mActivity;
    private final List<Store> mList;

    public StoreAdapter(MainActivity activity, List<Store> list, ItemClick onClickListener) {
        mActivity = activity;
        mList = list;
        mOnclick = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_store, parent, false);
        return new StoreAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder bholder, final int position) {
        ViewHolder holder = (ViewHolder) bholder;
        final Store item = mList.get(position);
        holder.mTitle.setText(item.getName());
        holder.mDescription.setText(item.getAddress());
        holder.mMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnclick.click(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle, mDescription;
        private final View mMain;

        public ViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.name);
            mDescription = view.findViewById(R.id.description);
            mMain = view.findViewById(R.id.main);
        }
    }
}