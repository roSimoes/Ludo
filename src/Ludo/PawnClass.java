package Ludo;
/**
 * Classe com os metodos associados ao movimento de um peao
 * @author Rodolfo Ferreira
 */
public class PawnClass {

	private int posicao; //posicao relativa do peao
	private int numero;  //index do peao
	private int cor;     // cor do peao 
	
	/**
	 * Contrutor que inicia a posicao relativa do peao a 1
	 */
	public PawnClass(int cor, int numero){
		this.cor = cor;
		this.numero = numero;
		posicao = 1;
	}
	
	/**
	 * Metodo que consulta a cor do peao 
	 * @return cor (peao)
	 */
	public int getColor(){
		return cor;
	}
	
	/**
	 * Metodo que consulta o index do peao 
	 * @return index (peao)
	 */
	public int getID(){
		return numero;
	}
	
	/**
	 * Metodo de consulta 'a posicao do peao
	 * @return posicao
	 */
	public int getPosition(){
		return posicao;
	}
	
	/**
	 * Adiciona a posicao relativa o valor do dado
	 * @param diceNumber
	 * @pre 1<=diceNumber<=6
	 */
	public void addPosition(int diceNumber){
		posicao += diceNumber;
	}
	
	/**
	 * Coloca o peao na posicao dada
	 * @param newPosition
	 */
	public void setPosition(int newPosition){
		posicao = newPosition;
	}
}
