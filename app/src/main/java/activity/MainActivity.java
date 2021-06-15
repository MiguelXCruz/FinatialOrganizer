package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.miguelxcruz.finatialorganizer.R;

public class MainActivity extends IntroActivity {
    TextView tvLogarse;
    Button btCadastrese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        btCadastrese = findViewById(R.id.btCadastrese);
        tvLogarse = findViewById(R.id.tvLogarse);

        iniciaSliders();

    }


    /*--------------Functions---------------------*/

        private void iniciaSliders(){
        //Fazendo com que os botões de sliders e a actionBar sejam inivisveis
        setButtonBackVisible(false);
        setButtonNextVisible(false);
        getSupportActionBar().hide();



        /*Chamando os sliders*/
        /*1*/
        addSlide(new FragmentSlide.Builder()
            .background(R.color.white)
                .fragment(R.layout.slide_1)
                .build()
        );

        /*2*/
        addSlide(new FragmentSlide.Builder()
        .background(R.color.white)
        .fragment(R.layout.slide_2)
                .build()
        );

        /*3*/
        addSlide(new FragmentSlide.Builder()
        .background(R.color.white)
        .fragment(R.layout.slide_3)
        .build());

        /*4*/
        addSlide(new FragmentSlide.Builder()
        .background(R.color.white)
                .fragment(R.layout.slide_4)
                .build()
        );

        /*5*/
        addSlide(new FragmentSlide.Builder()
        .background(R.color.white)
        .fragment(R.layout.slide_5)
        .canGoForward(false)
        .build()
        );
    } //final iniciaSliders

    public void onClickSignUp (View view){
        Intent it = new Intent (MainActivity.this, SignUpActivity.class);
        startActivity(it);
    }

    public void onClickSignIn(View view){
            Intent it = new Intent (MainActivity.this,SignInActivity.class);
            startActivity(it);
    }

}