# Lighty Control
A web application that allows multiple users to control smart bulbs that supports multiple modes. 
The integration with Hue or similar bulbs is not included, but can easily be implemented using the LightController class.

The project is also used to demonstrate and test different technologies.

### Image mode
![Image Mode](./docs/image_mode-tumb.png?raw=true)

[Screenshot Image Mode](./docs/image_mode.png?raw=true)

You select a predefined image or use you own and place the lights on the desired location of the image. The transition to 
different light colors is achieved by rotating the image at the desired speed. 

### Audio mode
![Audio Mode](./docs/audio_mode-tumb.png?raw=true)

[Screenshot Audio Mode](./docs/audio_mode.png?raw=true)

Using the Web Audio Api you can select an audio source (For example "What you hear" or a microphone) that will be used to
draw the spectrum. Now you can place your lights on the desired location so that it changes color depending on the music.

Only tested in Firefox!

### Pro mode
![Pro Mode](./docs/pro_mode-tumb.png?raw=true)

[Screenshot Pro Mode](./docs/pro_mode.png?raw=true)

Simple interface to perform experiments using PIXIjs to draw custom content on the canvas.

## Technologies used

* Java EE MVC / [Ozark](https://ozark.java.net/) / [JSR 371](https://jcp.org/en/jsr/detail?id=371)
* Websockets
* [PixiJS](http://www.pixijs.com/) / WebGL
* ...

## Prerequisites

* [Bower](https://bower.io)
* [Payara](http://www.payara.fish/) (Ozark is compatible with Glassfish / Payara)
* [Less](http://lesscss.org/)

Additionally recommended:

* [Netbeans](https://netbeans.org)

## Non-Windows users

Make sure to adapt `pom.xml` and remove `.cmd` from the executables (Bower)

## Installation

Just run mvn install using the desired profile and deploy an application server. Or open project in Netbeans, build project 
and run project

