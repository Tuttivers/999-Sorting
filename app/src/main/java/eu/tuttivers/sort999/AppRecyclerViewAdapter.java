package eu.tuttivers.sort999;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppRecyclerViewAdapter extends RecyclerView.Adapter<AppRecyclerViewAdapter.ViewHolder> {

    private ItemClickListener listener;
    private List<Item> items;

    AppRecyclerViewAdapter(ItemClickListener listener, List<Item> items) {
        this.listener = listener;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setModel(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        Item item;

        @BindView(R.id.photoView)
        ImageView photoView;

        @BindView(R.id.title_tv)
        TextView titleTv;

        @BindView(R.id.price_tv)
        TextView priceTv;

        ViewHolder(View itemView, final ItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (null != listener) {
                    listener.onItemClick(item);
                }
            });
        }

        void setModel(Item item) {
            this.item = item;
            Glide.with(photoView).load(item.photoURL).into(photoView);
            titleTv.setText(item.name);
            priceTv.setText(item.price);
        }

    }

    public interface ItemClickListener {
        void onItemClick(Item item);
    }
}