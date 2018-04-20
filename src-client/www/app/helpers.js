define(function(require){

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

    return {
        timeoutPromise: timeoutPromise
    }
});