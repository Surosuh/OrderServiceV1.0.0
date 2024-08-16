package com.example.orderservice;

public class Producao {
	private String acabamento;
    private String tipoimpressao;
    private String espessura;
    private String cor;
    private int prodnumeroOs;
    
    public Producao(String acabamento, String tipoimpressao, String espessura, String cor, int prodnumeroOs) {
    	this.acabamento = acabamento;
    	this.tipoimpressao = tipoimpressao;
    	this.espessura = espessura;
    	this.cor = cor;
    	this.prodnumeroOs = prodnumeroOs;
    }

    public String getAcabamento() {
    	return acabamento;
    }
    
    public String getTipoImpressao() {
        return tipoimpressao;
    }

    public String getEspessura() {
        return espessura;
    }

    public String getCor() {
        return cor;
    }

    public int getProdNumeroOs() {
        return prodnumeroOs;
    }
}
