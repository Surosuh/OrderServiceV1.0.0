package com.example.orderservice;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {

	private String url;
	private String username;
	private String password;
	private Connection connection;
	
	public Database(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public void connect() throws SQLException {
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Properties info = new Properties();
			info.put("user", username);
			info.put("password", password);
			connection = DriverManager.getConnection(url, info);
			System.out.println("connected to the database");
		} catch (ClassNotFoundException ex) {
			System.out.println("mySQL JDBC driver not found");
		}
	}
	
	public void disconnect() throws SQLException {
		if (connection != null) {
			connection.close();
			System.out.println("Disconnected from the database");
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
    public ResultSet getOrdensDeServico() throws SQLException{
    		Statement stmt = connection.createStatement();
    		ResultSet rs = stmt.executeQuery("SELECT * FROM ordemdeservico");

    	return rs;
    }
    
    public void insertOrdemServico(OrdemServico os) {
    	try {
    		//Connection conn = getConnection();
    		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO ordemdeservico (numeroOs, nomeOs, tipoOs , DataEmissao) VALUES (?, ?, ?, ?)");
    		pstmt.setInt(1, Integer.parseInt(os.getNumero()));
    		pstmt.setString(2, os.getNome());
    		pstmt.setString(3, os.getTipo());
    		
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    		LocalDate dataEmissao = LocalDate.parse(os.getDataEmissao(), formatter);
    		
    		pstmt.setDate(4, java.sql.Date.valueOf(dataEmissao));
    		pstmt.executeUpdate();
    		pstmt.close();
    		//conn.close();
    	} catch (SQLException ex) {
    		System.out.println("Error inserting data into database" + " " + ex.getMessage());
    	}
    }
    
    public void deleteOrdemServico(OrdemServico os) {
    	try {
    		//Connection conn = getConnection();
    		PreparedStatement pstmt = connection.prepareStatement("DELETE FROM ordemdeservico WHERE numeroOs = ?");
    		pstmt.setInt(1, Integer.parseInt(os.getNumero()));
    		pstmt.executeUpdate();
    		pstmt.close();
    		//conn.close();
    	} catch(SQLException ex){
    		System.out.println("Error deleting data from the database" + " " +ex.getMessage());
    	}
    }
    
    public void insertMarcenaria(Marcenaria mr) {
    	try {
    		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO marcenaria (Material, Medida, Espessura, Quantidade, Cor, numeroOs) VALUES (?, ?, ?, ?, ?, ?)");
    		pstmt.setString(1, mr.getMaterial());
    		pstmt.setString(2, mr.getMedidaCm());
    		pstmt.setString(3, mr.getEspessuraMm());
    		pstmt.setInt(4, mr.getQuantidade());
    		pstmt.setString(5, mr.getCor());
    		pstmt.setInt(6, mr.getNumeroOs());
    		
    		pstmt.executeUpdate();
    		pstmt.close();
    	}catch(SQLException ex){
    		System.out.println("Error creating into Marcenaria" + " " + ex.getMessage());
    	}
    }
    
    public void deleteMarcenaria(Marcenaria mr) {
    	try {
    		PreparedStatement pstmt = connection.prepareStatement("DELETE FROM marcenaria WHERE numeroOs = ?");
    		pstmt.setInt(1, mr.getNumeroOs());
    		pstmt.executeUpdate();
    		pstmt.close();
    	}catch(SQLException ex) {
    		System.out.println("Error deleting data from Marcenaria" + " " + ex.getMessage());
    	}
    }
    
    public void insertSerralheria(Serralheria sr) {
    	try {
    		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO serralheria (Material, Medida, Tamanho, Quantidade, Pintura, numeroOs) VALUES (?, ?, ?, ?, ?, ?)");
    		pstmt.setString(1, sr.getMaterial());
    		pstmt.setString(2, sr.getMedidaCm());
    		pstmt.setString(3, sr.getTamanhoMm());
    		pstmt.setInt(4, sr.getQuantidade());
    		pstmt.setString(5, sr.getPintura());
    		pstmt.setInt(6, sr.getSerNumeroOs());
    		
    		pstmt.executeUpdate();
    		pstmt.close();
    	}catch(SQLException ex){
    		System.out.println("Error creating into Marcenaria" + " " + ex.getMessage());
    	}
    }
    
    public void deleteSerralheria(Serralheria sr) {
    	try {
    		PreparedStatement pstmt = connection.prepareStatement("DELETE FROM serralheria WHERE numeroOs = ?");
    		pstmt.setInt(1, sr.getSerNumeroOs());
    		pstmt.executeUpdate();
    		pstmt.close();
    	}catch(SQLException ex) {
    		System.out.println("Error deleting data from Marcenaria" + " " + ex.getMessage());
    	}
    }
    
    public void insertProducao(Producao pr) {
    	try {
    		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Producao (Acabamento, TipoImpressao, Espessura, Cor, numeroOs) VALUES (?, ?, ?, ?, ?)");
    		pstmt.setString(1, pr.getAcabamento());
    		pstmt.setString(2, pr.getTipoImpressao());
    		pstmt.setString(3, pr.getEspessura());
    		pstmt.setString(4, pr.getCor());
    		pstmt.setInt(5, pr.getProdNumeroOs());
    		
    		pstmt.executeUpdate();
    		pstmt.close();
    	}catch(SQLException ex) {
    		System.out.println("Error inserting into Producao"+ " " + ex.getMessage());
    	}
    }
    
    public void deleteProducao(Producao pr) {
    	try {
    		PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Producao WHERE numeroOs = ?");
    		pstmt.setInt(1, pr.getProdNumeroOs());
    		pstmt.executeUpdate();
    		pstmt.close();
    	}catch(SQLException ex) {
    		System.out.println("Error deleting data from Producao" + " " + ex.getMessage());
    	}
    }
	
}
