# quasar-fire-operation

###### Powered By Rebel Alliance

##### Dependencies
* JDK 15
* Mongo DB
* Docker(maybe, it's up to you!)

##### Setup
This project will need the dependencies above to run. You can find the JDK 15 [here](https://sdkman.io/). I highly recommend you to download this guy, you can find many SDKs from many languages in there. I ran an instance of Mongodb on a Docker container with this command:
* docker run -d --name mongodb -p 127.0.0.1:27017:27017 -d mongo:3.4.2

You can, of course, use an instance of mongodb the way you like. At least, it's a maven project, so you will need to have maven installed.

##### Project Structure
The project uses Clean Architecture with some modifications:

![image](https://user-images.githubusercontent.com/9483458/112857590-61291880-9087-11eb-9bcc-8f5f69e435a1.png)
* **Configuration** -> All spring configurations, beans, etc...stays here.
* **Domains** -> Here we have the domains that can transit through the layers: Gateways and Usecases.
* **Gateways** -> It's the layer who is responsible for the input/output of the app. Example: The APIs can input some data to do something. The application can save some data at the database, so the data is leaving the application.
  * **HTTP** -> Here we have our RestControllers.
  * **MongoDB** -> Here we have our Repositories.
* **Usecase** -> In this layer we have the core of the application. All the business logics are here.

##### The Problem
At a unknown x,y coordinate we have a transmitter sending a message to 3 stellites with know x,y coordinates:
* Kenobi(-500;-200)
* Skywalker(100;-100)
* Sato(500;100)

Each satellite knows the distance, between itself and the transmitter, and part of the message. Example:
* Satellite: Kenobi
* Distance received: 145.7
* Message received: {This, , a, , message., , , }

You can notice that some parts of the message are missing, because the transmitter could not send all the message. The other parts are with the other satellites.
So, to know all the message, you will have to merge the satellites information.

The other problem is, you need to know the spaceship location (x;y) to retreive whatever the message says. To do that, we will use the satellites coordinates and distances from the spaceship.

##### The Solutions
###### Decryption of the message
The messages problem is a simple merge of information, we assume that the same message will reach each satellite with missing parts, like the example below:
* Kenobi: {This, , a, , message., , , }
* Skywalker: { , , , secret, message., , , us}
* Sato: {This, is, , , , Please, help, }

The missing parts are represented with Empty Strings. In case the program could not decrypt the message, it'll return an error.

###### Spaceship location
This part is more complicated. Here I used a trilateration of coordinates and distances.
**Knowing that Kenobi Satelitte is number 1, Skywalker is 2 and Sato is 3.**
We start with the distance(range) between two points equation:

![image](https://user-images.githubusercontent.com/9483458/113040166-df141f00-916e-11eb-93d4-07851b01221d.png)

Expanding out the squares, we had this equation for each satellite:

![image](https://user-images.githubusercontent.com/9483458/113044817-7c258680-9174-11eb-8ece-be66728b5dbe.png)

![image](https://user-images.githubusercontent.com/9483458/113044915-9495a100-9174-11eb-9d10-389c56627e22.png)

![image](https://user-images.githubusercontent.com/9483458/113044736-60ba7b80-9174-11eb-93b3-7cca03362cdd.png)

Now we need two new equations to find the spaceship position, so we subtract the second equation from the first one:

![image](https://user-images.githubusercontent.com/9483458/113046235-349ffa00-9176-11eb-82b3-8b2378fbf877.png)

Do the same, but this time subtract the third equation from the second:

![image](https://user-images.githubusercontent.com/9483458/113046659-b132d880-9176-11eb-968e-8b3124847884.png)

So we have our two equations to find the spaceship:

![image](https://user-images.githubusercontent.com/9483458/113046963-1686c980-9177-11eb-8cbd-26db6034aa91.png)

Where:

![image](https://user-images.githubusercontent.com/9483458/113047687-015e6a80-9178-11eb-9a86-a30eb35c940a.png)

![image](https://user-images.githubusercontent.com/9483458/113047740-15a26780-9178-11eb-9f27-28d4c58b8b7d.png)

This can be solved using Cramer's rule finding the determinants:

![image](https://user-images.githubusercontent.com/9483458/113048906-69fa1700-9179-11eb-8285-3d3cae0b8ec5.png)

Now following the equation below, we got the spaceship x,y coordinates:

![image](https://user-images.githubusercontent.com/9483458/113048212-ada05100-9178-11eb-97e5-cb344442e2ca.png)
![image](https://user-images.githubusercontent.com/9483458/113048287-bf81f400-9178-11eb-8170-f207bb5a0482.png)
