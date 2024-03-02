package app;

import java.util.List;
import java.util.Scanner;

import dao.DAO;
import dao.JogadorDAO;
import model.Jogador;

public class AplicacaoJogador {
	
	public static void main(String[] args) throws Exception {
		
		JogadorDAO jogadorDAO = new JogadorDAO();
		Jogador jogador = new Jogador();
		
		menu();
		
		Scanner sc = new Scanner(System.in);
		
		int opcao;
		do {
			System.out.println("Digite a opção desejada:");
			opcao = sc.nextInt();
			
			switch(opcao) {
				case 1:
					listarJogadores(jogadorDAO);
					break;
				case 2:
					inserirJogador(jogadorDAO, jogador, sc);
					break;
				case 3:
					excluirJogador(jogadorDAO, jogador, sc);
					break;
				case 4:
					atualizarJogador(jogadorDAO, jogador, sc);
					break;
				case 5:
					System.out.println("\n\n==== Saindo do programa");
					break;
				default:
					System.out.println("\n\n==== Opção inválida.");
					break;
			}
		} while(opcao != 5);
		
		
		sc.close();
		
		// Testes
//		testarAutenticacao(jogadorDAO, jogador);
//		mostrarJogadoresMasculinos(jogadorDAO);
//		testarAutenticacaoComMD5(jogadorDAO, jogador);
//		invadirComSQLInjection(jogadorDAO, jogador);
//		mostrarJogadoresOrdenadosPorId(jogadorDAO);
//		mostrarUsuariosOrdenadosPorLogin(jogadorDAO);
	}
	
	public static void menu() {
		System.out.println("MENU");
		System.out.println("1 - Listar");
		System.out.println("2 - Inserir");
		System.out.println("3 - Excluir");
		System.out.println("4 - Atualizar");
		System.out.println("5 - Sair");
	}
	
	public static void listarJogadores(JogadorDAO jogadorDAO) {
		System.out.println("\n\n==== Listando jogadores === ");
		List<Jogador> jogadores = jogadorDAO.getJogadores();
		for (Jogador jogador1 : jogadores) {
			System.out.println(jogador1);
		}
	}
	
	public static void inserirJogador(JogadorDAO jogadorDAO, Jogador jogador, Scanner sc) {
	    System.out.println("\n\n==== Inserir jogador === ");
	    
	    int id;
	    boolean idValido = false;
	    do {
	        System.out.println("Digite o ID do jogador:");
	        id = sc.nextInt();
	        Jogador jogadorExistente = jogadorDAO.get(id);
	        if (jogadorExistente != null) {
	            System.out.println("ID já existe. Por favor, insira outro ID.");
	        } else {
	            idValido = true;
	        }
	    } while (!idValido);
	    
	    System.out.println("Digite o login do jogador:");
	    String login = sc.next();
	    System.out.println("Digite a senha do jogador:");
	    String senha = sc.next();
	    
	    char sexo;
	    do {
	        System.out.println("Digite o sexo do jogador (M/F):");
	        String sexoInput = sc.next();
	        sexo = sexoInput.toUpperCase().charAt(0);
	        if (sexo != 'M' && sexo != 'F') {
	            System.out.println("Sexo inválido. Digite 'M' para masculino ou 'F' para feminino.");
	        }
	    } while (sexo != 'M' && sexo != 'F');

	    jogador = new Jogador(id, login, senha, sexo);
	    if (jogadorDAO.insert(jogador)) {
	        System.out.println("Inserção com sucesso -> " + jogador.toString());
	    } else {
	        System.out.println("Falha ao inserir o jogador.");
	    }
	}

	public static void excluirJogador(JogadorDAO jogadorDAO, Jogador jogador, Scanner sc) {
	    boolean jogadorEncontrado = false;
	    
	    do {
	        System.out.println("\n\n==== Excluir jogador ===");
	        System.out.println("Digite o ID do jogador que deseja excluir:");
	        int id = sc.nextInt();
	        
	        jogador = jogadorDAO.get(id);
	        if (jogador != null) {
	            jogadorEncontrado = true;
	            System.out.println("Jogador encontrado: " + jogador);
	            System.out.println("Deseja realmente excluir este jogador? (S/N)");
	            String confirmacao = sc.next();
	            if (confirmacao.equalsIgnoreCase("S")) {
	                if (jogadorDAO.delete(id)) {
	                    System.out.println("Jogador excluído com sucesso.");
	                } else {
	                    System.out.println("Falha ao excluir o jogador.");
	                }
	            } else {
	                System.out.println("Operação de exclusão cancelada.");
	            }
	        } else {
	            System.out.println("Jogador com ID " + id + " não encontrado. Por favor, digite outro ID.");
	        }
	    } while (!jogadorEncontrado);
	}
	
	public static void atualizarJogador(JogadorDAO jogadorDAO, Jogador jogador, Scanner sc) throws Exception {
	    System.out.println("Digite o ID do jogador que deseja atualizar:");
	    int idJogador = sc.nextInt();
	    
	    jogador = jogadorDAO.get(idJogador);
	    if (jogador == null) {
	        System.out.println("Jogador com ID " + idJogador + " não encontrado.");
	        return;
	    }

	    System.out.println("\n\n==== Atualizar jogador (ID: " + jogador.getId() + ") === ");

	    System.out.println("Digite o novo login do jogador:");
	    String novoLogin = sc.next();

	    System.out.println("Digite a nova senha do jogador:");
	    String novaSenha = sc.next();

	    jogador.setLogin(novoLogin);
	    jogador.setSenha(novaSenha);

	    jogadorDAO.update(jogador);
	}


	
	public static void testarAutenticacao(JogadorDAO jogadorDAO, Jogador jogador) {
		System.out.println("\n\n==== Testando autenticação ===");
		System.out.println("Jogador (" + jogador.getLogin() + "): " + jogadorDAO.autenticar("pablo", "pablo"));
	}
	
	public static void mostrarJogadoresMasculinos(JogadorDAO jogadorDAO) {
		System.out.println("\n\n==== Mostrar jogadores do sexo masculino === ");
		List<Jogador> jogadores = jogadorDAO.getSexoMasculino();
		for (Jogador u: jogadores) {
			System.out.println(u.toString());
		}
	}
	
	public static void testarAutenticacaoComMD5(JogadorDAO jogadorDAO, Jogador jogador) throws Exception {
		System.out.println("\n\n==== Testando autenticação ===");
		System.out.println("Jogador (" + jogador.getLogin() + "): " + jogadorDAO.autenticar("pablo", DAO.toMD5Password("pablo")));
		System.out.println("Jogador (" + jogador.getLogin() + "): " + jogadorDAO.autenticar("pablo", DAO.toMD5Login("pablo")));	
	}
	
	public static void invadirComSQLInjection(JogadorDAO jogadorDAO, Jogador jogador) {
		System.out.println("\n\n==== Invadir usando SQL Injection ===");
		System.out.println("Usuário (" + jogador.getLogin() + "): " + jogadorDAO.autenticar("pablo", "x' OR 'x' LIKE 'x"));
	}
	
	public static void mostrarJogadoresOrdenadosPorId(JogadorDAO jogadorDAO) {
		System.out.println("\n\n==== Mostrar jogadores ordenados por id === ");
		List<Jogador> jogadores = jogadorDAO.getOrderById();
		for (Jogador u: jogadores) {
			System.out.println(u.toString());
		}
	}
	
	public static void mostrarUsuariosOrdenadosPorLogin(JogadorDAO jogadorDAO) {
		System.out.println("\n\n==== Mostrar usuários ordenados por login === ");
		List<Jogador> jogadores = jogadorDAO.getOrderByLogin();
		for (Jogador u: jogadores) {
			System.out.println(u.toString());
		}
	}
}
