= = = = = = = = = = = = = =
 CSC1009 Web App Assignment
  Emotional Music Database
= = = = = = = = = = = = = =

= = = = = RUNNING THE WEB APP = = = = =
	- Import project into Eclipse
	- Enter src folder and navigate to default package
	- Run Main.java
	- On your browser, navigate to localhost:8080
	- Index page will display asking for login
	- Create an account by selecting 'Sign Up' or use the test account details (test account
	  contains both sample user and non-user playlists if you wish to explore this feature):
		- Username: testuser
		- Password: password
	- Enjoy!

! ! ! Important: The database contains 15 songs as this is only a demo project ! ! !

= = = = = = = = = = = =
 About the application
= = = = = = = = = = = =
As the core assignment of one of my Level 1 modules, I was a member of an externally selected team in which we had to build a web app running on a local Apache web server. A variety of project topics were provided from which we had to select one. We chose to build a web app which focused on allowing the user to search for music based on the emotions associated with that piece of music

The team consisted of four members. I was tasked with producing deliverables each week (on a module specific web app) to show that I was contributing to the project.   My specific job for the web app was to build the search functionality, which was the core part of the web app. Shortly after the inception of the project, one of our members failed to participate in the project in any way. As a result, I and the other members of the team, coordinated more closely and delegated additional work among each other to ensure that deadlines could be met to a satisfactory standard. 

I focused much of my time on learning the foundations as well as some more advanced areas of HTML and CSS. I prioritised these over Javascript as I believed that producing a functional product first was more important than spending time trying to implement additional features using Javascript. In order to build my own search algorithm, my algorithm was (very) loosely based on the ranking system described in a Stanford paper by Sergey Brin and Lawrence Page on their prototype search engine, Google. Upon executing a search, I created a score for each song stored in the database by simply adding one point to the song’s specific score each time one of the queries was matched in the song’s data (title, artist, genre, emotions etc). I then filtered out any songs with a score of 0 since these were irrelevant and sorted the rest by score descending, to provide the user with the most relevant results first. In addition, I also implemented playlist functionality to the web app, which provided the user with the ability to create and share their own playlists.

In the end, we were able to deliver on the core requirements of the project and achieved a 2:1. The project taught me the how vital careful time management is. I also learned the importance of being able to recognise when something was out of my scope and how it is more crucial to focus on delivering the fundamental features of a product first and foremost.
 