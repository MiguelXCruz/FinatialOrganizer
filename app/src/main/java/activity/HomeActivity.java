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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelxcruz.finatialorganizer.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.AdapterMovimentacao;
import config.ConfigFirebase;
import helper.Base64Custom;
import model.Movimentation;
import model.User;

public class HomeActivity extends AppCompatActivity {
    TextView tvShowUserName, tvBalanceGeral;
    ImageView ivLogout;
    MaterialCalendarView calendarView;

    RecyclerView rvMovimentation;
    AdapterMovimentacao adapterMovimentacao;
    List<Movimentation> movimentations = new ArrayList<>();
    Movimentation movimentation;

    Double despesaTotal = 0.0;
    Double receitaTotal = 0.0;
    Double resumoUsuario = 0.0;

    DatabaseReference databaseReference = ConfigFirebase.getFirebaseRef();
    FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAuth();
    DatabaseReference movimentationRef;

    String selectedMonthYear;


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
        calendarView = findViewById(R.id.calendarView);
        rvMovimentation = findViewById(R.id.rvMovimentation);


        ShowUserName();
        calendarViewConfiguration();
        getResumo();
        getMovimentations();


        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent it = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        });



        //configurando adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentations, this);

        //Setando R.V.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMovimentation.setLayoutManager(layoutManager);
        rvMovimentation.setHasFixedSize(true);
        rvMovimentation.setAdapter(adapterMovimentacao);
    }


/*functions --------------------------*/

    public void calendarViewConfiguration() {
        CharSequence months [] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio","Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(months);

        CalendarDay Startdate = calendarView.getCurrentDate();
        String selectedMonth = String.format("%02d",Startdate.getMonth());
        selectedMonthYear = String.valueOf(selectedMonth + "" + Startdate.getYear());



        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String selectedMonth = String.format("%02d",date.getMonth());
                selectedMonthYear = String.valueOf(selectedMonth + "" + date.getYear());

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

    public void getMovimentations(){
        /*Autenticando usuario */
        String CurrentUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codifing64Base(CurrentUser);

        movimentationRef = ConfigFirebase.getFirebaseRef();

        movimentationRef.child("movimentation").child(idUser).child(selectedMonthYear);



        movimentationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                movimentations.clear();
                for (DataSnapshot dados: snapshot.getChildren() ){
                    Movimentation movimentacao = dados.getValue( Movimentation.class );
                    movimentations.add( movimentacao );

                }

                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getResumo (){
        /*Autenticando usuario */
        String CurrentUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codifing64Base(CurrentUser);
        DatabaseReference userId = databaseReference.child("usuario").child(idUser);

        userId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                despesaTotal = user.getTotalSpending();
                receitaTotal = user.getTotalEarning();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("#,#####0.##");
                String formatValue = decimalFormat.format(resumoUsuario);
                tvBalanceGeral.setText("R$ " + formatValue);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

