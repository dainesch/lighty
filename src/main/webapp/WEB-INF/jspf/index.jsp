<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<t:siteTemplate>
    <jsp:body>
        <div id="imageScreen" class="row" data-key="<c:out value="${userMod.key}"/>">
            <div class="col-xs-4">
                <!-- IMAGE -->
                <div class="card card-block  text-xs-center">
                    <h5 class="card-title">Image</h5>
                    <p class="card-text form-group">
                        <input id="imgSelect" class="form-control" type="file" accept="image/*">
                    </p>
                    <div class="row">
                        <div class="col-xs-3">
                            <button id="imgWheel" type="button" class="btn btn-secondary btn-sm">
                                <img src="<c:url value="/img/wheel.png"/>" alt="wheel" class="img-fluid">
                            </button>
                        </div>
                        <div class="col-xs-3">
                            <button id="imgChart" type="button" class="btn btn-secondary btn-sm">
                                <img src="<c:url value="/img/chart.png"/>" alt="chart" class="img-fluid">
                            </button>
                        </div>
                        <div class="col-xs-3">
                            <button id="imgCust" type="button" class="btn btn-secondary btn-sm">
                                <img src="<c:url value="/img/cust.png"/>" alt="chart" class="img-fluid">
                            </button>
                        </div>
                        <div class="col-xs-3">
                            <button id="imgRad" type="button" class="btn btn-secondary btn-sm">
                                <img src="<c:url value="/img/radiation.png"/>" alt="chart" class="img-fluid">
                            </button>
                        </div>
                    </div>
                </div>
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
                                   data-slider-min="3"
                                   data-slider-max="10"
                                   data-slider-step="0.5"
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
                            Rotation
                        </div>
                        <div class="col-xs-8">
                            <input
                                type="text"
                                id="speedSlider"
                                name="speed"
                                data-provide="slider"
                                data-slider-min="0"
                                data-slider-max="0.005"
                                data-slider-step="0.00025"
                                data-slider-value="0.002"
                                data-slider-tooltip="hide"
                                >
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4 text-xs-right">
                            Scale
                        </div>
                        <div class="col-xs-8">
                            <input class=""
                                   type="text"
                                   id="scaleSlider"
                                   name="scale"
                                   data-provide="slider"
                                   data-slider-min="0"
                                   data-slider-max="4"
                                   data-slider-step="0.1"
                                   data-slider-value="1"
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


