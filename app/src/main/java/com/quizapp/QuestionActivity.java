package com.quizapp;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quizapp.Adapter.AnswerRecylerViewAdapter;
import com.quizapp.Adapter.QuestionRecyclerViewAdapter;
import com.quizapp.Interface.OnAnswerChooseListener;
import com.quizapp.Model.AnswerModel;
import com.quizapp.Model.QuestionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.view.ViewGroup.LayoutParams;


public class QuestionActivity extends AppCompatActivity {
    RecyclerView questionListRecyclerView;
    QuestionRecyclerViewAdapter questionRecyclerViewAdapter;
    LinearLayoutManager layoutManager;
    FloatingActionButton fabNext;
    ArrayList<QuestionModel> questionModelArrayList = new ArrayList<>();

    JSONArray answerViewArray = new JSONArray();
    JSONObject answerViewObject;
    JSONArray questionArr;
    private PopupWindow mPopupWindow;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questionListRecyclerView = findViewById(R.id.questionListRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        questionListRecyclerView.setLayoutManager(layoutManager);
        questionListRecyclerView.setNestedScrollingEnabled(false);
        fabNext = findViewById(R.id.fabNext);
        relativeLayout = findViewById(R.id.relativeLayout);

        try {
            questionArr = new JSONArray(loadJSONFromAsset());
            Log.d("QuestionArr----", String.valueOf(questionArr.length()));
            QuestionModel questionModel = null;
            for (int i = 0; i < questionArr.length(); i++) {
                questionModel = new QuestionModel();
                JSONObject questionObj = questionArr.getJSONObject(i);
                questionModel.setQuestion(questionObj.getString("question"));

                questionModel.setOptionA(questionObj.getString("optionA"));
                questionModel.setOptionB(questionObj.getString("optionB"));
                questionModel.setOptionC(questionObj.getString("optionC"));
                questionModel.setOptionD(questionObj.getString("optionD"));
                questionModel.setId(questionObj.getInt("id"));
                questionModelArrayList.add(questionModel);
            }
            questionRecyclerViewAdapter = new QuestionRecyclerViewAdapter(QuestionActivity.this, questionModelArrayList, new OnAnswerChoose());
            questionListRecyclerView.setAdapter(questionRecyclerViewAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            for (int i = 0; i < questionArr.length(); i++) {
                answerViewObject = new JSONObject();
                answerViewObject.put("id", i + 1);
                answerViewObject.put("option", "");
                answerViewObject.put("question", questionArr.getJSONObject(i).getString("question"));
                answerViewArray.put(answerViewObject);
            }

            Log.d("ANSWER", answerViewArray.toString());
        } catch (Exception e) {

        }

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("array", answerViewArray.toString());
                AnswerModel answerModel = null;
                for (int i = 0; i < answerViewArray.length(); i++) {
                    try {
                        if (answerViewArray.getJSONObject(i).getString("option").equals("")) {
                            Toast.makeText(QuestionActivity.this, "Please Select All Answers", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                Intent obj_intent = new Intent(QuestionActivity.this, AnswerActivity.class);
                obj_intent.putExtra("answerArray", answerViewArray.toString());
                startActivity(obj_intent);
            }
        });

        questionListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fabNext.hide();
                } else {
                    fabNext.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    class OnAnswerChoose implements OnAnswerChooseListener {
        @Override
        public void OnSelected(String option, Integer id, String question) {
            try {
                Log.d("SelectedArray", answerViewArray.getJSONObject(id).put("option", option).toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("question.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
