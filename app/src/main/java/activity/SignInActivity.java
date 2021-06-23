package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.miguelxcruz.finatialorganizer.R;

import config.ConfigFirebase;
import model.User;

public class SignInActivity extends AppCompatActivity {
    EditText etEmailLogin, etPasswordLogin;
    Button btSignIn;
    FirebaseAuth auth;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Login");
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btSignIn = findViewById(R.id.btSignIn);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();

                if (!email.isEmpty()){
                        if (!password.isEmpty()){


                            UserLogin(email, password, v);


                        }else{
                            etPasswordLogin.setError("Preencha");
                            etPasswordLogin.requestFocus();
                        }
                    }else{
                        etEmailLogin.setError("Preencha");
                        etEmailLogin.requestFocus();
                    }

            }
        });
    }


    private void UserLogin(String email, String password, View view){
        auth = ConfigFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){ //deu certo
                    Intent it = new Intent(SignInActivity.this,HomeActivity.class);
                    startActivity(it);
                    finish();
                    

                }else{ //deu erro
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "O Email que você está tentando logar NÃO existe no sistema";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "A senha está incorreta";
                    }catch (Exception e){
                        excecao = "Erro ao Logar: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Snackbar snackbar = Snackbar.make(view,excecao,Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });

    }
}