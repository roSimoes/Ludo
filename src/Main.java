import java.io.*;
import java.util.Scanner;

import Ludo.*;

/**
 * 
 * Classe principal do programa com o interpretador de comandos e metodos associados
 * @author Rodolfo Ferreira
 *
 */
public class Main {
	
	private static final String FICHEIRO = "jogo.txt";
	
	private static final String REGJOGADOR = "RJ";
	private static final String INICIARJOGO = "IJ";
	private static final String LANCADADO = "LD";
	private static final String JOGAR = "JOGAR";
	private static final String PASSAR = "PASSAR";
	private static final String SAIR = "S";
	private static final String AJUDINHA = "AJUDINHA";
	private static final String LISTARJOGADORES = "LJ";
	private static final String ADICIONARPEAO = "AP";
	private static final String LANCAMENTOINICIAL = "LID";
	private static final String REINICIARJOGO = "RIJ";
	
	private static final String VERDE = "VERDE";
	private static final String VERMELHO = "VERMELHO";
	private static final String AZUL = "AZUL";
	private static final String AMARELO = "AMARELO";
	
	private static final String ERROCOR = "Cor desconhecida.";
	private static final String ERROIJ = "Ha jogadores ainda por registar - nao e possivel iniciar o jogo.";
	private static final String ERRONOPLAYER = "Nenhum nome associado";
	private static final String ERRONOGAME = "Nao existe sessao de jogo em aberto.";
	private static final String ERROCOMANDO = "Comando invalido no contexto actual.";
	private static final String ERROJOGADA = "Jogada nao permitida.";
	private static final String ERROLANCAMENTO = "Lancamento deve ser realizado pelo jogador ";
	private static final String ERROPEAO = "Peao desconhecido.";
	
	private static final String AJUDATEXT = "S - Terminar programa\n"
										+ "Ajudinha - Listagem de comandos\n"
										+ "LJ - Lista de jogadores\n"
										+ "RJ - Registo de jogador\n"
										+ "IJ - Inicio de jogo\n"
										+ "RIJ - Reinicio de jogo\n"
										+ "LID - Lancamento de dado inicial\n"
										+ "LD - Lancamento de dado no decorrer do jogo\n"
										+ "JOGAR - Movimantacao de um peao\n"
										+ "AP - Arranque de um peao\n"
										+ "PASSAR - Passar a vez no jogo";
	private static final String JOGADORES = "Jogadores";
	private static final String INICIOJOGO = "Jogo iniciado";
	private static final String GOODBYE = "Ate a proxima.";
	private static final String JOGOGUARDADO = "Jogo guardado. Ate a proxima.";
	private static final String LANCAMENTOINITMESSAGE = "Jogo pode comecar. Primeiro jogador: ";
	private static final String REINICIARJOGOMSG = "Jogo reiniciado";
	

	/**
	 * Main - cria um scanner, le o comando e redireciona para o metodo certo
	 */
	public static void main(String[] args) {
		String cmd;
		Scanner in = new Scanner(System.in);
		BoardClass board = new BoardClass();
			
		do {
			cmd = in.next().toUpperCase();
			switch (cmd){
			case REGJOGADOR:
				registoJogador(in, board);
				break;
			case INICIARJOGO:
				iniciarJogo(board);
				break;
			case LANCADADO:
				lancaDado(in, board);
				break;
			case JOGAR:
				board = jogar(in, board);
				break;
			case PASSAR:
				passar(in, board);
				break;
			case AJUDINHA:
				System.out.println(AJUDATEXT);
				break;
			case LISTARJOGADORES:
				listarJogadores(board);
				break;
			case ADICIONARPEAO:
				adicionarPeao(in, board);
				break;
			case LANCAMENTOINICIAL:
				lancamentoInicial(in, board);
				break;
			case REINICIARJOGO:
				load(board);
				break;
			}
		} while (!cmd.equals(SAIR));
		if (board.getPlayingStatus()){
			save(board);
			System.out.println(JOGOGUARDADO);
		}
		System.out.println(GOODBYE);
		LudoGUI.deleteWindow();
		in.close();
	}

	private static void lancamentoInicial(Scanner in, BoardClass board) {
		String color = in.next();
		int cor = color2int(color);
		
		if ((cor>=LudoGUI.GREEN)&&(cor <=LudoGUI.YELLOW))
			if (board.getPlayingStatus()&&!board.getInitialTossStatus())
				if (cor==board.getCurrentPlayer()&&!board.getTossedStatus()){
					int lancamento = board.getPlayer(cor).tossDice();
					System.out.println(color.toUpperCase()+" obteve " + lancamento
							+ " no lancamento do dado.");
					if (board.canPlay())
						System.out.println(LANCAMENTOINITMESSAGE + int2color(board.getCurrentPlayer()));
					else board.nextPlayer();
				}
				else
					System.out.println(ERROLANCAMENTO + int2color(board.getCurrentPlayer()));
			else
				System.out.println(ERROCOMANDO);
		else
			System.out.println(ERROCOR);
	}

	private static void adicionarPeao(Scanner in, BoardClass board) {
		String color = in.next();
		int cor = color2int(color);
		int peao = in.nextInt();
		
		if ((cor>=LudoGUI.GREEN)&&(cor <=LudoGUI.YELLOW))
			if (board.getPlayingStatus()&&board.getInitialTossStatus())
				if (cor==board.getCurrentPlayer()&&board.getTossedStatus())
					if (peao>0&& peao<=LudoGUI.MAX_PAWNS){
						if (board.getPlayer(board.getCurrentPlayer()).getDice()==6){
							if (board.createNewPawn(cor, peao)){
								printBoard(board);
								LudoGUI.pawnPosition(cor, peao, 1);
							}
						}
						board.nextPlayer();
					}
					else
						System.out.println(ERROPEAO);
				else
					System.out.println(ERROJOGADA);
			else
				System.out.println(ERROCOMANDO);
		else
			System.out.println(ERROCOR);
	}

	/**
	 * Metodo de consulta que lista todos os jogadores
	 */
	private static void listarJogadores(BoardClass board) {
		
		System.out.println(JOGADORES);
		listarJogadoresGuardados(board);
		
	}
	
	private static void listarJogadoresGuardados(BoardClass board){
		PlayerClass temp;
		int i=LudoGUI.GREEN;
		while(i<=LudoGUI.YELLOW){
			temp = board.getPlayer(i);
			if (temp!=null)
				System.out.println(int2color(i).toUpperCase()+": "+temp.getName());
			else
				System.out.println(int2color(i).toUpperCase()+": "+ERRONOPLAYER);
			i++;
		}
	}

	/**
	 * Metodo que le o resto do comando PASSAR e interpreta-o
	 */
	private static void passar(Scanner in, BoardClass board) {
		String color = in.next();
		int cor = color2int(color);
		
		if ((cor>=LudoGUI.GREEN)&&(cor <=LudoGUI.YELLOW))
			if (board.getPlayingStatus()&&board.getInitialTossStatus())
				if (cor==board.getCurrentPlayer()&&board.getTossedStatus()){
					board.nextPlayer();
					printBoard(board);
				}
				else
					System.out.println(ERROJOGADA);
			else
				System.out.println(ERROCOMANDO);
		else
			System.out.println(ERROCOR);

	}

	/**
	 * Metodo que le o resto do comando JOGAR e interpreta-o
	 */
	private static BoardClass jogar(Scanner in, BoardClass board) {
		String color = in.next();
		int cor = color2int(color);
		int peao = in.nextInt();
		
		if ((cor>=LudoGUI.GREEN)&&(cor <=LudoGUI.YELLOW))
			if (board.getPlayingStatus()&&board.getInitialTossStatus())
				if (cor==board.getCurrentPlayer()&&board.getTossedStatus())
					if (peao>0&& peao<=LudoGUI.MAX_PAWNS){
						if (board.getPlayer(board.getCurrentPlayer()).hasPawn(peao)){
							if(!board.testPositionPawn(cor,peao)){
								LudoGUI.pawnPosition(cor, peao, board.movePawn(cor,peao));
								if (board.testPlayerWin(cor, LudoGUI.TRACK_LENGTH))
									board.addPodio(cor);
							}
							else
								board.nextPlayer();
							printBoard(board);
							
							if (board.testPlayerWin(cor, LudoGUI.TRACK_LENGTH))
								System.out.println("Jogador " + int2color(cor) + " finalizou o jogo.");
							if (board.testEndGame())
								board = winGame(board);
						}
					}
					else
						System.out.println(ERROPEAO);
				else
					System.out.println(ERROJOGADA);
			else
				System.out.println(ERROCOMANDO);
		else
			System.out.println(ERROCOR);

		return board;
	}

	/**
	 * Metodo que le o resto do comando LD e interpreta-o
	 */
	private static void lancaDado(Scanner in, BoardClass board) {
		String color = in.next();
		int cor = color2int(color);

		if ((cor>=LudoGUI.GREEN)&&(cor <=LudoGUI.YELLOW))
			if (board.getPlayingStatus()&&board.getInitialTossStatus()&&!board.getTossedStatus())
				if (cor == board.getCurrentPlayer())			
					System.out.println(color.toUpperCase() + " obteve " + board.getPlayer(board.getCurrentPlayer()).tossDice() + " no lancamento do dado.");
				else
					System.out.println(ERROLANCAMENTO + int2color(board.getCurrentPlayer()));
			else
				System.out.println(ERROCOMANDO);
		else
			System.out.println(ERROCOR);
				
	}

	/**
	 * Metodo que inicia o jogo se possivel
	 */
	private static void iniciarJogo(BoardClass board) {
		if (board.isReady()&&!board.getPlayingStatus()){ //regPlayers==4
			PlayerClass temp;
			
			System.out.println(INICIOJOGO);
			int i = LudoGUI.GREEN;
			while (i<=LudoGUI.YELLOW){
				temp = board.getPlayer(i);
				System.out.println(int2color(i).toUpperCase()+": "+temp.getName());
				i++;
			}
			board.beginGame();
			LudoGUI.createWindow();
		}
		else
			System.out.println(ERROIJ);
			
	}

	/**
	 * Metodo que le o resto do comando RJ e interpreta-o
	 */
	private static void registoJogador(Scanner in, BoardClass board) {
		String cor = in.next();in.skip(" ");
		String nome = in.nextLine();
		int color = color2int(cor);
		if (!board.getPlayingStatus())
			if ((color>=LudoGUI.GREEN)&&(color <=LudoGUI.YELLOW)){
				board.regPlayer(color, nome);
				System.out.println(cor.toUpperCase() + " associado a " + nome +".");
			}
			else
				System.out.println(ERROCOR);
		else
			System.out.println(ERROCOMANDO);
	}

	/**
	 * Metodo auxiliar que converte uma String na cor correspondente
	 * @return 1-4 se for uma cor valida, 0 se nao for
	 */
	private static int color2int(String cor) {
		int result = 0;
		switch (cor.toUpperCase()){
		case VERDE:
			result = LudoGUI.GREEN;
			break;
		case VERMELHO:
			result = LudoGUI.RED;
			break;
		case AZUL:
			result = LudoGUI.BLUE;
			break;
		case AMARELO:
			result = LudoGUI.YELLOW;
		}
		return result;
	}
	
	/**
	 * Metodo auxiliar que converte um numero numa String com a cor correspondente
	 * @return String com a cor, null caso nao seja um numero valido
	 */
	private static String int2color(int cor){
		String result = null;
		switch (cor){
		case 1:
			result = VERDE;
			break;
		case 2:
			result = VERMELHO;
			break;
		case 3:
			result = AZUL;
			break;
		case 4:
			result = AMARELO;
		}
		return result;
	}
	
	/**
	 * Metodo auxiliar que imprime o podio e cria um novo jogo
	 */
	private static BoardClass winGame(BoardClass b){
		System.out.println("PODIO");
		PlayerIterator it = b.getPodio();
		PlayerClass p = it.next();
		System.out.println("Vencedor: " + int2color(p.getColor()) + " " + p.getName());
		p = it.next();
		System.out.println("Segundo lugar: " + int2color(p.getColor()) + " " + p.getName());
		p = it.next();
		System.out.println("Terceiro lugar: " + int2color(p.getColor()) + " " + p.getName());
		p = it.next();
		System.out.println("Quarto lugar: " + int2color(p.getColor()) + " " + p.getName());
		LudoGUI.deleteWindow();
		return new BoardClass();
	}
	
	
	/**
	 * Metodo auxiliar imprime as posicoes de todos os peoes de todas as cores 
	 * @param board
	 */
	private static void printBoard(BoardClass board){
		for (int i = LudoGUI.GREEN; i<= LudoGUI.YELLOW; i++){
			System.out.println(int2color(i)+":");
			for(int n = 1; n <= LudoGUI.MAX_PAWNS; n++)
				if (board.getPlayer(i).hasPawn(n))
					System.out.println("PEAO(" + n + ", "+board.getPlayer(i).getPosition(n)+")");
				else
					System.out.println("PEAO(" + n + ", 0)");
		}
	}
	/**
	 * Metodo que restaura o jogo guardado 
	 * @param board
	 */
	
	private static void load(BoardClass board){
		try {
			FileReader pw = new FileReader(FICHEIRO);
			board.load(pw);
			pw.close();
		} catch (IOException e) {
			System.out.println(ERRONOGAME);
		}
		System.out.println(REINICIARJOGOMSG);
		listarJogadoresGuardados(board);
		LudoGUI.createWindow();
		for (int i = 1 ; i <= 4 ; i++)
			for(int j = 1 ; j<= 4 ; j ++)
				LudoGUI.pawnPosition(i, j, board.getPlayer(i).getPosition(j));
		
	}
	
	
	/**
	 * Metodo guarda o estado do jogo corrente
	 * @param board
	 */
	private static void save(BoardClass board){
		try {
			PrintWriter pw = new PrintWriter(FICHEIRO);
			board.save(pw);
			pw.close();
		} catch (FileNotFoundException e) {
			
		}
	}

}
