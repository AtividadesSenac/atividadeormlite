package milton.atividadeormlite.View;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import milton.atividadeormlite.DAO.MyORMLiteHelper;
import milton.atividadeormlite.Model.Eletrodomestico;
import milton.atividadeormlite.Model.Historico;
import milton.atividadeormlite.R;

public class CadastrarUtilizacaoActivity extends AppCompatActivity {


    Spinner spinnerNomeEletro,
            spinnerMetodoCalc;

    EditText editTextTempoUtilizacao,
            editTextValorKwH;
    boolean cliqueLongo = false;


    Button btnEnviar;
    Button btnBuscar;
    ListView listViewHistorico;

    Eletrodomestico eletrodomestico = new Eletrodomestico();
    Eletrodomestico eletrodomesticoSelecionado;
    Historico historico;
    ListView listViewEletrodomesticos;
    Historico hist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_utilizacao);
        recebeViews();
        carregaSpinnerEletrodomestico();
        carregaSpinnerMetodoCalculo();
        listaHistorico();
        buscaPotencia();
    }

    public void recebeViews() {

        spinnerNomeEletro = (Spinner) findViewById(R.id.spinnerNomeEletro);
        spinnerMetodoCalc = (Spinner) findViewById(R.id.spinnerMetodoCalc);

        editTextTempoUtilizacao = (EditText) findViewById(R.id.editTextTempoUtilizacao);
        editTextValorKwH = (EditText) findViewById(R.id.editTextValorKwH);

        btnEnviar = (Button) findViewById(R.id.btnEnviarUtilizacao);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);

        listViewHistorico = (ListView) findViewById(R.id.listViewHistorico);
    }

    public void carregaSpinnerEletrodomestico() {

        try {
            final Dao<Eletrodomestico, Integer> eletrodomesticoDao = MyORMLiteHelper.getInstance(CadastrarUtilizacaoActivity.this).getEletrodomesticoDao();
            ArrayList<Eletrodomestico> listEletro = (ArrayList) eletrodomesticoDao.queryForAll();

            ArrayAdapter<Eletrodomestico> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listEletro);
            spinnerNomeEletro.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(CadastrarUtilizacaoActivity.this, "Erro carregaSpinnerEletro() " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void carregaSpinnerMetodoCalculo() {

        try {
            String[] list = {"Por Minuto", "Por Hora"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            spinnerMetodoCalc.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(CadastrarUtilizacaoActivity.this, "Erro carregaSpinnerMetodoCalc() " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void cadastraUtilizacao(View v) {

        historico = new Historico();
        eletrodomestico = (Eletrodomestico) spinnerNomeEletro.getSelectedItem();
        Eletrodomestico ele = new Eletrodomestico();

        ele.setNome(eletrodomestico.getNome());
        ele.setMarca(eletrodomestico.getMarca());
        ele.setPotencia(eletrodomestico.getPotencia());
        if (spinnerMetodoCalc.getSelectedItem().toString().equals("Por Minuto")) {

            Integer minutos = Integer.parseInt(editTextTempoUtilizacao.getText().toString());
            Double potencia = eletrodomestico.getPotencia();
            Double valorKwh = Double.parseDouble(editTextValorKwH.getText().toString());

            historico.setTotalGasto(historico.calculoPorMinuto(potencia, minutos, valorKwh));
        } else {

            Integer minutos = Integer.parseInt(editTextTempoUtilizacao.getText().toString());
            Double potencia = eletrodomestico.getPotencia();
            Double valorKwh = Double.parseDouble(editTextValorKwH.getText().toString());

            historico.setTotalGasto(historico.calculoPorHora(potencia, minutos, valorKwh));
        }

        historico.setTempoUtilizado(Integer.parseInt(editTextTempoUtilizacao.getText().toString()));

        try {

            Dao<Historico, Integer> historicoDao = MyORMLiteHelper.getInstance(CadastrarUtilizacaoActivity.this).getHistoricoDao();

            historicoDao.create(historico);

            this.listaHistorico();

            buscaPotencia();
            Toast.makeText(CadastrarUtilizacaoActivity.this, "Consumo registrado !", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            System.out.println(e.getMessage());
            Toast.makeText(CadastrarUtilizacaoActivity.this, "Erro ao registrar consumo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public void registraUtilizacao(View v) {

        Eletrodomestico eletrodomestico;
        Historico historico = new Historico();
        int minutos;
        double potencia;
        double valorKwh;
        eletrodomestico = (Eletrodomestico) spinnerNomeEletro.getSelectedItem();

        if (spinnerMetodoCalc.getSelectedItem().toString().equals("Por Minuto")) {

            minutos = Integer.parseInt(editTextTempoUtilizacao.getText().toString());
            potencia = eletrodomestico.getPotencia();
            valorKwh = Double.parseDouble(editTextValorKwH.getText().toString());

            historico.setTotalGasto(historico.calculoPorMinuto(potencia, minutos, valorKwh));

        } else {

            minutos = Integer.parseInt(editTextTempoUtilizacao.getText().toString());
            potencia = eletrodomestico.getPotencia();
            valorKwh = Double.parseDouble(editTextValorKwH.getText().toString());

            historico.setTotalGasto(historico.calculoPorHora(potencia, minutos, valorKwh));
        }

        historico.setTempoUtilizado(Double.parseDouble(editTextTempoUtilizacao.getText().toString()));
        historico.setEletrodomestico(eletrodomestico);
        historico.setValorKwHora(Double.parseDouble(editTextValorKwH.getText().toString()));
        try {

            Dao<Historico, Integer> historicoDao = MyORMLiteHelper.getInstance(CadastrarUtilizacaoActivity.this).getHistoricoDao();

            historicoDao.create(historico);

            this.listaHistorico();

            Toast.makeText(CadastrarUtilizacaoActivity.this, "Consumo registrado !", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            System.out.println(e.getMessage());
            Toast.makeText(CadastrarUtilizacaoActivity.this, "Erro ao registrar consumo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void listaHistorico() {

        try {

            Dao<Historico, Integer> historicoDao = MyORMLiteHelper.getInstance(CadastrarUtilizacaoActivity.this).getHistoricoDao();
            QueryBuilder<Historico, Integer> queryHistorico = MyORMLiteHelper.getInstance(CadastrarUtilizacaoActivity.this).getHistoricoDao().queryBuilder();
            QueryBuilder<Eletrodomestico, Integer> queryEletrodomestico = MyORMLiteHelper.getInstance(CadastrarUtilizacaoActivity.this).getEletrodomesticoDao().queryBuilder();


            ArrayList<Historico> list = (ArrayList) queryEletrodomestico.join(queryHistorico).where().eq("ideletrodomestico", "eletrodomestico_id").query();

            ArrayAdapter<Historico> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listViewHistorico.setAdapter(adapter);

            System.out.println("LISTAHISTORICO " + list.size());

        } catch (Exception e) {
            Toast.makeText(CadastrarUtilizacaoActivity.this, "Erro ao listar histórico: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void buscaPotencia() {

        try {
            Historico historico = new Historico();
            List<Historico> list;
            int i = 0;
            Dao<Historico, Integer> historicoDao = MyORMLiteHelper.getInstance(CadastrarUtilizacaoActivity.this).getHistoricoDao();
            list = historicoDao.queryForAll();

            while (i < list.size()) {

                historico = list.get(i);

                i++;
            }
            editTextValorKwH.setText(String.valueOf(historico.getValorKwHora()));
        } catch (Exception e) {

            System.out.print(e.getMessage());

        }
    }

    public void escanearQR(View v) {

        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(this, "Você precisa baixar o Barcode", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(CadastrarUtilizacaoActivity.this);

            if (resultCode == RESULT_OK) {

                final String result = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");

                try {

                    final Dao<Eletrodomestico, Integer> eletrodomesticoDao = MyORMLiteHelper.getInstance(this).getEletrodomesticoDao();

                    final AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarUtilizacaoActivity.this);

                    alerta.setNegativeButton("FECHAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    alerta.setPositiveButton("Histórico", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<Historico> listTeste = null;


                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(CadastrarUtilizacaoActivity.this);


                            try {

                                final Dao<Historico, Integer> historicoDao = MyORMLiteHelper.getInstance(CadastrarUtilizacaoActivity.this).getHistoricoDao();
                                final QueryBuilder<Eletrodomestico, Integer> queryEletrodomestico = MyORMLiteHelper.getInstance(CadastrarUtilizacaoActivity.this).getEletrodomesticoDao().queryBuilder();
                                final QueryBuilder<Historico, Integer> queryHistorico = MyORMLiteHelper.getInstance(CadastrarUtilizacaoActivity.this).getHistoricoDao().queryBuilder();

                                ListView lvHistoricos = new ListView(getApplicationContext());
                                eletrodomesticoSelecionado = (Eletrodomestico) queryEletrodomestico.join(queryHistorico).where().eq("ideletrodomestico", result).query();

                                Collection<Historico> myCollection = eletrodomesticoSelecionado.getHistoricos();
                                ArrayList<Historico> listHistoricoIt = new ArrayList<Historico>();
                                List<Historico> list = new ArrayList<Historico>(myCollection);
                                listTeste = (ArrayList) queryEletrodomestico.join(queryHistorico).where().eq("ideletrodomestico", result).query();

                                Toast.makeText(CadastrarUtilizacaoActivity.this, "Tamanho lista " + listTeste.size(), Toast.LENGTH_SHORT).show();


                                Iterator<Historico> it = eletrodomesticoSelecionado.getHistoricos().iterator();
                                double soma = 0;

                                while (it.hasNext()) {
                                    hist = it.next();
                                    listHistoricoIt.add(hist);
                                    soma = soma + hist.getTotalGasto();
                                }

                                ArrayAdapter<Historico> adapterHistorico = new ArrayAdapter<>(CadastrarUtilizacaoActivity.this, android.R.layout.simple_list_item_1,
                                        listTeste);

                                //layout dentro do alert com os edittexts
                                final Context context = getApplicationContext();
                                final LinearLayout layout = new LinearLayout(context);
                                final TextView textViewTotalGasto = new TextView(CadastrarUtilizacaoActivity.this);
                                final TextView textViewResultado = new TextView(CadastrarUtilizacaoActivity.this);
                                textViewTotalGasto.setText("Total Gasto: R$ - " + String.valueOf(soma));
                                layout.setOrientation(LinearLayout.VERTICAL);
                                lvHistoricos.setAdapter(adapterHistorico);
                                layout.addView(textViewTotalGasto);
                                layout.addView(lvHistoricos);
                                builderSingle.setView(layout);
                                builderSingle.show();

                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                                Toast.makeText(CadastrarUtilizacaoActivity.this, "erroo " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                    alerta.show();


                } catch (Exception e) {
                    System.out.print(e.getMessage());

                    Toast.makeText(CadastrarUtilizacaoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


}

