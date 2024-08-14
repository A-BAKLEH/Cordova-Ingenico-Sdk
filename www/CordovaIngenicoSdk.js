var exec = require('cordova/exec');

exports.printHelloWorld = function(base64Input, success, error) {
    exec(success, error, 'CordovaIngenicoSdk', 'printHelloWorld', [base64Input]);
};
