package com.example.orderservice;

public class Marcenaria {
    private String material;
    private String medidaCm;
    private String espessuraMm;
    private int quantidade;
    private String cor;
    private int numeroOs;
    private String id;
    
    public Marcenaria(String material, String medidaCm, String espessuraMm, int quantidade, String cor, int numeroOs) {
    	this.material = material;
    	this.medidaCm = medidaCm;
    	this.espessuraMm = espessuraMm;
    	this.quantidade = quantidade;
    	this.cor = cor;
    	this.numeroOs = numeroOs;
    }

    public String getMaterial() {
    	return material;
    }
    
    public String getMedidaCm() {
        return medidaCm;
    }

    public String getEspessuraMm() {
        return espessuraMm;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getCor() {
        return cor;
    }

    public int getNumeroOs() {
        return numeroOs;
    }
    
    public String getid() {
    	return id;
    }
}
