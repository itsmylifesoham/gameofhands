define(function (require) {
    var globals = require('app/globals');
    var _connectionCheckInterval = 10000;
    var internet = _.extend({}, Backbone.Events);
    var errors = require('app/errors');

    document.addEventListener("offline", function () {
        internet.trigger("offline");
    }, false);
    document.addEventListener("online", function () {
        internet.trigger("online");
    }, false);

    internet.connected = function () {
        if (cordova.platformId !== "browser"){
            if (navigator.connection.type != Connection.NONE){
                internet.trigger("online");
                return Promise.resolve("connected!");
            }
            else
            {
                internet.trigger("offline");
                return Promise.reject(new errors.AppError(errors.errorTypes.INTERNET_DISCONNECTED));
            }
        }

        // for browser we need to make an xhr
        return new Promise(function (resolve, reject) {

            var xhr = createConnectionCheckingXHR();
            xhr.addEventListener("readystatechange", processRequest, false);
            xhr.send();

            function processRequest(e) {
                if (xhr.readyState == 4) {
                    if (xhr.status >= 200 && xhr.status < 304) {
                        internet.trigger("online");
                        resolve("connection exists!");
                    } else {
                        internet.trigger("offline");
                        reject(new errors.AppError(errors.errorTypes.INTERNET_DISCONNECTED));
                    }
                    xhr.removeEventListener("readystatechange", processRequest);
                }
            }
        });
    }

    function createConnectionCheckingXHR() {
        var xhr = new XMLHttpRequest();
        var file = globals.websiteEndPoint + "Content/connection-pixel.png";
        var randomNum = Math.round(Math.random() * 10000);
        xhr.open('HEAD', file + "?rand=" + randomNum, true);

        return xhr;
    }

    function checkConnectionAtInterval() {
        function sendXHR() {
            var xhr = createConnectionCheckingXHR();
            xhr.addEventListener("readystatechange", processRequest, false);
            xhr.send();

            function processRequest(e) {
                if (xhr.readyState == 4) {
                    if (xhr.status >= 200 && xhr.status < 304) {
                        internet.trigger("online");
                    } else {
                        internet.trigger("offline");
                    }
                    xhr.removeEventListener("readystatechange", processRequest);
                }
            }

            // and schedule a repeat
            setTimeout(sendXHR, _connectionCheckInterval);
        }

        sendXHR();
    }

    // enable periodic checking for browser
    if (cordova.platformId === "browser")
        checkConnectionAtInterval();

    return internet;


});