package milton.atividadeormlite.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import milton.atividadeormlite.Model.Eletrodomestico;
import milton.atividadeormlite.Model.Historico;

/**
 * Created by Aluno on 10/05/2016.
 */
public class MyORMLiteHelper extends OrmLiteSqliteOpenHelper {


    //Nome da base de dados
    private static final String DATABASE_NAME = "consumo.db";

    //Versao da base de dados
    private static final int DATABASE_VERSION = 1;

    //Caso você queria ter apenas uma instancia da base de dados.
    private static MyORMLiteHelper mInstance = null;


    private Dao<Eletrodomestico, Integer> eletrodomesticosDao = null;
    private Dao<Historico, Integer> historicoDao = null;

    public MyORMLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {

            //Criar tabelas no banco de dados
            TableUtils.createTable(connectionSource, Eletrodomestico.class);
            TableUtils.createTable(connectionSource, Historico.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {

            TableUtils.dropTable(connectionSource, Eletrodomestico.class, true);
            TableUtils.dropTable(connectionSource, Historico.class, true);

            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MyORMLiteHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyORMLiteHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Para cada tabela criada, Deve ser criado o seu Dao
     *
     * @return
     * @throws SQLException
     */
    public Dao<Eletrodomestico, Integer> getEletrodomesticoDao() throws SQLException {
        if (eletrodomesticosDao == null) {
            eletrodomesticosDao = getDao(Eletrodomestico.class);
        }
        return eletrodomesticosDao;
    }

    public Dao<Historico, Integer> getHistoricoDao() throws SQLException {
        if (historicoDao == null) {
            historicoDao = getDao(Historico.class);
        }
        return historicoDao;
    }

}
