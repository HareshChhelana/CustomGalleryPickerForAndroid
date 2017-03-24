package com.haresh.multipleimagepickerlibrary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haresh.multipleimagepickerlibrary.R;
import com.haresh.multipleimagepickerlibrary.models.Image;
import com.haresh.multipleimagepickerlibrary.listeners.ImageOnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImageGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private List<Image> mImages = new ArrayList<>();
    private List<Image> mSelectedImages = new ArrayList<>();

    private ImageOnItemClickListener mImageOnItemClickListener;

    public ImageGridAdapter(Context context, boolean showCamera, int column) {
        mContext = context;
        this.showCamera = showCamera;
    }

    public void setItemClickListener(ImageOnItemClickListener imageOnItemClickListener) {
        mImageOnItemClickListener = imageOnItemClickListener;
    }


    public void showSelectIndicator(boolean b) {
        showSelectIndicator = b;
    }

    public void setShowCamera(boolean b) {
        if (showCamera == b) return;

        showCamera = b;
        notifyDataSetChanged();
    }

    public boolean isShowCamera() {
        return showCamera;
    }


    public void select(int position, Image image) {
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
        } else {
            mSelectedImages.add(image);
        }
        notifyItemChanged(position);
    }


    public void setDefaultSelected(ArrayList<String> resultList) {
        for (String path : resultList) {
            Image image = getImageByPath(path);
            if (image != null) {
                mSelectedImages.add(image);
            }
        }
        if (mSelectedImages.size() > 0) {
            notifyDataSetChanged();
        }
    }

    private Image getImageByPath(String path) {
        if (mImages != null && mImages.size() > 0) {
            for (Image image : mImages) {
                if (image.path.equalsIgnoreCase(path)) {
                    return image;
                }
            }
        }
        return null;
    }


    public void setData(List<Image> images) {
        mSelectedImages.clear();

        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final ImageView indicator;
        private final View mask;


        public ViewHolder2(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            indicator = (ImageView) v.findViewById(R.id.checkmark);
            mask = v.findViewById(R.id.mask);
        }


        public ImageView getIndicator() {
            return indicator;
        }

        public ImageView getImage() {
            return image;
        }

        public View getMask() {
            return mask;
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {

        public ViewHolder1(View v) {
            super(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera) {
            if (position == 0) {
                return TYPE_CAMERA;
            } else {
                return TYPE_NORMAL;
            }
        } else {
            return TYPE_NORMAL;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_camera, parent, false);
            return new ViewHolder1(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_image, parent, false);
            return new ViewHolder2(view);
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_CAMERA:
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                viewHolder1.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageOnItemClickListener.imageOnItemClick(holder.getAdapterPosition(), null);
                    }
                });
                break;

            case TYPE_NORMAL:
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                final Image image = mImages.get(isShowCamera() ? (holder.getAdapterPosition() - 1) : holder.getAdapterPosition());
                if (image != null) {
                    if (showSelectIndicator) {
                        viewHolder2.getIndicator().setVisibility(View.VISIBLE);
                        if (mSelectedImages.contains(image)) {
                            viewHolder2.getIndicator().setImageResource(R.drawable.btn_selected);
                            viewHolder2.getMask().setVisibility(View.VISIBLE);
                        } else {
                            viewHolder2.getIndicator().setImageResource(R.drawable.btn_unselected);
                            viewHolder2.getMask().setVisibility(View.GONE);
                        }
                    } else {
                        viewHolder2.getIndicator().setVisibility(View.GONE);
                    }
                    File imageFile = new File(image.path);
                    if (imageFile.exists()) {
                        Glide.with(mContext).load(imageFile).placeholder(R.drawable.default_error).centerCrop().into(viewHolder2.getImage());
                    } else {
                        viewHolder2.getImage().setImageResource(R.drawable.default_error);
                    }
                }
                viewHolder2.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageOnItemClickListener.imageOnItemClick(holder.getAdapterPosition(), image);
                    }
                });
                break;

        }


    }

    @Override
    public int getItemCount() {
        return showCamera ? mImages.size() + 1 : mImages.size();
    }

}
