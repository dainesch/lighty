<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<t:siteTemplate>
    <jsp:body>
        <div id="imageScreen" class="row" data-key="<c:out value="${userMod.key}"/>">
            <div class="col-xs-4">
                <!-- Controls -->
                <div class="card card-block text-xs-center">
                    <h5 class="card-title">Controls</h5>
                    <div class="row">
                        <div class="col-xs-4 text-xs-right">
                            Update speed
                        </div>
                        <div class="col-xs-8">
                            <input class=""
                                   type="text"
                                   id="updateSlider"
                                   name="update"
                                   data-provide="slider"
                                   data-slider-min="0.2"
                                   data-slider-max="10"
                                   data-slider-step="0.2"
                                   data-slider-value="1"
                                   >
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 text-xs-right">
                            Shift speed
                        </div>
                        <div class="col-xs-8">
                            <input class=""
                                   type="text"
                                   id="shiftSlider"
                                   name="shift"
                                   data-provide="slider"
                                   data-slider-min="0.1"
                                   data-slider-max="1"
                                   data-slider-step="0.1"
                                   data-slider-value="0.8"
                                   data-slider-tooltip="hide"
                                   >
                        </div>
                    </div>
                    <div class="row spacerV10">
                        <label for="transColor" class="col-xs-4 text-xs-right">Base color</label>
                        <div class="col-xs-4 offset-xs-2">
                            <input class="form-control" type="color" value="#00ff00" id="transColor">
                        </div>
                    </div>
                    <div class="row spacerV20">
                        <div class="col-xs-4 offset-xs-4">
                            <button id="controlReset" type="button" class="btn btn-secondary btn-block">Reset</button>
                        </div>
                    </div>
                </div>
                <%@include file="lightPanel.jspf" %>
                <div class="card card-block  text-xs-center">
                    <h5 class="card-title">Code</h5>
                    <div class="row">
                        <div class="col-xs-6">
                            <button id="runButt" type="button" class="btn btn-primary btn-block">
                                Run code
                            </button>
                        </div>
                        <div class="col-xs-6">
                            <a class="btn btn-secondary btn-block" href="http://pixijs.github.io/docs/PIXI.Graphics.html" target="_blank">
                                PIXI Docs
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <div id="canvasCont" class="col-xs-8">

            </div>
        </div>
        <div class="row spacerV20">
            <div class="col-xs-12">
                <div class="card card-block">
                    <div id="scriptContent"></div>
                    <script type="template" id="template">
/**
* Custom layer
**/
var Custom = function () {
    Renderable.call(this, 100, new PIXI.Container()); // this, zIndex (and key), primary container
    this.graphics = new PIXI.Graphics(); // create manual graphics
    this.container.addChild(this.graphics); // add to this layer
    this.graphics.x=CANVASSIZE.width/2; // center
    this.graphics.y=CANVASSIZE.height/2;
};
$.extend(Custom.prototype, Renderable.prototype, {
    animate: function () {
        // draw shape
        this.graphics.beginFill(0xff1111);
		this.graphics.drawRoundedRect(0, 0, 20, 100, 10);
        this.graphics.endFill();
        // rotate graphics obj
        this.graphics.rotation += 0.01;
    },
    onResize: function () {
		// called if container changes size
    },
    update: function (comms) {
        // manualy send commands
    }
});

var custom = new Custom();
lighty.addRenderable(custom); // render graphics
comms.addUpdateObj(custom);  // register to send commands
                    </script>
                </div>
            </div>
        </div>
        <%@include file="statusControl.jspf" %>
    </jsp:body>
</t:siteTemplate>


