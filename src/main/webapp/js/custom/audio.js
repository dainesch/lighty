"use strict";

window.AudioContext = window.AudioContext || window.webkitAudioContext;

function hslToHex(h, s, l) {
    var r, g, b;

    if (s === 0) {
        r = g = b = l; // achromatic
    } else {
        var hue2rgb = function hue2rgb(p, q, t) {
            if (t < 0)
                t += 1;
            if (t > 1)
                t -= 1;
            if (t < 1 / 6)
                return p + (q - p) * 6 * t;
            if (t < 1 / 2)
                return q;
            if (t < 2 / 3)
                return p + (q - p) * (2 / 3 - t) * 6;
            return p;
        }

        var q = l < 0.5 ? l * (1 + s) : l + s - l * s;
        var p = 2 * l - q;
        r = hue2rgb(p, q, h + 1 / 3);
        g = hue2rgb(p, q, h);
        b = hue2rgb(p, q, h - 1 / 3);
    }
    return "" + ((1 << 24) + (Math.round(r * 255) << 16) + (Math.round(g * 255) << 8) + Math.round(b * 255)).toString(16).slice(1);

}




var AudioChart = function () {
    Renderable.call(this, 200, new PIXI.Container());
    this.graphics = new PIXI.Graphics();
    this.container.addChild(this.graphics);
    this.audioContext = new window.AudioContext();
    this.audioInput = null;
    this.realAudioInput = null;
    this.inputPoint = null;
    this.analyserNode = null;
    this.zeroGain = null;
    this.shift = 0;
    this.amplitude = 1;
    this.compression = 1;
    this.barCount = 100;

    this.registerActions();
};
$.extend(AudioChart.prototype, Renderable.prototype, {
    registerActions: function () {
        var _this = this;
        $('#shiftSlider').change(function () {
            _this.shift = parseFloat($(this).val());
        });
        $('#ampSlider').change(function () {
            _this.amplitude = parseFloat($(this).val());
        });
        $('#compSlider').change(function () {
            _this.compression = parseFloat($(this).val());
        });
        $('#barSlider').change(function () {
            _this.barCount = parseInt($(this).val());
        });

        $('#controlReset').click(function () {
            var def = parseFloat($('#shiftSlider').attr('data-slider-value'));
            $('#shiftSlider').slider('setValue', def);
            _this.shift = def;

            def = parseFloat($('#ampSlider').attr('data-slider-value'));
            $('#ampSlider').slider('setValue', def);
            _this.amplitude = def;

            def = parseFloat($('#compSlider').attr('data-slider-value'));
            $('#compSlider').slider('setValue', def);
            _this.compression = def;

            def = parseFloat($('#barSlider').attr('data-slider-value'));
            $('#barSlider').slider('setValue', def);
            _this.barCount = def;
        });
    },
    initAudio: function () {
        if (!navigator.getUserMedia)
            navigator.getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
        if (!navigator.cancelAnimationFrame)
            navigator.cancelAnimationFrame = navigator.webkitCancelAnimationFrame || navigator.mozCancelAnimationFrame;
        if (!navigator.requestAnimationFrame)
            navigator.requestAnimationFrame = navigator.webkitRequestAnimationFrame || navigator.mozRequestAnimationFrame;

        navigator.getUserMedia(
                {
                    "audio": {
                        "mandatory": {
                            "googEchoCancellation": "false",
                            "googAutoGainControl": "false",
                            "googNoiseSuppression": "false",
                            "googHighpassFilter": "false"
                        },
                        "optional": []
                    }
                }, this.gotStream.bind(this), function (e) {
            alert('Error getting audio');
            console.log(e);
        });
    },
    gotStream: function (stream) {
        this.inputPoint = this.audioContext.createGain();

        // Create an AudioNode from the stream.
        this.realAudioInput = this.audioContext.createMediaStreamSource(stream);
        this.audioInput = this.realAudioInput;
        this.audioInput.connect(this.inputPoint);

        this.analyserNode = this.audioContext.createAnalyser();
        this.analyserNode.fftSize = 2048;
        this.inputPoint.connect(this.analyserNode);

        this.zeroGain = this.audioContext.createGain();
        this.zeroGain.gain.value = 0.0;
        this.inputPoint.connect(this.zeroGain);
        this.zeroGain.connect(this.audioContext.destination);

    },
    animate: function () {
        if (this.zeroGain === null) {
            return;
        }
        // analyzer draw code here


        var BAR_WIDTH = Math.round(((CANVASSIZE.width / 0.9) - this.barCount) / this.barCount);
        var SPACING = BAR_WIDTH + 1;
        var numBars = Math.round(CANVASSIZE.width / 0.9 / SPACING);
        var freqByteData = new Uint8Array(this.analyserNode.frequencyBinCount);

        this.analyserNode.getByteFrequencyData(freqByteData);

        this.graphics.clear();
        var multiplier = this.analyserNode.frequencyBinCount / numBars;

        // Draw rectangle for each frequency bin.
        for (var i = 0; i < numBars; ++i) {
            var magnitude = 0;
            var offset = Math.floor(i * multiplier);
            // gotta sum/average the block, or we miss narrow-bandwidth spikes
            for (var j = 0; j < multiplier; j++)
                magnitude += freqByteData[offset + j];
            magnitude = magnitude / multiplier;

            // amp 
            magnitude = magnitude * this.amplitude;

            var magnitude2 = magnitude / CANVASSIZE.height;

            // comp
            magnitude2 = magnitude2 / this.compression;

            // shift
            magnitude2 = this.shift + magnitude2;
            if (magnitude2 > 1) {
                magnitude2 -= 1;
            }



            //this.graphics.fillStyle = "hsl( " + Math.round((i * 360) / numBars) + ", 100%, 50%)";

            //this.graphics.beginFill(0xFF3300);
            this.graphics.beginFill(parseInt(hslToHex(magnitude2, 1, 0.5), 16));
            //this.graphics.lineStyle(10, 0xffd900, 1);

            this.graphics.drawRect(i * SPACING, CANVASSIZE.height, BAR_WIDTH, -magnitude);

            this.graphics.endFill();
        }
    }

});

















// ****** main *********

var lighty = new Lighty();
var comms = new Comms();

var audioChart = new AudioChart();
var colBar = new ColorBar();
var lightCont = new LightCont();

lighty.addRenderable(audioChart);
lighty.addRenderable(colBar);
lighty.addRenderable(lightCont);

comms.addUpdateObj(lightCont);

comms.start();

window.addEventListener('load', audioChart.initAudio.bind(audioChart));
