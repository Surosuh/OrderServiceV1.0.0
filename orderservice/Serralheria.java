package com.example.orderservice;

public class Serralheria {
    private String material;
    private String medidaCm;
    private String tamanhoMm;
    private int quantidade;
    private String pintura;
    private int sernumeroOs;
    
    public Serralheria(String material, String medidaCm, String tamanhoMm, int quantidade, String pintura, int sernumeroOs) {
    	this.material = material;
    	this.medidaCm = medidaCm;
    	this.tamanhoMm = tamanhoMm;
    	this.quantidade = quantidade;
    	this.pintura = pintura;
    	this.sernumeroOs = sernumeroOs;
    }

    public String getMaterial() {
    	return material;
    }
    
    public String getMedidaCm() {
        return medidaCm;
    }

    public String getTamanhoMm() {
        return tamanhoMm;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getPintura() {
        return pintura;
    }

    public int getSerNumeroOs() {
        return sernumeroOs;
    }
}
