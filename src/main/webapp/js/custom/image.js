"use strict";


// ****** update *********

var lighty = new Lighty();
var comms = new Comms();

var backImg = new BackImage();
var colBar = new ColorBar();
var lightCont = new LightCont();

lighty.addRenderable(backImg);
lighty.addRenderable(colBar);
lighty.addRenderable(lightCont);

comms.addUpdateObj(lightCont);

comms.start();
