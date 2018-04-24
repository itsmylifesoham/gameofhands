define(function (require) {

    var errors = require('app/errors');

    function timeoutPromise(ms, promise) {
        return new Promise((resolve, reject) => {
            const timeoutId = setTimeout(() => {
                reject(new errors.RequestTimeoutError())
            }, ms);
            promise.then(
                (res) => {
                    clearTimeout(timeoutId);
                    resolve(res);
                },
                (err) => {
                    clearTimeout(timeoutId);
                    reject(err);
                }
            );
        })
    };


    function HashSet(valueArray){
        for(var i=0; i < valueArray.length;i++){
            this[valueArray[i]] = 1;
        }
    }

    HashSet.prototype.contains = function(value){
        return !_.isUndefined(value);
    }
    HashSet.prototype.add = function(value){
        this[value] = 1;
    }

    HashSet.prototype.remove = function(value){
        delete this[value];
    }

    HashSet.prototype.constructor = HashSet;


    return {
        timeoutPromise: timeoutPromise,
        HashSet: HashSet,
    }
});