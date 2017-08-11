package com.bignerdranch.travis.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CheatActivity extends AppCompatActivity {


    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.travis.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_IS_SHOWN = "com.bignerdranch.travis.geoquiz.answer_shown";
    private static final String KEY_INDEX = "index";

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private TextView mBuildTextView;
    private TextView mCheatTokensTextView;
    private Button mShowAnswerButton;

    private int cheatTokens = 3;


    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wassAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_IS_SHOWN,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);



        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mBuildTextView = (TextView) findViewById(R.id.build_text_view);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mCheatTokensTextView = (TextView) findViewById(R.id.cheat_tokens_text_view);

        if(savedInstanceState != null){
            mAnswerTextView.setText(savedInstanceState.getString(KEY_INDEX,"Answer"));
            cheatTokens = savedInstanceState.getInt(KEY_INDEX,3);
        }


        mCheatTokensTextView.setText("Cheat Tokens: " + cheatTokens);
        mBuildTextView.setText("API Level "+Build.VERSION.SDK);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cheatTokens --;
                mCheatTokensTextView.setText("Cheat Tokens: " + cheatTokens);

                if(cheatTokens > 0) {
                    if (mAnswerIsTrue) {
                        mAnswerTextView.setText(R.string.true_button);
                    } else {
                        mAnswerTextView.setText(R.string.false_button);
                    }
                    setAnswerShownResult(true);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        int cx = mShowAnswerButton.getWidth() / 2;
                        int cy = mShowAnswerButton.getHeight() / 2;
                        float radius = mShowAnswerButton.getWidth();
                        Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mShowAnswerButton.setVisibility(View.INVISIBLE);
                            }
                        });
                        anim.start();
                    } else {
                        mShowAnswerButton.setVisibility(View.INVISIBLE);
                    }
                }else{
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });


    }


    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_IS_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(KEY_INDEX,mAnswerTextView.getText().toString());
        savedInstanceState.putInt(KEY_INDEX,cheatTokens);
    }

}
