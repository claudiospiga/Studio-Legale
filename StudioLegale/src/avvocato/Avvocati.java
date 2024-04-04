package avvocato;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;

public class Avvocati {

   public Avvocati() throws IOException {
        // Creazione del server HTTP sulla porta 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Gestione della richiesta GET per la pagina degli avvocati
        server.createContext("/avvocati", new AvvocatiHandler());

      
    }

    static class AvvocatiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                handleGetRequest(exchange);
            } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                handlePostRequest(exchange);
            }
        }

        private void handleGetRequest(HttpExchange exchange) throws IOException {
            List<String> avvocati = new ArrayList<>();
            try (Connection conn = getConnection()) {
                String query = "SELECT * FROM avvocati";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nome = URLDecoder.decode(rs.getString("nome"), "UTF-8").replace("+", " ");
                        String cognome = URLDecoder.decode(rs.getString("cognome"), "UTF-8").replace("+", " ");
                        String descrizione = URLDecoder.decode(rs.getString("descrizione"), "UTF-8").replace("+", " ");
                        String telefono = URLDecoder.decode(rs.getString("telefono"), "UTF-8").replace("+", " ");
                        String email = URLDecoder.decode(rs.getString("email"), "UTF-8");
                        avvocati.add("ID: " + id + ", Nome: " + nome + ", Cognome: " + cognome + ", Descrizione: " + descrizione + ", Telefono: " + telefono + ", Email: " + email);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            StringBuilder response = new StringBuilder();
            response.append("<!DOCTYPE html>\n");
            response.append("<html lang=\"en\">\n");
            response.append("<head>\n");
            response.append("    <meta charset=\"UTF-8\">\n");
            response.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            response.append("    <title>Gestione Avvocati</title>\n");
            response.append("    <link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" rel=\"stylesheet\">\n");
            response.append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">\n");
            response.append("    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>\n\n");
            response.append("    <style>\n");
            response.append("        body {\n");
            response.append("             background-image: url('https://www.mad-hr.co.uk/wp-content/uploads/Law-Gavel-2.png');\n");
            response.append("             background-size: cover;\n");
            response.append("             background-repeat: no-repeat;\n");
            response.append("             background-attachment: fixed;\n");
            response.append("             color: white;\n");
            response.append("           }\n");
            response.append("           #id2 {\n");
            response.append("             padding: 20px;\n");
            response.append("             color: white;\n");
            response.append("             border-radius: 10px;\n");
            response.append("             transition: transform 0.3s ease; /* Smooth transition on hover */\n");
            response.append("           }\n");
            response.append("           #id2 {\n");
            response.append("             transition: font-size 0.3s ease; /* Smooth transition on font size change */\n");
            response.append("           }\n");
            response.append("           #id2:hover {\n");
            response.append("             font-size: 28px; /* Increase font size on hover */\n");
            response.append("           }\n");
            response.append("           .section{\n");
            response.append("            background-color: rgba(0, 0, 0, 0.27);\n");
            response.append("          padding: 20px;\n");
            response.append("          border-radius: 10px;\n");
            response.append("           }\n");
            response.append("    </style>\n");
            response.append("</head>\n");
            response.append("<body>\n");
            response.append("    <nav class=\"navbar bg-body-tertiary\"  >\n");
            response.append("        <div class=\"container-fluid\">\n");
            response.append("        <a class=\"navbar-brand\" href=\"/homeAmministratori\">"); 
            response.append(" <img src=https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQ5BszBe7YInYmdxKJXOv0UC1gv52exnWYXQ&usqp=CAU alt=\"Logo\" width=\"30\" height=\"24\" class=\"d-inline-block align-text-top\">"         );
            response.append("          Studio Legale Napoli\n");
            response.append("          </a>\n");
            response.append("        </div>\n");
            response.append("      </nav>\n");
            response.append("<div class=\"container\">\n");
            response.append("    <h1 class=\"mt-5 text-center\">Gestione Avvocati</h1>\n");
            response.append("    <h2 id=\"id2\">Lista Avvocati</h2>\n");
            response.append("<table class='table table-dark'>");
            response.append("<thead><tr>");
            response.append("<th>ID</th><th>Nome</th><th>Cognome</th><th>Descrizione</th><th>Telefono</th><th>Email</th><th>Azioni</th>");
            response.append("</tr></thead>");
            response.append("<tbody>");

            for (String avvocato : avvocati) {
                String[] parti = avvocato.split(", ");
                String id = parti[0].split(": ")[1];
                response.append("<tr>");
                for (String parte : parti) {
                    response.append("<td>").append(parte.split(": ")[1]).append("</td>");
                }
                response.append("<td>");
                response.append("<form method='POST' action='/avvocati/delete' onsubmit='return confirm(\"Sei sicuro di voler eliminare questo avvocato?\");'>");
                response.append("<input type='hidden' name='idToDelete' value='" + id + "'/>");
                response.append("<button type='submit' class='btn btn-danger btn-sm'>Elimina</button>");
                response.append("</form>");
                response.append("</td>");
                response.append("</tr>");
            }
            response.append("</tbody></table>");

            response.append("    <br><br>\n");
            response.append("<div class=\"section\">\n");
            response.append("    <h2 id=\"id2\" class=\"mt-4\">Aggiungi Avvocato</h2>\n");
            response.append("    <form class=\"mt-2\" method='post' action='/avvocati'>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"nome\">Nome:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='nome' required>\n");
            response.append("        </div>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"cognome\">Cognome:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='cognome' required>\n");
            response.append("        </div>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"descrizione\">Descrizione:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='descrizione' required>\n");
            response.append("        </div>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"telefono\">Telefono:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='telefono' required>\n");
            response.append("        </div>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"email\">Email:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='email' required>\n");
            response.append("        </div>\n");
            response.append("        <button type=\"submit\" class=\"btn btn-primary\">Aggiungi</button>\n");
            response.append("    </form>\n");
            response.append("</div>\n");
            response.append("<br><br>\n");
         
            response.append("<div class=\"section\">\n");
            response.append("    <h2 id=\"id2\" class=\"mt-4\">Modifica Avvocato</h2>\n");
            response.append("    <form class=\"mt-2\" method='post' action='/avvocati/update'>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"id\">ID Avvocato:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='id'>\n");
            response.append("        </div>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"new_nome\">Nuovo Nome:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='new_nome'>\n");
            response.append("        </div>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"new_cognome\">Nuovo Cognome:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='new_cognome'>\n");
            response.append("        </div>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"new_descrizione\">Nuova Descrizione:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='new_descrizione'>\n");
            response.append("        </div>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"new_telefono\">Nuovo Telefono:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='new_telefono'>\n");
            response.append("        </div>\n");
            response.append("        <div class=\"form-group\">\n");
            response.append("            <label for=\"new_email\">Nuova Email:</label>\n");
            response.append("            <input type='text' class=\"form-control\" name='new_email'>\n");
            response.append("        </div>\n");
            response.append("        <button type=\"submit\" class=\"btn btn-warning\">Modifica</button>\n");
            response.append("    </form>\n");
            response.append("</div>\n");
            response.append("</div>\n");
            response.append("<br><br><br>\n");
            response.append("<footer class=\"text-white py-4\" style=\" background-color: rgba(0, 0, 0, 0.723);\">\n");
            response.append("    <div class=\"container\">\n");
            response.append("        <div class=\"row\">\n");
            response.append("            <div class=\"col-md-4\">\n");
            response.append("                <h5>Avvocati</h5>\n");
            response.append("                <ul class=\"list-unstyled\">\n");
            response.append("                    <li>Avv. Marco Rossi</li>\n");
            response.append("                    <li>Avv. Laura Bianchi</li>\n");
            response.append("                    <li>Avv. Luca Verdi</li>\n");
            response.append("                </ul>\n");
            response.append("            </div>\n");
            response.append("            <div class=\"col-md-4\">\n");
            response.append("                <h5>Indirizzo</h5>\n");
            response.append("                <p>Via Roma, 123<br>00100 Roma (RM), Italia</p>\n");
            response.append("            </div>\n");
            response.append("            <div class=\"col-md-4\">\n");
            response.append("                <h5>Contatti</h5>\n");
            response.append("                <p>Email: info@studiolegale.com<br>Telefono: +39 0123 456789</p>\n");
            response.append("            </div>\n");
            response.append("        </div>\n");
            response.append("        <div class=\"row\">\n");
            response.append("            <div class=\"col-md-6\">\n");
            response.append("                <p>Orari di apertura:<br>Lunedì - Venerdì: 9:00 - 18:00</p>\n");
            response.append("            </div>\n");
            response.append("            <div class=\"col-md-6 text-md-right\">\n");
            response.append("                <p>&copy; 2024 Studio Legale. Tutti i diritti riservati.</p>\n");
            response.append("            </div>\n");
            response.append("        </div>\n");
            response.append("    </div>\n");
            response.append("</footer>\n\n");
            response.append("<script src=\"https://code.jquery.com/jquery-3.5.1.slim.min.js\"></script>\n");
            response.append("<script src=\"https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js\"></script>\n");
            response.append("<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js\"></script>\n");
            response.append("</body>\n");
            response.append("</html>\n");

            int lunghezza = response.toString().getBytes("UTF-8").length;
            
            // Invio della risposta al client
            exchange.sendResponseHeaders(200, lunghezza); 
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }

        private void handlePostRequest(HttpExchange exchange) throws IOException {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            String[] formDataArray = formData.split("&");

            if (formDataArray.length == 5 && exchange.getRequestURI().getPath().equals("/avvocati")) {
                addAvvocato(formDataArray);
            } else if (formDataArray.length == 1 && exchange.getRequestURI().getPath().equals("/avvocati/delete")) {
                deleteAvvocato(formDataArray[0]);
            } else if (exchange.getRequestURI().getPath().equals("/avvocati/update")) {
                updateAvvocato(formDataArray);
            }

            exchange.getResponseHeaders().set("Location", "/avvocati");
            exchange.sendResponseHeaders(302, -1);
        }

        private Connection getConnection() throws SQLException {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!");
        }

        private void addAvvocato(String[] formDataArray) {
            String nome = formDataArray[0].split("=")[1];
            String cognome = formDataArray[1].split("=")[1];
            String descrizione = formDataArray[2].split("=")[1];
            String telefono = formDataArray[3].split("=")[1];
            String email = formDataArray[4].split("=")[1];

            try (Connection conn = getConnection()) {
                String query = "INSERT INTO avvocati (nome, cognome, descrizione, telefono, email) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, nome);
                    pstmt.setString(2, cognome);
                    pstmt.setString(3, descrizione);
                    pstmt.setString(4, telefono);
                    pstmt.setString(5, email);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void deleteAvvocato(String formData) {
            // Assumendo che il formData per l'eliminazione sia nel formato "id=numeroID"
            String[] parts = formData.split("=");
            if (parts.length == 2) {
                try {
                    int idToDelete = Integer.parseInt(parts[1]); // Converti la stringa ID in un intero
                    try (Connection conn = getConnection()) {
                        String query = "DELETE FROM avvocati WHERE id=?";
                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                            pstmt.setInt(1, idToDelete); // Usa pstmt.setInt per impostare un parametro di tipo intero
                            pstmt.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Errore nella conversione dell'ID: " + e.getMessage());
                }
            }
        }


        private void updateAvvocato(String[] formDataArray) {
            String idToUpdate = formDataArray[0].split("=")[1];
            String query = "SELECT * FROM avvocati WHERE id=?";
            String nome = "";
            String cognome = "";
            String descrizione = "";
            String telefono = "";
            String email = "";

            try (Connection conn = getConnection()) {
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, idToUpdate);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        nome = rs.getString("nome");
                        cognome = rs.getString("cognome");
                        descrizione = rs.getString("descrizione");
                        telefono = rs.getString("telefono");
                        email = rs.getString("email");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

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
                        case "new_telefono":
                            telefono = value.isEmpty() ? telefono : value;
                            break;
                        case "new_email":
                            email = value.isEmpty() ? email : value;
                            break;
                    }
                }
            }

            try (Connection conn = getConnection()) {
                query = "UPDATE avvocati SET nome=?, cognome=?, descrizione=?, telefono=?, email=? WHERE id=?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, nome);
                    pstmt.setString(2, cognome);
                    pstmt.setString(3, descrizione);
                    pstmt.setString(4, telefono);
                    pstmt.setString(5, email);
                    pstmt.setString(6, idToUpdate);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
