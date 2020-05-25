# Android Template

Default project for Android.

## Step by Step
+ Clone the project and rename the folder to the project name you want
+ Rename the packages from com.arima.templateproject to whatever you want
+ Change the app_name string to the project name
+ Change the application id into the app Gradle file to the new package
+ Change the project name into the settings Gradle file
+ Create a firebase project using and associate it to the new app
+ Download the google-service.json file and remove the default one into /app (set the view to project)
+ When firebase try to check the connection, don't finish the process and go back to android studio
+ Go to the Gradle files and sync
+ Execute the app and wait until the validation is made (if it takes more than a minute, close and opens your app again)
+ Go to the firebase console and go to database and create a new database, set the test rules (you can set the rules base on your needs after)
+ Create a collection named example_collection with a document named example_document with a string field named example_field (set the value you want)
+ In the firebase console, go to Authentication and allow email/password as session log in
+ Now you can create a user and log in, you should be able to see the value on the main screen and if you press the email button you will see the email of the logged user
+ NOTE: Before pushing your code, don't forget to change the origin in the git repo!

## Default Activities

* Splash screen
* Login screen
* Create account screen
* Password Recovery screen
* Main activity screen

## Extras

* Firebase user creation
* Firebase user login
* Firebase password recovery
* Cloud Firestore store connection
* Shared Preferences manager

## Main changes you need to do...

### Splash screen
+ Create a vector asset with the name ic_logo (you need an SVG or a PSD) for the logo and replace the placeholder
+ You can change the background color in color/colorSplash

### App icon
+ Create an image asset with the name ic_launcher. Change the background if you want.

### Create new activities
+ All the activities extend default activity. All the managers are defined there and the onCreate function trigger 3 methods, setLayout, the one you should redefine if you have a layout for your activity; createInternalItems, where all the managers are created; and createViews, that you should redefine (always calling the super method because this creates the progress bar) to create your own views.

### Create new fragments
+ All the fragments extend default fragment. This is similar to the default activity.

### Loading
+ If you want to use the loading function, you MUST add a ContentLoadingProgressBar in your layout name it loading.

### Up to you!
+ Star adding your code!


## Contact
Jorge Espinosa: [jorge.espinosa.ald@gmail.com](mailto:jorge.espinosa.ald@gmail.com)