"use strict";

var editor = CodeMirror(function (elt) {
    $('#scriptContent').append(elt);
}, {
    mode: "javascript",
    lineNumbers: true,
    value: $('#template').html()
});

$('#runButt').click(function () {
    var val = editor.getValue();
    try {
        eval(val);
    } catch (err) {
        console.log(err);
    }

});

var lighty = new Lighty();
var comms = new Comms();

var lightCont = new LightCont();
lighty.addRenderable(lightCont);

comms.addUpdateObj(lightCont);

comms.start();