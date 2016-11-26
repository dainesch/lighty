<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<t:siteTemplate>
    <jsp:body>
        <div id="imageScreen" class="row" data-key="${userMod.key}">
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
                    <div class="row">
                        <div class="col-xs-4 text-xs-right">
                            Amplitude
                        </div>
                        <div class="col-xs-8">
                            <input class=""
                                   type="text"
                                   id="ampSlider"
                                   name="amp"
                                   data-provide="slider"
                                   data-slider-min="1"
                                   data-slider-max="4"
                                   data-slider-step="0.2"
                                   data-slider-value="2"
                                   data-slider-tooltip="hide"
                                   >
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 text-xs-right">
                            Compression
                        </div>
                        <div class="col-xs-8">
                            <input class=""
                                   type="text"
                                   id="compSlider"
                                   name="comp"
                                   data-provide="slider"
                                   data-slider-min="1"
                                   data-slider-max="4"
                                   data-slider-step="0.2"
                                   data-slider-value="1"
                                   data-slider-tooltip="hide"
                                   >
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 text-xs-right">
                            Color shift
                        </div>
                        <div class="col-xs-8">
                            <input class=""
                                   type="text"
                                   id="shiftSlider"
                                   name="shift"
                                   data-provide="slider"
                                   data-slider-min="0"
                                   data-slider-max="1"
                                   data-slider-step="0.05"
                                   data-slider-value="0"
                                   data-slider-tooltip="hide"
                                   >
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 text-xs-right">
                            Bar count
                        </div>
                        <div class="col-xs-8">
                            <input class=""
                                   type="text"
                                   id="barSlider"
                                   name="bar"
                                   data-provide="slider"
                                   data-slider-min="1"
                                   data-slider-max="200"
                                   data-slider-step="1"
                                   data-slider-value="80"
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
            </div>
            <div id="canvasCont" class="col-xs-8">

            </div>
        </div>
                <%@include file="statusControl.jspf" %>
    </jsp:body>
</t:siteTemplate>


