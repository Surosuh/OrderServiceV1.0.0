package com.example.orderservice;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OsMain extends JFrame{
	
    private JTable marcenariaTable;
    private JTable serralheriaTable;
    private JTable producaoTable;
    private DefaultTableModel marcenariaModel;
    private DefaultTableModel serralheriaModel;
    private DefaultTableModel producaoModel;
    private List<Marcenaria> Marcenaria;
    private List<Serralheria> Serralheria;
    private List<Producao> Producao;
    private Database database;
    
    public OsMain(String numeroOs, String nomeOs) throws SQLException {
        // Set up JFrame
        super( "NUMERO DA OS:" + " " + numeroOs + " " + "NOME DA OS:" + " " + nomeOs);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new GridLayout(4, 1));
        
        Marcenaria = new ArrayList<>();
        Serralheria = new ArrayList<>();
        Producao = new ArrayList<>();
        
        database = new Database("jdbc:mysql://localhost:3306/osdata", "root", "root32a");
        database.connect();

        // Create models 
        marcenariaModel = new DefaultTableModel();
        marcenariaModel.addColumn("Material");
	    marcenariaModel.addColumn("Medida(cm.)");
	    marcenariaModel.addColumn("Espessura(mm.)");
	    marcenariaModel.addColumn("Quantidade");
	    marcenariaModel.addColumn("Cor");

        serralheriaModel = new DefaultTableModel();
        serralheriaModel.addColumn("Material");
        serralheriaModel.addColumn("Medida(cm.)");
        serralheriaModel.addColumn("Tamanho(mm.)");
        serralheriaModel.addColumn("Quantidade");
        serralheriaModel.addColumn("Pintura(Cor)");

        producaoModel = new DefaultTableModel();
        producaoModel.addColumn("Acabamento");
        producaoModel.addColumn("Tipo de impressão");
        producaoModel.addColumn("Espessura(mm.)");
        producaoModel.addColumn("Cor");

        // Create JTables with models
        marcenariaTable = new JTable(marcenariaModel);
        serralheriaTable = new JTable(serralheriaModel);
        producaoTable = new JTable(producaoModel);

        // Create panels for each department
        JPanel marcenariaPanel = createDepartmentPanel("Marcenaria", marcenariaTable);
        JPanel serralheriaPanel = createDepartmentPanel("Serralheria", serralheriaTable);
        JPanel producaoPanel = createDepartmentPanel("Produção", producaoTable);
        
        // Create a panel for the buttons at the bottom
        JPanel buttonPanel = new JPanel();
        JButton criarButton = new JButton("Criar");
        criarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Marcenaria", "Serralheria", "Produção"};
                String selectedTable = (String) JOptionPane.showInputDialog(null, "Selecione a tabela", "Criar", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (selectedTable != null) {
                    if (selectedTable.equals("Marcenaria")) {
                    	CriarNovoMarcenaria();
                    	
                    } else if (selectedTable.equals("Serralheria")) {
                    	CriarNovoSerralheria();
                    	
                    } else if (selectedTable.equals("Produção")) {
                    	CriarNovoProducao();

                    }
                }
            }
        });

        JButton deletarButton = new JButton("Deletar");
        deletarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowMarcenaria = marcenariaTable.getSelectedRow();
                int selectedRowSerralheria = serralheriaTable.getSelectedRow();
                int selectedRowProducao = producaoTable.getSelectedRow();

                if (selectedRowMarcenaria != -1) {
                	Marcenaria selectedmarcenaria = Marcenaria.get(selectedRowMarcenaria);
                	int confirm = JOptionPane.showConfirmDialog(OsMain.this, "Deseja apagar esta linha da tabela Marcenaria?");
                	if(confirm == JOptionPane.YES_OPTION) {
                		database.deleteMarcenaria(selectedmarcenaria);
                		Marcenaria.remove(selectedRowMarcenaria);
                		marcenariaModel.removeRow(selectedRowMarcenaria);
                	}

                } else if (selectedRowSerralheria != -1) {
                	Serralheria selectedSerralheria = Serralheria.get(selectedRowSerralheria);
                	int confirm = JOptionPane.showConfirmDialog(OsMain.this, "Deseja apagar esta linha da tabela Serralheria?");
                	if(confirm == JOptionPane.YES_OPTION) {
                		database.deleteSerralheria(selectedSerralheria);
                		Serralheria.remove(selectedRowSerralheria);
                		serralheriaModel.removeRow(selectedRowSerralheria);
                	}

                } else if (selectedRowProducao != -1) {
                	Producao selectedproducao = Producao.get(selectedRowProducao);
                	int confirm = JOptionPane.showConfirmDialog(OsMain.this, "Deseja apagar esta linha da tabela Produção?");
                	if(confirm == JOptionPane.YES_OPTION) {
                		database.deleteProducao(selectedproducao);
                		Producao.remove(selectedRowProducao);
                		producaoModel.removeRow(selectedRowProducao);
                	}

                }
            }
        });

        buttonPanel.add(criarButton);
        buttonPanel.add(deletarButton);


        add(marcenariaPanel);
        add(serralheriaPanel);
        add(producaoPanel);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    // Method to create panels for each department
    private JPanel createDepartmentPanel(String departmentName, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(departmentName));

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    
	public void setNumeroOs(String numeroOs) throws SQLException {
	    String query = "SELECT Material, Medida, Espessura, Quantidade, Cor FROM Marcenaria WHERE numeroOs = ?";
	    String[] columnNames = {"Material", "Medida", "Espessura", "Quantidade", "Cor"};
	    fillTable(marcenariaModel, query, columnNames, numeroOs);

	    query = "SELECT Material, Medida, Tamanho, Quantidade, Pintura FROM Serralheria WHERE numeroOs = ?";
	    columnNames = new String[] {"Material", "Medida", "Tamanho", "Quantidade", "Pintura"};
	    fillTable(serralheriaModel, query, columnNames, numeroOs);

	    query = "SELECT Acabamento, TipoImpressao, Espessura, Cor FROM Producao WHERE numeroOs = ?";
	    columnNames = new String[] {"Acabamento", "TipoImpressao", "Espessura", "Cor"};
	    fillTable(producaoModel, query, columnNames, numeroOs);
	}
	
	private void fillTable(DefaultTableModel model, String query, String[] columnNames, String numeroOs) throws SQLException {
	    /*Database db = new Database("jdbc:mysql://localhost:3306/osdata", "root", "root32a");
	    db.connect();*/

	    PreparedStatement pstmt = database.getConnection().prepareStatement(query);
	    pstmt.setString(1, numeroOs);

	    ResultSet rs = pstmt.executeQuery();
	    
	    if (rs.next()) {
	        model.setRowCount(0);
	        do {
	            Object[] row = new Object[columnNames.length];
	            for (int i = 0; i < columnNames.length; i++) {
	                row[i] = rs.getObject(columnNames[i]);
	            }
	            model.addRow(row);
	            
	            Marcenaria marcenaria = new Marcenaria(row[0].toString(), row[1].toString(), row[2].toString(), (int) row[3], row[4].toString(), Integer.parseInt(numeroOs));
	            Marcenaria.add(marcenaria);
	            
	            Serralheria serralheria = new Serralheria(row[0].toString(), row[1].toString(), row[2].toString(), (int) row[3], row[4].toString(), Integer.parseInt(numeroOs));
	            Serralheria.add(serralheria);

	            Producao producao = new Producao(row[0].toString(), row[1].toString(), row[2].toString(), row[4].toString(), Integer.parseInt(numeroOs));
	            Producao.add(producao);
	            
	        } while (rs.next());
	    } else {
	        model.setRowCount(0);
	    }
	}
	
	private void CriarNovoMarcenaria() {
    	String[] inputs = new String[6];
    	String[] prompts = {"Informe o Material: ", "Informe a Medida em cm: ", "Informe a espessura em mm: ", "Informe a Quantidade: ", "Informe a Cor:", "Informe o Numero da Os:"};
    	
    	for (int i = 0; i < 6; i++) {
    		inputs[i] = JOptionPane.showInputDialog(prompts[i]);
    		if (inputs[i] == null) return;
    	}
    	
    	int quantidade = Integer.parseInt(inputs[3]);
    	int numeroos = Integer.parseInt(inputs[5]);
    	
        Marcenaria novaMr = new Marcenaria(inputs[0], inputs[1], inputs[2], quantidade, inputs[4], numeroos);
        database.insertMarcenaria(novaMr);
        Marcenaria.add(novaMr);
        marcenariaModel.addRow(new Object[] {novaMr.getMaterial(), novaMr.getMedidaCm(), novaMr.getEspessuraMm(), novaMr.getQuantidade(), novaMr.getCor(), novaMr.getNumeroOs()});
	}
	
	private void CriarNovoSerralheria() {
    	String[] inputs = new String[6];
    	String[] prompts = {"Informe o Material: ", "Informe a Medida em cm: ", "Informe o Tamanho em mm: ", "Informe a Quantidade: ", "Informe a Pintura:", "Informe o Numero da Os:"};
    	
    	for (int i = 0; i < 6; i++) {
    		inputs[i] = JOptionPane.showInputDialog(prompts[i]);
    		if (inputs[i] == null) return;
    	}
    	
    	int quantidade = Integer.parseInt(inputs[3]);
    	int numeroos = Integer.parseInt(inputs[5]);
    	
        Serralheria novaSr = new Serralheria(inputs[0], inputs[1], inputs[2], quantidade, inputs[4], numeroos);
        database.insertSerralheria(novaSr);
        Serralheria.add(novaSr);
        serralheriaModel.addRow(new Object[] {novaSr.getMaterial(), novaSr.getMedidaCm(), novaSr.getTamanhoMm(), novaSr.getQuantidade(), novaSr.getPintura(), novaSr.getSerNumeroOs()});
	}
	
	private void CriarNovoProducao() {
    	String[] inputs = new String[5];
    	String[] prompts = {"Informe o Acabamento: ", "Informe o Tipo da Impressão: ", "Informe a espessura em mm: ", "Informe a Cor: ", "Informe o Numero da Os:"};
    	
    	for (int i = 0; i < 5; i++) {
    		inputs[i] = JOptionPane.showInputDialog(prompts[i]);
    		if (inputs[i] == null) return;
    	}
    	
    	int numeroos = Integer.parseInt(inputs[4]);
    	
        Producao novaPr = new Producao(inputs[0], inputs[1], inputs[2], inputs[3], numeroos);
        database.insertProducao(novaPr);
        Producao.add(novaPr);
        producaoModel.addRow(new Object[] {novaPr.getAcabamento(), novaPr.getTipoImpressao(), novaPr.getEspessura(), novaPr.getCor(), novaPr.getProdNumeroOs()});
	}
	
}
