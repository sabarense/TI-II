package service;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import spark.Request;
import spark.Response;
import dao.ContatoDAO;
import model.Contato;

public class ContatoService {

    private ContatoDAO contatoDAO = new ContatoDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_UPDATE = 3;
    private final int FORM_ORDERBY_ID = 1;

    public ContatoService() {
        makeForm();
    }

    public void makeForm() {
        makeForm(FORM_INSERT, new Contato(), FORM_ORDERBY_ID);
    }

    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new Contato(), orderBy);
    }

    public void makeForm(int tipo, Contato contato, int orderBy) {
		String nomeArquivo = "formulario.html";
        form = "";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(nomeArquivo);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                form += (scanner.nextLine() + "\n");
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo formulario.html: " + e.getMessage());
        }

        String umContato = "";
        if (tipo != FORM_INSERT) {
            umContato += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umContato += "\t\t<tr>";
            umContato += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/contato/list/1\">Novo contato</a></b></font></td>";
            umContato += "\t\t</tr>";
            umContato += "\t</table>";
            umContato += "\t<br>";
        }

        if (tipo == FORM_INSERT || tipo == FORM_UPDATE) {
            String action = "/contato/";
            String name, buttonLabel;
            if (tipo == FORM_INSERT) {
                action += "insert";
                name = "Inserir Contato";
                buttonLabel = "Inserir";
            } else {
                action += "update/" + contato.getId();
                name = "Atualizar contato (ID " + contato.getId() + ")";
                buttonLabel = "Atualizar";
            }
            umContato += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
            umContato += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umContato += "\t\t<tr>";
            umContato += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
            umContato += "\t\t</tr>";
            umContato += "\t\t<tr>";
            umContato += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
            umContato += "\t\t</tr>";
            umContato += "\t\t<tr>";
            umContato += "\t\t\t<td>&nbsp;Nome: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\"" + contato.getNome() + "\"></td>";
            umContato += "\t\t\t<td>Email: <input class=\"input--register\" type=\"text\" name=\"email\" value=\"" + contato.getEmail() + "\"></td>";
            umContato += "\t\t\t<td>Telefone: <input class=\"input--register\" type=\"text\" name=\"telefone\" value=\"" + contato.getTelefone() + "\"></td>";
            umContato += "\t\t</tr>";
            umContato += "\t\t<tr>";
            umContato += "\t\t\t<td align=\"center\" colspan=\"3\"><input type=\"submit\" value=\"" + buttonLabel + "\" class=\"input--main__style input--button\"></td>";
            umContato += "\t\t</tr>";
            umContato += "\t</table>";
            umContato += "\t</form>";
        } else {
            System.out.println("ERRO! Tipo não identificado " + tipo);
        }
        form = form.replaceFirst("<UM-CONTATO>", umContato);

        String list = "<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">";
        list += "<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Contatos</b></font></td></tr>" +
                "<tr><td colspan=\"6\">&nbsp;</td></tr>" +
                "<tr>" +
                "<td><a href=\"/contato/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>" +
                "<td><b>Nome</b></td>" +
                "<td><b>Email</b></td>" +
                "<td><b>Telefone</b></td>" +
                "<td width=\"100\" align=\"center\"><b>Detalhar</b></td>" +
                "<td width=\"100\" align=\"center\"><b>Atualizar</b></td>" +
                "<td width=\"100\" align=\"center\"><b>Excluir</b></td>" +
                "</tr>";

        List<Contato> contatos;
        if (orderBy == FORM_ORDERBY_ID) {
            contatos = contatoDAO.getOrderById();
        } else {
            contatos = contatoDAO.getAll();
        }

        int i = 0;
        String bgcolor;
        for (Contato c : contatos) {
            bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
            list += "<tr bgcolor=\"" + bgcolor + "\">" +
                    "<td>" + c.getId() + "</td>" +
                    "<td>" + c.getNome() + "</td>" +
                    "<td>" + c.getEmail() + "</td>" +
                    "<td>" + c.getTelefone() + "</td>" +
                    "<td align=\"center\" valign=\"middle\"><a href=\"/contato/" + c.getId() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>" +
                    "<td align=\"center\" valign=\"middle\"><a href=\"/contato/update/" + c.getId() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>" +
                    "<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmDeleteContato('" + c.getId() + "', '" + c.getNome() + "', '" + c.getEmail() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>" +
                    "</tr>";
        }
        list += "</table>";
        form = form.replaceFirst("<LISTAR-CONTATO>", list);
    }

    public Object insert(Request request, Response response) {
        String nome = request.queryParams("nome");
        String email = request.queryParams("email");
        String telefone = request.queryParams("telefone");

        Contato contato = new Contato();
        contato.setNome(nome);
        contato.setEmail(email);
        contato.setTelefone(telefone);

        if (contatoDAO.insert(contato)) {
            response.status(201); // 201 Created
            return "Contato inserido com sucesso!";
        } else {
            response.status(500); // 500 Internal Server Error
            return "Erro ao inserir contato!";
        }
    }

    public Object getById(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Contato contato = contatoDAO.getById(id);
        if (contato != null) {
            return contato;
        } else {
            response.status(404); // 404 Not Found
            return "Contato não encontrado!";
        }
    }

    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Contato contato = contatoDAO.getById(id);

        if (contato != null) {
            response.status(200); // 200 OK
            makeForm(FORM_UPDATE, contato, FORM_ORDERBY_ID);
        } else {
            response.status(404); // 404 Not Found
            String resp = "Contato " + id + " não encontrado.";
            makeForm();
            form = form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }

        return form;
    }

    public Object getAll(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderby"));
        makeForm(orderBy);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return form;
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Contato contato = contatoDAO.getById(id);
        String resp = "";

        if (contato != null) {
            contato.setNome(request.queryParams("nome"));
            contato.setEmail(request.queryParams("email"));
            contato.setTelefone(request.queryParams("telefone"));
            contatoDAO.update(contato);
            response.status(200); // 200 OK
            resp = "Contato (ID " + contato.getId() + ") atualizado!";
        } else {
            response.status(404); // 404 Not Found
            resp = "Contato (ID " + id + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Contato contato = contatoDAO.getById(id);
        String resp = "";

        if (contato != null) {
            contatoDAO.delete(id);
            response.status(200); // 200 OK
            resp = "Contato (ID " + id + ") excluído!";
        } else {
            response.status(404); // 404 Not Found
            resp = "Contato (ID " + id + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }
}
