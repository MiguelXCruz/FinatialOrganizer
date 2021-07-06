package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.miguelxcruz.finatialorganizer.R;

import config.ConfigFirebase;
import util.MaskEditUtil;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();

        FirebaseAuth auth = ConfigFirebase.getFirebaseAuth();
        EditText etEmailReset;
        Button btSendEmail;
        ImageView ivBackButton_Reset;

        etEmailReset = findViewById(R.id.etEmailReset);
        btSendEmail = findViewById(R.id.btSendEmail);
        ivBackButton_Reset = findViewById(R.id.ivBackButton_Reset);



        /*Settando clicks*/

        btSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Validando email*/

                if (etEmailReset.getText().toString().matches(MaskEditUtil.FORMAT_EMAIL) || etEmailReset.getText().toString().matches(MaskEditUtil.FORMAT_EMAILwbr)) {
                    String email = etEmailReset.getText().toString();

                    resetPassword(auth,v,email );

                }else{
                    etEmailReset.setError("Digite um email válido");
                    etEmailReset.requestFocus();
                }
            }
        });

        ivBackButton_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /*methods*/

    public void resetPassword (FirebaseAuth auth, View view, String email){
        String success = "Email enviado com sucesso\n Lembre-se de checar também sua caixa de \bSpam caso não encontre o email";

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Snackbar.make(view,success,BaseTransientBottomBar.LENGTH_LONG).show();
                }else{ //erros
                    String failed = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        failed = "O Email que você está tentando recuperar NÃO existe no sistema";
                    }catch (FirebaseNetworkException e){
                        failed = "Você está sem internet para fazer essa solicitação no momento";
                    }catch (FirebaseTooManyRequestsException e){
                        failed = "A fila de solicitações está com tráfego muito grande \n Pedimos que aguarde e tente em alguns minutos ";
                        e.printStackTrace();
                    }catch (Exception e){
                        failed = "Algo deu errado tentando recuperar sua senha: \n" + e.getMessage();
                        e.printStackTrace();
                    }

                    Snackbar.make(view,failed,Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}