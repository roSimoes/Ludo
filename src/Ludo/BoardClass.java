package Ludo;

import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Classe com os metodos associados ao tabuleiro
 * @author Rodolfo Ferreira
 */
public class BoardClass {
	
	private static final int MAXPLAYERS = 4;

	private PlayerClass[] players;      // vector de jogadores 
	private int currentPlayer;			//variavel de controlo do proximo jogador
	private int regPlayers;				//numero de jogadores registados
	private boolean isPlaying;			//estado do jogo
	private boolean initialToss;		//estado do lancamento inicial
	private PlayerClass[] podio;        //vector de jogadores no podio (ordenado pelas vitorias)
	private int podioCounter;           //counter do vector de jogadores no podio  
	
	/**
	 * Cria um tabuleiro de jogo inicializa os valores das variaveis
	 */
	public BoardClass(){
		players = new PlayerClass[4];
		currentPlayer = 1;
		regPlayers=0;
		isPlaying = false;
		initialToss = false;
		podio = new PlayerClass[4];
		podioCounter = 0;
	}

	/**
	 * Metodo modificador que regista um novo jogador
	 * @pre 1<=color<=4
	 * @param color - cor do jogador
	 * @param name - nome do jogador
	 */
	public void regPlayer(int color, String name) {
		if (getPlayer(color)==null)
			regPlayers++;
		players[color-1] = new PlayerClass(color, name);
	}
	
	/**
	 * Metodo de consulta que verifica se ha 4 jogadores registados ( e possivel iniciar o jogo)
	 * @return regPlayers == MAXPLAYERS
	 */
	public boolean isReady(){
		return (regPlayers == MAXPLAYERS);
	}
	
	/**
	 * Metodo de consulta que dada uma cor retorna o jogador correspondente
	 * @pre 1<=color<=4
	 * @param cor - cor do jogador
	 * @return jogador
	 */
	public PlayerClass getPlayer(int cor){
		return players[cor-1];
	}

	/**
	 * Metodo modificador que inicia o jogo
	 */
	public void beginGame() {
		isPlaying = true;
	}
	
	/**
	 * Metodo de consulta do estado do jogo
	 * @return true -> esta a jogar   false -> nao esta a jogar 
	 */
	public boolean getPlayingStatus(){
		return isPlaying;
	}
	
	/**
	 * Metodo que consulta o estado do lancamento inicial do dado
	 * @return true -> lancamento feito   false -> nao foi feito o lancamento 
	 */
	public boolean getInitialTossStatus(){
		return initialToss;
	}
	
	/**
	 * Metodo de consulta do jogador corrente
	 * @return currentPlayer
	 */
	public int getCurrentPlayer(){
		return currentPlayer;
	}
	
	/**
	 * Metodo de consulta do jogador anterior ao corrente
	 * @return jogador anterior
	 */
	public int getPrevPlayer(){
		int res=4;
		if (currentPlayer>1)
			res = currentPlayer-1;
		return res;
	}
	
	/**
	 * Metodo modificador que efectua uma jogada calculando a nova posicao do peao e verificando se esta ocupada
	 * @pre 1<=color<=4
	 * @param color - cor do jogador
	 * @return numero de casas que o peao avancou
	 */
	public int movePawn(int color, int pawn){
		int toss = getPlayer(color).getDice();
		int newPos;
		
		boolean equals;
		do{
			if ((getPlayer(color).getPosition(pawn) + toss)<=52)
				newPos = (getPlayer(color).getPosition(pawn)+((color-1)*13))%52 + toss;
			else
				newPos = getPlayer(color).getPosition(pawn) + toss;
			equals = false;
			for(int i = 1;i <=4;i++)
				if (getPlayer(color).hasPawn(i)&&i!=pawn)
					if (getPlayer(color).getPosition(i) == (getPlayer(color).getPosition(pawn)+toss)
							&& (getPlayer(color).getPosition(pawn)+toss)<58){
						newPos--;
						toss--;
						equals = true;
					}
			
			for(int n = 1; n<=4;n++)
				for(int i = 1; i<= 4; i++)
					if (getPlayer(n).hasPawn(i))
						if(newPos == (getPlayer(n).getPosition(i)%52)&&(n!=color||i!=pawn)){
							newPos--;
							toss--;
							equals = true;
						}						
			
		}while(equals == true);
		
		getPlayer(color).addPosition(toss, pawn);
		nextPlayer();
		return getPlayer(color).getPosition(pawn);
	}
	
	/**
	 * Metodo modificador que faz a rotacao da vez do jogador pelo sentido dos ponteiros do relogio
	 */
	public void nextPlayer(){
		getPlayer(currentPlayer).resetTossedStatus();
		currentPlayer = currentPlayer%4 +1;
	}
	
	/**
	 * Metodo de consulta que verifica se a nova posicao do peao ultrapassa o limite do tabuleiro 
	 * ou se ja esta na casa final
	 * @param color - cor do jogador
	 * @return true - >= 58, false - < 58
	 */
	public boolean testPositionPawn(int color, int pawn){
		return (getPlayer(color).getPosition(pawn)+getPlayer(color).getDice())>58 || getPlayer(color).getPosition(pawn)==58;
	}
	
	/**
	 * Metodo de consulta ao estado do lancamento do dado do jogador corrente
	 * @return true - lancamento efectuado
	 */
	public boolean getTossedStatus(){
		return getPlayer(currentPlayer).getTossedStatus();
	}
	
	
	
	/**
	 * Metodo que testa se pode crear um novo peao e cria-o
	 * @param color(cor do jogador )
	 * @param pawn ( index do peao )
	 * @return true -> foi criado um peao   false -> nao foi criado um peao 
	 */
	public boolean createNewPawn(int color, int pawn){
		boolean res = false ;
		if (!getPlayer(color).hasPawn(pawn)){
			res = true;
			PlayerIterator pit = new PlayerIterator(players);
			PawnIterator pwit;
			PawnClass p;
			while (pit.hasNext()){
				pwit = pit.next().getPawns();
				while (pwit.hasNext()){
					p = pwit.next();
					if((1+(color-1)*13) == (p.getPosition()+((p.getColor()-1)*13))%52){
						res = false;
					}
				}
			}

			if (res)
				getPlayer(color).newPawn(pawn);
		}
		return res;
	}
	
	
	/**
	 * Metedo que verifica se todos os jogadores fizeram o lamcamento inicial 
	 * e mete como proximo jogador a jogar o que tiver o maior resultado no lamcamento inicial do dado  
	 * @return true -> o jogo pode ser iniciado    false -> caso contrario 
	 */
	public boolean canPlay(){
		int max = 0;
		int index = 0;
		boolean res = true;
		if (currentPlayer ==4){
			PlayerIterator it = new PlayerIterator(players);
			PlayerClass p;
			while (it.hasNext()){
				p=it.next();
				if (p.getDice()>0)
					if (p.getDice()>max){
						max = p.getDice();
						index = p.getColor();
					}
				p.resetTossedStatus();
			}
		}
		else
			res = false;
		if (res){
			initialToss = true;
			currentPlayer = index;
		}

		return res;
	}

	
	
	/**
	 * Metodo que guarda  as variaveis de instancia da class e os jogadores  
	 * @param pw
	 */
	public void save(PrintWriter pw) {
		pw.println(currentPlayer);
		pw.println(isPlaying);
		pw.println(initialToss);
		for(int i = 0; i<MAXPLAYERS;i++)
			players[i].save(pw);
		for(int i = 0; i<podioCounter; i++)
			pw.println(podio[i].getColor());
	}

	/**
	 * Metodo que restaura as variaves da instancia da class e os jogadores 
	 * @param pw
	 */
	public void load(FileReader pw) {
		int color;
		String name;
		Scanner in = new Scanner(pw);
		currentPlayer = in.nextInt();
		isPlaying = in.nextBoolean();
		initialToss = in.nextBoolean();
		for (int i = 0;i<MAXPLAYERS;i++){
			color = in.nextInt();in.nextLine();
			name = in.nextLine();
			players[i] = new PlayerClass(color, name);
			players[i].load(in);
		}
		while(in.hasNextInt())
			addPodio(in.nextInt());
		in.close();		
	}
	
	/**
	 * Metodo que testa se um jogador ganhou 
	 * @param player (cor do jogador )
	 * @param trackLenght (tamanho d todas as casas posiveis de ocupar pelo peao )
	 * @return
	 */
	public boolean testPlayerWin(int player, int trackLenght){
		boolean res = true;
		for (int i = 1; i<=4; i++)
			if (players[player-1].hasPawn(i)){
				if (players[player-1].getPosition(i) != trackLenght)
					res = false;
			}
			else
				res = false;					
		
		return res;
	}

	/**
	 * Metodo que adiciona um jogador o podio 
	 * @param cor(cor do jogador )
	 */
	public void addPodio(int cor) {
		podio[podioCounter++] = players[cor-1];
		if (podioCounter == 3){
			int soma = 0;
			for(int i = 0; i < podioCounter; i++)
				soma += podio[i].getColor();
			podio[podioCounter++] = players[9-soma];
		}			
	}
	
	
	/**
	 * Metodo que testa se o jogo ja terminou 
	 * @return true -> jogo terminado   false -> jogo por terminar 
	 */
	public boolean testEndGame(){
		return podioCounter==4;
	}
	
	
	
	/**
	 * Metodo que retorna o iterador do podio 
	 * @return iterador do podio 
	 */
	public PlayerIterator getPodio(){
		return new PlayerIterator(podio);
	}
}
