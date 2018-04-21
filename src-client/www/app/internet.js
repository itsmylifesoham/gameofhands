define(function (require) {
    var globals = require('app/globals');
    var errors = require('app/errors');
    var helpers = require('app/helpers');
    var _connectionCheckInterval = 10000;
    var _pingServerTimeout = 5000;
    var internet = _.extend({}, Backbone.Events);

    document.addEventListener("offline", function () {
        internet.trigger("offline");
    }, false);
    document.addEventListener("online", function () {
        internet.trigger("online");
    }, false);

    internet.connected = function () {
        if (cordova.platformId !== "browser") {
            if (navigator.connection.type != Connection.NONE) {
                internet.trigger("online");
                return Promise.resolve("connected!");
            }
            else {
                internet.trigger("offline");
                return Promise.reject(new errors.InternetDisconnectedError());
            }
        }

        // for browser we need to make an xhr
        return pingServer()
            .then(function () {
                internet.trigger('online');
                return Promise.resolve('connection exists!');
            })
            .catch(function () {
                internet.trigger('offline');
                return Promise.reject(new errors.InternetDisconnectedError())
            });
    }

    function createConnectionCheckingXHR() {
        var xhr = new XMLHttpRequest();
        var file = globals.websiteEndPoint + "Content/connection-pixel.png";
        var randomNum = Math.round(Math.random() * 10000);
        xhr.open('HEAD', file + "?rand=" + randomNum, true);

        return xhr;
    }

    function pingServer() {
        return helpers.timeoutPromise(_pingServerTimeout, new Promise(function (resolve, reject) {
            var xhr = createConnectionCheckingXHR();
            xhr.addEventListener("readystatechange", processRequest, false);
            xhr.send();

            function processRequest(e) {
                if (xhr.readyState == 4) {
                    if (xhr.status >= 200 && xhr.status < 304) {
                        resolve();
                    } else {
                        reject();
                    }
                    xhr.removeEventListener("readystatechange", processRequest);
                }
            }
        }));
    }

    function checkConnectionAtInterval() {

        function sendXHR() {
            pingServer()
                .then(function () {
                    internet.trigger('online');
                })
                .catch(function () {
                    internet.trigger('offline')
                });

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