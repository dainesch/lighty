"use strict";

var SERVERWS = "ws://localhost:8080/ws";
var PIXI = (typeof PIXI === 'undefined') ? null : PIXI;
if (PIXI === null) {
    alert("Pixie Lib not loaded");
}

var CANVASSIZE = {width: $('#canvasCont').width(), height: $('#canvasCont').parent().height() - 40};
var RENDERER = new PIXI.WebGLRenderer(CANVASSIZE.width, CANVASSIZE.height, {autoresize: true, antialias: true, transparent: true, preserveDrawingBuffer: true});
var TRANSCOLOR = '#00ff00';
var SHIFTSPEED = 0.8;
$('#canvasCont').append(RENDERER.view);



// ******** Lighty *********

var Lighty = function () {
    this.renderMap = new HashMap();
    window.onresize = this.onResize.bind(this);
    this.stage = new PIXI.Container();
    this.animate();
};
Lighty.prototype = {
    onResize: function () {
        CANVASSIZE = {width: $('#canvasCont').width(), height: $('#canvasCont').parent().height() - 20};
        RENDERER.resize(CANVASSIZE.width, CANVASSIZE.height);
        this.renderMap.forEach(function (value, key) {
            value.onResize.bind(value)();
        });
    },
    animate: function () {
        // start the timer for the next animation loop
        requestAnimationFrame(this.animate.bind(this));

        this.renderMap.forEach(function (value, key) {
            value.animate();
        });

        // this is the main render call that makes pixi draw your container and its children.
        RENDERER.render(this.stage);
    },
    addRenderable: function (r) {
        this.renderMap.set(r.getZIndex(), r);
        this.stage.removeChildren();
        var keys = this.renderMap.keys().sort(function (a, b) {
            return a - b;
        });
        // re-add all
        for (var i in keys) {
            this.stage.addChild(this.renderMap.get(keys[i]).getContainer());
        }
    },
    removeRenderable: function (r) {
        this.renderMap.remove(r.getZIndex());
        this.stage.removeChild(r.getContainer());
    }
};



// basic color

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}


var LampColor = function (r, g, b) {
    if (g === undefined) {
        this.r = r[0];
        this.g = r[1];
        this.b = r[2];
        this.a = r[3] / 255;
        if (this.a < 0.1) {
            var c = hexToRgb(TRANSCOLOR);
            this.r = c.r;
            this.g = c.g;
            this.b = c.b;
        }
    } else {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;
    }
};
LampColor.prototype = {
    getRGBA: function () {
        return "rgba(" + this.r + "," + this.g + "," + this.b + ",1)";
    }
};

// basic renderable
var Renderable = function (zIndex, container) {
    this.zIndex = zIndex;
    this.container = container;
};
Renderable.prototype = {
    animate: function () {

    },
    onResize: function () {

    },
    getZIndex: function () {
        return this.zIndex;
    },
    getContainer: function () {
        return this.container;
    }
};


// back image
var BackImage = function () {
    Renderable.call(this, -1, new PIXI.Container());
    this.speed = 0.003;

    var texture = PIXI.Texture.fromImage('/img/wheel.png');
    this.sprite = new PIXI.Sprite(texture);
    this.container.addChild(this.sprite);

    texture = PIXI.Texture.fromImage('/img/move-icon.png');
    this.arrow = new PIXI.Sprite(texture);
    this.container.addChild(this.arrow);

    this.loadImage('/img/wheel.png');
    this.registerActions();
};
$.extend(BackImage.prototype, Renderable.prototype, {
    registerActions: function () {
        var _this = this;
        $("#imgSelect").change(function (event) {
            var file = event.target.files[0];
            _this.loadFile(file);
        });
        $('#speedSlider').change(function () {
            _this.setSpeed(parseFloat($(this).val()));
        });
        $('#scaleSlider').change(function () {
            _this.setScale(parseFloat($(this).val()));
        });
        $('#shiftSlider').change(function () {
            SHIFTSPEED = parseFloat($(this).val());
        });
        $('#transColor').change(function () {
            TRANSCOLOR = $(this).val();
        });
        $('#controlReset').click(function () {
            var def = parseFloat($('#speedSlider').attr('data-slider-value'));
            $('#speedSlider').slider('setValue', def);
            _this.setSpeed(def);

            def = parseFloat($('#scaleSlider').attr('data-slider-value'));
            $('#scaleSlider').slider('setValue', def);
            _this.setScale(def);

            def = parseFloat($('#shiftSlider').attr('data-slider-value'));
            $('#shiftSlider').slider('setValue', def);
            SHIFTSPEED = def;
        });
        $('#imgWheel').click(function () {
            _this.loadImage('/img/wheel.png');
        });
        $('#imgChart').click(function () {
            _this.loadImage('/img/chart.png');
        });
        $('#imgCust').click(function () {
            _this.loadImage('/img/cust.png');
        });
        $('#imgRad').click(function () {
            _this.loadImage('/img/radiation.png');
        });
    },
    animate: function () {
        this.sprite.rotation += this.speed;
    },
    setScale: function (s) {
        this.sprite.scale.set(s);
    },
    loadImage: function (img) {
        var _this = this;
        this.container.removeChild(this.sprite);
        this.container.removeChild(this.arrow);
        this.container.x = 0;
        this.container.y = 0;
        PIXI.loader.reset();
        PIXI.loader.add(img);
        PIXI.loader.load(function () {
            var texture = PIXI.loader.resources[img].texture;
            _this.sprite = new PIXI.Sprite(texture);
            _this.sprite.x = texture.width / 2;
            _this.sprite.y = texture.height / 2;
            _this.sprite.anchor.set(0.5);
            _this.container.addChild(_this.sprite);

            texture = PIXI.Texture.fromImage('/img/move-icon.png');
            _this.arrow = new PIXI.Sprite(texture);
            _this.arrow.x = _this.sprite.x;
            _this.arrow.y = _this.sprite.y;
            _this.arrow.anchor.set(0.5);
            _this.container.addChild(_this.arrow);
            _this.addInteraction();
        });
    },
    loadFile: function (file) {
        var _this = this;

        // Ensure it's an image
        if (file.type.match(/image.*/)) {

            // Load the image
            var reader = new FileReader();
            reader.onload = function (readerEvent) {
                var image = new Image();
                image.onload = function (imageEvent) {

                    // Resize the image
                    var canvas = document.createElement('canvas');
                    var width = image.width;
                    var height = image.height;
                    var max_size = Math.min(CANVASSIZE.width, CANVASSIZE.height);
                    if (width > height) {
                        if (width > max_size) {
                            height *= max_size / width;
                            width = max_size;
                        }
                    } else {
                        if (height > max_size) {
                            width *= max_size / height;
                            height = max_size;
                        }
                    }

                    canvas.width = width;
                    canvas.height = height;
                    canvas.getContext('2d').drawImage(image, 0, 0, width, height);

                    _this.container.removeChild(_this.sprite);
                    _this.container.removeChild(_this.arrow);
                    _this.container.x = 0;
                    _this.container.y = 0;

                    var texture = PIXI.Texture.fromCanvas(canvas);
                    _this.sprite = new PIXI.Sprite(texture);
                    _this.sprite.x = texture.width / 2;
                    _this.sprite.y = texture.height / 2;
                    _this.sprite.anchor.set(0.5);
                    _this.container.addChild(_this.sprite);

                    texture = PIXI.Texture.fromImage('/img/move-icon.png');
                    _this.arrow = new PIXI.Sprite(texture);
                    _this.arrow.x = _this.sprite.x;
                    _this.arrow.y = _this.sprite.y;
                    _this.arrow.anchor.set(0.5);
                    _this.container.addChild(_this.arrow);
                    _this.addInteraction();

                };
                image.src = readerEvent.target.result;
            };
            reader.readAsDataURL(file);
        }
    },
    setSpeed: function (s) {
        this.speed = s;
    },
    addInteraction: function () {
        this.arrow.interactive = true;
        this.arrow.buttonMode = true;
        this.arrow
                // events for drag start
                .on('mousedown', this.onDragStart)
                .on('touchstart', this.onDragStart)
                // events for drag end
                .on('mouseup', this.onDragEnd)
                .on('mouseupoutside', this.onDragEnd)
                .on('touchend', this.onDragEnd)
                .on('touchendoutside', this.onDragEnd)
                // events for drag move
                .on('mousemove', this.onDragMove)
                .on('touchmove', this.onDragMove);
    },
    onDragStart: function (event) {
        // store a reference to the data
        // the reason for this is because of multitouch
        // we want to track the movement of this particular touch
        this.data = event.data;
        this.oldPos = this.data.getLocalPosition(this.parent);

        this.alpha = 0.5;
        this.dragging = true;
    },
    onDragEnd: function () {
        this.alpha = 1;

        this.dragging = false;
        var difx = -this.oldPos.x + this.position.x;
        var dify = -this.oldPos.y + this.position.y;

        this.parent.position.x += difx;
        this.parent.position.y += dify;
        this.position.x = this.parent.getChildAt(0).x;
        this.position.y = this.parent.getChildAt(0).y;

        // set the interaction data to null
        this.data = null;
    },
    onDragMove: function () {
        if (this.dragging)
        {
            var newPosition = this.data.getLocalPosition(this.parent);
            this.position.x = newPosition.x;
            this.position.y = newPosition.y;
        }
    }
});

// ***** Color bar *-********

var ColorBar = function () {
    Renderable.call(this, 500, new PIXI.Container());
    var _this = this;
    PIXI.loader.add('/img/spec.png');
    PIXI.loader.load(function () {
        var texture = PIXI.loader.resources['/img/spec.png'].texture;
        _this.specBar = new PIXI.Sprite(texture);
        _this.specBar.x = CANVASSIZE.width - CANVASSIZE.width / 10;
        _this.specBar.height = CANVASSIZE.height;
        _this.container.addChild(_this.specBar);
    });
};
$.extend(ColorBar.prototype, Renderable.prototype, {
    onResize: function () {
        this.specBar.height = CANVASSIZE.height;
        this.specBar.x = CANVASSIZE.width - CANVASSIZE.width / 10;
    }
});


// ***** Light Stuff ********


var Light = function (number, name) {

    this.number = number;
    this.name = name;
    this.color = null;
    this.container = new PIXI.Container();

    var texture = PIXI.Texture.fromImage('/img/light.png');
    this.sprite = new PIXI.Sprite(texture);
    this.sprite.scale.x = 0.5;
    this.sprite.scale.y = 0.5;
    this.container.addChild(this.sprite);

    this.text = new PIXI.Text('' + number, {font: '14px Arial', fill: 'red', align: 'left'});
    this.text.anchor.set(0.5);
    this.text.position.x = 15;
    this.text.position.y = 8;
    this.container.addChild(this.text);

    this.container.position.x = 200;
    this.container.position.y = 150;

    this.addInteraction();

};

Light.prototype = {
    addInteraction: function () {
        this.container.interactive = true;
        this.container.buttonMode = true;
        this.container
                // events for drag start
                .on('mousedown', this.onDragStart)
                .on('touchstart', this.onDragStart)
                // events for drag end
                .on('mouseup', this.onDragEnd)
                .on('mouseupoutside', this.onDragEnd)
                .on('touchend', this.onDragEnd)
                .on('touchendoutside', this.onDragEnd)
                // events for drag move
                .on('mousemove', this.onDragMove)
                .on('touchmove', this.onDragMove);
    },
    onDragStart: function (event) {
        // store a reference to the data
        // the reason for this is because of multitouch
        // we want to track the movement of this particular touch
        this.data = event.data;
        this.alpha = 0.5;
        this.dragging = true;
    },
    onDragEnd: function () {
        this.alpha = 1;

        this.dragging = false;

        // set the interaction data to null
        this.data = null;
    },
    onDragMove: function () {
        if (this.dragging)
        {
            var newPosition = this.data.getLocalPosition(this.parent);
            this.position.x = newPosition.x - this.width / 2;
            this.position.y = newPosition.y - this.height / 4;
        }
    },
    getColor: function () {
        var buf = new Uint8Array(4);
        RENDERER.gl.readPixels(
                this.container.position.x + this.container.width / 2,
                RENDERER.height - this.container.position.y - this.container.height + 5,
                1, 1, RENDERER.gl.RGBA, RENDERER.gl.UNSIGNED_BYTE, buf);
        var newcol = new LampColor(buf);

        if (SHIFTSPEED === 1 || this.color === null) {
            this.color = newcol;

        } else {
            this.color.r = Math.round(this.color.r * (1 - SHIFTSPEED) + newcol.r * SHIFTSPEED);
            this.color.g = Math.round(this.color.g * (1 - SHIFTSPEED) + newcol.g * SHIFTSPEED);
            this.color.b = Math.round(this.color.b * (1 - SHIFTSPEED) + newcol.b * SHIFTSPEED);
        }
        return this.color;

    }
};

// ******** Light Cont *******

var LightCont = function () {
    Renderable.call(this, 1000, new PIXI.Container());
    this.lightMap = new HashMap();
    this.registerActions();
};
$.extend(LightCont.prototype, Renderable.prototype, {
    onResize: function () {

    },
    animate: function () {
        /*this.lightMap.forEach(function (value, key) {
         value.animate();
         });*/
    },
    registerActions: function () {
        var _this = this;
        $('#lightAdd').click(function () {
            var id = parseInt($('#lightList').val());
            var name = $("#lightList option:selected").text();
            if (isNaN(id) || _this.lightMap.has(id)) {
                // ignore
                return;
            }
            var l = new Light(id, name);
            _this.container.addChild(l.container);
            _this.lightMap.set(id, l);
        });
        $('#lightRem').click(function () {
            var id = parseInt($('#lightList').val());
            if (_this.lightMap.has(id)) {
                _this.container.removeChild(_this.lightMap.get(id).container);
                _this.lightMap.get(id).remove();
                _this.lightMap.remove(id);

            }
        });
    },
    update: function (comms) {
        var toSend = [];

        this.lightMap.forEach(function (value, key) {
            var color = value.getColor();
            $('#currLights').find('.lightState[data-id="' + key + '"]').css("background-color", color.getRGBA());
            toSend.push({
                id: key,
                r: color.r,
                g: color.g,
                b: color.b
            });
        });

        comms.send({
            type: 'LIGHTSTATE',
            states: toSend
        });
    }
});
// ********* Comms ************


var Comms = function () {
    this.updateSpeed = 1;
    this.ws = new WebSocket(SERVERWS);
    this.key = $('#imageScreen').attr("data-key");
    this.ws.onmessage = this.onMessage;
    this.updateMap = new HashMap();

    var _this = this;
    $('#updateSlider').change(function () {
        _this.updateSpeed = parseFloat($(this).val());
    });

    $('#controlReset').click(function () {
        var def = parseFloat($('#updateSlider').attr('data-slider-value'));
        $('#updateSlider').slider('setValue', def);
        _this.updateSpeed = def;
    });
    $('#kickButt').click(function () {
        _this.sendKick();
    });
    $('#streamButt').click(function () {
        _this.sendStream();
    });
    $('#otherButt').click(function () {
        var key = $('#userList').val();
        if (key !== undefined && key.length >= 1) {
            _this.sendOtherStream(key[0]);
        }
    });
};
Comms.prototype = {
    start: function () {
        this.update();
        window.setTimeout(this.start.bind(this), this.updateSpeed * 1000);
    },
    update: function () {
        if (this.ws.readyState !== this.ws.OPEN) {
            return;
        }
        var _this = this;
        this.updateMap.forEach(function (value, key) {
            value.update(_this);
        });
    },
    send: function (obj) {
        if (this.ws.readyState !== this.ws.OPEN) {
            return;
        }
        if (obj.key === undefined) {
            obj.key = this.key;
        }
        this.ws.send(JSON.stringify(obj));
    },
    onMessage: function (event) {
        var msg = JSON.parse(event.data);
        if (msg.type === 'LIGHTSTATE') {
            // update display
            for (var i in msg.states) {
                var l = msg.states[i];
                $('#activeLights').find('.lightState[data-id="' + l.id + '"]').css("background-color", new LampColor(l.r, l.g, l.b).getRGBA());
            }
            if (msg.name !== undefined) {
                $('#curUser').val(msg.name);
            } else {
                $('#curUser').val('');
            }
        }

    },
    sendKick: function () {
        this.send({
            type: 'COMMAND',
            command: 'KICK'
        });
    },
    sendStream: function () {
        this.send({
            type: 'COMMAND',
            command: 'STREAM'
        });
    },
    sendOtherStream: function (k) {
        this.send({
            type: 'COMMAND',
            command: 'OTHERSTREAM',
            username: k
        });
    },
    addUpdateObj: function (obj) {
        this.updateMap.set(obj.getZIndex(), obj);
    },
    removeUpdateObj: function (obj) {
        this.updateMap.remove(obj.getZIndex());
    }
};
















