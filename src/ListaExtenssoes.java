import java.util.List;

public class ListaExtenssoes implements Comparable<ListaExtenssoes> {

	String chave;
	int valor;
	
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

	public ListaExtenssoes() {

	}

	@Override
	public int compareTo(ListaExtenssoes o) {
		if (valor < o.valor) {
			return -1;
		}
		if (valor > o.valor) {
			return 1;
		}
		return 0;
	}

}
