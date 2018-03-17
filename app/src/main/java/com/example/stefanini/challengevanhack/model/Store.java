package com.example.stefanini.challengevanhack.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public class Store implements Serializable{

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("cousineId")
    private long cousineId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCousineId() {
        return cousineId;
    }

    public void setCousineId(long cousineId) {
        this.cousineId = cousineId;
    }
}
