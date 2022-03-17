package kirimatt.currencytransfer.daos;


import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Map;

public class CurrencyDAO implements Serializable {
    private String id;
    private Integer numCode;
    private String charCode;
    private Double nominal;
    private String name;
    private Double value;
    private Double previous;

    public CurrencyDAO(String id, Integer numCode, String charCode, Double nominal,
                       String name, Double value, Double previous) {
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
        this.previous = previous;
    }

    public CurrencyDAO(Map map) {
        this.id = String.valueOf(map.get("ID"));
        this.numCode = Integer.valueOf(String.valueOf(map.get("NumCode")));
        this.charCode = String.valueOf(map.get("CharCode"));
        this.nominal = Double.valueOf(String.valueOf(map.get("Nominal")));
        this.name = String.valueOf(map.get("Name"));
        this.value = Double.valueOf(String.valueOf(map.get("Value")));
        this.previous = Double.valueOf(String.valueOf(map.get("Previous")));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumCode() {
        return numCode;
    }

    public void setNumCode(Integer numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public Double getNominal() {
        return nominal;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getPrevious() {
        return previous;
    }

    public void setPrevious(Double previous) {
        this.previous = previous;
    }

    @NonNull
    @Override
    public String toString() {
        return "CurrencyDAO{" +
                "id='" + id + '\'' +
                ", numCode=" + numCode +
                ", charCode='" + charCode + '\'' +
                ", nominal=" + nominal +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", previous=" + previous +
                '}';
    }
}
