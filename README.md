# challenge dagger java8 manager example
This code is from a 6-hour challenge I did recently, with this I want to demonstrate how to use dagger and the manager design pattern along with recycle view and a randomized list.<br>
you will need my library DigiJedi included as a submodule or cloneable from here : https://github.com/antoniotari/DigiJedi.git <br>
It might have a bunch of TODOs, but remember I completed it in only 5 hours (counting a 1 hour lunch break).<br><br>

The app is a quiz where the questions and the answers are randomized at every start<br>
The json file is stored inside the asset folder, the parse the file I use a factory method inside the Questions class that creates modules that make sense for the app. <br>
The questions of the quiz are handled by a questions manager that takes care of giving the user the "next question", and also has a method to save and recover the state when the activity gets destroyed and created.<br>
I use butterknife to inject the views and dagger for dependency injection.<br><br>
ps.The reading and parsing of the json is done on the UI thread (wrong!!), sorry about that, I was focusing on other stuff, and ran out of time
