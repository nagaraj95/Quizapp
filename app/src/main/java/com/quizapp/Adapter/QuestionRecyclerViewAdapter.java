package com.quizapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.quizapp.Interface.OnAnswerChooseListener;
import com.quizapp.Model.QuestionModel;
import com.quizapp.R;


import java.util.ArrayList;

public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<QuestionModel> questionModelArrayList = new ArrayList<>();
    OnAnswerChooseListener onAnswerChooseListener;

    public QuestionRecyclerViewAdapter(Context context, ArrayList<QuestionModel> questionModelArrayList, OnAnswerChooseListener onAnswerChooseListener) {
        this.context = context;
        this.questionModelArrayList = questionModelArrayList;
        this.onAnswerChooseListener = onAnswerChooseListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_questions, parent, false);

        QuestionRecyclerViewAdapter.ViewHolder vh = new QuestionRecyclerViewAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.questionText.setText( questionModelArrayList.get(i).getQuestion());

        viewHolder.optionA.setText(questionModelArrayList.get(i).getOptionA());
        viewHolder.optionB.setText(questionModelArrayList.get(i).getOptionB());
        viewHolder.optionC.setText(questionModelArrayList.get(i).getOptionC());
        viewHolder.optionD.setText(questionModelArrayList.get(i).getOptionD());

        viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("Checked id", String.valueOf(checkedId));
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    onAnswerChooseListener.OnSelected(checkedRadioButton.getText().toString(), (i), questionModelArrayList.get(i).getQuestion());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        RadioButton optionA, optionB, optionC, optionD;
        RadioGroup radioGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionText = itemView.findViewById(R.id.questionText);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
        }
    }
}
