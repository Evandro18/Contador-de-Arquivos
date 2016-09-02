package br.edu.ifms.contador;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                Principal window = new Principal();
                window.janela.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     * @wbp.parser.entryPoint
     */
    public Principal() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        janela = new JFrame();
        janela.setBounds(100, 100, 1100, 628);
        janela.setLocationRelativeTo(null);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setTitle("Contador de Arquivos");
        janela.getContentPane().setLayout(null);

        campoCaminho = new JTextField();
        campoCaminho.setBounds(271, 14, 509, 20);
        campoCaminho.setEditable(false);

        janela.getContentPane().add(campoCaminho);
        campoCaminho.setColumns(10);
        listExtensoes = new ArrayList<>();

        JButton btnAbrir = new JButton("Abrir...");
        btnAbrir.setBounds(813, 7, 95, 33);
        btnAbrir.setIcon(new ImageIcon(Principal.class.getResource("/img/folder.png")));
        btnAbrir.addActionListener(arg0 -> {
            JFileChooser fc = new JFileChooser();
            try {
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int res = fc.showOpenDialog(janela);

                if (res == JFileChooser.APPROVE_OPTION)
                    diretorio = fc.getSelectedFile();

                String caminho;

                if (diretorio != null)
                    caminho = diretorio.getPath();
                else
                    caminho = "";

                campoCaminho.setFocusable(true);
                campoCaminho.setText(caminho);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        janela.getContentPane().add(btnAbrir);

        JLabel lblDiretrio = new JLabel("Diretorio");
        lblDiretrio.setBounds(196, 17, 71, 14);
        janela.getContentPane().add(lblDiretrio);

        JButton btnIniciar = new JButton("Iniciar");
        btnIniciar.setBounds(481, 44, 89, 33);
        btnIniciar.setIcon(new ImageIcon(Principal.class.getResource("/img/play.png")));
        btnIniciar.addActionListener(e -> {
            if (diretorio == null)
                JOptionPane.showMessageDialog(janela, "Selecione um diretorio.");
            else if (!diretorio.isDirectory())
                JOptionPane.showMessageDialog(janela, "Selecione um diretorio valido.");
            else {
                extenssoes = new Hashtable<>();
                varreDiretorios(diretorio.getAbsolutePath());
                preencherTabela();
            }
        });
        janela.getContentPane().add(btnIniciar);

        table = new JTable();
        preencherTabela();
        JScrollPane painelRolagem = new JScrollPane();
        painelRolagem.setBounds(7, 81, 185, 490);

        janela.getContentPane().add(painelRolagem);
        painelRolagem.setViewportView(table);
        criarMaisUsadas();
    }

    private void criarMaisUsadas() {
        maisUsadas = new ArrayList<>();
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
        // Isso ira criar o conjunto de dados
        PieDataset dataset = createDataset();

        // com base no conjunto de dados que criamos o grafico
        JFreeChart chart = createChart(dataset, "");
        chart.removeLegend();
        // vamos colocar o grafico em um painel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));
        JScrollPane painelRolagem2 = new JScrollPane();
        painelRolagem2.setBounds(196, 81, 863, 490);

        janela.getContentPane().add(painelRolagem2);
        painelRolagem2.setViewportView(chartPanel);
        chartPanel.setLayout(null);
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

    private void varreDiretorios(String caminhoDiretorio) {
        File arquivo = abreArquivo(caminhoDiretorio);

        if (arquivo != null && arquivo.exists() && arquivo.isDirectory())
            varreDiretorios(arquivo, "");
    }

    private void varreDiretorios(File diretorio, String tabulacoes) {
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
                        if (partes.length > 1 && partes.length <= 2)
                            extensao = partes[partes.length - 1];
                        if (partes.length > 2)
                            extensao = partes[partes.length - 2] + "." + partes[partes.length - 1];
                        if (partes.length == 1)
                            extensao = "nenhuma";
                        if (extenssoes.containsKey(extensao)) {
                            int qtd = extenssoes.get(extensao) + 1;
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

    private File abreArquivo(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        if (arquivo.exists())
            return arquivo;
        return null;
    }

    private PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();

        for (String string : listExtensoes) {
            if (maisUsadas.contains(string))
                result.setValue(string, (int) extenssoes.get(string));
            else {
                outros = "outros";
                qtdOutros = qtdOutros++;
            }
        }

        if (outros != null)
            result.setValue(outros, qtdOutros);
        return result;
    }

    private JFreeChart createChart(PieDataset dataset, String title) {
        //titulo, grafico, dados, include, legenda
        JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
}