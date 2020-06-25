package by.alon22.cordova.firebase;

import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;


public class FirebaseUtils {
    public static JSONObject parseText(FirebaseVisionText text) throws Exception {
        JSONArray blocks = new JSONArray();
        for (FirebaseVisionText.TextBlock textBlock : text.getTextBlocks()) {
            JSONArray lines = new JSONArray();
            for (FirebaseVisionText.Line line : textBlock.getLines()) {
                JSONArray elements = new JSONArray();
                for (FirebaseVisionText.Element element : line.getElements()) {
                    JSONObject elementObject = new JSONObject();
                    elementObject.put("cornerPoints", parsePoints(element.getCornerPoints()));
                    elementObject.put("text", element.getText());
                    elementObject.put("frame", parseBoundingBox(element.getBoundingBox()));
                    elementObject.put("recognizedLanguages", element.getRecognizedLanguages());
                    elements.put(elementObject);
                }
                JSONObject lineObject = new JSONObject();
                lineObject.put("cornerPoints", parsePoints(line.getCornerPoints()));
                lineObject.put("text", line.getText());
                lineObject.put("frame", parseBoundingBox(line.getBoundingBox()));
                lineObject.put("recognizedLanguages", line.getRecognizedLanguages());
                lineObject.put("elements", elements);
                lines.put(lineObject);
            }
            JSONObject block = new JSONObject();
            block.put("cornerPoints", parsePoints(textBlock.getCornerPoints()));
            block.put("text", textBlock.getText());
            block.put("frame", parseBoundingBox(textBlock.getBoundingBox()));
            block.put("recognizedLanguages", textBlock.getRecognizedLanguages());
            block.put("lines", lines);
            blocks.put(block);
        }

        JSONObject result = new JSONObject();
        result.put("text", text.getText());
        result.put("blocks", blocks);

        return result;
    }

    public static JSONArray parseBarcode(List<FirebaseVisionBarcode> barcodes) throws Exception {
        JSONArray array = new JSONArray();
        for (FirebaseVisionBarcode barcode : barcodes) {

            JSONObject barcodeMap = new JSONObject();
            barcodeMap.put("valueType", barcode.getValueType());
            barcodeMap.put("format", barcode.getFormat());
            barcodeMap.put("rawValue", barcode.getRawValue());
            barcodeMap.put("displayValue", barcode.getDisplayValue());
            barcodeMap.put("cornerPoints", parsePoints(barcode.getCornerPoints()));
            barcodeMap.put("boundingBox",barcode.getBoundingBox());


            if (barcode.getEmail() != null) {
                JSONObject email = new JSONObject();
                email.put("address", barcode.getEmail().getAddress());
                email.put("body", barcode.getEmail().getBody());
                email.put("subject", barcode.getEmail().getSubject());
                email.put("type", barcode.getEmail().getType());
                barcodeMap.put("email", email);
            }

            if (barcode.getPhone() != null) {
                JSONObject phone = new JSONObject();
                phone.put("number", barcode.getPhone().getNumber());
                phone.put("type", barcode.getPhone().getType());
                barcodeMap.put("phone", phone);
            }

            if (barcode.getSms() != null) {
                JSONObject sms = new JSONObject();
                sms.put("phoneNumber", barcode.getSms().getPhoneNumber());
                sms.put("message", barcode.getSms().getMessage());
                barcodeMap.put("sms", sms);
            }

            if (barcode.getUrl() != null) {
                JSONObject url = new JSONObject();
                url.put("title", barcode.getUrl().getTitle());
                url.put("url", barcode.getUrl().getUrl());
                barcodeMap.put("url", url);
            }

            if (barcode.getWifi() != null) {
                JSONObject wifi = new JSONObject();
                wifi.put("ssid", barcode.getWifi().getSsid());
                wifi.put("password", barcode.getWifi().getPassword());
                wifi.put("type", barcode.getWifi().getEncryptionType());
                barcodeMap.put("wifi", wifi);
            }

            if (barcode.getGeoPoint() != null) {
                JSONObject geoPoint = new JSONObject();
                geoPoint.put("latitude", barcode.getGeoPoint().getLat());
                geoPoint.put("longitude", barcode.getGeoPoint().getLng());
                barcodeMap.put("geoPoint", geoPoint);
            }

            if (barcode.getCalendarEvent() != null) {
                JSONObject calendarEvent = new JSONObject();
                calendarEvent.put("eventDescription", barcode.getCalendarEvent().getDescription());
                calendarEvent.put("location", barcode.getCalendarEvent().getLocation());
                calendarEvent.put("organizer", barcode.getCalendarEvent().getOrganizer());
                calendarEvent.put("status", barcode.getCalendarEvent().getStatus());
                calendarEvent.put("summary", barcode.getCalendarEvent().getSummary());
                calendarEvent.put("start", toISOString(barcode.getCalendarEvent().getStart()));
                calendarEvent.put("end", toISOString(barcode.getCalendarEvent().getEnd()));
                barcodeMap.put("calendarEvent", calendarEvent);
            }

            if (barcode.getContactInfo() != null) {
                JSONObject contactInfo = new JSONObject();
                contactInfo.put("title", barcode.getContactInfo().getTitle());
                contactInfo.put("name", barcode.getContactInfo().getName().getFormattedName());
                contactInfo.put("addresses", parseAddresses(barcode.getContactInfo().getAddresses()));
                contactInfo.put("phones", parsePhones(barcode.getContactInfo().getPhones()));
                contactInfo.put("emails", parseEmails(barcode.getContactInfo().getEmails()));
                contactInfo.put("organization", barcode.getContactInfo().getOrganization());
                contactInfo.put("urls", TextUtils.join(",", barcode.getContactInfo().getUrls()));
                barcodeMap.put("contactInfo", contactInfo);
            }

            if (barcode.getDriverLicense() != null) {
                JSONObject driverLicense = new JSONObject();
                driverLicense.put("firstName", barcode.getDriverLicense().getFirstName());
                driverLicense.put("middleName", barcode.getDriverLicense().getMiddleName());
                driverLicense.put("lastName", barcode.getDriverLicense().getLastName());
                driverLicense.put("gender", barcode.getDriverLicense().getGender());
                driverLicense.put("addressCity", barcode.getDriverLicense().getAddressCity());
                driverLicense.put("addressState", barcode.getDriverLicense().getAddressState());
                driverLicense.put("addressStreet", barcode.getDriverLicense().getAddressStreet());
                driverLicense.put("addressZip", barcode.getDriverLicense().getAddressZip());
                driverLicense.put("birthDate", barcode.getDriverLicense().getBirthDate());
                driverLicense.put("documentType", barcode.getDriverLicense().getDocumentType());
                driverLicense.put("licenseNumber", barcode.getDriverLicense().getLicenseNumber());
                driverLicense.put("expiryDate", barcode.getDriverLicense().getExpiryDate());
                driverLicense.put("issuingDate", barcode.getDriverLicense().getIssueDate());
                driverLicense.put("issuingCountry", barcode.getDriverLicense().getIssuingCountry());
                barcodeMap.put("driverLicense", driverLicense);
            }

            array.put(barcodeMap);
        }
        return array;
    }

    private static JSONArray parsePhones(List<FirebaseVisionBarcode.Phone> phones) throws Exception {
        JSONArray array = new JSONArray();
        for (FirebaseVisionBarcode.Phone phone : phones) {

            JSONObject phoneMap = new JSONObject();
            phoneMap.put("number", phone.getNumber());
            phoneMap.put("type", phone.getType());

            array.put(phoneMap);
        }
        return array;
    }

    private static JSONArray parseAddresses(List<FirebaseVisionBarcode.Address> addresses) throws Exception {
        JSONArray array = new JSONArray();
        for (FirebaseVisionBarcode.Address address : addresses) {

            JSONObject addressMap = new JSONObject();
            addressMap.put("addressLine", TextUtils.join(",", address.getAddressLines()));
            addressMap.put("type", address.getType());

            array.put(addressMap);
        }
        return array;
    }

    private static JSONArray parseEmails(List<FirebaseVisionBarcode.Email> emails) throws Exception {
        JSONArray array = new JSONArray();
        for (FirebaseVisionBarcode.Email email : emails) {

            JSONObject emailMap = new JSONObject();
            emailMap.put("address", email.getAddress());
            emailMap.put("body", email.getBody());
            emailMap.put("subject", email.getSubject());
            emailMap.put("type", email.getType());

            array.put(emailMap);
        }
        return array;
    }

    private static JSONArray parsePoints(Point[] points) throws Exception {
        JSONArray array = new JSONArray();
        for (Point point : points) {

            JSONObject pointMap = new JSONObject();
            pointMap.put("x", point.x);
            pointMap.put("y", point.y);
            array.put(pointMap);
        }
        return array;
    }

    private static JSONObject parseBoundingBox(Rect rect) throws Exception {
        JSONObject rectMap = new JSONObject();
        rectMap.put("x", rect.left);
        rectMap.put("y", rect.top);
        rectMap.put("width", rect.width());
        rectMap.put("height", rect.height());

        return rectMap;
    }

    private static String toISOString(FirebaseVisionBarcode.CalendarDateTime date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z");
        return dateFormat.format(date);
    }
}
