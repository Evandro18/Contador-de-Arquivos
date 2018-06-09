package br.edu.ifms.contador;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class TableModel extends DefaultTableModel {

    private static final String[] NOMES_COLUNAS = {"Tipo", "Quantidade"};

    TableModel(String[][] dados) {
        super(dados, NOMES_COLUNAS);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}