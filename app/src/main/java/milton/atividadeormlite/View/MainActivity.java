package milton.atividadeormlite.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import milton.atividadeormlite.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void chamaCadastroEletrodomesticoActivity(View v) {

        Intent it = new Intent(this, CadastroEletrodomesticoActivity.class);
        startActivity(it);

    }

    public void chamaCadastrarUtilizacaoActivity(View v) {

        Intent it = new Intent(this, CadastrarUtilizacaoActivity.class);
        startActivity(it);

    }
}
