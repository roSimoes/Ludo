package Ludo;

/**
 * Classe com os metodos associados ao iterador de peoes 
 * @author Rodolfo Ferreira
 */
public class PawnIterator {
	private PawnClass[] pawns;
	private int currentIndex;
	
	/**
	 * Cria um iterador de peoes
	 * @param pawns (vector de peoes)
	 */
	public PawnIterator(PawnClass[] pawns){
		this.pawns = pawns;
		rewind();
	}
	
	/**
	 * Metodo que encontra o primeiro peao do vector 
	 */
	public void rewind(){
		currentIndex = 0;
		while(pawns[currentIndex] == null && currentIndex < 4)
			currentIndex++;
	}
	
	/**
	 * Metodo que verifica se existe proximo peao 
	 * @return true -> existe proximo peao   false -> nao existe proximo peao 
	 */
	public boolean hasNext(){
		return currentIndex<pawns.length;
	}
	
	
	/**
	 * Metodo que retorna o proximo peao existente e avanca para o seguinte  
	 * @return retorna o proximo peao 
	 */
	public PawnClass next(){
		PawnClass res = pawns[currentIndex++];
		while(pawns[currentIndex] == null && currentIndex < 4)
			currentIndex++;
		return res;
	}
}
