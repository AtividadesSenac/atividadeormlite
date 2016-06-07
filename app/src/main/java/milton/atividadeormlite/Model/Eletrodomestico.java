package milton.atividadeormlite.Model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Aluno on 10/05/2016.
 */
@DatabaseTable(tableName = "eletrodomestico")
public class Eletrodomestico {


    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true, columnName = "ideletrodomestico")
    private Integer ideletrodomestico;
    @DatabaseField(width = 150)
    private String nome;
    @DatabaseField(width = 150)
    private String marca;
    @DatabaseField
    private double potencia;
    @ForeignCollectionField
    private Collection<Historico> historicos;


    public Eletrodomestico() {
    }

    public Integer getIdeletrodomestico() {
        return ideletrodomestico;
    }

    public void setIdeletrodomestico(Integer ideletrodomestico) {
        this.ideletrodomestico = ideletrodomestico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPotencia() {
        return potencia;
    }

    public void setPotencia(double potencia) {
        this.potencia = potencia;
    }


    public Collection<Historico> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(Collection<Historico> historicos) {
        this.historicos = historicos;
    }

    @Override
    public String toString() {
        return this.getNome() + " - " + this.getMarca() + " " + this.getPotencia();
    }
}
