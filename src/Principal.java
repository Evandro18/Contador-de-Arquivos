import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

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
import javax.swing.JPanel;

public class Principal {

	private JFrame janela;
	private JTextField campoCaminho;
	private JTable table;
	private File diretorio;
	private Hashtable<String, Integer> extenssoes;
	private List<String> listExtensoes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal window = new Principal();
					window.janela.setVisible(true);
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
		janela = new JFrame();
		janela.getContentPane().setBackground(new Color(210, 180, 140));
		janela.setBounds(100, 100, 712, 533);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.getContentPane().setLayout(null);
		janela.setTitle("Contador de Arquivos");
		campoCaminho = new JTextField();
		campoCaminho.setBounds(158, 36, 385, 22);
		janela.getContentPane().add(campoCaminho);
		campoCaminho.setColumns(10);
		listExtensoes = new ArrayList<>();
		JButton btnAbrir = new JButton("Abrir...");
		btnAbrir.setBackground(new Color(135, 206, 250));
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int res = fc.showOpenDialog(janela);
				if (res == JFileChooser.APPROVE_OPTION) {
					diretorio = fc.getSelectedFile();
				}
				campoCaminho.setFocusable(true);
				String caminho = diretorio.getPath();
				campoCaminho.setText(caminho);
			}
		});
		btnAbrir.setBounds(555, 36, 91, 25);
		janela.getContentPane().add(btnAbrir);

		JLabel lblDiretrio = new JLabel("Diretório");
		lblDiretrio.setBounds(74, 39, 66, 15);
		janela.getContentPane().add(lblDiretrio);

		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setBackground(new Color(135, 206, 250));
		btnIniciar.setBounds(230, 70, 114, 25);
		btnIniciar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				extenssoes = new Hashtable<>();
				varreDiretorios(diretorio.getAbsolutePath());
				preencherTabela();
			}
		});
		janela.getContentPane().add(btnIniciar);

		table = new JTable();
		preencherTabela();
		JScrollPane painelRolagem = new JScrollPane();
		painelRolagem.setBounds(12, 98, 125, 391);
		janela.getContentPane().add(painelRolagem, "cell 1 8,alignx left");
		painelRolagem.setViewportView(table);
	}

	private void gerarGrafico() {
		// grafico
		// Isso irá criar o conjunto de dados
		PieDataset dataset = createDataset();

		// com base no conjunto de dados que criamos o gráfico
		JFreeChart chart = createChart(dataset, "");

		// vamos colocar o gráfico em um painel
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));
		JScrollPane painelRolagem2 = new JScrollPane();
		painelRolagem2.setBounds(158, 98, 536, 391);
		janela.getContentPane().add(painelRolagem2);
		painelRolagem2.setViewportView(chartPanel);

	}

	private void preencherTabela() {
		
		String[][] dados = new String[listExtensoes.size()][2];
		int i = 0;
		for (String strExtensao : listExtensoes) {
			dados[i][0] = strExtensao;
			dados[i][1] = String.valueOf(extenssoes.get(strExtensao));
			i++;
		}
		TableModel modelo = new TableModel(dados);
		table.setModel(modelo);
		gerarGrafico();
		
		if (extenssoes != null)	extenssoes.clear();
		listExtensoes.clear();
		
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

	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		for (String string : listExtensoes) {
			result.setValue(string, (int) extenssoes.get(string));
		}
		return result;
	}

	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart3D(title, // título /
																// gráfico
				dataset, // dados
				true, // include lenda
				true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;

	}
}
