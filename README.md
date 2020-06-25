cordova-plugin-firebase-vision
========================

Cordova plugin for Firebase MLKit Vision

<a href="https://www.buymeacoffee.com/alon22" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" style="height: 51px !important;width: 217px !important;" ></a>

# Installation
Run:
```
cordova plugin add cordova-plugin-firebase-mlvision --save
```

# Setup
Place your Firebase configuration files, GoogleService-Info.plist for iOS and google-services.json for android, in the root folder of your cordova project.

```
- My Project/
    platforms/
    plugins/
    www/
    config.xml
    google-services.json       <--
    GoogleService-Info.plist   <--
    ...
```

Add this lines in your config.xml
```xml
    <platform name="android">
        ...
        <resource-file src="google-services.json" target="app/google-services.json" />
    </platform>
    <platform name="ios">
        ...
        <resource-file src="GoogleService-Info.plist" />
    </platform>
```

# Usage
## Text recognition

```js
FirebaseVisionPlugin.onDeviceTextRecognizer(FILE_URI,
    (text) => {
        console.log(text);
    },
    (error) => {
        console.error(error);;
    })
})
```

## Barcode detector
```js
FirebaseVisionPlugin.barcodeDetector(FILE_URI,
    (json) => {
        console.log(json);
    },
    (error) => {
        console.error(error);;
    })
})
```

# Support
|   |Android|iOS|
|---|---|---|
|Text recognition|X|X|
|Face detection| | |
|Barcode scanning|X|X|
|Image labeling| | |
|Object detection & tracking| | |
|Landmark recognition| | |
