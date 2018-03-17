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
import com.example.stefanini.challengevanhack.util.SharedPrefs;

import java.util.List;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ProductAdapter.class.getSimpleName();
    private final View.OnClickListener mOnclick;

    private Context mContext;
    private final MainActivity mActivity;
    private final List<Product> mList;
    private List<Product> mCartProducts;

    public ProductAdapter(MainActivity activity, List<Product> list, View.OnClickListener onClickListener) {
        mActivity = activity;
        mList = list;
        mCartProducts = SharedPrefs.getProductsInCart(mActivity);
        mOnclick = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product, parent, false);
        return new ProductAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder bholder, final int position) {
        ViewHolder holder = (ViewHolder) bholder;
        final Product item = mList.get(position);
        holder.mTitle.setText(item.getName());
        holder.mDescription.setText(item.getDescription());
        String dollarPrice = mActivity.getString(R.string.dollar, String.valueOf(item.getPrice()));
        holder.mPrice.setText(dollarPrice);
        holder.mIcon.setVisibility(mCartProducts.contains(item) ? View.VISIBLE : View.GONE);
        holder.mMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemoveCart(item);
            }
        });
    }

    private void addOrRemoveCart(final Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if (mCartProducts.contains(product)) {
            builder.setMessage("Can you remove from Cart?").setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCartProducts.remove(product);
                            SharedPrefs.saveProductsInCart(mActivity, mCartProducts);
                            notifyDataSetChanged();
                            mOnclick.onClick(null);
                        }
                    }).setNegativeButton("No", null).show();
        } else {
            builder.setMessage("Can you add to Cart?").setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCartProducts.add(0, product);
                            SharedPrefs.saveProductsInCart(mActivity, mCartProducts);
                            notifyDataSetChanged();
                            mOnclick.onClick(null);
                        }
                    }).setNegativeButton("No", null).show();
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle, mDescription, mPrice;
        private final View mMain, mIcon;

        public ViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.name);
            mDescription = view.findViewById(R.id.description);
            mPrice = view.findViewById(R.id.price);
            mIcon = view.findViewById(R.id.cart_icon);
            mMain = view.findViewById(R.id.main);
        }
    }
}