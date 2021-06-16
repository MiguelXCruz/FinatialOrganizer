package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miguelxcruz.finatialorganizer.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Movimentation;

public class SpendingActivity extends AppCompatActivity {
    EditText etMoney_Spending, etCategory_Spendings, etDate_Spending, etDescription_Spending;
    Button btAddSpending;
    Movimentation movimentation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);
        etMoney_Spending = findViewById(R.id.etMoney_Spending);
        etCategory_Spendings = findViewById(R.id.etCategory_Spendings);
        etDate_Spending = findViewById(R.id.etDate_Spending);
        etDescription_Spending = findViewById(R.id.etDescription_Spending);
        btAddSpending = findViewById(R.id.btAddSpending);

        /*Mostrando a data de hoje*/
        String todayDate = getAtualDate();
        etDate_Spending.setText(todayDate);

        btAddSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double valorTratado = Double.valueOf(etMoney_Spending.getText().toString()).doubleValue();

                String date = etDate_Spending.getText().toString();

                /*Armazenando dentro da movimentação todos os dados obtidos da tela*/
                movimentation = new Movimentation();
                movimentation.setValue(valorTratado);
                movimentation.setCategory(etCategory_Spendings.getText().toString());
                movimentation.setDescription(etDescription_Spending.getText().toString());
                movimentation.setDate(date);
                movimentation.setType("s");

                /*Salvando*/
                movimentation.save(date);
            }
        });





    }

    public String getAtualDate (){
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String returnedDate = formatDate.format(date);

        return returnedDate;
    }

}