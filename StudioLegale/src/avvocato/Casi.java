package avvocato;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;

public class Casi {
   public Casi() throws IOException {
  
        // Creazione del server HTTP sulla porta 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Gestione della richiesta GET per la pagina dei casi
        server.createContext("/casi", new CasiHandler());

   }

    static class CasiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                // Connessione al database e recupero dei casi
                List<String> casi = new ArrayList<>();
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                    String query = "SELECT * FROM casi";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        ResultSet rs = pstmt.executeQuery();
                        
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String titolo = URLDecoder.decode(rs.getString("titolo"), "UTF-8").replace("+", " ");
                            String tipologia = URLDecoder.decode(rs.getString("tipologia"), "UTF-8").replace("+", " ");
                            String descrizione = URLDecoder.decode(rs.getString("descrizione"), "UTF-8").replace("+", " ");
                            String data = rs.getString("data");
                            String cliente = URLDecoder.decode(rs.getString("cliente"), "UTF-8").replace("+", " ");
                            String avvocato = URLDecoder.decode(rs.getString("avvocato"), "UTF-8").replace("+", " ");
                            String giudice = URLDecoder.decode(rs.getString("giudice"), "UTF-8").replace("+", " ");
                            String statoCaso = rs.getString("statoCaso");
                            String partiCoinvolte = URLDecoder.decode(rs.getString("partiCoinvolte"), "UTF-8").replace("+", " ");
                            String attivitaSvolte = URLDecoder.decode(rs.getString("attivitaSvolte"), "UTF-8").replace("+", " ");
                            String scadenze = URLDecoder.decode(rs.getString("scadenze"), "UTF-8").replace("+", " ");
                            int documentazione = rs.getInt("documentazione");

                            casi.add("ID: " + id + ", Titolo Caso: " + titolo + ", Tipologia del caso: " + tipologia +
                                ", Descrizione del caso: " + descrizione + ", Data del Caso: " + data +
                                ", Cliente: " + cliente + ", Avvocato: " + avvocato +
                                ", Giudice: " + giudice + ", Stato del caso: " + statoCaso +
                                ", Parti coinvolte: " + partiCoinvolte + ", Attivita svolte: " + attivitaSvolte +
                                ", Scadenze: " + scadenze + ", Documentazione: " + documentazione);
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
                response.append("    <meta charset=\"UTF-8\">");
                response.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                response.append("    <title>Gestione Casi</title>");
                response.append("    <!-- Bootstrap CSS -->");
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
                response.append("    <nav class=\"navbar bg-body-tertiary\"  >");
                response.append("        <div class=\"container-fluid\">");
                response.append("        <a class=\"navbar-brand\" href=\"/homeAmministratori\">"); 
                response.append(" <img src=https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQ5BszBe7YInYmdxKJXOv0UC1gv52exnWYXQ&usqp=CAU alt=\"Logo\" width=\"30\" height=\"24\" class=\"d-inline-block align-text-top\">" );
                response.append("          Studio Legale Napoli");
                response.append("          </a>");
                response.append("        </div>");
                response.append("      </nav>");
                response.append("<div class=\"container\">");
                response.append("    <h1 class=\"mt-5 text-center\">Gestione Casi</h1>");
                response.append("    <h2 id=\"id2\" class=\"mt-4\">Lista Casi</h2>");
                response.append("<div class='table-responsive'>"); // Rende la tabella responsive
                response.append("<table class='table table-dark table-hover'>"); 
                response.append("<thead>");
                response.append("<tr>");
                response.append("<th>ID</th>");
                response.append("<th>Titolo</th>");
                response.append("<th>Tipologia</th>");
                response.append("<th>Descrizione</th>");
                response.append("<th>Data</th>");
                response.append("<th>Cliente</th>");
                response.append("<th>Avvocato</th>");
                response.append("<th>Giudice</th>");
                response.append("<th>Stato del caso</th>");
                response.append("<th>Parti coinvolte</th>");
                response.append("<th>Attività svolte</th>");
                response.append("<th>Scadenze</th>");
                response.append("<th>Documentazione</th>");
                response.append("</tr>");
                response.append("</thead>");
                response.append("<tbody>");
                for (String caso : casi) {
                    String[] datiCaso = caso.split(", ");
                    response.append("<tr>");
                    for (String dato : datiCaso) {
                        String valore = dato.substring(dato.indexOf(":") + 2); 
                        response.append("<td>").append(valore).append("</td>");
                    }
                    // Aggiungi il pulsante di eliminazione
                    String casoId = datiCaso[0].split(":")[1].trim(); // Assicurati che questo estragga correttamente l'ID del caso
                    response.append("<td>")
                             .append("<form action='/casi/delete' method='post'>")
                             .append("<input type='hidden' name='idToDelete' value='" + casoId + "'/>")
                             .append("<button type='submit' class='btn btn-danger'>Elimina</button>")
                             .append("</form>")
                             .append("</td>");
                    response.append("</tr>");
                }
                response.append("</tbody>");
                response.append("</table>");
                response.append("</div>");

                response.append("<br><br>");
                response.append("<div class=\"section\">");
                response.append("    <h2 id=\"id2\" class=\"mt-4\">Aggiungi Caso</h2>");
                response.append("    <form class=\"mt-2\" method='post' action='/casi'>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"titolo\">Titolo del Caso:</label>");
                response.append("            <input type='text' class=\"form-control\" name='titolo' required>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"tipologia\">Tipologia del Caso:</label>");
                response.append("            <select class=\"form-control\" name='tipologia' required>");
                response.append("                <option value='civile'>Civile</option>");
                response.append("                <option value='penale'>Penale</option>");
                response.append("                <option value='commerciale'>Commerciale</option>");
                response.append("            </select>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"cliente\">Cliente:</label>");
                response.append("            <input type='text' class=\"form-control\" name='cliente' required>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"avvocato\">Avvocato:</label>");
                response.append("            <input type='text' class=\"form-control\" name='avvocato' required>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"giudice\">Giudice:</label>");
                response.append("            <input type='text' class=\"form-control\" name='giudice' required>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"descrizione\">Descrizione del Caso:</label>");
                response.append("            <input type='text' class=\"form-control\" name='descrizione' required>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"data\">Data del Caso:</label>");
                response.append("            <input type='date' class=\"form-control\" name='data' required>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"statoCaso\">Stato del Caso:</label>");
                response.append("            <input type='text' class=\"form-control\" name='statoCaso' required>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"partiCoinvolte\">Parti Coinvolte:</label>");
                response.append("            <input type='text' class=\"form-control\" name='partiCoinvolte' required>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"attivitaSvolte\">Attività Svolte:</label>");
                response.append("            <input type='text' class=\"form-control\" name='attivitaSvolte' required>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"scadenze\">Scadenze:</label>");
                response.append("            <input type='text' class=\"form-control\" name='scadenze' required>");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"documentazione\">Documentazione:</label>");
                response.append("            <input type='number' class=\"form-control\" name='documentazione' required>");
                response.append("        </div>");
                response.append("        <button type=\"submit\" class=\"btn btn-primary\">Aggiungi Caso</button>");
                response.append("    </form>");
                response.append("</div>");
                response.append("    <br><br>");
               
               
               
                response.append("    <div class=\"section\">");
                response.append("    <h2 id=\"id2\" class=\"mt-4\">Modifica Caso</h2>");
                response.append("    <form class=\"mt-2\" method='post' action='/casi/update'>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"id\">ID Caso:</label>");
                response.append("            <input type='number' class=\"form-control\" name='id' >");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_titolo\">Nuovo Titolo:</label>");
                response.append("            <input type='text' class=\"form-control\" name='new_titolo' >");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_tipologia\">Nuova Tipologia:</label>");
                response.append("            <input type='text' class=\"form-control\" name='new_tipologia' >");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_descrizione\">Nuova Descrizione:</label>");
                response.append("            <input type='text' class=\"form-control\" name='new_descrizione' >");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_data\">Nuova Data:</label>");
                response.append("            <input type='date' class=\"form-control\" name='new_data' >");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_statoCaso\">Nuovo Stato del Caso:</label>");
                response.append("            <input type='text' class=\"form-control\" name='new_statoCaso' >");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_partiCoinvolte\">Nuove Parti Coinvolte:</label>");
                response.append("            <input type='text' class=\"form-control\" name='new_partiCoinvolte' >");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_attivitaSvolte\">Nuove Attività Svolte:</label>");
                response.append("            <input type='text' class=\"form-control\" name='new_attivitaSvolte' >");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_scadenze\">Nuove Scadenze:</label>");
                response.append("            <input type='text' class=\"form-control\" name='new_scadenze' >");
                response.append("        </div>");
                response.append("        <div class=\"form-group\">");
                response.append("            <label for=\"new_documentazione\">Nuova Documentazione:</label>");
                response.append("            <input type='number' class=\"form-control\" name='new_documentazione' >");
                response.append("        </div>");
                response.append("        <button type=\"submit\" class=\"btn btn-warning\">Modifica</button>");
                response.append("    </form>");
                response.append("</div>");
                response.append("</div>");
             
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
                response.append("<script src=\"https://code.jquery.com/jquery-3.5.1.slim.min.js\"></script>");
                response.append("<script src=\"https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js\"></script>");
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
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String formData = br.readLine();
                    String[] formDataArray = formData.split("&");

                    // Gestione aggiunta caso
                    if ( exchange.getRequestURI().getPath().equals("/casi")) {
                        // Codice per aggiungere un caso
                        String titolo = formDataArray[0].split("=")[1];
                        String tipologia = formDataArray[1].split("=")[1];
                        String cliente = formDataArray[2].split("=")[1];
                        String avvocato = formDataArray[3].split("=")[1];
                        String giudice = formDataArray[4].split("=")[1];
                        String descrizione = formDataArray[5].split("=")[1];
                        String data = formDataArray[6].split("=")[1];
                        String statoCaso = formDataArray[7].split("=")[1];
                        String partiCoinvolte = formDataArray[8].split("=")[1];
                        String attivitaSvolte = formDataArray[9].split("=")[1];
                        String scadenze = formDataArray[10].split("=")[1];
                        int documentazione = Integer.parseInt(formDataArray[11].split("=")[1]);

                        // Connessione al database e inserimento del caso
                        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                            String query = "INSERT INTO casi (titolo, tipologia, cliente, avvocato, giudice, descrizione, data, statoCaso, partiCoinvolte, attivitaSvolte, scadenze, documentazione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                                pstmt.setString(1, titolo);
                                pstmt.setString(2, tipologia);
                                pstmt.setString(3, cliente);
                                pstmt.setString(4, avvocato);
                                pstmt.setString(5, giudice);
                                pstmt.setString(6, descrizione);
                                pstmt.setString(7, data);
                                pstmt.setString(8, statoCaso);
                                pstmt.setString(9, partiCoinvolte);
                                pstmt.setString(10, attivitaSvolte);
                                pstmt.setString(11, scadenze);
                                pstmt.setInt(12, documentazione);
                                pstmt.executeUpdate();
                            }
                            exchange.getResponseHeaders().set("Location", "/casi");
                            exchange.sendResponseHeaders(302, -1);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    // Metodo per eliminare un caso
                    else if (exchange.getRequestURI().getPath().equals("/casi/delete")) {
                        String idToDelete = formDataArray[0].split("=")[1];
                        // Connessione al database e eliminazione del caso
                        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                            String query = "DELETE FROM casi WHERE id=?";
                            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                                pstmt.setString(1, idToDelete);
                                pstmt.executeUpdate();
                            }
                            exchange.getResponseHeaders().set("Location", "/casi");
                            exchange.sendResponseHeaders(302, -1);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                     //modifica
                    else if (formDataArray.length >= 10 && exchange.getRequestURI().getPath().equals("/casi/update")) {
                    	String idToUpdate = formDataArray[0].split("=")[1];
                    	// Recupera i valori precedenti dal database
                    	String query = "SELECT * FROM casi WHERE id=?";
                    	String titolo = "";
                    	String tipologia = "";
                    	String descrizione = "";
                    	String data = "";
                    	String statoCaso = "";
                    	String partiCoinvolte = "";
                    	String attivitaSvolte = "";
                    	String scadenze = "";
                    	int documentazione = 0;

                    	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                    		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    			pstmt.setString(1, idToUpdate);
                    			ResultSet rs = pstmt.executeQuery();
                    			if (rs.next()) {
                    				titolo = rs.getString("titolo");
                    				tipologia = rs.getString("tipologia");
                    				descrizione = rs.getString("descrizione");
                    				data = rs.getString("data");
                    				statoCaso = rs.getString("statoCaso");
                    				partiCoinvolte = rs.getString("partiCoinvolte");
                    				attivitaSvolte = rs.getString("attivitaSvolte");
                    				scadenze = rs.getString("scadenze");
                    				documentazione = rs.getInt("documentazione");
                    				
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
                    				case "new_titolo":
                    					titolo = value.isEmpty() ? titolo : value;
                    					break;
                    				case "new_tipologia":
                    					tipologia = value.isEmpty() ? tipologia : value;
                    					break;
                    				case "new_descrizione":
                    					descrizione = value.isEmpty() ? descrizione: value;
                    					break;
                    				case "new_data":
                    					data = value.isEmpty() ? data : value;
                    					break;
                    				case "new_statoCaso":
                    					statoCaso = value.isEmpty() ? statoCaso : value;
                    					break;
                    				case "new_partiCoinvolte":
                    					partiCoinvolte = value.isEmpty() ? partiCoinvolte : value;
                    					break;
                    				case "new_ativitaSvolte":
                    					attivitaSvolte = value.isEmpty() ? attivitaSvolte : value;
                    					break;
                    				case "new_scadenze":
                    					scadenze = value.isEmpty() ? scadenze : value;
                    					break;
                    				case "new_documentazione":
                    					documentazione = value.isEmpty() ? documentazione : Integer.parseInt(value);
                    					break;
                    				
                    				default:
                    					// Ignora altri campi
                    					break;
                    			}
                    		}
                    	}




                            // Connessione al database e modifica del caso
                            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                                 query = "UPDATE casi SET titolo=?, tipologia=?, descrizione=?, data=?, statoCaso=?, partiCoinvolte=?, attivitaSvolte=?, scadenze=?, documentazione=? WHERE id=?";
                                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                                    pstmt.setString(1, titolo);
                                    pstmt.setString(2, tipologia);
                                    pstmt.setString(3, descrizione);
                                    pstmt.setString(4, data);
                                    pstmt.setString(5, statoCaso);
                                    pstmt.setString(6, partiCoinvolte);
                                    pstmt.setString(7, attivitaSvolte);
                                    pstmt.setString(8, scadenze);
                                    pstmt.setInt(9, documentazione);
                                    pstmt.setString(10, idToUpdate);
                                    pstmt.executeUpdate();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                    }
                       
                        // Reindirizza l'utente alla pagina dei casi dopo l'azione eseguita
                        exchange.getResponseHeaders().set("Location", "/casi");
                        exchange.sendResponseHeaders(302, -1);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
} // Fine della classe CasiHandler