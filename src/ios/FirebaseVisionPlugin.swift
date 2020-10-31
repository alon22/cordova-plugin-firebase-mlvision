import MLKitTextRecognition
import MLKitBarcodeScanning
import MLKitImageLabeling
import MLKitVision
import UIKit

@objc(FirebaseVisionPlugin)
class FirebaseVisionPlugin: CDVPlugin {

    @objc(onDeviceTextRecognizer:)
    func onDeviceTextRecognizer(command: CDVInvokedUrlCommand) {
        guard let imageURL = command.arguments.first as? String else {
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Image URL required")
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            return
        }
        getImage(imageURL: imageURL) { (image, error) in
            if let error = error {
                let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            } else {
                let textRecognizer = TextRecognizer.textRecognizer()
                let visionImage = VisionImage(image: image!)
                textRecognizer.process(visionImage) { (text, error) in
                    if let error = error {
                        let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
                        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                    } else {
                        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: text?.toJSON())
                        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                    }
                }
            }
        }
    }

    @objc(barcodeDetector:)
    func barcodeDetector(command: CDVInvokedUrlCommand) {
        guard let imageURL = command.arguments.first as? String else {
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Image URL required")
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            return
        }
        getImage(imageURL: imageURL) { (image, error) in
            if let error = error {
                let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            } else {
                let barcodeDetector = BarcodeScanner.barcodeScanner()
                let visionImage = VisionImage(image: image!)
                barcodeDetector.process(visionImage) { (barcodes, error) in
                    if let error = error {
                        let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
                        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                    } else {
                        let barcodesDict = barcodes?.compactMap({ $0.toJSON() })
                        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: barcodesDict)
                        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                    }
                }
            }
        }
    }

    @objc(imageLabeler:)
    func imageLabeler(command: CDVInvokedUrlCommand) {
        guard let imageURL = command.arguments.first as? String else {
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Image URL required")
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            return
        }
        getImage(imageURL: imageURL) { (image, error) in
            if let error = error {
                let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            } else {
                let options = ImageLabelerOptions()
                options.confidenceThreshold = 0.7
                let imageLabeler = ImageLabeler.imageLabeler(options: options)
                let visionImage = VisionImage(image: image!)
                imageLabeler.process(visionImage) { (labels, error) in
                    if let error = error {
                        let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
                        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                    } else {
                        let labelsDict = labels?.compactMap({$0.toJSON()})
                        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: labelsDict)
                        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                    }
                }
            }
        }
    }

    private func getImage(imageURL: String, _ completion: @escaping (_ image: UIImage?, _ error: Error?) -> Void) {
        guard let url = URL(string: imageURL) else {
            let error = NSError(domain: "cordova-plugin-firebase-mlvision",
                                code: -1,
                                userInfo: [NSLocalizedDescriptionKey : "URLImageError"])
            completion(nil, error)
            return
        }
        URLSession.shared.dataTask(with: url) { (data, response, error) in
            if let error = error {
                completion(nil, error)
            } else {
                guard let data = data, let image = UIImage(data: data) else {
                    let error = NSError(domain: "cordova-plugin-firebase-mlvision",
                                        code: -1,
                                        userInfo: [NSLocalizedDescriptionKey : "DownloadImageError"])
                    completion(nil, error)
                    return
                }
                completion(image, nil)
            }
        }
        .resume()
    }
}
