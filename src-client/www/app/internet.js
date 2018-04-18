define(function (require) {
    var globals = require('app/globals');

    function doesConnectionExist() {

        return new Promise(function (resolve, reject) {
            var xhr = new XMLHttpRequest();
            var file = globals.websiteEndPoint + "Content/connection-pixel.png";
            var randomNum = Math.round(Math.random() * 10000);
            xhr.open('HEAD', file + "?rand=" + randomNum, true);
            xhr.addEventListener("readystatechange", processRequest, false);
            xhr.send();

            function processRequest(e) {
                if (xhr.readyState == 4) {
                    if (xhr.status >= 200 && xhr.status < 304) {
                        resolve("connection exists!");
                    } else {
                        reject("connection doesn't exist!");
                    }
                }
            }
        });

    }

    return {
        doesConnectionExist: doesConnectionExist,
    }


});