package milton.atividadeormlite.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes;

import milton.atividadeormlite.DAO.MyORMLiteHelper;
import milton.atividadeormlite.Model.Eletrodomestico;
import milton.atividadeormlite.Model.Historico;
import milton.atividadeormlite.R;

public class CadastroEletrodomesticoActivity extends Activity {

    EditText editTextNomeEletro,
            editTextMarcaEletro,
            editTextWattsEletro;
    ListView listViewEletrodomesticos;
    Button buttonCadastrar;
    Eletrodomestico eletrodomestico;
    Eletrodomestico eletrodomesticoSelecionado;
    ArrayAdapter<Eletrodomestico> adapterEletrodomesticos;
    boolean cliqueLongo = false;

    Historico hist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_eletrodomestico);
        this.recebeViews();
        this.getClique();
    }

    public void recebeViews() {

        //EditTexts
        editTextNomeEletro = (EditText) findViewById(R.id.editTextNomeEletro);
        editTextMarcaEletro = (EditText) findViewById(R.id.editTextMarcaEletro);
        editTextWattsEletro = (EditText) findViewById(R.id.editTextWattsEletro);
        //ListView
        listViewEletrodomesticos = (ListView) findViewById(R.id.listViewEletrodomesticos);
        //Button
        buttonCadastrar = (Button) findViewById(R.id.buttonCadastrar);

    }

    public void cadastroEletrodomestico(View v) {

        eletrodomestico = new Eletrodomestico();

        eletrodomestico.setNome(editTextNomeEletro.getText().toString());
        eletrodomestico.setMarca(editTextMarcaEletro.getText().toString());
        eletrodomestico.setPotencia(Integer.parseInt(editTextWattsEletro.getText().toString()));

        try {

            Dao<Eletrodomestico, Integer> eletrodomesticoDao = MyORMLiteHelper.getInstance(this).getEletrodomesticoDao();
            eletrodomesticoDao.create(eletrodomestico);
            //list
            ArrayList<Eletrodomestico> list = (ArrayList) eletrodomesticoDao.queryForAll();
            ArrayAdapter<Eletrodomestico> adapter = new ArrayAdapter<Eletrodomestico>(this, android.R.layout.simple_list_item_1, list);
            listViewEletrodomesticos.setAdapter(adapter);
            this.getClique();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(CadastroEletrodomesticoActivity.this, "Ocorreu um erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void getClique() {

        try {

            final Dao<Eletrodomestico, Integer> eletrodomesticoDao = MyORMLiteHelper.getInstance(this).getEletrodomesticoDao();
            ArrayList<Eletrodomestico> list = (ArrayList) eletrodomesticoDao.queryForAll();
            final ArrayAdapter<Eletrodomestico> adapter = new ArrayAdapter<Eletrodomestico>(this, android.R.layout.simple_list_item_1, list);
            listViewEletrodomesticos.setAdapter(adapter);

            listViewEletrodomesticos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (cliqueLongo == false) {
                        eletrodomesticoSelecionado = (Eletrodomestico) adapter.getItem(position);

                        final AlertDialog.Builder alerta = new AlertDialog.Builder(CadastroEletrodomesticoActivity.this);
                        final EditText dataDevolucao = new EditText(CadastroEletrodomesticoActivity.this);

                        //layout dentro do alert com os edittexts
                        final Context context = view.getContext();
                        final LinearLayout layout = new LinearLayout(context);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        alerta.setTitle("Eletrodomesticos");
                        alerta.setMessage("Nome: " + eletrodomesticoSelecionado.getNome() + "\n" +
                                "Marca: " + eletrodomesticoSelecionado.getMarca());

                        layout.addView(dataDevolucao);
                        alerta.setNegativeButton("EXCLUIR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {

                                    final Dao<Eletrodomestico, Integer> eletrodomesticoDao = MyORMLiteHelper.getInstance(CadastroEletrodomesticoActivity.this).getEletrodomesticoDao();
                                    eletrodomesticoDao.deleteById(eletrodomesticoSelecionado.getIdeletrodomestico());
                                    Toast.makeText(CadastroEletrodomesticoActivity.this, "Eletrodoméstico excluido com sucesso!", Toast.LENGTH_SHORT).show();
                                    getClique();
                                } catch (Exception e) {

                                }

                            }
                        });

                        alerta.setPositiveButton("HISTÓRICO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                AlertDialog.Builder builderSingle = new AlertDialog.Builder(CadastroEletrodomesticoActivity.this);
                                ListView listViewHistorico = new ListView(CadastroEletrodomesticoActivity.this);
                                ArrayList<Historico> listHistoricoIt = new ArrayList<Historico>();
                                Collection<Historico> myCollection = eletrodomesticoSelecionado.getHistoricos();
                                List<Historico> list = new ArrayList<Historico>(myCollection);

                                try {

                                    final Dao<Historico, Integer> historicoDao = MyORMLiteHelper.getInstance(CadastroEletrodomesticoActivity.this).getHistoricoDao();
                                    Iterator<Historico> it = eletrodomesticoSelecionado.getHistoricos().iterator();


                                    double soma = 0;

                                    while (it.hasNext()) {
                                        hist = it.next();
                                        listHistoricoIt.add(hist);
                                        soma = soma + hist.getTotalGasto();
                                    }

                                    ArrayAdapter<Historico> adapterHistorico = new ArrayAdapter<>(CadastroEletrodomesticoActivity.this, android.R.layout.simple_list_item_1,
                                            listHistoricoIt);

                                    //layout dentro do alert com os edittexts
                                    final Context context = getApplicationContext();
                                    final LinearLayout layout = new LinearLayout(context);
                                    final TextView textViewTotalGasto = new TextView(CadastroEletrodomesticoActivity.this);
                                    final TextView textViewResultado = new TextView(CadastroEletrodomesticoActivity.this);
                                    textViewTotalGasto.setText("Total Gasto: R$ - " + String.valueOf(soma));
                                    layout.setOrientation(LinearLayout.VERTICAL);
                                    listViewHistorico.setAdapter(adapterHistorico);
                                    layout.addView(textViewTotalGasto);
                                    layout.addView(listViewHistorico);
                                    builderSingle.setView(layout);
                                    builderSingle.show();

                                } catch (Exception e) {
                                    System.out.println(e.getMessage() + " - " + e.getCause());
                                    Toast.makeText(CadastroEletrodomesticoActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        alerta.show();
                    } else

                    {
                        cliqueLongo = false;
                    }
                }
            });

        } catch (
                Exception e
                )

        {

        }

    }
}