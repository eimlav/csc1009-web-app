package views;

import org.h2.mvstore.MVMap;

import model.Account;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class Independent extends DynamicWebPage
{

	public Independent(DatabaseInterface db,FileStoreInterface fs)
	{
		super(db,fs);
	}

	public boolean process(WebRequest toProcess)
	{
		if(toProcess.path.equalsIgnoreCase("Independent.html") || toProcess.path.equalsIgnoreCase("Independent"))
		{
			boolean loggedIn = verifyCookie(toProcess);
			
			String stringToSendToWebBrowser = "";
			stringToSendToWebBrowser += "<html>\n";
			stringToSendToWebBrowser += "  \n";
			stringToSendToWebBrowser += "  <head>\n";
			stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
			stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"search.js\"></script>\n";
			stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\"\n";
			stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "    <link href=\"search.css\" rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "    <link href=\"https://fonts.googleapis.com/css?family=Lato\" rel=\"stylesheet\">\n";
			stringToSendToWebBrowser += "    <title>e.mu | Search</title>\n";
			stringToSendToWebBrowser += "  </head>\n";
			stringToSendToWebBrowser += "  \n";
			stringToSendToWebBrowser += "  <body>\n";
			stringToSendToWebBrowser += "    <div class=\"navbar navbar-default navbar-static-top\">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"navbar-header\">\n";
			stringToSendToWebBrowser += "          <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-ex-collapse\">\n";
			stringToSendToWebBrowser += "            <span class=\"sr-only\">Toggle navigation</span>\n";
			stringToSendToWebBrowser += "            <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "            <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "            <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "          </button>\n";
			stringToSendToWebBrowser += "          <a class=\"navbar-brand\" href=\"#\"><span>e.mu</span></a>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "        <div class=\"collapse navbar-collapse\" id=\"navbar-ex-collapse\">\n";
			stringToSendToWebBrowser += "          <ul class=\"nav navbar-nav navbar-right\">\n";
			stringToSendToWebBrowser += "            <li class=\"disabled\">\n";
			stringToSendToWebBrowser += "              <a href=\"\\enterPage.html\">HOME</a>\n";
			stringToSendToWebBrowser += "            </li>\n";
			stringToSendToWebBrowser += "            <li class=\"disabled\">\n";
			stringToSendToWebBrowser += "              <a href=\"\\search\">SEARCH</a>\n";
			stringToSendToWebBrowser += "            </li>\n";
			stringToSendToWebBrowser += "            <li class=\"disabled\">\n";
			stringToSendToWebBrowser += "              <a href=\"\\playlists\">PLAYLISTS</a>\n";
			stringToSendToWebBrowser += "            </li>\n";
			stringToSendToWebBrowser += "            <li class=\"active\">\n";
			stringToSendToWebBrowser += "              <a href=\"\\independent\">INDEPENDENT</a>\n";
			stringToSendToWebBrowser += "            </li>\n";
			stringToSendToWebBrowser += "            <li class=\"disabled\">\n";
			stringToSendToWebBrowser += "              <a href=\"\\upload.html\">UPLOAD</a>\n";
			stringToSendToWebBrowser += "            </li>\n";
			
			if(loggedIn) {
				stringToSendToWebBrowser += "            <li class=\"disabled\">\n";
				stringToSendToWebBrowser += "              <a href=\"\\enterPage.html\" onclick=\"logout()\">LOGOUT</a>\n";
				stringToSendToWebBrowser += "            </li>\n";
			}
			
			stringToSendToWebBrowser += "            <li>\n";
			stringToSendToWebBrowser += "              <form class=\"navbar-form navbar-left\" role=\"search\" id=\"nav-bar-search\" action=\"/search/resultsStn\">\n";
			stringToSendToWebBrowser += "                <div class=\"form-group\">\n";
			stringToSendToWebBrowser += "                  <input type=\"text\" class=\"form-control\" placeholder=\"Search\" id=\"nav-bar-search-input\">\n";
			stringToSendToWebBrowser += "                  <input type=\"hidden\" value=\"\" name=\"querys\" id=\"finalStnQueryNav\">\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "              </form>\n";
			stringToSendToWebBrowser += "            </li>\n";
			stringToSendToWebBrowser += "          </ul>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "    <div class=\"section\">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "            <h1 class=\"main-header\">INDEPENDENT</h1>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "    <div class=\"section\" id=\"footer\">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "            <div id=\"footer\">\n";
			stringToSendToWebBrowser += "              <p class=\"text-center\">Scroll down to see the work of many, all hosted here.</p>\n";
			stringToSendToWebBrowser += "            </div>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "    <div class=\"section\">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "            <hr>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "    <div class=\"section\">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-6\">\n";
			stringToSendToWebBrowser += "            <img src=\"http://pingendo.github.io/pingendo-bootstrap/assets/placeholder.png\"\n";
			stringToSendToWebBrowser += "            class=\"img-responsive img-thumbnail\">\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-6\">\n";
			stringToSendToWebBrowser += "            <h1 id=\"trackName\">trackname</h1>\n";
			stringToSendToWebBrowser += "            <h3 id=\"artistName\">artist</h3>\n";
			stringToSendToWebBrowser += "            <h4 id=\"album\">album</h4>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "    <div class=\"section\">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-6\">\n";
			stringToSendToWebBrowser += "            <img src=\"http://pingendo.github.io/pingendo-bootstrap/assets/placeholder.png\"\n";
			stringToSendToWebBrowser += "            class=\"img-responsive img-thumbnail\">\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-6\">\n";
			stringToSendToWebBrowser += "            <h1 id=\"trackName\">trackname</h1>\n";
			stringToSendToWebBrowser += "            <h3 id=\"artistName\">artist</h3>\n";
			stringToSendToWebBrowser += "            <h4 id=\"album\">album</h4>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "    <div class=\"section\">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-6\">\n";
			stringToSendToWebBrowser += "            <img src=\"http://pingendo.github.io/pingendo-bootstrap/assets/placeholder.png\"\n";
			stringToSendToWebBrowser += "            class=\"img-responsive img-thumbnail\">\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-6\">\n";
			stringToSendToWebBrowser += "            <h1 id=\"trackName\">trackname</h1>\n";
			stringToSendToWebBrowser += "            <h3 id=\"artistName\">artist</h3>\n";
			stringToSendToWebBrowser += "            <h4 id=\"album\">album</h4>\n";
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