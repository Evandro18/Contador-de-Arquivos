import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Principal {

	private JFrame frame;
	private JTextField textField;
	private JTable table;
	private JTable table_1;
	private File diretorio;
	private Hashtable extenssoes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal window = new Principal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Principal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Contador de Arquivos");
		textField = new JTextField();
		textField.setBounds(25, 39, 298, 19);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnAbrir = new JButton("Abrir...");
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int res = fc.showOpenDialog(frame);
				if (res == JFileChooser.APPROVE_OPTION) {
					diretorio = fc.getSelectedFile();
					System.out.println(diretorio.getName());
				}
			}
		});
		btnAbrir.setBounds(343, 36, 91, 25);
		frame.getContentPane().add(btnAbrir);

		JLabel lblDiretrio = new JLabel("Diretório");
		lblDiretrio.setBounds(30, 12, 66, 15);
		frame.getContentPane().add(lblDiretrio);

		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setBounds(173, 98, 114, 25);
		btnIniciar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				extenssoes = new Hashtable<>();
				varreDiretorios(diretorio.getAbsolutePath());
				preencherTabela();
			}
		});
		frame.getContentPane().add(btnIniciar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 135, 434, 128);
		frame.getContentPane().add(scrollPane);

		table_1 = new JTable();
		scrollPane.setRowHeaderView(table_1);
	}

	private void preencherTabela() {
		String[][] dados = new String[1][2];
		dados[0][0] = "doc";
		dados[0][1] = "3";
		TableModel model = new TableModel(dados);
		table_1.setModel(model);
	}

	public void varreDiretorios(String caminhoDiretorio) {
		File arquivo = abreArquivo(caminhoDiretorio);
		if (arquivo != null && arquivo.exists() && arquivo.isDirectory()) {
			varreDiretorios(arquivo, "");
		}
	}

	private void varreDiretorios(File diretorio, String tabulacoes) {
		String[] listaDiretorios = diretorio.list();
		if (listaDiretorios != null && listaDiretorios.length > 0) {
			for (String nomeSubDiretorio : diretorio.list()) {
				nomeSubDiretorio = diretorio.getPath() + "/" + nomeSubDiretorio;
				File subDiretorio = abreArquivo(nomeSubDiretorio);
				if (subDiretorio != null && subDiretorio.exists()) {
					if (subDiretorio.isDirectory()) {
						varreDiretorios(subDiretorio, tabulacoes + "\t");
					} else {
						String[] partes = subDiretorio.getName().split("\\.");
						if (partes.length > 1) {
							String extensao = partes[partes.length - 1];
							System.out.println(extensao);
							if (extenssoes.containsKey(extensao)) {
								 //Fazer alguma coisa
							} else {
//								fazer alguma coisa
							}
							extenssoes.put(extensao, 0);
						}
					}
				}
			}
		}
	}

	public File abreArquivo(String caminhoArquivo) {
		File arquivo = new File(caminhoArquivo);
		if (arquivo.exists()) {
			return arquivo;
		}
		return null;
	}
}
