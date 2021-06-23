package model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import config.ConfigFirebase;
import helper.Base64Custom;

public class Movimentation {

    private String date;
    private String description;
    private String category;
    private String type;
    private Double value;
    private String key;



    public Movimentation() {
    }

    public void save(String ChoosedDate){
        FirebaseAuth auth = ConfigFirebase.getFirebaseAuth();
        String idUser = Base64Custom.codifing64Base(auth.getCurrentUser().getEmail());

        String datePattern = firebaseDatePatern(ChoosedDate);

        DatabaseReference firebase = ConfigFirebase.getFirebaseRef();
        firebase.child("movimentation")
                .child(idUser)
                .child(datePattern)
                .push()
                .setValue(this);
    }

    public static String firebaseDatePatern (String ChoosedDate){
        String returndate [] = ChoosedDate.split("/");
        String dia = returndate[0];
        String mes = returndate[1];
        String ano = returndate[2];

        String DatePattern = mes + ano;
        return DatePattern;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
