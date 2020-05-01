var exec = require('cordova/exec');

exports.onDeviceTextRecognizer = function (image, success, error) {
    exec(success, error,'FirebaseVisionPlugin', 'onDeviceTextRecognizer', [image])
}
