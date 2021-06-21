package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelxcruz.finatialorganizer.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import config.ConfigFirebase;
import helper.Base64Custom;
import helper.DateCustom;
import model.Movimentation;
import model.User;

public class SpendingActivity extends AppCompatActivity {
    EditText etMoney_Spending, etCategory_Spendings, etDate_Spending, etDescription_Spending;
    Button btAddSpending;
    Movimentation movimentation;

    DatabaseReference firebaseRef = ConfigFirebase.getFirebaseRef();
    FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAuth();
    private double despesatotal;
    private double despesaTotalAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);
        etMoney_Spending = findViewById(R.id.etMoney_Spending);
        etCategory_Spendings = findViewById(R.id.etCategory_Spendings);
        etDate_Spending = findViewById(R.id.etDate_Spending);
        etDescription_Spending = findViewById(R.id.etDescription_Spending);
        btAddSpending = findViewById(R.id.btAddSpending);

        recoveryTotalSpending();
        etDate_Spending.setText(DateCustom.todayDate());

        btAddSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Validando campos*/

                if (!etMoney_Spending.getText().toString().equals("")){
                    if (!etCategory_Spendings.getText().toString().equals("")){
                        if (!etDate_Spending.getText().toString().equals("")){
                            if (!etDescription_Spending.getText().toString().equals("")){

                                double valorTratado = Double.valueOf(etMoney_Spending.getText().toString()).doubleValue();

                                String date = etDate_Spending.getText().toString();



                                /*Armazenando dentro da movimentação todos os dados obtidos da tela*/
                                movimentation = new Movimentation();
                                movimentation.setValue(valorTratado);
                                movimentation.setCategory(etCategory_Spendings.getText().toString());
                                movimentation.setDescription(etDescription_Spending.getText().toString());
                                movimentation.setDate(date);
                                movimentation.setType("s");



                                Double despesagerada = valorTratado;
                                despesaTotalAtualizada = despesatotal + despesagerada;

                                attSpendings(despesaTotalAtualizada);


                                /*Salvando*/
                                movimentation.save(date);


                                Intent it = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(it);
                                Toast.makeText(getApplicationContext(), "Despezas adicionadas com sucesso", Toast.LENGTH_SHORT).show();

                            }else{
                                etDescription_Spending.setError("Preencha");
                                etDescription_Spending.requestFocus();
                            }
                        }else{
                            etDate_Spending.setError("Preencha");
                            etDate_Spending.requestFocus();
                        }
                    }else{
                        etCategory_Spendings.setError("Preencha");
                        etCategory_Spendings.requestFocus();
                    }
                }else{
                    Toast.makeText(SpendingActivity.this, "Informe o valor da despesa", Toast.LENGTH_SHORT).show();
                    etMoney_Spending.requestFocus();
                }

            }
        });


    }

    public void recoveryTotalSpending(){
        String CurrentUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codifing64Base(CurrentUser);
        DatabaseReference userref = firebaseRef.child("usuario").child(idUser);

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                /*despesatotal= user.getTotalSpending();*/

                /*Testando o GET do firebase*/

                System.out.println("DespesaTotal" + user.getTotalSpending());
                System.out.println("Email" + user.getEmail());
                System.out.println("Nome" + user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void attSpendings (Double despesaTotalAtualizada){
        String CurrentUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codifing64Base(CurrentUser);
        DatabaseReference userref = firebaseRef.child("usuario").child(idUser);

        userref.child("totalSpending").setValue(despesaTotalAtualizada);

    }

}