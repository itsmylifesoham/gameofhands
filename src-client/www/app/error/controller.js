define(function (require) {

        var globals = require('app/globals');
        var ErrorRetryView = require('app/error/views/error-retry');
        var ErrorOkView = require('app/error/views/error-ok');
        var ErrorUnknownView = require('app/error/views/error-unknown');
        var errors = require('app/errors');
        var AppController = require('app/common/app-controller');
        var remote = require('app/remote');

        var _errorController = new AppController('error');

        function _displayErrorRetryView(appError) {
            globals.app.$el.append(new ErrorRetryView(appError).render().el);
        }

        function _displayErrorOkView(appError, okAction) {
            globals.app.$el.append(new ErrorOkView(appError, okAction).render().el);
        }

        function _displayErrorUnknownView() {
            globals.app.$el.append(new ErrorUnknownView().render().el);
        }


        _errorController.displayErrorView = function (appError) {

            // return if an error is already being displayed ignore any new errors as we may be waiting for user action
            if (globals.app.error)
                return;

            globals.app.error = true;

            if (appError.errorType === errors.errorTypes.INTERNET_DISCONNECTED
                || appError.errorType === errors.errorTypes.SFS_CONNECTION_ERROR
                || appError.errorType === errors.errorTypes.REQUEST_TIMEOUT_ERROR)
                _displayErrorRetryView(appError);
            else if (appError.errorType === errors.errorTypes.FACEBOOK_LOGIN_ERROR
                || appError.errorType === errors.errorTypes.WEBSITE_LOGIN_ERROR
                || appError.errorType === errors.errorTypes.SFS_LOGIN_ERROR) {
                _displayErrorOkView(appError, function () {
                    remote.invokeControllerMethod('login', 'displayLoginView');
                });
            }
            else {
                _displayErrorUnknownView();
            }


        }

        return _errorController;
    });