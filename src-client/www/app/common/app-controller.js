define(function(require){
    var AppController = function(moduleName){
        var controllerChanel= Backbone.Radio.channel('app/' + moduleName + '/controller');
        controllerChanel.reply('methodInvoke', function(methodName){
            if (!this[methodName] || !_.isFunction(this[methodName]))
                throw new Error("the remotely invoked methodname:" + methodName + " does not exits or is not a function");

            var args = Array(arguments.length-1);

            for (var key=1; key < arguments.length; key++) {
                args[key-1] = arguments[key];
            }

            var context = this;
            return this[methodName].apply(context, args);
        }, this);

    }

    AppController.prototype.constructor = AppController;

    return AppController;


});