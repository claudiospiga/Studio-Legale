package avvocato;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.*;

public class Precedenti {
     
    // Dettagli della connessione al database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/studiolegale";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "marina97!";
    public Precedenti() throws IOException {
        // Creazione del server HTTP sulla porta 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Gestione della richiesta GET per la pagina degli avvocati
        server.createContext("/precedenti", new precedentiHandler());

       }

     
    

    static class precedentiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                // Connessione al database e recupero degli avvocati
                List<String> avvocati = new ArrayList<>();
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT * FROM precedenti";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        ResultSet rs = pstmt.executeQuery();
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String nome = rs.getString("nome");
                            String cognome = rs.getString("cognome");
                            String descrizione = rs.getString("descrizione");
                            avvocati.add("ID: " + id + ", Nome: " + nome + ", Cognome: " + cognome + ", Descrizione: " + descrizione);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Risposta al client
                StringBuilder response = new StringBuilder();
                response.append("<!DOCTYPE html>");
                response.append("<html lang=\"en\">");
                response.append("<head>");
                response.append("    <meta charset=\"UTF-8\">");
                response.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                response.append("    <title>Gestione Precedenti</title>");
                response.append("    <link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" rel=\"stylesheet\">");
                response.append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">");
                response.append("    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>");
                response.append("    <style>");
                response.append("        body {");
                response.append("             background-image: url('https://www.mad-hr.co.uk/wp-content/uploads/Law-Gavel-2.png');");
                response.append("             background-size: cover;");
                response.append("             background-repeat: no-repeat;");
                response.append("             background-attachment: fixed;");
                response.append("             color: white;");
                response.append("           }");
                response.append("           #id2 {");
                response.append("             padding: 20px;");
                response.append("             color: white;");
                response.append("             border-radius: 10px;");
                response.append("             transition: transform 0.3s ease; /* Smooth transition on hover */");
                response.append("           }");
                response.append("           #id2 {");
                response.append("             transition: font-size 0.3s ease; /* Smooth transition on font size change */");
                response.append("           }");
                response.append("           #id2:hover {");
                response.append("             font-size: 28px; /* Increase font size on hover */");
                response.append("           }");
                response.append("           .section{");
                response.append("            background-color: rgba(0, 0, 0, 0.27);");
                response.append("          padding: 20px;");
                response.append("          border-radius: 10px;");
                response.append("           }");
                response.append("    </style>");
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

                // Parte principale del contenuto
                response.append("<div class=\"container\">");
                response.append("    <h1 class=\"mt-5\">Gestione Precedenti</h1>");

                // Lista Precedenti
                response.append("    <h2 id=\"id2\" class=\"mt-4\">Lista Precedenti</h2>");
             // Creazione della tabella
                response.append("<div class='table-responsive'>");
                response.append("<table class='table table-dark table-hover'>");
                response.append("<thead>");
                response.append("<tr><th>ID</th><th>Nome</th><th>Cognome</th><th>Descrizione</th><th>Azioni</th></tr>");
                response.append("</thead>");
                response.append("<tbody>");

                for (String avvocato : avvocati) {
                    String[] parts = avvocato.split(", ");
                    String id = parts[0].substring(parts[0].indexOf(":") + 2);
                    
                    response.append("<tr>");
                    for (String part : parts) {
                        response.append("<td>").append(part.substring(part.indexOf(":") + 2)).append("</td>");
                    }

                    // Colonna delle azioni con il pulsante "Elimina"
                    response.append("<td>");
                    response.append("<form method='POST' action='/precedenti/delete' onsubmit='return confirm(\"Sei sicuro di voler eliminare questo dato?\");'>");
                    response.append("<input type='hidden' name='id' value='" + id + "' />");
                    response.append("<button type='submit' class='btn btn-danger'>Elimina</button>");
                    response.append("</form>");
                    response.append("</td>");

                    response.append("</tr>");
                }

                response.append("</tbody>");
                response.append("</table>");
                response.append("</div>");
                response.append("<div class=\"section\">");
                response.append("    <h2 id=\"id2\" class=\"mt-4\">Aggiungi Precedenti</h2>");
                response.append("    <form class=\"mt-2\" method='post' action='/precedenti'>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"nome\">Nome:</label>");
                response.append("            <input type='text' class=\"form-control\" name='nome'>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"cognome\">Cognome:</label>");
                response.append("            <input type='text' class=\"form-control\" name='cognome'>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"descrizione\">Descrizione:</label>");
                response.append("            <input type='text' class=\"form-control\" name='descrizione'>");
                response.append("        </div>");
                response.append("        <button type=\"submit\" class=\"btn btn-primary\">Aggiungi</button>");
                response.append("    </form>");
                response.append("</div>");
                response.append("<br><br>");

            
                



                // Modifica Precedenti
                response.append("<div class=\"section\">");
                response.append("    <h2 id=\"id2\" class=\"mt-4\">Modifica Precedenti</h2>");
                response.append("    <form class=\"mt-2\" method='post' action='/precedenti/update'>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"id\">ID Precedenti:</label>");
                response.append("            <input type='text' class=\"form-control\" name='id'>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_nome\">Nuovo Nome:</label>");
                response.append("            <input type='text' class=\"form-control\" name='new_nome'>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_cognome\">Nuovo Cognome:</label>");
                response.append("            <input type='text' class=\"form-control\" name='new_cognome'>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_descrizione\">Nuova Descrizione:</label>");
                response.append("            <input type='text' class=\"form-control\" name='new_descrizione'>");
                response.append("        </div>");
                response.append("        <button type=\"submit\" class=\"btn btn-warning\">Modifica</button>");
                response.append("    </form>");
                response.append("</div>");

                // Parte finale del codice HTML
                response.append("</div>");
                response.append("<br><br><br>");
                response.append("<footer class='text-white py-4' style='background-color: rgba(0, 0, 0, 0.723);'>");
                response.append("    <div class='container'>");
                response.append("        <div class='row'>");
                response.append("            <div class='col-md-4'>");
                response.append("                <h5>Avvocati</h5>");
                response.append("                <ul class='list-unstyled'>");
                response.append("                    <li>Avv. Marco Rossi</li>");
                response.append("                    <li>Avv. Laura Bianchi</li>");
                response.append("                    <li>Avv. Luca Verdi</li>");
                response.append("                </ul>");
                response.append("            </div>");
                response.append("            <div class='col-md-4'>");
                response.append("                <h5>Indirizzo</h5>");
                response.append("                <p>Via Roma, 123<br>00100 Roma (RM), Italia</p>");
                response.append("            </div>");
                response.append("            <div class='col-md-4'>");
                response.append("                <h5>Contatti</h5>");
                response.append("                <p>Email: info@studiolegale.com<br>Telefono: +39 0123 456789</p>");
                response.append("            </div>");
                response.append("        </div>");
                response.append("        <div class='row'>");
                response.append("            <div class='col-md-6'>");
                response.append("                <p>Orari di apertura:<br>Lunedì - Venerdì: 9:00 - 18:00</p>");
                response.append("            </div>");
                response.append("            <div class='col-md-6 text-md-right'>");
                response.append("                <p>&copy; 2024 Studio Legale. Tutti i diritti riservati.</p>");
                response.append("            </div>");
                response.append("        </div>");
                response.append("    </div>");
                response.append("</footer>");
                response.append("<!-- Bootstrap JS -->");
                response.append("<script src=\"https://code.jquery.com/jquery-3.5.1.slim.min.js\"></script>");
                response.append("<script src=\"https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js\"></script>");
                response.append("<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js\"></script>");
                response.append("</body>");
                response.append("</html>");

                exchange.getResponseHeaders().set("Content-Type", "text/html");
                int lunghezza1 =response.toString().getBytes("UTF-8").length;
    			exchange.sendResponseHeaders(200, lunghezza1);
              
                OutputStream os = exchange.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();
            } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                     BufferedReader br = new BufferedReader(isr)) {
                    String formData = br.readLine();
                    String[] formDataArray = formData.split("&");

                    // Gestione aggiunta precedente
                    if (formDataArray.length == 3 && exchange.getRequestURI().getPath().equals("/precedenti")) {
                        // Codice per aggiungere un av
                    	// Codice per aggiungere un avvocato
                        String nome = formDataArray[0].split("=")[1];
                        String cognome = formDataArray[1].split("=")[1];
                        String descrizione = formDataArray[2].split("=")[1];
                  
                        // Connessione al database e inserimento del precedente
                        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                            String query = "INSERT INTO precedenti (nome, cognome, descrizione) VALUES (?, ?, ?)";
                            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                                pstmt.setString(1, nome);
                                pstmt.setString(2, cognome);
                                pstmt.setString(3, descrizione);
                                pstmt.executeUpdate();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    // Gestione eliminazione precdenti
                    else if (formDataArray.length == 1 && exchange.getRequestURI().getPath().equals("/precedenti/delete")) {
                        String idToDelete = formDataArray[0].split("=")[1];
                        // Connessione al database e eliminazione del precedente
                        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                            String query = "DELETE FROM precedenti WHERE id=?";
                            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                                pstmt.setString(1, idToDelete);
                                pstmt.executeUpdate();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                 // Gestione modifica 
                    else if (formDataArray.length >= 4 && exchange.getRequestURI().getPath().equals("/precedenti/update")) {
                        String idToUpdate = formDataArray[0].split("=")[1];

                        // Recupera i valori precedenti dal database
                        String query = "SELECT * FROM precedenti WHERE id=?";
                        String nome = "";
                        String cognome = "";
                        String descrizione = "";
                        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                                pstmt.setString(1, idToUpdate);
                                ResultSet rs = pstmt.executeQuery();
                                if (rs.next()) {
                                    nome = rs.getString("nome");
                                    cognome = rs.getString("cognome");
                                    descrizione = rs.getString("descrizione");
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // Aggiorna solo i campi non vuoti
                        for (String field : formDataArray) {
                            String[] parts = field.split("=");
                            if (parts.length == 2) {
                                String fieldName = parts[0];
                                String value = parts[1];
                                switch (fieldName) {
                                    case "new_nome":
                                        nome = value.isEmpty() ? nome : value;
                                        break;
                                    case "new_cognome":
                                        cognome = value.isEmpty() ? cognome : value;
                                        break;
                                    case "new_descrizione":
                                        descrizione = value.isEmpty() ? descrizione : value;
                                        break;
                                    default:
                                        // Ignora altri campi
                                        break;
                                }
                            }
                        }

                        // Connessione al database e modifica dei precedenti
                        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                            query = "UPDATE precedenti SET nome=?, cognome=?, descrizione=? WHERE id=?";
                            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                                pstmt.setString(1, nome);
                                pstmt.setString(2, cognome);
                                pstmt.setString(3, descrizione);
                                pstmt.setString(4, idToUpdate);
                                pstmt.executeUpdate();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    // Reindirizza l'utente alla pagina degli avvocati dopo l'azione eseguita
                    exchange.getResponseHeaders().set("Location", "/precedenti");
                    exchange.sendResponseHeaders(302, -1);
                }
            }
        }
    }
}