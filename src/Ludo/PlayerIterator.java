package Ludo;
/**
 * Classe com os metodos associados ao iterador de jogadores 
 * @author Rodolfo Ferreira
 */
public class PlayerIterator{
	
	private PlayerClass[] players;
	private int currentIndex;
	
	/**
	 * Cria um iterador de jogadores 
	 * @param pawns (vector de jogadores )
	 */
	public PlayerIterator(PlayerClass[] players){
		this.players = players;
		rewind();
	}
	
	/**
	 * Metodo que vai para a primeira posicao do vector 
	 */
	public void rewind(){
		currentIndex = 0;
	}
	
	
	/**
	 * Metodo que verifica se existe proximo jogador  
	 * @return true -> existe proximo jogador    false -> nao existe proximo jogador  
	 */
	public boolean hasNext(){
		return currentIndex<players.length;
	}
	
	
	/**
	 * Metodo que retorna o proximo jogador e aponta para o seguinte 
	 * @return
	 */
	public PlayerClass next(){
		return players[currentIndex++];
	}

}
