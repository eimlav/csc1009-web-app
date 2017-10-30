package views;

import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

import org.h2.mvstore.MVMap;

import model.Account;


public class LoginSuccessfull extends DynamicWebPage
{

	public LoginSuccessfull(DatabaseInterface db,FileStoreInterface fs)
	{
		super(db,fs);
	}

	public boolean process(WebRequest toProcess)
	{
		if(toProcess.path.equalsIgnoreCase("LoginSuccessfull"))
		{
			String username = toProcess.params.get("username");
			String password = toProcess.params.get("password");

			MVMap<String, Account> accounts = db.s.openMap("Accounts");
			
			if(!accounts.containsKey(username))
		{
				String stringToSendToWebBrowser = "";
				stringToSendToWebBrowser += "<html>\n";
				stringToSendToWebBrowser += "  \n";
				stringToSendToWebBrowser += "  <head>\n";
				stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
				stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
				stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
				stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
				stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\"\n";
				stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
				stringToSendToWebBrowser += "    <link href=\"http://pingendo.github.io/pingendo-bootstrap/themes/default/bootstrap.css\"\n";
				stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
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
				stringToSendToWebBrowser += "          <h1 class=\"text-info text-left\">Unsuccessfull Login!</h1>\n";
				stringToSendToWebBrowser += "          <div class=\"row\">\n";
				stringToSendToWebBrowser += "            <div class=\"col-md-12\">\n";
				stringToSendToWebBrowser += "              <h2 class=\"text-warning\">\n";
				stringToSendToWebBrowser += "                <a href=\"Index.html\">Click here to go back and try again!</a>\n";
				stringToSendToWebBrowser += "              </h2>\n";
				stringToSendToWebBrowser += "            </div>\n";
				stringToSendToWebBrowser += "          </div>\n";
				stringToSendToWebBrowser += "        </div>\n";
				stringToSendToWebBrowser += "      </div>\n";
				stringToSendToWebBrowser += "      <div class=\"cover-image\" style=\"background-color : rgb(29,91,103)\"></div>&gt;</div>\n";
				stringToSendToWebBrowser += "  </body>\n";
				stringToSendToWebBrowser += "\n";
				stringToSendToWebBrowser += "</html>\n";
				toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
				return true;
			}
			
			Account account = accounts.get(username);
			
			if(!account.password.contentEquals(password))
			{
				
				String stringToSendToWebBrowser = "";
				stringToSendToWebBrowser += "<html>\n";
				stringToSendToWebBrowser += "  \n";
				stringToSendToWebBrowser += "  <head>\n";
				stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
				stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
				stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
				stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
				stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\"\n";
				stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
				stringToSendToWebBrowser += "    <link href=\"http://pingendo.github.io/pingendo-bootstrap/themes/default/bootstrap.css\"\n";
				stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
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
				stringToSendToWebBrowser += "          <h1 class=\"text-info text-left\">Unsuccessfull Login!</h1>\n";
				stringToSendToWebBrowser += "          <div class=\"row\">\n";
				stringToSendToWebBrowser += "            <div class=\"col-md-12\">\n";
				stringToSendToWebBrowser += "              <h2 class=\"text-warning\">\n";
				stringToSendToWebBrowser += "                <a href=\"Index.html\">Click here to go back and try again!</a>\n";
				stringToSendToWebBrowser += "              </h2>\n";
				stringToSendToWebBrowser += "            </div>\n";
				stringToSendToWebBrowser += "          </div>\n";
				stringToSendToWebBrowser += "        </div>\n";
				stringToSendToWebBrowser += "      </div>\n";
				stringToSendToWebBrowser += "      <div class=\"cover-image\" style=\"background-color : rgb(29,91,103)\"></div>&gt;</div>\n";
				stringToSendToWebBrowser += "  </body>\n";
				stringToSendToWebBrowser += "\n";
				stringToSendToWebBrowser += "</html>\n";
				toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
				return true;
			}		
		
			String stringToSendToWebBrowser = "";
			stringToSendToWebBrowser += "<html>\n";
			stringToSendToWebBrowser += "  \n";
			stringToSendToWebBrowser += "  <head>\n";
			stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
			stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\"\n";
			stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "    <link href=\"http://pingendo.github.io/pingendo-bootstrap/themes/default/bootstrap.css\"\n";
			stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
                        stringToSendToWebBrowser += "	 <script>\n";
			stringToSendToWebBrowser += "	 	window.onload = function() {\n";
			stringToSendToWebBrowser += "	 		document.cookie = \"username=" + username + "\";";
			stringToSendToWebBrowser += "	 		document.cookie = \"password=" + password + "\";";
			stringToSendToWebBrowser += "		}";
			stringToSendToWebBrowser += "	 </script>";
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
			stringToSendToWebBrowser += "          <h1 class=\"text-info text-left\">Logged in!</h1>\n";
			stringToSendToWebBrowser += "          <div class=\"row\">\n";
			stringToSendToWebBrowser += "            <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "              <h2 class=\"text-warning\">\n";
			stringToSendToWebBrowser += "                <a href=\"EnterPage.html\">Click Here to go to homepage!</a>\n";
			stringToSendToWebBrowser += "              </h2>\n";
			stringToSendToWebBrowser += "            </div>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "      <div class=\"cover-image\" style=\"background-color : rgb(29,91,103)\"></div>&gt;</div>\n";
			stringToSendToWebBrowser += "  </body>\n";
			stringToSendToWebBrowser += "\n";
			stringToSendToWebBrowser += "</html>\n";
			toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
			return true;
		}
		return false;
	}

}