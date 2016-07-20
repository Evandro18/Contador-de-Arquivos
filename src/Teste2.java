import java.util.Hashtable;

public class Teste2 {

	public static void main(String[] args) {
		Hashtable hashtable = new Hashtable<>();
		hashtable.put("x", 1);
		System.out.println(hashtable.get("x"));
		hashtable.put("x", 3);
		System.out.println(hashtable.get("x"));
	}
}
