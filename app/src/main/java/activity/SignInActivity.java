package activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.miguelxcruz.finatialorganizer.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Login");
    }
}