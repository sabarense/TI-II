package app;

import static spark.Spark.*;

import service.ContatoService;

public class AplicacaoContatosWebService {
	
	private static ContatoService contatoService = new ContatoService();
	
    public static void main(String[] args) {
    	port(6796);
    	
        staticFiles.location("/public");
        
        post("/contato/insert", (request, response) -> contatoService.insert(request, response));

        get("/contato/:id", (request, response) -> contatoService.getById(request, response));
        
        get("/contato/list/:orderby", (request, response) -> contatoService.getAll(request, response));

        get("/contato/update/:id", (request, response) -> contatoService.getToUpdate(request, response));
        
        post("/contato/update/:id", (request, response) -> contatoService.update(request, response));
           
        get("/contato/delete/:id", (request, response) -> contatoService.delete(request, response));
    }
}
