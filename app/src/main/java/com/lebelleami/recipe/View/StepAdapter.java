package com.lebelleami.recipe.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lebelleami.recipe.Model.Step;
import com.lebelleami.recipe.R;
import com.lebelleami.recipe.Controllers.StepsActivity;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{
    private Context context;
    private List<Step> steps;


    public StepAdapter(Context applicationContext, List<Step> mSteps)
    {
        this.steps = mSteps;
        this.context = applicationContext;
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.step_list_item, viewGroup, false);
        StepAdapter.StepViewHolder stepViewHolder = new StepAdapter.StepViewHolder(view);
        return stepViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final StepAdapter.StepViewHolder stepViewHolder, final int i) {
        final Step step = steps.get(i);
        stepViewHolder.step_Title.setText(step.getShortDescription());
        int id = steps.get(i).getId();
        stepViewHolder.step_Id.setText(String.valueOf(id));



        stepViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = stepViewHolder.getAdapterPosition();
                Step step = steps.get(pos);

                int id = steps.get(pos).getId();

                //Toast.makeText(v.getContext(), "Testy" + pos, Toast.LENGTH_SHORT).show();

                Bundle arguments = new Bundle();
                arguments.putParcelable("Step", step);


                Intent intent = new Intent(context, StepsActivity.class);
                intent.putExtra("steps", arguments);
                intent.putExtra("position", pos);
                intent.putExtra("pos", id);
                intent.putExtra("stepno", getItemCount());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {

        if (steps == null) {
            return 0;
        } else {
            return steps.size();
        }
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        TextView step_Title, step_Id;
        ImageView step_imageIcon;


        public StepViewHolder (View itemView){
            super(itemView);
            step_Title = itemView.findViewById(R.id.step_title);
            step_Id = itemView.findViewById(R.id.step_id);
            step_imageIcon = itemView.findViewById(R.id.step_imageView);

            }


    }}


