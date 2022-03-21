package kirimatt.currencytransfer.daos;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class JsonDao implements Serializable {

    private static final long serialVersionUID = -7985517136515437414L;
    @SerializedName("Date")
    private String date;
    @SerializedName("PreviousDate")
    private String previousDate;
    @SerializedName("PreviousURL")
    private String previousURL;
    @SerializedName("Timestamp")
    private String timestamp;
    @SerializedName("Valute")
    private Map<String, CurrencyDao> currencyMap;

    public JsonDao() {
    }

    public JsonDao(String date, String previousDate, String previousURL,
                   String timestamp, Map<String, CurrencyDao> currencyMap) {
        this.date = date;
        this.previousDate = previousDate;
        this.previousURL = previousURL;
        this.timestamp = timestamp;
        this.currencyMap = currencyMap;
    }

    public JsonDao(JsonDao jsonDao) {
        this.date = jsonDao.getDate();
        this.previousDate = jsonDao.getPreviousDate();
        this.previousURL = jsonDao.getPreviousURL();
        this.timestamp = jsonDao.getTimestamp();
        this.currencyMap = jsonDao.getCurrencyMap();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

    public String getPreviousURL() {
        return previousURL;
    }

    public void setPreviousURL(String previousURL) {
        this.previousURL = previousURL;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, CurrencyDao> getCurrencyMap() {
        return currencyMap;
    }

    public void setCurrencyMap(Map<String, CurrencyDao> currencyMap) {
        this.currencyMap = currencyMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "JsonDao{" +
                "date='" + date + '\'' +
                ", previousDate='" + previousDate + '\'' +
                ", previousURL='" + previousURL + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", currency=" + currencyMap +
                '}';
    }
}