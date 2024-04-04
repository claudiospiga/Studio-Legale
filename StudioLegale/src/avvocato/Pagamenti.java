
package avvocato;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;



public class Pagamenti {
	 public Pagamenti() throws IOException {
	        // Creazione del server HTTP sulla porta 8080
	        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
	        server.createContext("/pagamenti", new PagamentiHandler());

	 }

	    // Gestore per le richieste relative ai pagamenti
	    static class PagamentiHandler implements HttpHandler {
	        @Override
	        public void handle(HttpExchange exchange) throws IOException {
	            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
	                // Gestisci la richiesta GET per recuperare i dati dei pagamenti
	                List<String> pagamentiList = new ArrayList<>();
	                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudioLegale", "root", "marina97!")) {
	                    // Query per recuperare i dati dei pagamenti e dei clienti associati
	                    String query = "SELECT p.*, c.nome, c.cognome, c.email FROM pagamenti p JOIN clienti c ON p.id_cliente = c.id";
	                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	                        ResultSet rs = pstmt.executeQuery();
	                        while (rs.next()) {
	                            // Estrai i dati del pagamento e del cliente associato
	                            int id = rs.getInt("id");
	                            int idCliente = rs.getInt("id_cliente");
	                            String nome = URLDecoder.decode(rs.getString("nome"), "UTF-8").replace("+", " ");
	                            String cognome = URLDecoder.decode(rs.getString("cognome"), "UTF-8").replace("+", " ");
	                            String email = URLDecoder.decode(rs.getString("email"), "UTF-8");
	                            String tipoPagamento = URLDecoder.decode(rs.getString("tipoPagamento"), "UTF-8").replace("+", " ");
	                            double prezzo = rs.getDouble("prezzo");
	                            String data = rs.getString("data"); // Supponiamo che la data sia memorizzata come stringa per semplicità
	                            // Aggiungi i dettagli del pagamento alla lista dei pagamenti
	                            pagamentiList.add("ID: " + id + ", ID Cliente: " + idCliente + ", Nome: " + nome + ", Cognome: " + cognome + ", Email: " + email +
	                                    ", Tipo Pagamento: " + tipoPagamento + ", Prezzo: " + prezzo + ", Data: " + data);
	                        }
	                    }
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }

	                // Genera la risposta HTML
	                StringBuilder response = new StringBuilder();
	                response.append("<!DOCTYPE html>");
	                response.append("<html>");
	                response.append("<head>");
	                response.append("<meta charset=\"UTF-8\">");
	                response.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
	                response.append("<title>Lista Pagamenti</title>");
	                response.append("    <link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" rel=\"stylesheet\">");
	                response.append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">");
	                response.append("    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>");
	             
	                response.append("<style>");
	                response.append("body {");
	                response.append("background-image: url('https://www.mad-hr.co.uk/wp-content/uploads/Law-Gavel-2.png');");
	                response.append("background-size: cover;");
	                response.append("background-repeat: no-repeat;");
	                response.append("background-attachment: fixed;");
	                response.append("color: white;");
	                response.append("}");
	                response.append("#id2 {");
	                response.append("padding: 20px;");
	                response.append("color: white;");
	                response.append("border-radius: 10px;");
	                response.append("transition: transform 0.3s ease; /* Smooth transition on hover */");
	                response.append("}");
	                response.append("#id2 {");
	                response.append("transition: font-size 0.3s ease; /* Smooth transition on font size change */");
	                response.append("}");
	                response.append("#id2:hover {");
	                response.append("font-size: 28px; /* Increase font size on hover */");
	                response.append("}");
	                response.append(".section {");
	                response.append("background-color: rgba(0, 0, 0, 0.27);");
	                response.append("padding: 20px;");
	                response.append("border-radius: 10px;");
	                response.append("}");
	                response.append("</style>");
	                response.append("</head>");
	                response.append("<body>");
	                // Codice del navbar
	                response.append("    <nav class=\"navbar bg-body-tertiary\"  >");
	                response.append("        <div class=\"container-fluid\">");
	                response.append("        <a class=\"navbar-brand\" href=\"/homeAmministratori\">"); 
	                response.append(" <img src=https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQ5BszBe7YInYmdxKJXOv0UC1gv52exnWYXQ&usqp=CAU alt=\"Logo\" width=\"30\" height=\"24\" class=\"d-inline-block align-text-top\">"         );
	                response.append("          Studio Legale Napoli");
	                response.append("          </a>");
	                response.append("        </div>");
	                response.append("      </nav>");

	                response.append("<div class=\"container\">");
	                response.append("<h2 class=\"text-center mt-5\" id=\"id2\">Lista Pagamenti</h2>");

	                // Creazione della tabella
	                response.append("<table class='table table-dark table-hover'>");
	                response.append("<thead class='thead-light'>");
	                response.append("<tr><th>ID</th><th>ID Cliente</th><th>Nome Cliente</th><th>Cognome Cliente</th><th>Email</th><th>Tipo Pagamento</th><th>Prezzo</th><th>Data</th><th>Azioni</th></tr>");
	                response.append("</thead>");
	                response.append("<tbody>");

	                for (String pagamento : pagamentiList) {
	                    String[] parts = pagamento.split(", ");
	                    String id = parts[0].substring(parts[0].indexOf(":") + 2);
	                    
	                    response.append("<tr>");
	                    for (String part : parts) {
	                        response.append("<td>").append(part.substring(part.indexOf(":") + 2)).append("</td>");
	                    }

	                    // Colonna delle azioni con il pulsante "Elimina"
	                    response.append("<td>");
	                    response.append("<form method='POST' action='/pagamenti' onsubmit='return confirm(\"Sei sicuro di voler eliminare questo pagamento?\");'>");
	                    response.append("<input type='hidden' name='id_pagamento' value='" + id + "' />");
	                    response.append("<input type='hidden' name='_method' value='DELETE' />");
	                    response.append("<button type='submit' class='btn btn-danger'>Elimina</button>");
	                    response.append("</form>");
	                    response.append("</td>");

	                    response.append("</tr>");
	                }

	                response.append("</tbody>");
	                response.append("</table>");

	                // Form per aggiungere un nuovo pagamento
	                response.append("<div class=\"section\">");
	                response.append("<div class=\"container mt-5\">");
	                response.append("<h2 class=\"mt-4\" id=\"id2\">Aggiungi Pagamento</h2>");
	                response.append("<form id=\"addPaymentForm\" class=\"form-inline justify-content-center\" method=\"post\" action=\"/pagamenti\">");
	                response.append("<div class=\"form-group mr-2\">");
	                response.append("<label for=\"client\" class=\"mr-2\">Seleziona Cliente:</label>");
	                response.append("<select id=\"client\" class=\"form-control mr-2\" name=\"id_cliente\">");
	                
	                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudioLegale", "root", "marina97!")) {
	                    String query = "SELECT id, nome, cognome FROM clienti";
	                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	                        ResultSet rs = pstmt.executeQuery();
	                        while (rs.next()) {
	                           
	                            int idCliente = rs.getInt("id");
	                            String nomeCliente = rs.getString("nome");
	                            String cognomeCliente = rs.getString("cognome");
	                            
	                            response.append("<option value='" + idCliente + "'>").append(nomeCliente).append(" ").append(cognomeCliente).append("</option>");
	                        }
	                    }
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }

	                response.append("</select><br>");
	                response.append("</div>");
	                response.append("<div class=\"form-group mr-2\">");
	                response.append("<label for=\"paymentType\" class=\"mr-2\">Tipo Pagamento:</label>");
	                response.append("<input type=\"text\" id=\"paymentType\" class=\"form-control mr-2\" name=\"tipoPagamento\">");
	                response.append("</div>");
	                response.append("<div class=\"form-group mr-2\">");
	                response.append("<label for=\"price\" class=\"mr-2\">Prezzo:</label>");
	                response.append("<input type=\"text\" id=\"price\" class=\"form-control mr-2\" name=\"prezzo\">");
	                response.append("</div>");
	                response.append("<div class=\"form-group mr-2\">");
	                response.append("<label for=\"date\" class=\"mr-2\">Data:</label>");
	                response.append("<input type=\"date\" id=\"date\" class=\"form-control mr-2\" name=\"data\">");
	                response.append("</div>");
	                response.append("<br><br><br>");
	                response.append("<input type=\"submit\" class=\"btn btn-primary\" value=\"Aggiungi\">");
	                response.append("</form>");
	                response.append("</div>");
	                response.append("</div>");
	                response.append("<br><br>");
	                
	                response.append("<br><br><br>");
	                response.append("<footer class=\"text-white py-4\" style=\"background-color: rgba(0, 0, 0, 0.723);\">");
	                response.append("<div class=\"container\">");
	                response.append("<div class=\"row\">");
	                response.append("<div class=\"col-md-4\">");
	                response.append("<h5>Avvocati</h5>");
	                response.append("<ul class=\"list-unstyled\">");
	                response.append("<li>Avv. Marco Rossi</li>");
	                response.append("<li>Avv. Laura Bianchi</li>");
	                response.append("<li>Avv. Luca Verdi</li>");
	                response.append("</ul>");
	                response.append("</div>");
	                response.append("<div class=\"col-md-4\">");
	                response.append("<h5>Indirizzo</h5>");
	                response.append("<p>Via Roma, 123<br>00100 Roma (RM), Italia</p>");
	                response.append("</div>");
	                response.append("<div class=\"col-md-4\">");
	                response.append("<h5>Contatti</h5>");
	                response.append("<p>Email: info@studiolegale.com<br>Telefono: +39 0123 456789</p>");
	                response.append("</div>");
	                response.append("</div>");
	                response.append("<div class=\"row\">");
	                response.append("<div class=\"col-md-6\">");
	                response.append("<p>Orari di apertura:<br>Lunedì - Venerdì: 9:00 - 18:00</p>");
	                response.append("</div>");
	                response.append("<div class=\"col-md-6 text-md-right\">");
	                response.append("<p>&copy; 2024 Studio Legale. Tutti i diritti riservati.</p>");
	                response.append("</div>");
	                response.append("</div>");
	                response.append("</div>");
	                response.append("</footer>");
	                response.append("</body>");
	                response.append("</html>");
	                // Invia la risposta al client
	                exchange.getResponseHeaders().set("Content-Type", "text/html");
	                int responseLength = response.toString().getBytes("UTF-8").length;
	                exchange.sendResponseHeaders(200, responseLength);

	                OutputStream os = exchange.getResponseBody();
	                os.write(response.toString().getBytes());
	                os.close();
	            } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
	                // Gestisci la richiesta POST per aggiungere o cancellare un pagamento
	                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
	                BufferedReader br = new BufferedReader(isr);
	                String formData = br.readLine();
	                String[] formDataArray = formData.split("&");
	                int idCliente;
	                String tipoPagamento;
	                double prezzo;
	                String data;

	                if (formData.contains("_method=DELETE")) {
	                    // Gestisci la richiesta DELETE per cancellare un pagamento
	                    int idPagamento = Integer.parseInt(formDataArray[0].split("=")[1]);
	                    cancellaPagamento(idPagamento);
	                } else {
	                    // Gestisci la richiesta POST per aggiungere un nuovo pagamento
	                    idCliente = Integer.parseInt(formDataArray[0].split("=")[1]);
	                    tipoPagamento = URLDecoder.decode(formDataArray[1].split("=")[1], "UTF-8");
	                    prezzo = Double.parseDouble(formDataArray[2].split("=")[1]);
	                    data = URLDecoder.decode(formDataArray[3].split("=")[1], "UTF-8");

	                    // Inserisci il nuovo pagamento nel database
	                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudioLegale", "root", "marina97!")) {
	                        String query = "INSERT INTO pagamenti (id_cliente, tipoPagamento, prezzo, data) VALUES (?, ?, ?, ?)";
	                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	                            pstmt.setInt(1, idCliente);
	                            pstmt.setString(2, tipoPagamento);
	                            pstmt.setDouble(3, prezzo);
	                            pstmt.setString(4, data);
	                            pstmt.executeUpdate();
	                        }
	                    } catch (SQLException e) {
	                        e.printStackTrace();
	                    }
	                }

	                // Reindirizza l'utente alla pagina dei pagamenti dopo aver aggiunto o cancellato il pagamento
	                exchange.getResponseHeaders().set("Location", "/pagamenti");
	                exchange.sendResponseHeaders(302, -1);
	            } else {
	                // Se il metodo della richiesta non è né GET né POST, restituisci l'errore 405 Method Not Allowed
	                exchange.sendResponseHeaders(405, -1);
	            }
	        }

	        // Metodo per cancellare un pagamento dal database
	        private void cancellaPagamento(int idPagamento) {
	            // Query per cancellare il pagamento con l'ID specificato
	            String deleteQuery = "DELETE FROM pagamenti WHERE id = ?";

	            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudioLegale", "root", "marina97!")) {
	                try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
	                    pstmt.setInt(1, idPagamento);
	                    int rowsAffected = pstmt.executeUpdate();
	                    if (rowsAffected > 0) {
	                        System.out.println("Pagamento con ID " + idPagamento + " cancellato con successo.");
	                    } else {
	                        System.out.println("Nessun pagamento trovato con l'ID " + idPagamento);
	                    }
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
}