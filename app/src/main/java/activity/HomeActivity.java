package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        swipe();


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
                getMovimentations();
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

        DatabaseReference movimentationRef = databaseReference.child("movimentation").child(idUser).child(selectedMonthYear);


        movimentationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                movimentations.clear();
                for (DataSnapshot dados: snapshot.getChildren() ){

                    Movimentation movimentacao = dados.getValue( Movimentation.class );
                    movimentacao.setKey(dados.getKey());
                    movimentations.add( movimentacao );

                }

                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void  swipe (){
        ItemTouchHelper.Callback itemtouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragflags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.END;
                return makeMovementFlags(dragflags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excludeMovimentation(viewHolder);
            }
        };

        new ItemTouchHelper(itemtouch).attachToRecyclerView(rvMovimentation);
    }

    public void excludeMovimentation (RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);

        alertdialog.setTitle("Exclusão de movimentação");
        alertdialog.setMessage("Tem certeza que deseja excluir essa movimentação?");

        alertdialog.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentation = movimentations.get(position);

                String CurrentUser = firebaseAuth.getCurrentUser().getEmail();
                String idUser = Base64Custom.codifing64Base(CurrentUser);

                DatabaseReference movimentationRef = databaseReference.child("movimentation").child(idUser).child(selectedMonthYear);

                movimentationRef.child(movimentation.getKey()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);
                attMoney();

            }
        });


        alertdialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeActivity.this, "Operação Cancelada", Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        });

        alertdialog.create();
        alertdialog.show();

    }

    public void attMoney (){
        String CurrentUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codifing64Base(CurrentUser);

        DatabaseReference userId = databaseReference.child("usuario").child(idUser);

        if (movimentation.getType().equals("e")){
            receitaTotal = receitaTotal - movimentation.getValue();
            userId.child("totalEarning").setValue(receitaTotal);
        }

        if (movimentation.getType().equals("s")){
            despesaTotal = despesaTotal - movimentation.getValue();
            userId.child("totalSpending").setValue(despesaTotal);
        }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}

