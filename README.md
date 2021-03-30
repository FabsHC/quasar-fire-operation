# quasar-fire-operation

##### Dependencies
* JDK 15
* Mongo DB
* Docker(maybe, it's up to you!)

##### Setup
This project will need the dependencies above to run. You can find the JDK 15 here: https://sdkman.io/ . I highly recommend you to download this guy, you can find many SDKs from many languages in there. I ran an instance of Mongodb on a Docker container with this command:
* docker run -d --name mongodb -p 127.0.0.1:27017:27017 -d mongo:3.4.2

You can, of course, use an instance of mongodb the way you like. At least, it's a maven project, so you will need to have maven installed.

##### Project Structure
The project uses Clean Architecture with some modifications:

![image](https://user-images.githubusercontent.com/9483458/112857590-61291880-9087-11eb-9bcc-8f5f69e435a1.png)
* Configuration -> All spring configurations, beans, etc...stays here.
* Domains -> Here we have the domains that can transit through the layers: Gateways and Usecases.
* Gateways -> It's the layer who is responsible for the input/output of the app. Example: The APIs can input some data to do something. The application can save some data at the database, so the data is leaving the application.
  * HTTP -> Here we have our RestControllers.
  * MongoDB -> Here we have our Repositories.
* Usecase -> In this layer we have the core of the application. All the business logics are here.
