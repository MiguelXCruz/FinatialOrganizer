package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.miguelxcruz.finatialorganizer.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import config.ConfigFirebase;
import helper.Base64Custom;
import helper.DateCustom;
import model.Movimentation;
import model.User;

public class EarningsActivity extends AppCompatActivity {
    EditText etMoney_Earnings,etCategory_Eanings, etDate_Earnings, etDescription_Earnings  ;
    Button btAddEarnings;
    Movimentation movimentation;

    DatabaseReference databaseReference = ConfigFirebase.getFirebaseRef();
    FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAuth();
    private double EarningTotal;
    private double ganhosTotalAtualizada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        etMoney_Earnings = findViewById(R.id.etMoney_Earnings);
        etCategory_Eanings = findViewById(R.id.etCategory_Eanings);
        etDate_Earnings = findViewById(R.id.etDate_Earnings);
        etDescription_Earnings = findViewById(R.id.etDescription_Earnings);
        btAddEarnings = findViewById(R.id.btAddEarnings);

        etDate_Earnings.setText(DateCustom.todayDate());

        int numericType = UCharacter.NumericType.NUMERIC;
        etMoney_Earnings.setInputType(numericType);

        recoveryTotalEarnings();



        btAddEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double treatedValue = Double.valueOf(etMoney_Earnings.getText().toString()).doubleValue();

                String date = etDate_Earnings.getText().toString();


                /*Validando campos*/

                if (!etMoney_Earnings.equals("")){
                    if (!etCategory_Eanings.equals("")){
                        if (!etDate_Earnings.equals("")){
                            if (!etDescription_Earnings.equals("")){

                                movimentation = new Movimentation();
                                movimentation.setValue(treatedValue);
                                movimentation.setCategory(etCategory_Eanings.getText().toString());
                                movimentation.setDate(date);
                                movimentation.setDescription(etDescription_Earnings.getText().toString());
                                movimentation.setType("e");

                                ganhosTotalAtualizada = EarningTotal + treatedValue;

                                attEarnings(ganhosTotalAtualizada);

                                movimentation.save(date);

                                Intent it = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(it);
                                Toast.makeText(EarningsActivity.this, "Ganhos adicionados com sucesso", Toast.LENGTH_SHORT).show();

                            }else{
                                etDescription_Earnings.setError("Preencha");
                                etDescription_Earnings.requestFocus();
                            }
                        }else{
                            etDate_Earnings.setError("Preencha");
                            etDate_Earnings.requestFocus();
                        }
                    }else{
                        etCategory_Eanings.setError("Preencha");
                        etCategory_Eanings.requestFocus();
                    }
                }else{
                    Toast.makeText(EarningsActivity.this, "Informe o valor dos ganhos", Toast.LENGTH_SHORT).show();
                    etMoney_Earnings.requestFocus();
                }





            }
        });
    }



    public void recoveryTotalEarnings(){
        String CurrentUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codifing64Base(CurrentUser);
        DatabaseReference userref = databaseReference.child("usuario").child(idUser);

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                EarningTotal= user.getTotalEarning();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void attEarnings (Double ganhos){
        String CurrentUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codifing64Base(CurrentUser);
        DatabaseReference userref = databaseReference.child("usuario").child(idUser);

        userref.child("totalEarning").setValue(ganhos);

    }

}