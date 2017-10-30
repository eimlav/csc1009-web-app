package views;
import org.h2.mvstore.MVMap;

import model.Account;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class Index extends DynamicWebPage
{

	public Index(DatabaseInterface db,FileStoreInterface fs)
	{
		super(db,fs);
	}

	public boolean process(WebRequest toProcess)
	{
		if(toProcess.path.equalsIgnoreCase("Index.html") || toProcess.path.equalsIgnoreCase("index"))
		{
                        if(verifyCookie(toProcess)) {
				String url = "/enterPage";
				toProcess.r = new WebResponse( WebResponse.HTTP_REDIRECT, WebResponse.MIME_HTML,
										   "<html><body>Redirected: <a href=\"" + url + "\">" +
										   url + "</a></body></html>");
				toProcess.r.addHeader( "Location", url );
				return true;
			}
			String stringToSendToWebBrowser = "";
			stringToSendToWebBrowser += "<!DOCTYPE html>\n";
			stringToSendToWebBrowser += "<html>\n";
			stringToSendToWebBrowser += "  \n";
			stringToSendToWebBrowser += "  <head>\n";
			stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
			stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\"\n";
			stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "    <link href=\"index.css\" rel=\"stylesheet\" type=\"text/css\">\n";   
			stringToSendToWebBrowser += "  </head>\n";
			stringToSendToWebBrowser += "  \n";
			stringToSendToWebBrowser += "  <body>\n";
			stringToSendToWebBrowser += "    <div class=\"cover\">\n";
			stringToSendToWebBrowser += "      <div class=\"navbar\">\n";
			stringToSendToWebBrowser += "        <div class=\"container\">\n";
			stringToSendToWebBrowser += "          <div class=\"navbar-header\">\n";
			stringToSendToWebBrowser += "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-ex-collapse\">\n";
			stringToSendToWebBrowser += "              <span class=\"sr-only\">Toggle navigation</span>\n";
			stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "            </button>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "          <div class=\"collapse navbar-collapse\" id=\"navbar-ex-collapse\">\n";
			stringToSendToWebBrowser += "            <ul class=\"nav navbar-nav navbar-right\"></ul>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "      <div class=\"cover-image\" style=\"background-color : rgb(29,91,103)\"></div>\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12 text-center\">\n";
			stringToSendToWebBrowser += "            <h1 class=\"text-info\">e.mu</h1>\n";
			stringToSendToWebBrowser += "            <p class=\"lead text-info\">All the music. All in one place</p>\n";
			stringToSendToWebBrowser += "            <link href=\"http://fonts.googleapis.com/css?family=Open+Sans:700,600\"\n";
			stringToSendToWebBrowser += "            rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "            <form method=\"get\" action=\"LoginSuccessfull\">\n";
			stringToSendToWebBrowser += "              <div class=\"box\">\n";
			stringToSendToWebBrowser += "                <input type=\"username\" name=\"username\" placeholder=\"username\" onfocus=\"field_focus(this, 'email');\"\n";
			stringToSendToWebBrowser += "                onblur=\"field_blur(this, 'email');\" class=\"email\">\n";
			stringToSendToWebBrowser += "                <input type=\"password\" name=\"password\" placeholder=\"password\" onfocus=\"field_focus(this, 'email');\"\n";
			stringToSendToWebBrowser += "                onblur=\"field_blur(this, 'email');\" class=\"email\">\n";
			stringToSendToWebBrowser += "                <button type=\"submit\" class=\"btn btn-success\">Sign In! </button>\n";
			stringToSendToWebBrowser += "                <a class=\"btn btn-success\" href=\"SignUp\">Sign Up! </a>\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "            </form>\n";
			stringToSendToWebBrowser += "              <!-- End Box -->\n";
			stringToSendToWebBrowser += "              <div class=\"section\" style=\"background-color : rgb(29,91,103)\">\n";
			stringToSendToWebBrowser += "                <div class=\"container\">\n";
			stringToSendToWebBrowser += "                  <div class=\"row\">\n";
			stringToSendToWebBrowser += "                    <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "                      <hr>\n";
			stringToSendToWebBrowser += "                    </div>\n";
			stringToSendToWebBrowser += "                  </div>\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <div class=\"section\" style=\"background-color : rgb(29,91,103)\">\n";
			stringToSendToWebBrowser += "                <div class=\"container\">\n";
			stringToSendToWebBrowser += "                  <div class=\"row\">\n";
			stringToSendToWebBrowser += "                    <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "                      <h2 class=\"text-center text-info\">ABOUT US</h2>\n";
			stringToSendToWebBrowser += "                      <p class=\"text-center text-warning\">Welcome to e.mu! All the music you want to hear all in one place. Read\n";
			stringToSendToWebBrowser += "                        more about us below:</p>\n";
			stringToSendToWebBrowser += "                    </div>\n";
			stringToSendToWebBrowser += "                  </div>\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <div class=\"section text-left\" style=\"background-color : rgb(29,91,103)\">\n";
			stringToSendToWebBrowser += "                <div class=\"container\">\n";
			stringToSendToWebBrowser += "                  <div class=\"row\">\n";
			stringToSendToWebBrowser += "                    <div class=\"col-md-6\">\n";
			stringToSendToWebBrowser += "                      <h1 class=\"text-info\">Streaming</h1>\n";
			stringToSendToWebBrowser += "                      <p>e.mu allows anyone from around the world to listen and stream whatever\n";
			stringToSendToWebBrowser += "                        genre of &nbsp;music that they wish and we have even implemeted an emotion\n";
			stringToSendToWebBrowser += "                        section that works the exact same way as the genres&nbsp;</p>\n";
			stringToSendToWebBrowser += "                    </div>\n";
			stringToSendToWebBrowser += "                    <div class=\"col-md-23\">\n";
			stringToSendToWebBrowser += "                      <img src=\"StreamingIcon.png\" class=\"center-block img-responsive\">\n";
			stringToSendToWebBrowser += "                    </div>\n";
			stringToSendToWebBrowser += "                  </div>\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <div class=\"section\" style=\"background-color : rgb(29,91,103)\">\n";
			stringToSendToWebBrowser += "                <div class=\"container\">\n";
			stringToSendToWebBrowser += "                  <div class=\"row\">\n";
			stringToSendToWebBrowser += "                    <div class=\"col-md-6\">\n";
			stringToSendToWebBrowser += "                      <img src=\"IndependantArtistsImage.jpg\" class=\"center-block img-circle img-responsive\"\n";
			stringToSendToWebBrowser += "                      height=\"600\" width=\"400\">\n";
			stringToSendToWebBrowser += "                    </div>\n";
			stringToSendToWebBrowser += "                    <div class=\"col-md-6\">\n";
			stringToSendToWebBrowser += "                      <h1 class=\"text-info\">Independent Artists</h1>\n";
			stringToSendToWebBrowser += "                      <p>Not only does our site focus on allowing users to stream music we also\n";
			stringToSendToWebBrowser += "                        highly support independant artists who create their own music and want\n";
			stringToSendToWebBrowser += "                        the world to hear it. All you have to do is submit your work and it will\n";
			stringToSendToWebBrowser += "                        be uploaded to our site for everyone to see and hear and they can give\n";
			stringToSendToWebBrowser += "                        you feedback on what they think and this could be a start to great things\n";
			stringToSendToWebBrowser += "                        for you as a music producer..</p>\n";
			stringToSendToWebBrowser += "                    </div>\n";
			stringToSendToWebBrowser += "                  </div>\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "            </form>\n";
			stringToSendToWebBrowser += "            <footer>\n";
			stringToSendToWebBrowser += "              <a href=\"SignUp.html\">\n";
			stringToSendToWebBrowser += "              </a>\n";
			stringToSendToWebBrowser += "              <div class=\"container\" style=\"background-color : rgb(29,91,103)\">\n";
			stringToSendToWebBrowser += "                <a href=\"SignUp.html\">\n";
			stringToSendToWebBrowser += "                </a>\n";
			stringToSendToWebBrowser += "                <div class=\"row\">\n";
			stringToSendToWebBrowser += "                  <a href=\"SignUp.html\">\n";
			stringToSendToWebBrowser += "                  <div class=\"col-sm-6\">\n";
			stringToSendToWebBrowser += "                    <h1 class=\"text-info\">Follow us!</h1>\n";
			stringToSendToWebBrowser += "                    <p>Follow us on Instagram, Twitter and Facebook &nbsp;for more news on the\n";
			stringToSendToWebBrowser += "                      newest music and updates on the best playlists out there!</p>\n";
			stringToSendToWebBrowser += "                  </div>\n";
			stringToSendToWebBrowser += "                  </a>\n";
			stringToSendToWebBrowser += "                  <a href=\"https://www.instagram.com/\"><i class=\"fa fa-5x fa-fw fa-instagram text-inverse\"></i></a>\n";
			stringToSendToWebBrowser += "                  <a href=\"https://twitter.com/\" draggable=\"true\"><i class=\"fa fa-5x fa-fw fa-twitter text-inverse\"></i></a>\n";
			stringToSendToWebBrowser += "                  <a href=\"https://www.facebook.com/\"><i class=\"fa fa-5x fa-facebook fa-fw text-inverse\"></i></a>\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "            </footer>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "  </body>\n";
			stringToSendToWebBrowser += "\n";
			stringToSendToWebBrowser += "</html>\n";
			toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
			return true;
		}
		return false;
	}

    public boolean verifyCookie(WebRequest toProcess) {
		String username = toProcess.cookies.get("username");
		String password = toProcess.cookies.get("password");
		
		MVMap<String, Account> accountsMap = db.s.openMap("Accounts");
		for(String key : accountsMap.keySet()) {
			System.out.println(accountsMap.get(key).username + " " + accountsMap.get(key).password + "\n");
		}
		Account account = accountsMap.get(username);
		
		if(account != null) {
			if(account.password.equals(password)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}