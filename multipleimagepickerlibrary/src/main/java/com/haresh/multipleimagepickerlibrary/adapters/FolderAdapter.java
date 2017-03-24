package com.haresh.multipleimagepickerlibrary.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haresh.multipleimagepickerlibrary.R;
import com.haresh.multipleimagepickerlibrary.models.Folder;
import com.haresh.multipleimagepickerlibrary.listeners.FolderOnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private Context mContext;
    private FolderOnItemClickListener mFolderOnItemClickListener;
    private List<Folder> mFolders = new ArrayList<>();
    private int lastSelected = 0;

    public FolderAdapter(Context context) {
        mContext = context;
    }

    public void setItemClickListener(FolderOnItemClickListener folderOnItemClickListener) {
        mFolderOnItemClickListener = folderOnItemClickListener;
    }

    public void setData(List<Folder> folders) {
        if (folders != null && folders.size() > 0) {
            mFolders = folders;
        } else {
            mFolders.clear();
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView cover;
        private final TextView name;
        private final TextView size;
        private final ImageView indicator;


        public ViewHolder(View v) {
            super(v);
            cover = (ImageView) v.findViewById(R.id.cover);
            name = (TextView) v.findViewById(R.id.name);
            size = (TextView) v.findViewById(R.id.size);
            indicator = (ImageView) v.findViewById(R.id.indicator);
        }

        public ImageView getCover() {
            return cover;
        }

        public TextView getName() {
            return name;
        }

        public TextView getSize() {
            return size;
        }

        public ImageView getIndicator() {
            return indicator;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Folder folder = mFolders.get(holder.getAdapterPosition());
        if (folder == null) {
            return;
        }
        Glide.with(mContext).load(new File(folder.cover.path)).error(R.drawable.default_error).centerCrop().into(holder.getCover());
        if (holder.getAdapterPosition() == 0) {
            if (getTotalImageSize() > 1) {
                holder.getSize().setText(String.format(mContext.getResources().getString(R.string.photos_unit), getTotalImageSize()));
            } else if (getTotalImageSize() == 1) {
                holder.getSize().setText(String.format(mContext.getResources().getString(R.string.photo_unit), getTotalImageSize()));
            } else {
                holder.getSize().setText(mContext.getResources().getString(R.string.no_photo_unit));
            }
        } else {
            if (folder.images != null) {
                if (folder.images.size() > 1) {
                    holder.getSize().setText(String.format(mContext.getResources().getString(R.string.photos_unit), mFolders.get(holder.getAdapterPosition()).images.size()));
                } else if (folder.images.size() == 1) {
                    holder.getSize().setText(String.format(mContext.getResources().getString(R.string.photo_unit), mFolders.get(holder.getAdapterPosition()).images.size()));
                } else {
                    holder.getSize().setText(mContext.getResources().getString(R.string.no_photo_unit));
                }
            } else {
                holder.getSize().setText(mContext.getResources().getString(R.string.no_photo_unit));
            }
        }

        if (holder.getAdapterPosition() == 0) {
            holder.getName().setText(R.string.folder_all);
        } else {
            holder.getName().setText(folder.name);
        }

        if (lastSelected == holder.getAdapterPosition()) {
            holder.indicator.setVisibility(View.VISIBLE);
        } else {
            holder.indicator.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFolderOnItemClickListener.folderOnItemClick(holder.getAdapterPosition(), folder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFolders.size();
    }

    private int getTotalImageSize() {
        int result = 0;
        if (mFolders != null && mFolders.size() > 0) {
            for (Folder f : mFolders) {
                result += f.images.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

}
