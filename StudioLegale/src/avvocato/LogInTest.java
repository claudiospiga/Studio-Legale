package avvocato;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import avvocato.Avvocati.AvvocatiHandler;
import avvocato.Casi.CasiHandler;
import avvocato.Clienti.AggiungiCliente;
import avvocato.Documentazione.DocumentazioniHandler;
import avvocato.Pagamenti.PagamentiHandler;
import avvocato.Precedenti.precedentiHandler;



public class LogInTest {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/login", new LogInHandler());
        server.createContext("/homeAmministratori", new HomeAmmHndler());
        server.createContext("/homeSegreteria", new HomeSegreteriaHandler());
        server.createContext("/documentazione", new DocumentazioniHandler());
		server.createContext("/precedenti", new precedentiHandler());
		server.createContext("/pagamenti", new PagamentiHandler());
		server.createContext("/avvocati", new AvvocatiHandler());
		server.createContext("/casi", new CasiHandler());
		server.createContext("/clienti", new AggiungiCliente());
        server.start();
        System.out.println("Server avviato");
    }

    static class LogInHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                String htmlLogIn = "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\">\n" +
                        "    <title>Document</title>\n" +
                        "    <style>\n" +
                        "        /* Add custom styles here */\n" +
                        "        /* Adjust margins and paddings as needed */\n" +
                        "        .container {\n" +
                        "          background-color: rgba(0, 0, 0, 0.27);\n" +
                        "          padding: 20px;\n" +
                        "          margin: 20px;\n" +
                        "          color: white;\n" +
                        "          border-radius: 10px;\n" +
                        "        }\n" +
                        "        #id2 {\n" +
                        "          background-color: rgba(0, 0, 0, 0.27);\n" +
                        "          padding: 20px;\n" +
                        "          color: white;\n" +
                        "          border-radius: 10px;\n" +
                        "          transition: transform 0.3s ease; /* Smooth transition on hover */\n" +
                        "         \n" +
                        "        }\n" +
                        "        #id2:hover {\n" +
                        "          transform: scale(1.05); /* Increase size on hover */\n" +
                        "        }\n" +
                        "        /* Center the form horizontally */\n" +
                        "        .center {\n" +
                        "          margin: 0 auto;\n" +
                        "          max-width: 400px; /* Adjust as needed */\n" +
                        "        }\n" +
                        "        /* Style for the login button */\n" +
                        "        .btn-login {\n" +
                        "         \n" +
                        "        }\n" +
                        "        /* Set background image */\n" +
                        "        body {\n" +
                        "          background-image: url('https://www.mad-hr.co.uk/wp-content/uploads/Law-Gavel-2.png');\n" +
                        "          background-size: cover;\n" +
                        "          background-repeat: no-repeat;\n" +
                        "          background-attachment: fixed;\n" +
                        "        }\n" +
                        "        .welcome-message {\n" +
                        "          font-size: 20px;\n" +
                        "          transition: font-size 0.3s ease; /* Smooth transition on font size change */\n" +
                        "         \n" +
                        "        }\n" +
                        "        .welcome-message:hover {\n" +
                        "          font-size: 22px; /* Increase font size on hover */\n" +
                        "        }\n" +
                        "      </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<br>\n" +
                        "    <div class=\"container center \" id=\"id2\">\n" +
                        "        <div class=\"text-center\">\n" +
                        "          <!-- Welcome message with hover effect -->\n" +
                        "          <h1 class=\"welcome-message\">Benvenuto al gestionale per lo Studio Legale BitCamp!</h1>\n" +
                        "        </div>\n" +
                        "      </div>\n" +
                        "<br><br><br><br>\n" +
                        "    <div class=\"container center\">\n" +
                        "        <div class=\"center\">\n" +
                        "          <form action=\"/login\" method=\"post\">\n" +
                        "            <div class=\"form-group\">\n" +
                        "              <label for=\"username\"><b>Username</b></label>\n" +
                        "              <input type=\"text\" class=\"form-control\" id=\"username\" placeholder=\"Enter Username\" name=\"username\" required>\n" +
                        "            </div>\n" +
                        "            <div class=\"form-group\">\n" +
                        "              <label for=\"password\"><b>Password</b></label>\n" +
                        "              <input type=\"password\" class=\"form-control\" id=\"password\" placeholder=\"Enter Password\" name=\"password\" required>\n" +
                        "            </div>\n" +
                        "            <button type=\"submit\" class=\"btn btn-primary btn-login\">Login</button>\n" +
                        "          </form>\n" +
                        "        </div>\n" +
                        "      </div>\n" +
                        "      \n" +
                        "      <!-- Link to Bootstrap JS (optional) -->\n" +
                        "      <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js\"></script>\n" +
                        "      \n" +
                        "    \n" +
                        "</body>\n" +
                        "</html>";
                // Imposta l'intestazione della risposta
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                // Imposta lo status code e la lunghezza della risposta
                exchange.sendResponseHeaders(200, htmlLogIn.getBytes().length);

                // Scrive la risposta al client
                OutputStream os = exchange.getResponseBody();
                os.write(htmlLogIn.getBytes());
                os.close();
            } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String formLogIn = br.readLine();
                String[] logInArray = formLogIn.split("&");
                String username = logInArray[0].split("=")[1];
                String password = logInArray[1].split("=")[1];
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studiolegale", "root", "marina97!")) {
                    String query = "SELECT * FROM login WHERE username = ? AND password = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, username);
                        pstmt.setString(2, password);
                        try (ResultSet rs = pstmt.executeQuery()) {
                            if (rs.next()) {
                                String ruolo = rs.getString("ruolo");
                                if (ruolo.equals("amministratore")) {
                                    //MOFICIARE IL REINDIRIZZAMNETO
                                    String redirectScript = "<script>window.location.href='/homeAmministratori';</script>";
                                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                                    exchange.sendResponseHeaders(200, redirectScript.getBytes().length);
                                    OutputStream os = exchange.getResponseBody();
                                    os.write(redirectScript.getBytes());
                                    os.close();
                                } else {
                                    //MODIFICARE IL REINDIRIZZAMENTO
                                    String redirectScript = "<script>window.location.href='/homeSegreteria';</script>";
                                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                                    exchange.sendResponseHeaders(200, redirectScript.getBytes().length);
                                    OutputStream os = exchange.getResponseBody();
                                    os.write(redirectScript.getBytes());
                                    os.close();
                                }
                            }

                        } catch (SQLException e) {
                            System.out.println("C'è stto un errore nel prendere i dati");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        System.out.println("C'è stto un errore nell'inserire i dati");
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    System.out.println("C'è stato un errore nella connessione al database");
                    e.printStackTrace();
                }
            }
        }
    }

    static class HomeAmmHndler implements HttpHandler{
		@Override
		public void handle(HttpExchange exchange) throws IOException{
		if(exchange.getRequestMethod().equalsIgnoreCase("GET")) {
			String htmlLogIn ="<html>"+
									     "<head>" +
									     "    <meta charset=\"UTF-8\">\n"+
									     "    <link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" rel=\"stylesheet\">"+
            "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">"+
            "    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>"+
									     "<style>"+
									     "body {"+
									     "background-image: url('https://www.mad-hr.co.uk/wp-content/uploads/Law-Gavel-2.png');"+
									     "background-size: cover;"+
									     "background-repeat: no-repeat;"+
									     "background-attachment: fixed;"+
									     "}"+
									     "</style>"+
									     "<body>"+
									     "<nav class=\"navbar bg-body-tertiary\">"+
									     " <div class=\"container-fluid\">"+
									     " <a class=\"navbar-brand\" href=\"#\">"+
									     " <img src=https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQ5BszBe7YInYmdxKJXOv0UC1gv52exnWYXQ&usqp=CAU alt=\"Logo\" width=\"30\" height=\"24\" class=\"d-inline-block align-text-top\">"+
									     " Studio Legale Napoli"+
									     "</a>"+
									     "</div>"+
									     " </nav>"+
									     "<br><br><br><br>"+
									     "<div class=\"container\">"+
									     "<div class=\"row\">"+
									     "<div class=\"col-md-4\">"
									     + "            <div class=\"card mb-3\">"
									     + "              <img src=https://4legalleads.com/wp-content/uploads/2020/05/4legalleads-building-successful-attorney-client-relationships.jpg class=\"card-img-top\" alt=\"clienti\">"
									     + "              <div class=\"card-body text-center\"style=\" background-color: rgba(209, 184, 128, 0.634)\">"
									     + "                <h5 class=\"card-title\">Clienti</h5>"
									     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>"
									     + "                <a href=\"/clienti\" class=\"btn btn-primary\">Pagina Clienti</a>"
									     + "              </div>"
									     + "            </div>"
									     + "          </div>"
									     + " <div class=\"col-md-4\">"
									     + "            <div class=\"card mb-3\">"
									     + "              <img src=https://www.normanmattar.com/wp-content/uploads/2023/06/shutterstock_1174972879.jpg class=\"card-img-top\" alt=\"...\">\r\n"
									     + "              <div class=\"card-body text-center\" style=\" background-color: rgba(209, 184, 128, 0.634)\">\r\n"
									     + "                <h5 class=\"card-title\">Precedenti</h5>\r\n"
									     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n"
									     + "                <a href=\"/precedenti\" class=\"btn btn-primary\">Pagina Precedenti</a>\r\n"
									     + "              </div>\r\n"
									     + "            </div>\r\n"
									     + "          </div>"
									     +" <div class=\"col-md-4\">"
									     + "            <div class=\"card mb-3\">"
									     + "              <img src=https://www.fjsolicitors.co.uk/wp-content/uploads/2022/12/drafting-legal-documents-scaled.jpg class=\"card-img-top\" alt=\"...\">\r\n"
									     + "              <div class=\"card-body text-center\" style=\" background-color: rgba(209, 184, 128, 0.634)\">\r\n"
									     + "                <h5 class=\"card-title\">Documentazione</h5>\r\n"
									     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n"
									     + "                <a href=\"/documentazione\" class=\"btn btn-primary\">Pagina Documentazione</a>\r\n"
									     + "              </div>\r\n"
									     + "            </div>\r\n"
									     + "          </div>\r\n"
									     + "        </div>"
									     +" <div class=\"row\">"
									     + "          <div class=\"col-md-4\">"
									     + "            <div class=\"card mb-3\">"
									     + "              <img src=https://gisondolaw.com/wp-content/uploads/2022/07/What-is-a-Lawyer-Retainer-Fee-and-How-does-it-Work.jpg class=\"card-img-top\" alt=\"...\">\r\n"
									     + "              <div class=\"card-body text-center\"style=\" background-color: rgba(209, 184, 128, 0.634)\">\r\n"
									     + "                <h5 class=\"card-title\">Pagamenti</h5>\r\n"
									     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n"
									     + "                <a href=\"/pagamenti\" class=\"btn btn-primary\">Pagina Pagamenti</a>"
									     + "              </div>\r\n"
									     + "            </div>\r\n"
									     + "          </div>"
									     +" <div class=\"col-md-4\">"
									     + "            <div class=\"card mb-3\">"
									     + "              <img src=https://www.liveabout.com/thmb/wieOyiN-kAyJzN8-IEEPHLhrv5U=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/lawyers-talking-in-corridor-104821121-e9bba332f33843f0aa863fd9c5d97601.jpg class=\"card-img-top\" alt=\"...\">\r\n"
									     + "              <div class=\"card-body text-center\"style=\" background-color: rgba(209, 184, 128, 0.634)\">"
									     + "                <h5 class=\"card-title\">Avvocati</h5>\r\n"
									     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n"
									     + "                <a href=\"/avvocati\" class=\"btn btn-primary\">Pagina Avvocati</a>"
									     + "              </div>"
									     + "            </div>"
									     + "          </div>"
									     +" <div class=\"col-md-4\">"
									     + "            <div class=\"card mb-3\">"
									     + "              <img src=https://imgcdn.agendadigitale.eu/wp-content/uploads/2022/05/05030330/legge-avvocatura-giudice-161210124413-1200x916.jpg class=\"card-img-top\" alt=\"...\">\r\n"
									     + "              <div class=\"card-body text-center\" style=\"background-color: rgba(209, 184, 128, 0.634)\">"
									     + "                <h5 class=\"card-title\">Casi</h5>"
									     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n"
									     + "                <a href=\"/casi\" class=\"btn btn-primary\">Pagina Casi</a>"
									     + "              </div>"
									     + "            </div>"
									     + "          </div>"
									     + "        </div>"
									     + "      </div>"
									     +"<br><br><br>"
									     + "<footer class=\"text-white py-4\" style=\" background-color: rgba(0, 0, 0, 0.723);\">"
									     + "        <div class=\"container\">\r\n"
									     + "          <div class=\"row\">\r\n"
									     + "            <div class=\"col-md-4\">\r\n"
									     + "              <h5>Avvocati</h5>\r\n"
									     + "              <ul class=\"list-unstyled\">\r\n"
									     + "                <li>Avv. Marco Rossi</li>\r\n"
									     + "                <li>Avv. Laura Bianchi</li>\r\n"
									     + "                <li>Avv. Luca Verdi</li>\r\n"
									     + "              </ul>\r\n"
									     + "            </div>\r\n"
									     + "            <div class=\"col-md-4\">\r\n"
									     + "              <h5>Indirizzo</h5>\r\n"
									     + "              <p>Via Roma, 123<br>00100 Roma (RM), Italia</p>\r\n"
									     + "            </div>\r\n"
									     + "            <div class=\"col-md-4\">\r\n"
									     + "              <h5>Contatti</h5>\r\n"
									     + "              <p>Email: info@studiolegale.com<br>Telefono: +39 0123 456789</p>\r\n"
									     + "            </div>\r\n"
									     + "          </div>\r\n"
									     + "         \r\n"
									     + "          <div class=\"row\">\r\n"
									     + "            <div class=\"col-md-6\">\r\n"
									     + "              <p>Orari di apertura:<br>Lunedì - Venerdì: 9:00 - 18:00</p>\r\n"
									     + "            </div>\r\n"
									     + "            <div class=\"col-md-6 text-md-right\">\r\n"
									     + "              <p>&copy; 2024 Studio Legale. Tutti i diritti riservati.</p>\r\n"
									     + "            </div>\r\n"
									     + "          </div>\r\n"
									     + "        </div>\r\n"
									     + "      </footer>"
									     +"</body>"+
									     "</html>";
			// Imposta l'intestazione della risposta
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            int lunghezza = htmlLogIn.toString().getBytes("UTF-8").length;
            
            // Invio della risposta al client
            exchange.sendResponseHeaders(200, lunghezza);

            // Scrive la risposta al client
            OutputStream os = exchange.getResponseBody();
            os.write(htmlLogIn.getBytes());
            os.close();
			}
		}
	}
}

 class HomeSegreteriaHandler implements HttpHandler{
	@Override
	public void handle(HttpExchange exchange) throws IOException{
	if(exchange.getRequestMethod().equalsIgnoreCase("GET")) {
		String htmlLogIn ="<html>"+
								     "<head>" +
								     "    <meta charset=\"UTF-8\">\n"+
								     "    <link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" rel=\"stylesheet\">"+
        "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">"+
        "    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>"+
								     "<style>"+
								     "body {"+
								     "background-image: url('https://www.mad-hr.co.uk/wp-content/uploads/Law-Gavel-2.png');"+
								     "background-size: cover;"+
								     "background-repeat: no-repeat;"+
								     "background-attachment: fixed;"+
								     "}"+
								     "</style>"+
								     "<body>"+
								     "<nav class=\"navbar bg-body-tertiary\">"+
								     " <div class=\"container-fluid\">"+
								     " <a class=\"navbar-brand\" href=\"#\">"+
								     " <img src=https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQ5BszBe7YInYmdxKJXOv0UC1gv52exnWYXQ&usqp=CAU alt=\"Logo\" width=\"30\" height=\"24\" class=\"d-inline-block align-text-top\">"+
								     " Studio Legale Napoli"+
								     "</a>"+
								     "</div>"+
								     " </nav>"+
								     "<br><br><br><br>"+
								     "<div class=\"container\">"+
								     "<div class=\"row\">"+
								     "<div class=\"col-md-4\">"
								     + "            <div class=\"card mb-3\">"
								     + "              <img src=https://4legalleads.com/wp-content/uploads/2020/05/4legalleads-building-successful-attorney-client-relationships.jpg class=\"card-img-top\" alt=\"clienti\">"
								     + "              <div class=\"card-body text-center\"style=\" background-color: rgba(209, 184, 128, 0.634)\">"
								     + "                <h5 class=\"card-title\">Clienti</h5>"
								     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>"
								     + "                <a href=\"/clienti\" class=\"btn btn-primary\">Pagina Clienti</a>"
								     + "              </div>"
								     + "            </div>"
								     + "          </div>"
								     + " <div class=\"col-md-4\">"
								     + "            <div class=\"card mb-3\">"
								     + "              <img src=https://www.normanmattar.com/wp-content/uploads/2023/06/shutterstock_1174972879.jpg class=\"card-img-top\" alt=\"...\">\r\n"
								     + "              <div class=\"card-body text-center\" style=\" background-color: rgba(209, 184, 128, 0.634)\">\r\n"
								     + "                <h5 class=\"card-title\">Precedenti</h5>\r\n"
								     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n"
								     + "                <a href=\"/precedenti\" class=\"btn btn-primary\">Pagina Precedenti</a>\r\n"
								     + "              </div>\r\n"
								     + "            </div>\r\n"
								     + "          </div>"
								     +" <div class=\"col-md-4\">"
								     + "            <div class=\"card mb-3\">"
								     + "              <img src=https://www.fjsolicitors.co.uk/wp-content/uploads/2022/12/drafting-legal-documents-scaled.jpg class=\"card-img-top\" alt=\"...\">\r\n"
								     + "              <div class=\"card-body text-center\" style=\" background-color: rgba(209, 184, 128, 0.634)\">\r\n"
								     + "                <h5 class=\"card-title\">Documentazione</h5>\r\n"
								     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n"
								     + "                <a href=\"/documentazione\" class=\"btn btn-primary\">Pagina Documentazione</a>\r\n"
								     + "              </div>\r\n"
								     + "            </div>\r\n"
								     + "          </div>\r\n"
								     + "        </div>"
								     +" <div class=\"row\">"
								     + "          <div class=\"col-md-4\">"
								     + "            <div class=\"card mb-3\">"
								     + "              <img src=https://gisondolaw.com/wp-content/uploads/2022/07/What-is-a-Lawyer-Retainer-Fee-and-How-does-it-Work.jpg class=\"card-img-top\" alt=\"...\">\r\n"
								     + "              <div class=\"card-body text-center\"style=\" background-color: rgba(209, 184, 128, 0.634)\">\r\n"
								     + "                <h5 class=\"card-title\">Pagamenti</h5>\r\n"
								     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n"
								     + "                <a href=\"/pagamenti\" class=\"btn btn-primary\">Pagina Pagamenti</a>"
								     + "              </div>\r\n"
								     + "            </div>\r\n"
								     + "          </div>"
								     +" <div class=\"col-md-4\">"
								     + "            <div class=\"card mb-3\">"
								     + "              <img src=https://imgcdn.agendadigitale.eu/wp-content/uploads/2022/05/05030330/legge-avvocatura-giudice-161210124413-1200x916.jpg class=\"card-img-top\" alt=\"...\">\r\n"
								     + "              <div class=\"card-body text-center\" style=\"background-color: rgba(209, 184, 128, 0.634)\">"
								     + "                <h5 class=\"card-title\">Casi</h5>"
								     + "                <p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n"
								     + "                <a href=\"/casi\" class=\"btn btn-primary\">Pagina Casi</a>"
								     + "              </div>"
								     + "            </div>"
								     + "          </div>"
								     + "        </div>"
								     + "      </div>"
								     +"<br><br><br>"
								     + "<footer class=\"text-white py-4\" style=\" background-color: rgba(0, 0, 0, 0.723);\">"
								     + "        <div class=\"container\">\r\n"
								     + "          <div class=\"row\">\r\n"
								     + "            <div class=\"col-md-4\">\r\n"
								     + "              <h5>Avvocati</h5>\r\n"
								     + "              <ul class=\"list-unstyled\">\r\n"
								     + "                <li>Avv. Marco Rossi</li>\r\n"
								     + "                <li>Avv. Laura Bianchi</li>\r\n"
								     + "                <li>Avv. Luca Verdi</li>\r\n"
								     + "              </ul>\r\n"
								     + "            </div>\r\n"
								     + "            <div class=\"col-md-4\">\r\n"
								     + "              <h5>Indirizzo</h5>\r\n"
								     + "              <p>Via Roma, 123<br>00100 Roma (RM), Italia</p>\r\n"
								     + "            </div>\r\n"
								     + "            <div class=\"col-md-4\">\r\n"
								     + "              <h5>Contatti</h5>\r\n"
								     + "              <p>Email: info@studiolegale.com<br>Telefono: +39 0123 456789</p>\r\n"
								     + "            </div>\r\n"
								     + "          </div>\r\n"
								     + "         \r\n"
								     + "          <div class=\"row\">\r\n"
								     + "            <div class=\"col-md-6\">\r\n"
								     + "              <p>Orari di apertura:<br>Lunedì - Venerdì: 9:00 - 18:00</p>\r\n"
								     + "            </div>\r\n"
								     + "            <div class=\"col-md-6 text-md-right\">\r\n"
								     + "              <p>&copy; 2024 Studio Legale. Tutti i diritti riservati.</p>\r\n"
								     + "            </div>\r\n"
								     + "          </div>\r\n"
								     + "        </div>\r\n"
								     + "      </footer>"
								     +"</body>"+
								     "</html>";
		// Imposta l'intestazione della risposta
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        int lunghezza = htmlLogIn.toString().getBytes("UTF-8").length;
        
        // Invio della risposta al client
        exchange.sendResponseHeaders(200, lunghezza);

        // Scrive la risposta al client
        OutputStream os = exchange.getResponseBody();
        os.write(htmlLogIn.getBytes());
        os.close();
		}
	}
}