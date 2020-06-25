import FirebaseMLVision

extension VisionText {
    func toJSON() -> [AnyHashable: Any] {
        let blocksParsed = blocks.compactMap { (block) -> Any? in
            let lines = block.lines.compactMap { (line) -> Any? in
                let elements = line.elements.compactMap { (element) -> Any? in
                    return [
                        "cornerPoints": element.cornerPoints?.toJSON() as Any,
                        "text": element.text,
                        "frame": element.frame.toJSON() as Any,
                        "recognizedLanguages": element.recognizedLanguages.compactMap({$0.languageCode})
                    ]
                }
                return [
                    "cornerPoints": line.cornerPoints?.toJSON() as Any,
                    "text": line.text,
                    "frame": line.frame.toJSON() as Any,
                    "recognizedLanguages": line.recognizedLanguages.compactMap({$0.languageCode}),
                    "elements": elements
                ]
            }
            return [
                "cornerPoints": block.cornerPoints?.toJSON() as Any,
                "text": block.text,
                "frame": block.frame.toJSON() as Any,
                "recognizedLanguages": block.recognizedLanguages.compactMap({$0.languageCode}),
                "lines": lines
            ]
        }

        return [
            "text": text,
            "blocks": blocksParsed
        ]
    }
}

extension VisionBarcode {
    func toJSON() -> [AnyHashable: Any] {
        var response = [
            "valueType": valueType.rawValue,
            "format": format.rawValue,
            "rawValue" : rawValue as Any,
            "displayValue" : displayValue as Any,
            "cornerPoints" : cornerPoints?.toJSON() as Any,
        ]
        if let email = email {
            response["email"] = [
                "address" : email.address as Any,
                "body" : email.body as Any,
                "subject" : email.subject as Any,
                "type" : email.type.rawValue as Any
            ]
        }
        if let phone = phone {
            response["phone"] = [
                "number" : phone.number as Any,
                "type" : phone.type.rawValue as Any
            ]
        }
        if let sms = sms {
            response["sms"] = [
                "phoneNumber": sms.phoneNumber as Any,
                "message": sms.message as Any
            ]
        }
        if let url = url {
            response["url"] = [
                "title": url.title as Any,
                "url": url.url as Any
            ]
        }
        if let wifi = wifi {
            response["wifi"] = [
                "ssid" : wifi.ssid as Any,
                "password" : wifi.password as Any,
                "type" : wifi.type.rawValue as Any
            ]
        }
        if let geoPoint = geoPoint {
            response["geoPoint"] = [
                "latitude" : geoPoint.latitude as Any,
                "longitude" : geoPoint.longitude as Any
            ]
        }
        if let calendarEvent = calendarEvent {
            response["calendarEvent"] = [
                "eventDescription": calendarEvent.eventDescription as Any,
                "location": calendarEvent.location as Any,
                "organizer": calendarEvent.organizer as Any,
                "status": calendarEvent.status as Any,
                "summary": calendarEvent.summary as Any,
                "start": calendarEvent.start?.toISOString() as Any,
                "end": calendarEvent.end?.toISOString() as Any
            ]
        }

        if let contactInfo = contactInfo {
            response["contactInfo"] = [
                "title": contactInfo.jobTitle as Any,
                "name": contactInfo.name?.formattedName as Any,
                "addresses": contactInfo.addresses?.compactMap({ (address) -> [String: Any]? in
                    return [
                        "addressLine" : address.addressLines?.joined(separator: ",") as Any,
                        "type" : address.type.rawValue
                    ]
                }) as Any,
                "phones": contactInfo.phones?.compactMap({ (phone) -> [String: Any]? in
                    return [
                        "number": phone.number as Any,
                        "type": phone.type.rawValue
                    ]
                }) as Any,
                "emails": contactInfo.emails?.compactMap({ (email) -> [String: Any]? in
                    return [
                        "address": email.address as Any,
                        "body": email.body as Any,
                        "type": email.type.rawValue
                    ]
                }) as Any,
                "organization": contactInfo.organization as Any,
                "urls": contactInfo.urls?.joined(separator: ",") as Any
            ]
        }


        if let driverLicense = driverLicense {
            response["driverLicense"] = [
                "firstName": driverLicense.firstName as Any,
                "middleName": driverLicense.middleName as Any,
                "lastName": driverLicense.lastName as Any,
                "gender": driverLicense.gender as Any,
                "addressCity": driverLicense.addressCity as Any,
                "addressState": driverLicense.addressState as Any,
                "addressStreet": driverLicense.addressStreet as Any,
                "addressZip": driverLicense.addressZip as Any,
                "birthDate": driverLicense.birthDate as Any,
                "documentType": driverLicense.documentType as Any,
                "licenseNumber": driverLicense.licenseNumber as Any,
                "expiryDate": driverLicense.expiryDate as Any,
                "issuingDate": driverLicense.issuingDate as Any,
                "issuingCountry": driverLicense.issuingCountry as Any
            ]
        }
        return response
    }
}

extension Date {
    func toISOString() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        return formatter.string(from: self)
    }
}

extension CGRect {
    func toJSON() -> [AnyHashable: Any] {
        return [
            "x" : origin.x,
            "y" : origin.y,
            "width": size.width,
            "height": size.height
        ]
    }
}

extension Array where Element: NSValue {
    func toJSON() -> [[AnyHashable: Any]] {
        return self.compactMap({
            [
                "x" : ($0 as! CGPoint).x,
                "y" : ($0 as! CGPoint).y
            ]
        })
    }
}
