package milton.atividadeormlite.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Milton on 16/05/2016.
    este é um teste para github
 */

@DatabaseTable(tableName = "historico")
public class Historico {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private int id;

    @DatabaseField(foreign = true)
    private Eletrodomestico eletrodomestico;

    @DatabaseField
    private double totalGasto;

    @DatabaseField
    private double tempoUtilizado;

    @DatabaseField
    private double valorKwHora;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Eletrodomestico getEletrodomestico() {
        return eletrodomestico;
    }

    public void setEletrodomestico(Eletrodomestico eletrodomestico) {
        this.eletrodomestico = eletrodomestico;
    }

    public double getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(double totalGasto) {
        this.totalGasto = totalGasto;
    }

    public double getTempoUtilizado() {
        return tempoUtilizado;
    }

    public void setTempoUtilizado(double tempoUtilizado) {
        this.tempoUtilizado = tempoUtilizado;
    }

    public double getValorKwHora() {
        return valorKwHora;
    }

    public void setValorKwHora(double valorKwHora) {
        this.valorKwHora = valorKwHora;
    }

    public double calculoPorMinuto(Double potencia, Integer minutos, double valor) {

        double horas, kwh, totalGasto;

        horas = minutos / 60;
        kwh = (potencia / 1000) * horas;
        totalGasto = valor * kwh;

        return totalGasto;
    }

    public double calculoPorHora(Double potencia, Integer horas, double valor) {

        double kwh, totalGasto;

        kwh = (potencia / 1000) * horas;
        totalGasto = valor * kwh;

        return totalGasto;
    }

    public ArrayList<String> getMetodoCalculo() {

        ArrayList<String> listMetodo = new ArrayList<>();

        listMetodo.set(0, "Por Minuto");
        listMetodo.set(1, "Por Hora");

        return listMetodo;
    }


    @Override
    public String toString() {
        return eletrodomestico.getNome() + " - R$:" + getTotalGasto();
    }
}
