package Ludo;

import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

/**
 * Classe com os metodos associados ao jogador
 * @author Rodolfo Ferreira
 */
public class PlayerClass {

	private PawnClass[] pawns; 	//peao do jogador
	private int color;			//cor do jogador 1-4
	private String name;		//nome do jogador
	private int diceValue;		//valor do dado jogado
	private boolean diceTossed;	//controla se o lancamento do dado ja foi efectuado
	
	/**
	 * Cria um novo jogador com uma cor e um nome associados
	 * @pre 1<=color<=4
	 * @param color - cor do jogador
	 * @param name - nome do jogador
	 */
	public PlayerClass(int color, String name){
		this.color=color;
		this.name=name;
		pawns = new PawnClass[4];
		diceTossed = false;
	}
	/**
	 * Cria um novo peao com o index dado 
	 * @param index peao
	 */
	public void newPawn(int pawn){
			pawns[pawn-1] = new PawnClass(color, pawn);
	}
	
	/**
	 * Metodo que verifica se existe o peao , com um determinado index 
	 * @param index peao 
	 * @return true -> existe peao     false -> nao existe peao 
	 */
	public boolean hasPawn(int pawn){
		return pawns[pawn-1]!=null;
	}
	
	/**
	 * Metodo de consulta 'a cor do jogador
	 * @return color
	 */
	public int getColor(){
		return color;
	}
	
	/**
	 * Metodo de consulta 'a posicao do peao do jogador
	 * @return posicao do peao
	 */
	public int getPosition(int pawn){
		return pawns[pawn-1].getPosition();
	}
	
	
	/**
	 * Metodo modificador que adiciona o valor do dado 'a posicao do peao
	 * @pre 1<=diceNumber<=6
	 * @param diceNumber - valor do dado
	 */
	public void addPosition(int diceNumber, int pawn){
		pawns[pawn-1].addPosition(diceNumber);
	}
	
	/**
	 * Metodo de consulta ao nome do jogador
	 * @return name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Metodo modificador que lanca o dado e altera o valor do dado e o estado do lancamento
	 * @return diceValue - valor do dado
	 */
	public int tossDice(){
		Random r = new Random();
		diceValue = r.nextInt(6)+1;
		diceTossed = true;
		return diceValue;
	}
	
	/**
	 * Metodo de consulta ao estado do lancamento do dado
	 * @return diceTossed
	 */
	public boolean getTossedStatus(){
		return diceTossed;
	}
	
	/**
	 * Metodo de consulta ao valor do lancamento do dado
	 * @return deceValue
	 */
	public int getDice(){
		return diceValue;
		
	}
	
	/**
	 * Metodo modificador que repoe o estado inicial do lancamento do dado
	 */
	public void resetTossedStatus(){
		diceTossed = false;
	}

	
	
	/**
	 * Metodo que salva o processo actual do jogo 
	 * @param objecto PrintWriter (que faz a impresscao no ficheiro ) 
	 */
	public void save(PrintWriter pw) {
		pw.println(color);
		pw.println(name);
		pw.println(diceValue);
		pw.println(diceTossed);
		for(int i = 1; i<=4; i++)
			if(hasPawn(i))
				pw.println(pawns[i-1].getPosition());
			else
				pw.println(0);
	}

	
	/**
	 * Metodo que restaura o estado do jogo guardado no ficheiro jogo.txt 
	 * @param scanner   (que faz a leitura no ficheiro ) 
	 */
	public void load(Scanner in) {
		int pos;
		diceValue = in.nextInt();
		diceTossed = in.nextBoolean();
		for(int i = 0; i<4; i++){
			pos = in.nextInt();
			if (pos > 0){
				pawns[i] = new PawnClass(color, i+1);
				pawns[i].setPosition(pos);
			}
		}
	}
	
	/**
	 * Metodo que retorna um interador para os peoes de um jogador 
	 * @return peoes do jogador 
	 */
	public PawnIterator getPawns(){
		return new PawnIterator(pawns);
	}
}
