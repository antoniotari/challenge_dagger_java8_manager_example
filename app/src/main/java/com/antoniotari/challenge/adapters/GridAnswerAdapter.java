package com.antoniotari.challenge.adapters;

import com.antoniotari.android.injection.ApplicationGraph;
import com.antoniotari.android.jedi.ScreenDimension;
import com.antoniotari.challenge.R;
import com.antoniotari.challenge.adapters.GridAnswerAdapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Antonio Tari
 */
public class GridAnswerAdapter extends RecyclerView.Adapter<ViewHolder> {

    public interface OnItemClickListener{
        void onClick(int position,int hash);
    }

    private List<String> mImageUrls;
    private OnItemClickListener mOnClickListener;
    private View selectedView;

    public GridAnswerAdapter(List<String> imageUrls){
        mImageUrls=imageUrls;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_answer, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public void setOnClickListener(OnItemClickListener listener){
        mOnClickListener=listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.cv.setOnClickListener(v -> onCellClick(holder,position,mImageUrls.get(position).hashCode()));
        holder.answerImage.setImageURI(Uri.parse(mImageUrls.get(position)));
    }

    private void onCellClick(ViewHolder holder,int position,int hash){
        //TODO do not keep reference of the view, rewrite the layout to use holder.itemView.setSelected()
        //instead of the overlay view
        if(selectedView!=null){
            selectedView.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.overlay.setBackgroundColor(Color.parseColor("#99FAFAD2"));
        selectedView=holder.overlay;
        mOnClickListener.onClick(position, hash);
    }

   /* public void add(int position, String item) {
        mImageUrls.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mImageUrls.indexOf(item);
        mImageUrls.remove(position);
        notifyItemRemoved(position);
    }*/

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind (R.id.cv)CardView cv;
        @Bind (R.id.overlayButtonView)View overlay;
        @Bind(R.id.answerImage)SimpleDraweeView answerImage;
        @Bind(R.id.cellFrame)FrameLayout frameLayout;

        @Inject
        ScreenDimension mScreenDimension;

        ViewHolder(View itemView) {
            super(itemView);
            ApplicationGraph.getObjectGraph().inject(this);
            ButterKnife.bind(this, itemView);

            //make a square imege view
            int height = mScreenDimension.getScreenWidthPX()/2;
            CardView.LayoutParams imageParams=( CardView.LayoutParams)frameLayout.getLayoutParams();
            imageParams.height=height;
            answerImage.setLayoutParams(imageParams);
        }
    }
}
