package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Jogador;

public class JogadorDAO extends DAO {
	
	public JogadorDAO() {
		super();
		conectar();
	}

	public void finalize() {
		close();
	}
	
	
	public boolean insert(Jogador jogador) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "INSERT INTO jogadores (id, login, senha, sexo) "
				       + "VALUES ("+jogador.getId()+ ", '" + jogador.getLogin() + "', '"  
				       + jogador.getSenha() + "', '" + jogador.getSexo() + "');";
			System.out.println(sql);
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Jogador get(int id) {
		Jogador jogador = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM jogadores WHERE id=" + id;
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 jogador = new Jogador(rs.getInt("id"), rs.getString("login"), rs.getString("senha"), rs.getString("sexo").charAt(0));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return jogador;
	}
	
	
	public List<Jogador> get() {
		return get("");
	}
	
	public List<Jogador> getJogadores() {
	    return get("");
	}

	
	public List<Jogador> getOrderById() {
		return get("id");		
	}
	
	
	public List<Jogador> getOrderByLogin() {
		return get("login");		
	}
	
	
	public List<Jogador> getOrderBySexo() {
		return get("sexo");		
	}
	
	
	private List<Jogador> get(String orderBy) {	
	
		List<Jogador> jogadors = new ArrayList<Jogador>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM jogadores" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Jogador u = new Jogador(rs.getInt("id"), rs.getString("login"), rs.getString("senha"), rs.getString("sexo").charAt(0));
	            jogadors.add(u);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return jogadors;
	}


	public List<Jogador> getSexoMasculino() {
		List<Jogador> jogadores = new ArrayList<Jogador>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM jogadores WHERE jogadores.sexo LIKE 'M'";
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Jogador u = new Jogador(rs.getInt("id"), rs.getString("login"), rs.getString("senha"), rs.getString("sexo").charAt(0));
	        	jogadores.add(u);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return jogadores;
	}
	
	
	public boolean update(Jogador jogador) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE jogadores SET login = '" + jogador.getLogin() + "', senha = '"  
				       + jogador.getSenha() + "', sexo = '" + jogador.getSexo() + "'"
					   + " WHERE id = " + jogador.getId();
			System.out.println(sql);
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "DELETE FROM jogadores WHERE id = " + id;
			System.out.println(sql);
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public boolean autenticar(String login, String senha) {
		boolean resp = false;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM jogadores WHERE login LIKE '" + login + "' AND senha LIKE '" + senha  + "'";
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);
			resp = rs.next();
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return resp;
	}

		
}