package com.example.orderservice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
	
	 private JTable ordemServicoTable;
	 private List<OrdemServico> ordensDeServico;
	 private DefaultTableModel tableModel;
	 private Database db;
	
	public MainWindow() throws SQLException {
        super("Cardin OS"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        db = new Database("jdbc:mysql://localhost:3306/osdata","root", "root32a");
        db.connect();
        //ordensDeServico = db.getOrdensDeServico();
        
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Arquivo");
        menuBar.add(fileMenu);

        JMenuItem novoItem = new JMenuItem("Novo");
        novoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	criarNovaOS();
            }
        });
        
        fileMenu.add(novoItem);

        JMenuItem abrirItem = new JMenuItem("Abrir OS");
        abrirItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        fileMenu.add(abrirItem);

        /*JMenuItem salvarItem = new JMenuItem("Salvar");
        salvarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	OsMain frame1 = new OsMain(); 
            }
        });
        
        fileMenu.add(salvarItem);*/

        JMenuItem sairItem = new JMenuItem("Sair");
        sairItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); 
            }
        });
        
        fileMenu.add(sairItem);

        JMenu editMenu = new JMenu("Editar");
        menuBar.add(editMenu);

        JMenuItem excluirItem = new JMenuItem("Excluir");
        excluirItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordemServicoTable.getSelectedRow();
                if (selectedRow != -1) {
                	String numero = (String) ordemServicoTable.getValueAt(selectedRow, 0);
                    OrdemServico selectedOs = ordensDeServico.get(selectedRow);
                    int confirm = JOptionPane.showConfirmDialog(MainWindow.this, "Deseja apagar a OS" + " " + selectedOs.getNumero() + "?");
                    if(confirm == JOptionPane.YES_OPTION) {
                    db.deleteOrdemServico(selectedOs);
                	ordensDeServico.remove(selectedRow);
                	tableModel.removeRow(selectedRow);
                    }
                }
            }
        });
        
        editMenu.add(excluirItem);
        
        JMenu servmenu = new JMenu("Ordens de Serviço");
        menuBar.add(servmenu);
        
        JMenu dataItem = new JMenu("Por Data");
        dataItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        servmenu.add(dataItem);
        
        JMenuItem maisAntigoItem = new JMenuItem("Mais Antigo");
        maisAntigoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orderPerDate(true);
            }
        });
        dataItem.add(maisAntigoItem);
        
        JMenuItem maisNovoItem = new JMenuItem("Mais Novo");
        maisNovoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orderPerDate(false);
            }
        });
        dataItem.add(maisNovoItem);
        
        JMenuItem clientItem = new JMenuItem("Por Cliente");
        clientItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String clientName = JOptionPane.showInputDialog("Informe o nome do Cliente");
            	if (clientName == null) return;
            	
            	List<OrdemServico> filteredList = new ArrayList<>();
            	for(OrdemServico os : ordensDeServico) {
            		if(os.getNome().toLowerCase().contains(clientName.toLowerCase())) {
            			filteredList.add(os);
            		}
            	}
            	
            	tableModel.setRowCount(0);
            	for (OrdemServico os : filteredList) {
            		tableModel.addRow(new Object[] {os.getNumero(), os.getNome(), os.getTipo(), os.getDataEmissao()});
            	}
            }
        });
        
        servmenu.add(clientItem);
        
        JMenuItem ResetItem = new JMenuItem("Resetar");
        ResetItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.setRowCount(0);
		        for (OrdemServico os : ordensDeServico) {
		            tableModel.addRow(new Object[] {os.getNumero(), os.getNome(), os.getTipo(), os.getDataEmissao()});
		        }
			}
		});
        
        servmenu.add(ResetItem);
        
        setJMenuBar(menuBar);

        // Adicione uma caixa de seleção para ordens de serviço
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());


        // Crie uma lista de ordens de serviço
        ordensDeServico = new ArrayList<>();
        
        String[] columnNames = {"Número da OS", "Nome da OS", "Tipo da Os" ,"Data de Emissão"};
        tableModel = new DefaultTableModel(columnNames, 0) {
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
        		return false;
        	}
        };
        
        ResultSet rs = db.getOrdensDeServico();
        ordensDeServico = new ArrayList<>();
        
        while (rs.next()) {
            //Formatando a data de emissao para padrao brasileiro
            String dataEmissao = rs.getString("DataEmissao");
            DateTimeFormatter inputforFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            DateTimeFormatter outputforFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(dataEmissao, inputforFormatter);
            String formattedDate = outputforFormatter.format(date);
        	
        	
            OrdemServico os = new OrdemServico(
                rs.getString("numeroOs"),
                rs.getString("NomeOs"),
                rs.getString("tipoOs"),
                //rs.getString("DataEmissao")
                formattedDate
            );
            
            ordensDeServico.add(os);
            tableModel.addRow(new Object[] {os.getNumero(), os.getNome(), os.getTipo(), os.getDataEmissao()});
            
        }
        
        rs.close();

        // Crie a tabela para mostrar as ordens de serviço
        ordemServicoTable = new JTable(tableModel);
        ordemServicoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane tableScrollPane = new JScrollPane(ordemServicoTable);
        contentPane.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        GridLayout layout = new GridLayout(3, 1);
        buttonPanel.setLayout(layout);
        layout.setVgap(25);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15,5,15,8));
   
        JButton criarButton = new JButton("Criar OS");
        criarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Abra uma janela para criar uma nova OS
                criarNovaOS();
            }
        });
        buttonPanel.add(criarButton);

        // Adicione um botão para visualizar a OS selecionada
        JButton visualizarButton = new JButton("Visualizar");
        visualizarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int selectedIndex = ordemServicoTable.getSelectedRow();

                if (selectedIndex != -1) {
                    // Get the numeroOs value from the selected row
                    String numeroOs = (String) ordemServicoTable.getValueAt(selectedIndex, 0);
                    String nomeOs = (String) ordemServicoTable.getValueAt(selectedIndex, 1);

                    // Open the OsWindow with the numeroOs value
                    OsMain osMain = null;
					try {
						osMain = new OsMain(numeroOs, nomeOs);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    try {
						osMain.setNumeroOs(numeroOs);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    osMain.setVisible(true);
                } else {
                    // Show a message to the user to select a row
                    JOptionPane.showMessageDialog(MainWindow.this, "Selecione uma linha para vizualizar.");
                }
            }
        });        
        buttonPanel.add(visualizarButton);
        
        // Botão Apagar OS
        JButton apagarButton = new JButton("Apagar OS");
        apagarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordemServicoTable.getSelectedRow();
                if (selectedRow != -1) {
                	//String numero = (String) ordemServicoTable.getValueAt(selectedRow, 0);
                    OrdemServico selectedOs = ordensDeServico.get(selectedRow);
                    int confirm = JOptionPane.showConfirmDialog(MainWindow.this, "Deseja apagar a OS" + " " + selectedOs.getNumero() + "?");
                    if(confirm == JOptionPane.YES_OPTION) {
                    db.deleteOrdemServico(selectedOs);
                	ordensDeServico.remove(selectedRow);
                	tableModel.removeRow(selectedRow);
                    }
                }
            }
        });
        buttonPanel.add(apagarButton);

        contentPane.add(buttonPanel, BorderLayout.EAST);

        getContentPane().add(contentPane);
        
        
        // Defina o tamanho da janela e a posição
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Mostra a janela
        setVisible(true);
        
	}

    private void criarNovaOS() {
    	String[] inputs = new String[4];
    	String[] prompts = {"Informe o número da OS:", "Informe o nome da OS:", "Informe o tipo da OS:", "Informe a data de emissão:"};
    	
    	for (int i = 0; i < 4; i++) {
    		inputs[i] = JOptionPane.showInputDialog(prompts[i]);
    		if (inputs[i] == null) return;
    	}
    	
        OrdemServico novaOs = new OrdemServico(inputs[0], inputs[1], inputs[2], inputs[3]);
        db.insertOrdemServico(novaOs);
        ordensDeServico.add(novaOs);
        tableModel.addRow(new Object[] {novaOs.getNumero(), novaOs.getNome(), novaOs.getTipo(), novaOs.getDataEmissao()});
	}
    
    private void orderPerDate(boolean oldestfirst) {
    	tableModel.setRowCount(0);
    	
    	
    	Collections.sort(ordensDeServico, new Comparator<OrdemServico>() {
    		public int compare(OrdemServico os1, OrdemServico os2) {
    			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
    			LocalDate date1 = LocalDate.parse(os1.getDataEmissao(), formatter);
    			LocalDate date2 = LocalDate.parse(os2.getDataEmissao(), formatter);
    			
    			if (oldestfirst) {
    				return date1.compareTo(date2);
    			} else {
    				return date2.compareTo(date1);
    			}
    		}
		});
    	
    	for (OrdemServico os : ordensDeServico) {
    		tableModel.addRow(new Object[] {os.getNumero(), os.getNome(), os.getTipo(), os.getDataEmissao()});
    	}
    }
    
}


