import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class TableModel extends DefaultTableModel {

	public static final String[] NOMES_COLUNAS = { "TIPO", "QUANTIDADE"};

	public TableModel(String[][] dados) {
		super(dados, NOMES_COLUNAS);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
