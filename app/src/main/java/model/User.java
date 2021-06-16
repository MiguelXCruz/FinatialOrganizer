package model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import config.ConfigFirebase;

public class User {
    private String idUsuario;
    private String name;
    private String email;
    private String password;
    private Double totalAmount = 0.00;
    private Double totalSpending = 0.00;

    public User() {
    }

    public void save(){
        DatabaseReference firebase = ConfigFirebase.getFirebaseRef();
        firebase.child("usuario")
                .child(this.idUsuario)
                .setValue( this );


        DatabaseReference firebasedatabase = ConfigFirebase.getFirebaseRef();
        firebasedatabase.child("usuario")
                .child(this.idUsuario).setValue(this);
    }


    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalSpending() {
        return totalSpending;
    }

    public void setTotalSpending(Double totalSpending) {
        this.totalSpending = totalSpending;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
