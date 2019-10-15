package com.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.model.Question;
import com.model.QuestionBank;
import com.perso.topquiz.R;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

   private TextView mQuestionTextView;

   private Button mAnswerButton1;
   private Button mAnswerButton2;
   private Button mAnswerButton3;
   private Button mAnswerButton4;

   private QuestionBank mQuestionBank;
   private Question mCurrentQuestion;

   private int mScore;
   private int mNumberOfQuestions;

   public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
   public static final String BUNDLE_STATE_SCORE = "currentScore";
   public static final String BUNDLE_STATE_QUESTION= "currentQuestion";

   private boolean mEnableTouchEvent;

    /**
     *
     * @param savedInstanceState : bundle qui contient l'état de l'activité et créé quand la méthode onSaveInstanceState(...)
     *                           est exécutée (cf. plus bas)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        System.out.println("onCreate()");

        mQuestionBank = this.generateQuestions();

        if(savedInstanceState != null){
            mScore = savedInstanceState.getInt(BUNDLE_EXTRA_SCORE);
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mScore = 0;
            mNumberOfQuestions = 4;
        }

        mEnableTouchEvent = true;

        // Référencer les éléments graphiques :
        mQuestionTextView = findViewById(R.id.question1);
        mAnswerButton1 = findViewById(R.id.answer1);
        mAnswerButton2 = findViewById(R.id.answer2);
        mAnswerButton3 = findViewById(R.id.answer3);
        mAnswerButton4 = findViewById(R.id.answer4);

        mAnswerButton1.setTag(0);
        mAnswerButton2.setTag(1);
        mAnswerButton3.setTag(2);
        mAnswerButton4.setTag(3);

        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayQuestion(mCurrentQuestion);
    }

    /**
     *
     * @param outState : infos mémorisées par Android pour les restituer
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mNumberOfQuestions);

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onClick(View v) {
        int responseIndex = (int) v.getTag();

        if(responseIndex == mCurrentQuestion.getAnswerIndex()) {
            // Good answer
            Toast.makeText(this, "Correct !", Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            // Wrong answer
            Toast.makeText(this, "Wrong answer !", Toast.LENGTH_SHORT).show();
        }

        mEnableTouchEvent = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvent = true;

                /**
                 * If this is the last question, ends the game.
                 * Else, display the next question.
                 */
                if(--mNumberOfQuestions == 0){
                    // End the game
                    endGame();
                } else {
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2000); // LENGTH_SHORT is usually 2 second long.
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvent && super.dispatchTouchEvent(ev);
    }

    private void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done !")
                .setMessage("Your score is : " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();

                        /**
                         * Pour mettre le score dans l'intent, la méthode putExtra() prend :
                         *
                         * - identifiant associé au score : permet à l'activité appelant de récupérer le score en utilisant
                         * cet identifiant
                         * - le score lui-même
                         */
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);

                        /**
                         * Méthode setResult() permet d'enregistrer l'intent auprès d'Android (système) afin ensuite qu'il puisse
                         * envoyer le résultat à la MainActivity :
                         *
                         * - 1er paramètre indique que tout s'est passé sans erreur (RESULT_OK)
                         * - 2ème paramètre : l'intent en question
                         */
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }

    public void displayQuestion(final Question question){
        mQuestionTextView.setText(question.getQuestion());
        mAnswerButton1.setText(question.getChoiceList().get(0));
        mAnswerButton2.setText(question.getChoiceList().get(1));
        mAnswerButton3.setText(question.getChoiceList().get(2));
        mAnswerButton4.setText(question.getChoiceList().get(3));
    }

    private QuestionBank generateQuestions() {

        Question question1 = new Question("Qu'est-ce qu'un lépidoptèrophile ?",
                Arrays.asList(
                        "Quelqu'un qui aime errer sur son lieu de travail",
                        "Quelqu'un qui aime jouer à Butterfly Go avec son téléphone dans une main et une épuisette dans l'autre",
                        "Quelqu'un qui aime collectionner les papillons",
                        "Quelqu'un qui aime s'écouter parler"
                ),
                2);

        Question question2 = new Question("How many countries are there in the European Union?",
                Arrays.asList("15", "24", "28", "32"),
                2);

        Question question3 = new Question("Who is the creator of the Android operating system?",
                Arrays.asList("Andy Rubin", "Steve Wozniak", "Jake Wharton", "Paul Smith"),
                0);

        Question question4 = new Question("When did the first man land on the moon?",
                Arrays.asList("1958", "1962", "1967", "1969"),
                3);

        Question question5 = new Question("What is the capital of Romania?",
                Arrays.asList("Bucarest", "Warsaw", "Budapest", "Berlin"),
                0);

        Question question6 = new Question("Who did the Mona Lisa paint?",
                Arrays.asList("Michelangelo", "Leonardo Da Vinci", "Raphael", "Carravagio"),
                1);

        Question question7 = new Question("In which city is the composer Frédéric Chopin buried?",
                Arrays.asList("Strasbourg", "Warsaw", "Paris", "Moscow"),
                2);

        Question question8 = new Question("What is the country top-level domain of Belgium?",
                Arrays.asList(".bg", ".bm", ".bl", ".be"),
                3);

        Question question9 = new Question("What is the house number of The Simpsons?",
                Arrays.asList("42", "101", "666", "742"),
                3);

        Question question10 = new Question("What is the name of the current french president?",
                Arrays.asList("François Hollande", "Emmanuel Macron", "Jacques Chirac", "François Mitterand"),
                1);

        return new QuestionBank(Arrays.asList(
                question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9,
                question10));
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("GameActivity::onStart()");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        System.out.println("GameActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("GameActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("GameActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("GameActivity::onDestroy()");
    }
}
