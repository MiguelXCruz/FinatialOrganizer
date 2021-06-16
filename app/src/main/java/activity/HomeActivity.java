package activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelxcruz.finatialorganizer.R;

import config.ConfigFirebase;
import model.User;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TextView tvShowUserName;
    ImageView ivLogout;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        tvShowUserName = findViewById(R.id.tvShowUserName);
        ivLogout = findViewById(R.id.ivLogout);

        ShowUserName();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent it = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        });

    }

    private void ShowUserName(){
        auth = ConfigFirebase.getFirebaseAuth();
        String username = auth.getCurrentUser().getEmail();


        tvShowUserName.setText("Olá, "+ username);
    }
}