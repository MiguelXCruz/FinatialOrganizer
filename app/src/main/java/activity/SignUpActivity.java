package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelxcruz.finatialorganizer.R;

import config.ConfigFirebase;
import model.User;

import static com.miguelxcruz.finatialorganizer.R.layout.activity_sign_up;

public class SignUpActivity extends AppCompatActivity {
    EditText etNameRegister, etEmailRegister, etPasswordRegister;
    Button btSignup;
    TextView tvUseTerms;
    FirebaseAuth auth;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_sign_up);
        setTitle("Cadastro");

        etNameRegister = findViewById(R.id.etNameRegister);
        etEmailRegister = findViewById(R.id.etEmailRegister);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
        btSignup = findViewById(R.id.btSignup);
        tvUseTerms = findViewById(R.id.tvUseTerms);

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etNameRegister.getText().toString();
                String email = etEmailRegister.getText().toString();
                String password = etPasswordRegister.getText().toString();

                if (!name.isEmpty()){
                    if (!email.isEmpty()){
                        if (!password.isEmpty()){


                            user = new User();
                            user.setName(name);
                            user.setEmail(email);
                            user.setPassword(password);

                            UserRegister();


                        }else{
                            etPasswordRegister.setError("Preencha");
                            etPasswordRegister.requestFocus();
                        }
                    }else{
                        etEmailRegister.setError("Preencha");
                        etEmailRegister.requestFocus();
                    }
                }else{
                    etNameRegister.setError("Preencha");
                    etNameRegister.requestFocus();
                }


            }
        });
    }

    public void UserRegister(){
        auth = ConfigFirebase.getFirebaseAuth();

        auth.createUserWithEmailAndPassword(
          user.getEmail(),user.getPassword()     /*<<<<<<<< passando o usuário e a senha para o firebase*/
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {  /*Completando a task e testando se ela deu certo*/
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){ //deu certo
                    Toast.makeText(SignUpActivity.this, "Registro realizado com sucesso", Toast.LENGTH_SHORT).show();
                }else{ //deu erro
                    Toast.makeText(SignUpActivity.this, "Erro ao registrar usuário", Toast.LENGTH_SHORT).show();
                }



            }

        });
    }

}