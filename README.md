cordova-plugin-firebase-vision
===============

Cordova plugin for Firebase MLKit Vision

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

# TODO
- [X] Text recognition
- [ ] Face detection
- [ ] Barcode scanning
- [ ] Image labeling
- [ ] Object detection & tracking
- [ ] Landmark recognition
- [ ] Language identification
- [ ] Translation
- [ ] Smart Reply
- [ ] AutoML model inference
- [ ] Custom model inference
