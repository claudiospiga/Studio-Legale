package avvocato;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;

public class Documentazione {

   public Documentazione() throws IOException {
 
        // Creazione del server HTTP sulla porta 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Gestione della richiesta GET per la pagina degli avvocati
        server.createContext("/documentazione", new DocumentazioniHandler());
        server.setExecutor(null); // Gestore predefinito
        
     
    }

    static class DocumentazioniHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                // Connessione al database e recupero dei documenti
                List<String> documenti = new ArrayList<>();
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                    String query = "SELECT * FROM documentazione";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        ResultSet rs = pstmt.executeQuery();
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String nomeDocumento = URLDecoder.decode(rs.getString("nomeDocumento"), "UTF-8");
                            String descrizione = URLDecoder.decode(rs.getString("descrizione"), "UTF-8");
                            String tipoDocumento = URLDecoder.decode(rs.getString("tipoDocumento"), "UTF-8");
                            String dataCreazione = URLDecoder.decode(rs.getString("dataCreazione"), "UTF-8");
                            documenti.add("ID: " + id + ", Nome Documento: " + nomeDocumento + ", Descrizione: " + descrizione + ", Tipo di Documento: " + tipoDocumento + ", Data Creazione: " + dataCreazione);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                StringBuilder response = new StringBuilder();
                response.append("<!DOCTYPE html>");
                response.append("<html lang=\"en\">");
                response.append("<head>");
                response.append("    <meta charset=\"UTF-8\">");
                response.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                response.append("    <title>Gestione Documentazione</title>");
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
                response.append("    <h1 class=\"mt-5\">Gestione Documentazione</h1>");
                response.append("    <h2 id=\"id2\" class=\"mt-4\">Lista Documenti</h2>");
                response.append("<div class='table-responsive'>");
                response.append("<table class='table table-dark table-hover'>");
                response.append("<thead>");
                response.append("<tr>");
                response.append("<th>ID</th>");
                response.append("<th>Nome Documento</th>");
                response.append("<th>Descrizione</th>");
                response.append("<th>Tipo Documento</th>");
                response.append("<th>Data Creazione</th>");
                response.append("<th>Azioni</th>");  
                response.append("</tr>");
                response.append("</thead>");
                response.append("<tbody>");

                for (String documento : documenti) {
                   
                    String[] parti = documento.split(", ");
                    String idDocumento = parti[0].split(": ")[1];
                    
                    response.append("<tr>");
                    // Ciclo per inserire i dati del documento nelle celle della tabella
                    for (String parte : parti) {
                        response.append("<td>").append(parte.split(": ")[1]).append("</td>");
                    }
                   // cella per il pulsante di azione di eliminazione
                    response.append("<td>");
                    // Form per l'eliminazione
                    response.append("<form method='POST' action='/documentazione/delete' onsubmit='return confirm(\"Sei sicuro di voler eliminare questo documento?\");'>");
                    response.append("<input type='hidden' name='idToDelete' value='" + idDocumento + "'/>");
                    response.append("<button type='submit' class='btn btn-danger btn-sm'>Elimina</button>");
                    response.append("</form>");
                    response.append("</td>");
                    response.append("</tr>");
                }

                response.append("</tbody>");
                response.append("</table>");
                response.append("</div>");
                
                response.append("<br><br>");

             // Aggiungi Documento
             response.append("<div class=\"section\">");
             response.append("    <h2 id=\"id2\" class=\"mt-4\">Aggiungi Documento</h2>");
             response.append("    <form class=\"mt-2\" method='post' action='/documentazione'>");
             response.append("        <div class=\"form-group\">");
             response.append("            <label for=\"nomeDocumento\">Nome Documento:</label>");
             response.append("            <input type='text' class=\"form-control\" name='nomeDocumento'>");
             response.append("        </div>");
             response.append("        <div class=\"form-group\">");
             response.append("            <label for=\"descrizione\">Descrizione:</label>");
             response.append("            <input type='text' class=\"form-control\" name='descrizione'>");
             response.append("        </div>");
             response.append("        <div class=\"form-group\">");
             response.append("            <label for=\"tipoDocumento\">Tipo Documento:</label>");
             response.append("            <select class=\"form-control\" name='tipoDocumento'>");
             response.append("                <option value='civile'>Civile</option>");
             response.append("                <option value='penale'>Penale</option>");
             response.append("                <option value='commerciale'>Commerciale</option>");
             response.append("            </select>");
             response.append("        </div>");
             response.append("        <div class=\"form-group\">");
             response.append("            <label for=\"dataCreazione\">Data Creazione:</label>");
             response.append("            <input type='date' class=\"form-control\" name='dataCreazione'>");
             response.append("        </div>");
             response.append("        <button type=\"submit\" class=\"btn btn-primary\">Aggiungi Documento</button>");
             response.append("    </form>");
             response.append("</div>");
             response.append("<br><br>");

       

             // Modifica Documento
             response.append("<div class=\"section\">");
             response.append("    <h2 id=\"id2\" class=\"mt-4\">Modifica Documento</h2>");
             response.append("    <form class=\"mt-2\" method='post' action='/documentazione/update'>");
             response.append("        <div class=\"form-group\">");
             response.append("            <label for=\"id\">ID Documento:</label>");
             response.append("            <input type='text' class=\"form-control\" name='id'>");
             response.append("        </div>");
             response.append("        <div class=\"form-group\">");
             response.append("            <label for=\"new_nomeDocumento\">Nuovo Nome Documento:</label>");
             response.append("            <input type='text' class=\"form-control\" name='new_nomeDocumento'>");
             response.append("        </div>");
             response.append("        <div class=\"form-group\">");
             response.append("            <label for=\"new_descrizione\">Nuova Descrizione:</label>");
             response.append("            <input type='text' class=\"form-control\" name='new_descrizione'>");
             response.append("        </div>");
             response.append("        <div class=\"form-group\">");
             response.append("            <label for=\"new_tipoDocumento\">Nuovo Tipo Documento:</label>");
             response.append("            <select class=\"form-control\" name='new_tipoDocumento'>");
             response.append("                <option value='civile'>Civile</option>");
             response.append("                <option value='penale'>Penale</option>");
             response.append("                <option value='commerciale'>Commerciale</option>");
             response.append("            </select>");
             response.append("        </div>");
             response.append("        <div class=\"form-group\">");
             response.append("            <label for=\"new_dataCreazione\">Nuova Data:</label>");
             response.append("            <input type='date' class=\"form-control\" name='new_dataCreazione'>");
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

             // Script Bootstrap
             response.append("<script src=\"https://code.jquery.com/jquery-3.5.1.slim.min.js\"></script>");
             response.append("<script src=\"https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js\"></script>");
             response.append("<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js\"></script>");
             response.append("</body>");
             response.append("</html>");

 int lunghezza = response.toString().getBytes("UTF-8").length;
             
             // Invio della risposta al client
             exchange.sendResponseHeaders(200, lunghezza);   //response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();
            } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                try {
                // Recupera i dati inviati dal form per aggiungere un nuovo avvocato
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();
                String[] formDocumentazioneArray = formData.split("&");
                
                if (exchange.getRequestURI().getPath().equals("/documentazione")) {
                String nomeDocumento = formDocumentazioneArray[0].split("=")[1];
                String descrizione = formDocumentazioneArray[1].split("=")[1];
                String tipoDocumento = formDocumentazioneArray[2].split("=")[1];
                String dataCreazione = formDocumentazioneArray[3].split("=")[1];
              

                // Connessione al database e inserimento dell'avvocato
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                    String query = "INSERT INTO documentazione (nomeDocumento, descrizione, tipoDocumento, dataCreazione) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, nomeDocumento);
                        pstmt.setString(2, descrizione);
                        pstmt.setString(3, tipoDocumento);
                        pstmt.setString(4, dataCreazione);
                        pstmt.executeUpdate();
                    }
                    exchange.getResponseHeaders().set("Location", "/documentazione");
                    exchange.sendResponseHeaders(302, -1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                }
                // Gestione eliminazione avvocato
                else if (formDocumentazioneArray.length == 1 && exchange.getRequestURI().getPath().equals("/documentazione/delete")) {
                    String idToDelete = formDocumentazioneArray[0].split("=")[1];
                    // Connessione al database e eliminazione dell'avvocato
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                        String query = "DELETE FROM documentazione WHERE id=?";
                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                            pstmt.setString(1, idToDelete);
                            pstmt.executeUpdate();
                        }
                        exchange.getResponseHeaders().set("Location", "/documentazione");
                        exchange.sendResponseHeaders(302, -1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
              //modifica
                else if (formDocumentazioneArray.length >= 5 && exchange.getRequestURI().getPath().equals("/documentazione/update")) {
                    String idToUpdate = formDocumentazioneArray[0].split("=")[1];
                    
                    // Recupera i valori precedenti dal database
                    String query = "SELECT * FROM documentazione WHERE id=?";
                    String nomeDocumento = "";
                    String descrizione = "";
                    String tipoDocumento = "";
                    String dataCreazione = "";
                    
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                            pstmt.setString(1, idToUpdate);
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next()) {
                                nomeDocumento = rs.getString("nomeDocumento");
                                descrizione = rs.getString("descrizione");
                                tipoDocumento = rs.getString("tipoDocumento");
                                dataCreazione = rs.getString("dataCreazione");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    // Aggiorna solo i campi non vuoti
                    for (String field : formDocumentazioneArray) {
                        String[] parts = field.split("=");
                        if (parts.length == 2) {
                            String fieldName = parts[0];
                            String value = parts[1];
                            switch (fieldName) {
                                case "new_nomeDocumento":
                                    nomeDocumento = value.isEmpty() ? nomeDocumento : value;
                                    break;
                                case "new_descrizione":
                                    descrizione = value.isEmpty() ? descrizione : value;
                                    break;
                                case "new_tipoDocumento":
                                    tipoDocumento = value.isEmpty() ? tipoDocumento: value;
                                    break;
                                case "new_dataCreazione":
                                    dataCreazione = value.isEmpty() ? dataCreazione : value;
                                    break;
                                default:
                                    // Ignora altri campi
                                    break;
                            }
                        }
                    }

                    // Connessione al database e modifica del caso
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                        query = "UPDATE documentazione SET nomeDocumento=?, descrizione=?, tipoDocumento=?, dataCreazione=? WHERE id=?";
                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {                
                            pstmt.setString(1, nomeDocumento);
                            pstmt.setString(2, descrizione);
                            pstmt.setString(3, tipoDocumento);
                            pstmt.setString(4, dataCreazione);
                            pstmt.setString(5, idToUpdate); // Aggiunta del quinto parametro
                            pstmt.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }


                // Reindirizza l'utente alla pagina degli avvocati dopo l'azione eseguita
                exchange.getResponseHeaders().set("Location", "/documentazione");
               exchange.sendResponseHeaders(302, -1);
            }catch (NumberFormatException e) {
                e.printStackTrace();
            }
}
    }
}
}

