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
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;

public class Principal {

	private JFrame janela;
	private JTextField campoCaminho;
	private JTable table;
	private File diretorio;
	private Hashtable<String, Integer> extenssoes;
	private List<String> listExtensoes;
	private List<String> maisUsadas;
	private int qtdOutros = 0;
	private String outros;
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
		janela.setBounds(100, 100, 1336, 700);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.getContentPane().setLayout(null);
		janela.setTitle("Contador de Arquivos");
		campoCaminho = new JTextField();
		campoCaminho.setBounds(298, 36, 514, 22);
		campoCaminho.setEditable(false);
		janela.getContentPane().add(campoCaminho);
		campoCaminho.setColumns(10);
		listExtensoes = new ArrayList<>();
		JButton btnAbrir = new JButton("Abrir...");
		btnAbrir.setBackground(new Color(135, 206, 250));
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				try {
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int res = fc.showOpenDialog(janela);
					if (res == JFileChooser.APPROVE_OPTION) {
						diretorio = fc.getSelectedFile();
					}
					campoCaminho.setFocusable(true);
					String caminho = diretorio.getPath();
					campoCaminho.setText(caminho);
				} catch (Exception e) {
				}
			}
		});
		btnAbrir.setBounds(824, 34, 91, 25);
		janela.getContentPane().add(btnAbrir);

		JLabel lblDiretrio = new JLabel("Diret��rio");
		lblDiretrio.setBounds(214, 39, 66, 15);
		janela.getContentPane().add(lblDiretrio);

		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setBackground(new Color(135, 206, 250));
		btnIniciar.setBounds(431, 81, 114, 25);
		btnIniciar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (diretorio == null) {
					JOptionPane.showMessageDialog(janela, "Selecione um diret��rio.");
				} else if (!diretorio.isDirectory()) {
					JOptionPane.showMessageDialog(janela, "Selecione um diret��rio v��lido.");
				} else {
					extenssoes = new Hashtable<>();
					varreDiretorios(diretorio.getAbsolutePath());
					preencherTabela();
				}
			}
		});
		janela.getContentPane().add(btnIniciar);

		table = new JTable();
		preencherTabela();
		JScrollPane painelRolagem = new JScrollPane();
		painelRolagem.setBounds(7, 118, 189, 490);
		janela.getContentPane().add(painelRolagem, "cell 1 8,alignx left");
		painelRolagem.setViewportView(table);
		criarMaisUsadas();
	}

	private void criarMaisUsadas() {
		maisUsadas = new ArrayList<String>();
		maisUsadas.add("exe");
		maisUsadas.add("mp3");
		maisUsadas.add("wma");
		maisUsadas.add("avi");
		maisUsadas.add("bmp");
		maisUsadas.add("gif");
		maisUsadas.add("jpeg");
		maisUsadas.add("jpg");
		maisUsadas.add("png");
		maisUsadas.add("zip");
		maisUsadas.add("rar");
		maisUsadas.add("txt");
		maisUsadas.add("doc");
		maisUsadas.add("ls");
		maisUsadas.add("ppt");
		maisUsadas.add("pdf");
		maisUsadas.add("html");
		maisUsadas.add("js");
		maisUsadas.add("php");
		maisUsadas.add("java");
		maisUsadas.add("py");
		maisUsadas.add("docx");
		maisUsadas.add("pps");
		maisUsadas.add("xls");
		maisUsadas.add("xlsx");
		maisUsadas.add("wmv");
		maisUsadas.add("xml");
		maisUsadas.add("sql");
		maisUsadas.add("odt");
		maisUsadas.add("tar.gz");
		maisUsadas.add("pkt");
	}

	private void gerarGrafico() {
		// grafico
		// Isso ir�� criar o conjunto de dados
		PieDataset dataset = createDataset();

		// com base no conjunto de dados que criamos o gr��fico
		JFreeChart chart = createChart(dataset, "");
		chart.removeLegend();
		// vamos colocar o gr��fico em um painel
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));
		JScrollPane painelRolagem2 = new JScrollPane();
		painelRolagem2.setBounds(205, 118, 870, 490);
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

		if (extenssoes != null)
			extenssoes.clear();
		listExtensoes.clear();

	}

	public void varreDiretorios(String caminhoDiretorio) {
		File arquivo = abreArquivo(caminhoDiretorio);
		if (arquivo != null && arquivo.exists() && arquivo.isDirectory()) {
			varreDiretorios(arquivo, "");
		}
	}

	private void varreDiretorios(File diretorio, String tabulacoes) {
//		String outros = "outros...";
//		extenssoes.put(outros, 0);
		int qtdOutros = 0;
		String extensao = "";
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
						if (partes.length > 1 && partes.length <= 2) {
							extensao = partes[partes.length - 1];
						}
						if (partes.length > 2) {
							extensao = partes[partes.length - 2] + "." + partes[partes.length - 1];
						}
						if (partes.length == 1) {
							extensao = "nenhuma";
						}
						if (extenssoes.containsKey(extensao)) {
							int qtd = (int) extenssoes.get(extensao) + 1;
							extenssoes.put(extensao, qtd);
						} else {
							// if (maisUsadas.contains(extensao)) {
							extenssoes.put(extensao, 1);
							listExtensoes.add(extensao);
							// } else {
							// qtdOutros = (int) extenssoes.get(outros) + 1;
							// extenssoes.put(outros, qtdOutros);
							// if (!listExtensoes.contains(outros)) {
							// listExtensoes.add(outros);
							// }
							// }
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
			if (maisUsadas.contains(string)) {
				result.setValue(string, (int) extenssoes.get(string));
			} else {
				outros = "outros";
				qtdOutros = qtdOutros++;									
			}
		}
		if (outros != null) {
			result.setValue(outros, qtdOutros);
		}
		return result;
	}

	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart3D(title, // t��tulo / //
																// gr��fico
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
