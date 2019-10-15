package com.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.model.User;
import com.perso.topquiz.R;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private User mUser;
    public static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private SharedPreferences mPreferences;
    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME";

    @Override // "Ctrl + o" pour ouvrir fenêtre de choix des méthodes qui peuvent être overriden
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // L'appel du super est réclamé si absent mais ça a l'air de tourner sans... (?)
        super.onActivityResult(requestCode, resultCode, data);

        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            // Fetch the score from the intent :
            /**
             * Récupération du score via l'intent (identifiant BUNDLE_EXTRA_SCORE) :
             * le paramètre à 0 est un score par défaut si jamais la valeur du score n'est pas présente.
             */
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);

            /**
             * Explications --> cf. plus bas dans la méthode onClick() :
             */
            mPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();

            greetUser();
        }
    }

    private void greetUser(){
        String firstName = mPreferences.getString(PREF_KEY_FIRSTNAME, null);

        if(null != firstName){
            int score = mPreferences.getInt(PREF_KEY_SCORE, 0);
            String fullText = "Welcome back " + firstName + " !"
                    + "\nYour last score was : " + score +"..."
                    + "\nWill you do better this time ?";
            mGreetingText.setText(fullText);
            mNameInput.setText(firstName);
            mNameInput.setSelection(firstName.length());
            mPlayButton.setEnabled(true);
        }
    }

    /**
     * Appelée quand l'activité est créée par le système et qu'elle entre dans l'état "Created".
     * S'y déroule généralement :
     * - mise en place GUI
     * - initialisation variables
     * - configurations de listeners
     * - connexion au modèle
     * Activité créée mais non visible (donc pas d'interactions possibles à ce stade)
     * @param savedInstanceState : variable qui contient le dernier état sauvegardé de l'activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("MainActivity::onCreate()");

        mUser = new User();

        // Rendre accessibles uniquement à l'application, les préférences du joueurs :
        mPreferences = getPreferences(MODE_PRIVATE);

        // Référencer les éléments graphiques :
        mGreetingText = findViewById(R.id.helloWorld);
        mNameInput = findViewById(R.id.textZone);
        mPlayButton = findViewById(R.id.button);

        mPlayButton.setEnabled(false); // Bouton grisé, inactif

        greetUser();

        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPlayButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUser.setFirstname(mNameInput.getText().toString());

                /**
                 * Dans l'ordre :
                 * - mPreferences : accès aux préférences
                 * - edit() : édition des valeurs des préférences
                 * - putString() : y mettre une nouvelle valeur de type String
                 *      - clé "firstname" pour cette valeur (à mettre de préférence en constante)
                 *      - mUser.getFirstName() : valeur qu'on souhaite lui associer (prénom du user)
                 * - apply() : les préférences peuvent prendre en compte les modifications
                 */
                mPreferences.edit().putString(PREF_KEY_FIRSTNAME, mUser.getFirstname()).apply();

                // User clicked the button :
                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                //startActivity(gameActivityIntent);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    /**
     * Appelée par le système quand l'activité entre dans l'état "Started".
     * GUI devient visible mais il n'est pas encore possible d'interagir avec les éléments graphiques.
     */
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("MainActivity::onStart()");
    }

    /**
     * Appelée quand l'activité entre dans l'état "Resumed".
     * L'activité devient entièrement opérationnelle.
     * L'application est utilisable et les éléments graphiques sont cliquables.
     * L'application reste dans cet état tant qu'il n'y a pas d'interruption (appel téléphonique, démarrage d'une autre activité,
     * affichage d'une boîte de dialogue...).
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        System.out.println("MainActivity::onResume()");
    }

    /**
     * Appelée quand l'activité entre dans l'état "Paused" (interruption par un appel téléphonique, nouvelle activité démarrée...).
     * C'est le pendant de la méthode onResume() : tout ce qui est initié dans onResume() doit être mise en pause ici (animation...).
     * Les traitements effectués ici doivent être les plus courts possibles.
     */
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("MainActivity::onPause()");
    }

    /**
     * Appelée quand l'activité entre dans l'état "Stopped".
     * Quand une activité est démarrée, l'activité appelante se retrouve ainsi "Stopped". Elle n'est plus visible à l'utilisateur.
     * Les traitements liés à la mise à jour de l'IHM peuvent être arrêtés.
     * Les traitements effectués dans cette méthode peuvent être plus importants (ex. : sauvegarder certaines valeurs dans les
     * sharedPreferences).
     */
    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("MainActivity::onStop()");
    }

    /**
     * Appelée quand l'activité est arrêtée après :
     * - avoir appelé la méthode finish()
     * - décision du système de l'arrêter pour libérer de la mémoire
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("MainActivity::onDestroy()");
    }
}
