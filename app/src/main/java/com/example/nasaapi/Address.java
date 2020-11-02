package com.example.nasaapi;

public class Address {
    private int idAdress;
    private String Adress;

    public Address(int idAdress, String adress) {
        this.idAdress = idAdress;
        Adress = adress;
    }

    public int getIdAdress() {
        return idAdress;
    }

    public void setIdAdress(int idAdress) {
        this.idAdress = idAdress;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }
}
