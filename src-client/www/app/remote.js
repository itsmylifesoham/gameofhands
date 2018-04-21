define(function(require){
    function invokeControllerMethod (controllerModuleName, methodName) {

        var args = Array(arguments.length);
        args[0] = 'methodInvoke';

        for (var key=1; key < args.length; key++) {
            args[key] = arguments[key];
        }
        var controllerChannel = Backbone.Radio.channel('app/' + controllerModuleName + '/controller');
        return controllerChannel.request.apply(controllerChannel, args);
    }

    return {
        invokeControllerMethod: invokeControllerMethod,
    }

});