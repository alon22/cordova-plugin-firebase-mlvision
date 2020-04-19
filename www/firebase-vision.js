var exec = require('cordova/exec');

exports.onDeviceRecognizeImage = function (image, success, error) {
    exec(success, error,'FirebaseVision', 'onDeviceRecognizeImage', [image])
}
