package avvocato;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;

public class Clienti {
    public static void main(String[] args) throws IOException {
        // Creazione del server HTTP sulla porta 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/clienti", new AggiungiCliente());

        // Avvio del server
        server.start();
        System.out.println("server in eseguzione");
    }

    static class AggiungiCliente implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
        	if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                // Connessione al database e recupero dei clienti
                List<String> clienti = new ArrayList<>();
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudioLegale", "root", "marina97!")) {
                    String query = "SELECT * FROM clienti";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        ResultSet rs = pstmt.executeQuery();
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String nome = URLDecoder.decode(rs.getString("nome"), "UTF-8");
                            String cognome = URLDecoder.decode(rs.getString("cognome"), "UTF-8");
                            String sesso = URLDecoder.decode(rs.getString("sesso"), "UTF-8");
                            String indirizzo = URLDecoder.decode(rs.getString("indirizzo"), "UTF-8");
                            String nazionalita = URLDecoder.decode(rs.getString("nazionalita"), "UTF-8");
                            String documento = URLDecoder.decode(rs.getString("documento"), "UTF-8");
                            String email = URLDecoder.decode(rs.getString("email"), "UTF-8");
                            String telefono = URLDecoder.decode(rs.getString("telefono"), "UTF-8");
                            String annotazioni = URLDecoder.decode(rs.getString("annotazioni"), "UTF-8");
                            String statoCivile = URLDecoder.decode(rs.getString("statoCivile"), "UTF-8");
                            clienti.add("ID: " + id + ", Nome: " + nome + ", Cognome: " + cognome + ", Sesso: " + sesso +
                                        ", Indirizzo: " + indirizzo + ", Nazionalità: " + nazionalita + ", Documento: " + documento +
                                        ", Email: " + email + ", Telefono: " + telefono + ", Annotazioni: " + annotazioni +
                                        ", Stato Civile: " + statoCivile);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Risposta al client
                StringBuilder response = new StringBuilder();
                response.append("<!DOCTYPE html>");
                response.append("<html>");

                response.append("<head>");
                response.append("<meta charset=\"UTF-8\">");
                response.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                response.append("<title>Gestione Clienti</title>");
                response.append("<link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" rel=\"stylesheet\">");
                response.append("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">");
                response.append("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>");
                response.append("<style>");
                response.append("body {");
                response.append("    background-image: url('https://www.mad-hr.co.uk/wp-content/uploads/Law-Gavel-2.png');");
                response.append("    background-size: cover;");
                response.append("    background-repeat: no-repeat;");
                response.append("    background-attachment: fixed;");
                response.append("    color: white;");
                response.append("}");
                response.append("#id2 {");
                response.append("    padding: 20px;");
                response.append("    color: white;");
                response.append("    border-radius: 10px;");
                response.append("    transition: transform 0.3s ease;");
                response.append("}");
                response.append("#id2 {");
                response.append("    transition: font-size 0.3s ease;");
                response.append("}");
                response.append("#id2:hover {");
                response.append("    font-size: 28px;");
                response.append("}");
                response.append(".section {");
                response.append("    background-color: rgba(0, 0, 0, 0.27);");
                response.append("    padding: 20px;");
                response.append("    border-radius: 10px;");
                response.append("}");
                response.append("</style>");
                response.append("</head>");

                response.append("<body>");

                response.append("<nav class=\"navbar bg-body-tertiary\">");
                response.append("    <div class=\"container-fluid\">");
                response.append("        <a class=\"navbar-brand\" href=\"/homeAmministratori\">");  
                response.append(" <img src=https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQ5BszBe7YInYmdxKJXOv0UC1gv52exnWYXQ&usqp=CAU alt=\"Logo\" width=\"30\" height=\"24\" class=\"d-inline-block align-text-top\">"  );
                response.append("        Studio Legale Napoli");
                response.append("        </a>");
                response.append("    </div>");
                response.append("</nav>");

                response.append("<div class=\"container\">");

                response.append("<div class=\"section\">");
                response.append("<h1 class=\"mt-5\">Gestione Clienti</h1>");
                response.append("</div>");

                response.append("<div class=\"section\">");
                response.append("<h2 id=\"id2\" class=\"mt-5\">Lista Clienti</h2>");
                response.append("<div class='table-responsive'>");
                response.append("<table class='table table-dark table-hover'>");
                response.append("<thead>");
                response.append("<tr>");
                response.append("<th>ID</th>");
                response.append("<th>Nome</th>");
                response.append("<th>Cognome</th>");
                response.append("<th>Sesso</th>");
                response.append("<th>Indirizzo</th>");
                response.append("<th>Nazionalità</th>");
                response.append("<th>Documento</th>");
                response.append("<th>Email</th>");
                response.append("<th>Telefono</th>");
                response.append("<th>Annotazioni</th>");
                response.append("<th>Stato Civile</th>");
                response.append("</tr>");
                response.append("</thead>");
                response.append("<tbody>");

                for (String cliente : clienti) {
                    String[] datiCliente = cliente.split(", ");
                    response.append("<tr>");
                    for (String dato : datiCliente) {
                        String valore = dato.substring(dato.indexOf(":") + 2);
                        response.append("<td>").append(valore).append("</td>");
                    }
                    // Aggiungi colonne per i pulsanti
                    String idCliente = datiCliente[0].split(": ")[1]; // Assumi che il primo elemento sia l'ID
                    response.append("<td>");
                    
                    response.append("<form method='post' action='/clienti/delete' onsubmit='return confirm(\"Sei sicuro di voler eliminare questo cliente?\");' style='display: inline;'>");
                    response.append("<input type='hidden' name='idToDelete' value='" + idCliente + "'/>");
                    response.append("<button type='submit' class='btn btn-danger btn-sm'>Elimina</button>"); // Pulsante Elimina
                    response.append("</form>");
                    response.append("</td>");
                    response.append("</tr>");
                }

                response.append("</tbody>");
                response.append("</table>");
                response.append("</div>"); // Chiusura div table-responsive
                response.append("</div>");
                response.append("<br><br>");
                //form aggiungi

                response.append("<div class=\"section\">");
                response.append("<h2 id=\"id2\" class=\"mt-5\">Aggiungi Cliente</h2>");
                response.append("<form method='post' action='/clienti' class=\"mt-3\">");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"nome\" class=\"form-label\">Nome:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"nome\" name='nome'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"cognome\" class=\"form-label\">Cognome:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"cognome\" name='cognome'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"sesso\" class=\"form-label\">Sesso:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"sesso\" name='sesso'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"indirizzo\" class=\"form-label\">Indirizzo:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"indirizzo\" name='indirizzo'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"nazionalita\" class=\"form-label\">Nazionalità:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"nazionalita\" name='nazionalita'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"documento\" class=\"form-label\">Documento:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"documento\" name='documento'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"email\" class=\"form-label\">Email:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"email\" name='email'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"telefono\" class=\"form-label\">Telefono:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"telefono\" name='telefono'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"annotazioni\" class=\"form-label\">Annotazioni:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"annotazioni\" name='annotazioni'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"statoCivile\" class=\"form-label\">Stato Civile:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"statoCivile\" name='statoCivile'>");
                response.append("</div>");
                response.append("<button type=\"submit\" class=\"btn btn-primary\">Aggiungi</button>");
                response.append("</form>");
                response.append("</div>");
                response.append("<br><br>");

           
                //form modifica


                response.append("<div class=\"section\">");
                response.append("<h2 id=\"id2\" class=\"mt-5\">Modifica cliente</h2>");
                response.append("<form method='post' action='/clienti/update' class=\"mt-3\">");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"id_modifica\" class=\"form-label\">ID Cliente:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"id_modifica\" name='id'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"new_nome\" class=\"form-label\">Nuovo Nome:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"new_nome\" name='new_nome'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"new_cognome\" class=\"form-label\">Nuovo Cognome:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"new_cognome\" name='new_cognome'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"new_sesso\" class=\"form-label\">Nuovo Sesso:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"new_sesso\" name='new_sesso'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"new_indirizzo\" class=\"form-label\">Nuovo Indirizzo:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"new_indirizzo\" name='new_indirizzo'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"new_nazionalita\" class=\"form-label\">Nuova Nazionalità:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"new_nazionalita\" name='new_nazionalita'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"new_documento\" class=\"form-label\">Nuovo Documento:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"new_documento\" name='new_documento'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"new_email\" class=\"form-label\">Nuova Email:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"new_email\" name='new_email'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"new_telefono\" class=\"form-label\">Nuovo Telefono:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"new_telefono\" name='new_telefono'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"new_annotazioni\" class=\"form-label\">Nuove Annotazioni:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"new_annotazioni\" name='new_annotazioni'>");
                response.append("</div>");
                response.append("<div class=\"mb-3\">");
                response.append("<label for=\"new_statoCivile\" class=\"form-label\">Nuovo Stato Civile:</label>");
                response.append("<input type='text' class=\"form-control\" id=\"new_statoCivile\" name='new_statoCivile'>");
                response.append("</div>");
                response.append("<button type=\"submit\" class=\"btn btn-warning\">Modifica</button>");
                response.append("</form>");
                response.append("</div>");

                response.append("</div>");
                response.append("<br><br>");

                response.append("<footer class=\"text-white py-4\" style=\" background-color: rgba(0, 0, 0, 0.723);\">");
                response.append("    <div class=\"container\">");
                response.append("        <div class=\"row\">");
                response.append("            <div class=\"col-md-4\">");
                response.append("                <h5>Avvocati</h5>");
                response.append("                <ul class=\"list-unstyled\">");
                response.append("                    <li>Avv. Marco Rossi</li>");
                response.append("                    <li>Avv. Laura Bianchi</li>");
                response.append("                    <li>Avv. Luca Verdi</li>");
                response.append("                </ul>");
                response.append("            </div>");
                response.append("            <div class=\"col-md-4\">");
                response.append("                <h5>Indirizzo</h5>");
                response.append("                <p>Via Roma, 123<br>00100 Roma (RM), Italia</p>");
                response.append("            </div>");
                response.append("            <div class=\"col-md-4\">");
                response.append("                <h5>Contatti</h5>");
                response.append("                <p>Email: info@studiolegale.com<br>Telefono: +39 0123 456789</p>");
                response.append("            </div>");
                response.append("        </div>");
                response.append("        <div class=\"row\">");
                response.append("            <div class=\"col-md-6\">");
                response.append("                <p>Orari di apertura:<br>Lunedì - Venerdì: 9:00 - 18:00</p>");
                response.append("            </div>");
                response.append("            <div class=\"col-md-6 text-md-right\">");
                response.append("                <p>&copy; 2024 Studio Legale. Tutti i diritti riservati.</p>");
                response.append("            </div>");
                response.append("        </div>");
                response.append("    </div>");
                response.append("</footer>");

                response.append("</body>");
                response.append("</html>");

                exchange.getResponseHeaders().set("Content-Type", "text/html");
                int lunghezza1 =response.toString().getBytes("UTF-8").length;
    			exchange.sendResponseHeaders(200, lunghezza1);
              
                OutputStream os = exchange.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();
            }  else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();
                String[] formDataArray = formData.split("&");
                
                // Gestione aggiunta cliente
                if (formDataArray.length == 10 && exchange.getRequestURI().getPath().equals("/clienti")) {
                    // Codice per aggiungere un cliente
                	
                     String nome = formDataArray[0].split("=")[1];
                     String cognome = formDataArray[1].split("=")[1];
                     String sesso = formDataArray[2].split("=")[1];
                     String indirizzo = formDataArray[3].split("=")[1];
                     String nazionalita = formDataArray[4].split("=")[1];
                     String documento = formDataArray[5].split("=")[1];
                     String email = formDataArray[6].split("=")[1];
                     String telefono = formDataArray[7].split("=")[1];
                     String annotazioni = formDataArray[8].split("=")[1];
                     String statoCivile = formDataArray[9].split("=")[1];

                    // Connessione al database e inserimento dell'cliente
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                        String query = "INSERT INTO clienti (nome, cognome, sesso, indirizzo, nazionalita, documento, email, telefono, annotazioni, statoCivile) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        	 pstmt.setString(1, nome);
                             pstmt.setString(2, cognome);
                             pstmt.setString(3, sesso);
                             pstmt.setString(4, indirizzo);
                             pstmt.setString(5, nazionalita);
                             pstmt.setString(6, documento);
                             pstmt.setString(7, email);
                             pstmt.setString(8, telefono);
                             pstmt.setString(9, annotazioni);
                             pstmt.setString(10, statoCivile);
                             pstmt.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // Gestione eliminazione cliente
                else if (formDataArray.length == 1 && exchange.getRequestURI().getPath().equals("/clienti/delete")) {
                    String idToDelete = formDataArray[0].split("=")[1];
                    // Connessione al database e eliminazione dell'cliente
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                        String query = "DELETE FROM clienti WHERE id=?";
                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                            pstmt.setString(1, idToDelete);
                            pstmt.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // Gestione modifica cliente
           
                else if (formDataArray.length >= 10 && exchange.getRequestURI().getPath().equals("/clienti/update")) {
                    String idToUpdate = formDataArray[0].split("=")[1];
                    // Recupera i valori precedenti dal database
                    String query = "SELECT * FROM clienti WHERE id=?";
                    String nome = "";
                    String cognome = "";
                    String sesso = "";
                    String indirizzo = "";
                    String nazionalita = "";
                    String documento = "";
                    String email = "";
                    String telefono = "";
                    String annotazioni = "";
                    String statoCivile = "";
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                            pstmt.setString(1, idToUpdate);
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next()) {
                                nome = rs.getString("nome");
                                cognome = rs.getString("cognome");
                                sesso = rs.getString("sesso");
                                indirizzo = rs.getString("indirizzo");
                                nazionalita = rs.getString("nazionalita");
                                documento = rs.getString("documento");
                                email = rs.getString("email");
                                telefono = rs.getString("telefono");
                                annotazioni = rs.getString("annotazioni");
                                statoCivile = rs.getString("statoCivile");
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
                                case "new_sesso":
                                    sesso = value.isEmpty() ? sesso : value;
                                    break;
                                case "new_indirizzo":
                                    indirizzo = value.isEmpty() ? indirizzo : value;
                                    break;
                                case "new_nazionalita":
                                    nazionalita = value.isEmpty() ? nazionalita : value;
                                    break;
                                case "new_documento":
                                    documento = value.isEmpty() ? documento : value;
                                    break;
                                case "new_email":
                                    email = value.isEmpty() ? email : value;
                                    break;
                                case "new_telefono":
                                    telefono = value.isEmpty() ? telefono : value;
                                    break;
                                case "new_annotazioni":
                                    annotazioni = value.isEmpty() ? annotazioni : value;
                                    break;
                                case "new_statoCivile":
                                    statoCivile = value.isEmpty() ? statoCivile : value;
                                    break;
                                default:
                                    // Ignora altri campi
                                    break;
                            }
                        }
                    }
                    // Connessione al database e modifica del cliente
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                        query = "UPDATE clienti SET nome=?, cognome=?, sesso=?, indirizzo=?, nazionalita=?, documento=?, email=?, telefono=?, annotazioni=?, statoCivile=? WHERE id=?";
                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                            pstmt.setString(1, nome);
                            pstmt.setString(2, cognome);
                            pstmt.setString(3, sesso);
                            pstmt.setString(4, indirizzo);
                            pstmt.setString(5, nazionalita);
                            pstmt.setString(6, documento);
                            pstmt.setString(7, email);
                            pstmt.setString(8, telefono);
                            pstmt.setString(9, annotazioni);
                            pstmt.setString(10, statoCivile);
                            pstmt.setString(11, idToUpdate);
                            pstmt.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }


                // Reindirizza l'utente alla pagina degli avvocati dopo l'azione eseguita
                exchange.getResponseHeaders().set("Location", "/clienti");
                exchange.sendResponseHeaders(302, -1);
            }
}
    }
}