package br.edu.ifms.contador;

public class ListaExtenssoes implements Comparable<ListaExtenssoes> {

    private String chave;
    private int valor;

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    @Override
    public int compareTo(ListaExtenssoes o) {
        return Integer.compare(valor, o.valor);
    }

}