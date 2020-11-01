cordova-plugin-firebase-mlvision
========================

Cordova plugin for Firebase MLKit Vision

<a href="https://www.buymeacoffee.com/alon22" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" style="height: 51px !important;width: 217px !important;" ></a>

# Installation
Run:
```
cordova plugin add cordova-plugin-firebase-mlvision --save
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

## Image Labeler
```js
FirebaseVisionPlugin.imageLabeler(FILE_URI,
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
|Image labeling|X|X|
|Object detection & tracking| | |
|Landmark recognition| | |

# Know Issues
## iOS
Build iOS from command line failed in
`PhaseScriptExecution [CP] Copy Pods Resources`

Running from Xcode work correctly
