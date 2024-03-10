package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Contato;

public class ContatoDAO extends DAO {

    public ContatoDAO() {
        super();
        conectar();
    }

    public boolean insert(Contato contato) {
        boolean status = false;
        try (PreparedStatement st = conexao.prepareStatement("INSERT INTO contatos (nome, email, telefone) VALUES (?, ?, ?)")) {
            st.setString(1, contato.getNome());
            st.setString(2, contato.getEmail());
            st.setString(3, contato.getTelefone());
            st.executeUpdate();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public Contato getById(int id) {
        Contato contato = null;
        try (Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = st.executeQuery("SELECT * FROM contatos WHERE id=" + id);
            if (rs.next()) {
                contato = new Contato(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone")
                );
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return contato;
    }

    public List<Contato> getOrderById() {
        List<Contato> contatos = new ArrayList<>();
        try (Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = st.executeQuery("SELECT * FROM contatos ORDER BY id");
            while (rs.next()) {
                Contato contato = new Contato(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone")
                );
                contatos.add(contato);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return contatos;
    }

    public List<Contato> getAll() {
        List<Contato> contatos = new ArrayList<>();
        try (Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = st.executeQuery("SELECT * FROM contatos");
            while (rs.next()) {
                Contato contato = new Contato(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone")
                );
                contatos.add(contato);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return contatos;
    }

    public boolean update(Contato contato) {
        boolean status = false;
        try (PreparedStatement st = conexao.prepareStatement("UPDATE contatos SET nome = ?, email = ?, telefone = ? WHERE id = ?")) {
            st.setString(1, contato.getNome());
            st.setString(2, contato.getEmail());
            st.setString(3, contato.getTelefone());
            st.setInt(4, contato.getId());
            st.executeUpdate();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public boolean delete(int id) {
        boolean status = false;
        try (Statement st = conexao.createStatement()) {
            st.executeUpdate("DELETE FROM contatos WHERE id = " + id);
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public void closeConnection() {
        close();
    }
}
