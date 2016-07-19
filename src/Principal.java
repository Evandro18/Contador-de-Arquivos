import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;

public class Principal {

	private JFrame frame;
	private JTextField campoCaminho;
	private JTable table;
	private JTable table_1;
	private File diretorio;
	private Hashtable extenssoes;
	private List<String> listExtensoes;

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
		listExtensoes = new ArrayList<>();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(210, 180, 140));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Contador de Arquivos");
		campoCaminho = new JTextField();
		campoCaminho.setBounds(25, 39, 306, 19);
		frame.getContentPane().add(campoCaminho);
		campoCaminho.setColumns(10);

		JButton btnAbrir = new JButton("Abrir...");
		btnAbrir.setBackground(new Color(135, 206, 250));
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int res = fc.showOpenDialog(frame);
				if (res == JFileChooser.APPROVE_OPTION) {
					diretorio = fc.getSelectedFile();
				}
				campoCaminho.setFocusable(true);
				String caminho = diretorio.getPath();
				campoCaminho.setText(caminho);
			}
		});
		btnAbrir.setBounds(343, 36, 91, 25);
		frame.getContentPane().add(btnAbrir);

		JLabel lblDiretrio = new JLabel("Diretório");
		lblDiretrio.setBounds(25, 22, 66, 15);
		frame.getContentPane().add(lblDiretrio);

		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setBackground(new Color(135, 206, 250));
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
		String[][] dados = new String[listExtensoes.size()][2];
		int i = 0;
		for (String strExtensao : listExtensoes) {
			dados[i][0] = strExtensao;
			System.out.println(strExtensao);
			dados[i][1] = String.valueOf(extenssoes.get(strExtensao));
			TableModel model = new TableModel(dados);
			table_1.setModel(model);
			i++;
		}
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
							if (extenssoes.containsKey(extensao)) {
								 int qtd = (int) extenssoes.get(extensao) + 1;
								 extenssoes.put(extensao, qtd);
							} else {
								extenssoes.put(extensao, 1);
								listExtensoes.add(extensao);
							}
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
