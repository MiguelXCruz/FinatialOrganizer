package activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.miguelxcruz.finatialorganizer.R;

import config.ConfigFirebase;
import helper.Base64Custom;
import model.User;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TextView tvShowUserName, tvBalanceGeral;
    ImageView ivLogout;

    DatabaseReference databaseReference = ConfigFirebase.getFirebaseRef();
    FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAuth();

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
        tvBalanceGeral = findViewById(R.id.tvBalanceGeral);


        ShowUserName();
        getTotalAmount();

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

    public void openEarnings (View view){
        Intent it = new Intent(HomeActivity.this, EarningsActivity.class);
        startActivity(it);
    }

    public void openSpendings (View view){
        Intent it = new Intent(HomeActivity.this, SpendingActivity.class);
        startActivity(it);
    }


    private void ShowUserName(){
        String CurrentUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codifing64Base(CurrentUser);
        DatabaseReference userref = databaseReference.child("usuario").child(idUser);

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
               String username = user.getName();
                tvShowUserName.setText("Olá, "+ username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void getTotalAmount (){
        /*Autenticando usuario */
        String CurrentUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codifing64Base(CurrentUser);

        DatabaseReference userId = databaseReference.child("usuario").child(idUser);

        userId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}