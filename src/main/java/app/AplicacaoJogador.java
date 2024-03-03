package app;

import java.util.List;
import java.util.Scanner;
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
					System.out.println("\n\n==== Saindo do programa ====");
					break;
				default:
					System.out.println("\n\n==== Opção inválida. ====");
					break;
			}
		} while(opcao != 5);
		
		
		sc.close();
		
	}
	
	public static void menu() {
		System.out.println("MENU");
		System.out.println("1 - Listar");
		System.out.println("2 - Inserir");
		System.out.println("3 - Excluir");
		System.out.println("4 - Atualizar");
		System.out.println("5 - Sair");
		System.out.println();
	}
	
	public static void listarJogadores(JogadorDAO jogadorDAO) {
	    System.out.println("\n\n==== Listando jogadores ==== ");
	    System.out.println();
	    List<Jogador> jogadores = jogadorDAO.getJogadores();
	    if (jogadores.isEmpty()) {
	    	System.out.println();
	        System.out.println("Não existem jogadores para listar.");
	        System.out.println();
	    } else {
	        for (Jogador jogador : jogadores) {
	            System.out.println(jogador);
	        }
	    }
	    menu();
	}

	
	public static void inserirJogador(JogadorDAO jogadorDAO, Jogador jogador, Scanner sc) throws Exception {
	    System.out.println("\n\n==== Inserir jogador ==== ");
	    System.out.println();
	    
	    int id;
	    boolean idValido = false;
	    do {
	        System.out.println("Digite o ID do jogador:");
	        id = sc.nextInt();
	        Jogador jogadorExistente = jogadorDAO.getById(id);
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
	    String senhaCriptografada = JogadorDAO.toMD5Password(senha);
	    
	    char sexo;
	    do {
	        System.out.println("Digite o sexo do jogador (M/F):");
	        String sexoInput = sc.next();
	        sexo = sexoInput.toUpperCase().charAt(0);
	        if (sexo != 'M' && sexo != 'F') {
	            System.out.println("Sexo inválido. Digite 'M' para masculino ou 'F' para feminino.");
	        }
	    } while (sexo != 'M' && sexo != 'F');

	    jogador = new Jogador(id, login, senhaCriptografada, sexo);
	    if (jogadorDAO.insert(jogador)) {
	        System.out.println("Inserção com sucesso -> " + jogador.toString());
	        menu();
	    } else {
	        System.out.println("Falha ao inserir o jogador.");
	        menu();
	    }
	}

	public static void excluirJogador(JogadorDAO jogadorDAO, Jogador jogador, Scanner sc) {
	    List<Jogador> jogadores = jogadorDAO.getJogadores();
	    
	    if (jogadores.isEmpty()) {
	    	System.out.println();
	        System.out.println("Não existem jogadores no banco de dados para excluir.");
	        System.out.println();
	        menu();
	        return;
	    }
	    
	    boolean jogadorEncontrado = false;
	    
	    do {
	        System.out.println("\n\n==== Excluir jogador ==== ");
	        System.out.println();
	        System.out.println("Digite o ID do jogador que deseja excluir:");
	        int id = sc.nextInt();
	        
	        jogador = jogadorDAO.getById(id);
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
	    
	    menu();
	}

	
	public static void atualizarJogador(JogadorDAO jogadorDAO, Jogador jogador, Scanner sc) throws Exception {
	    List<Jogador> jogadores = jogadorDAO.getJogadores();

	    if (jogadores.isEmpty()) {
	    	System.out.println();
	        System.out.println("Não há jogadores no banco de dados para atualizar.");
	        System.out.println();
	        menu();
	        return;
	    }

	    int idJogador;
	    Jogador jogadorExistente;

	    do {
	        System.out.println("Digite o ID do jogador que deseja atualizar:");
	        idJogador = sc.nextInt();
	        
	        jogadorExistente = jogadorDAO.getById(idJogador);
	        if (jogadorExistente == null) {
	            System.out.println("Jogador com ID " + idJogador + " não encontrado. Por favor, insira um ID válido.");
	        }
	    } while (jogadorExistente == null);

	    System.out.println("\n\n==== Atualizar jogador (ID: " + jogadorExistente.getId() + ") ==== ");
	    System.out.println();

	    System.out.println("Digite o novo login do jogador:");
	    String novoLogin = sc.next();

	    System.out.println("Digite a nova senha do jogador:");
	    String novaSenha = sc.next();

	    jogadorExistente.setLogin(novoLogin);
	    jogadorExistente.setSenha(JogadorDAO.toMD5Password(novaSenha));

	    jogadorDAO.update(jogadorExistente);
	    
	    menu();
	}


	
	public static void mostrarJogadoresMasculinos(JogadorDAO jogadorDAO) {
		System.out.println("\n\n==== Mostrar jogadores do sexo masculino ==== ");
	    System.out.println();
		List<Jogador> jogadores = jogadorDAO.getSexoMasculino();
		for (Jogador jogador : jogadores) {
			System.out.println(jogador.toString());
		}
	}
	
	public static void mostrarJogadoresFemininos(JogadorDAO jogadorDAO) {
		System.out.println("\n\n==== Mostrar jogadores do sexo feminino ==== ");
	    System.out.println();
		List<Jogador> jogadores = jogadorDAO.getSexoFeminino();
		for (Jogador jogador : jogadores) {
			System.out.println(jogador.toString());
		}
	}
	
	public static void mostrarJogadoresOrdenadosPorId(JogadorDAO jogadorDAO) {
		System.out.println("\n\n==== Mostrar jogadores ordenados por id ==== ");
	    System.out.println();
		List<Jogador> jogadores = jogadorDAO.getOrderById();
		for (Jogador jogador : jogadores) {
			System.out.println(jogador);
		}
	}
	
	public static void mostrarJogadoresOrdenadosPorLogin(JogadorDAO jogadorDAO) {
		System.out.println("\n\n==== Mostrar usuários ordenados por login ==== ");
	    System.out.println();
		List<Jogador> jogadores = jogadorDAO.getOrderByLogin();
		for (Jogador jogador : jogadores) {
			System.out.println(jogador.toString());
		}
	}
}
