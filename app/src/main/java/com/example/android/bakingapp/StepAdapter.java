package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final StepAdapterOnClickHandler mClickHandler;
    private Step[] mSteps;

    public StepAdapter(StepAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        StepViewHolder viewHolder = new StepViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        String stepDescription = "Step" + " " + position + ": " + mSteps[position].getShortDescription();
        holder.listItemStep.setText(stepDescription);
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) {
            return 0;
        }
        return mSteps.length;
    }

    public void setMovieData(Step[] steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    //the interface that recieves onClick messages
    public interface StepAdapterOnClickHandler {
        void onClick(Step clickedStep);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.rv_step)
        TextView listItemStep;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        // This gets called by the child views during a click.
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step step = mSteps[adapterPosition];
            mClickHandler.onClick(step);
        }
    }
}
