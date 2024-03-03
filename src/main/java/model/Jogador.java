package model;

public class Jogador {
	private int id;
	private String login;
	private String senha;
	private char sexo;
	
	public Jogador() {
		this.id = -1;
		this.login = "";
		this.senha = "";
		this.sexo = '*';
	}
	
	public Jogador(int id, String login, String senha, char sexo) {
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.sexo = sexo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public char getSexo() {
		return sexo;
	}

	public void setSexo(char sexo) {
		this.sexo = sexo;
	}

	@Override
	public String toString() {
		return "Jogador [Id = " + id + ", Login = " + login + ", Senha = " + senha + ", Sexo = " + sexo + "]";
	}	
}
