import FirebaseMLVision

extension VisionBarcode {
    func toJSON() -> [AnyHashable: Any] {
        var response = [
            "valueType": valueType.rawValue,
            "format": format.rawValue,
            "rawValue" : rawValue as Any,
            "displayValue" : displayValue as Any,
            "cornerPoints" : cornerPoints?.compactMap({
                [
                    "x" : ($0 as! CGPoint).x,
                    "y" : ($0 as! CGPoint).y
                ]
            }) as Any,
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
                "start": calendarEvent.start as Any,
                "end": calendarEvent.end as Any
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
