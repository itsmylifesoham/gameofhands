'use strict';

require.config({
    baseUrl: 'lib',
    paths: {
        app: '../app',
    },
    shim: {}
});

/* obtained from https://stackoverflow.com/questions/11581611/load-files-in-specific-order-with-requirejs */
var requireQueue = function (modules, callback) {
    function load(queue, results) {
        if (queue.length) {
            require([queue.shift()], function (result) {
                results.push(result);
                load(queue, results);
            });
        } else {
            callback.apply(null, results);
        }
    }

    load(modules, []);
};

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var phonegapApp = {
    initialize: function () {
        this.bindEvents();
    },
    bindEvents: function () {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    onDeviceReady: function () {

        // hide status bar if its not a browser
        if (cordova.platformId !== "browser")
            StatusBar.hide();

        // init app
        require(['app/main', 'app/globals'], function (AppView, globals) {
            var app = new AppView('#app');
            globals.app = app;
            app.start();
        });
    },


};


phonegapApp.initialize();

