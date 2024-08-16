package com.example.orderservice;

public class OrdemServico {
	 	private String numero;
	    private String dataEmissao;
	    private String nome;
	    private String tipo;
	    
	    
	    public OrdemServico(String numero, String nome, String tipo ,String dataEmissao) {
	        this.numero = numero;
	        this.nome = nome;
	        this.tipo = tipo;
	        this.dataEmissao = dataEmissao;
	    }
	    
	    public String getNome(){
	    	return nome;
	    }
	    
	    public String getTipo() {
	    	return tipo;
	    }
	    
	    public String getNumero() {
	        return numero;
	    }

	    public String getDataEmissao() {
	        return dataEmissao;
	    }
}
