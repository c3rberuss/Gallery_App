package com.c3rberuss.gallery.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.c3rberuss.gallery.FullScreenDialog;
import com.c3rberuss.gallery.R;
import com.c3rberuss.gallery.listeners.ItemClickListener;
import com.c3rberuss.gallery.listeners.ItemLongClickListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private Context context;
    private List<String> images;
    private int layout;
    private FragmentManager fm;
    private int pos_;


    public ImagesAdapter(Context context, List<String> images, int layout, FragmentManager fm) {
        this.context = context;
        this.images = images;
        this.layout = layout;
        this.fm = fm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Picasso.get().load(new File(images.get(position))).fit().placeholder(R.drawable.spin).into(holder.image);

        holder.setItemClickListener(new ItemClickListener() {
            @Override

            public void onItemClick(View v, int pos) {
                File file = new File(images.get(pos));

                if (file.exists()) {

                    FullScreenDialog dialog = new FullScreenDialog(images.get(pos));
                    FragmentTransaction ft = fm.beginTransaction();
                    dialog.show(ft, FullScreenDialog.TAG);

                }
            }

        });

        holder.setItemLongClickListener(new ItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {

                pos_ = pos;

                new AlertDialog.Builder(context)
                        .setTitle("Confirmación")
                        .setMessage("¿Realmente desea eliminar la imagen?")
                        .setIcon(R.drawable.alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                File f = new File(images.get(pos_));

                                if (f.exists()) {
                                    f.delete();
                                    images.remove(pos_);
                                    notifyDataSetChanged();
                                }


                                Toast.makeText(context, "Se ha eliminado correctamente!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView image;
        public PhotoView image_show;
        ItemClickListener itemClickListener;
        ItemLongClickListener itemLongClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.imageViewLayout);
            this.image.setOnClickListener(this);
            this.image.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        public void setItemLongClickListener(ItemLongClickListener ilc) {
            this.itemLongClickListener = ilc;
        }

        @Override
        public boolean onLongClick(View v) {
            this.itemLongClickListener.onItemLongClick(v, getAdapterPosition());
            return false;
        }
    }
}