import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';

class NativeContact {
    static const MethodChannel _channel = const MethodChannel('native_contact');

    /// Adds the [contact] to the device contact list
    static Future addNewContact(ContactInfo contact) =>
        _channel.invokeMethod('addNewContact', ContactInfo._toMap(contact));
}

class ContactInfo {

  String name;
  String company;
  String jobTitle;
  String website;
  List<PostalAddress> postalAddresses;
  List<Item> emails;
  List<Item> phones;
  Uint8List avatar;

  ContactInfo({
      this.name,
      this.company,
      this.jobTitle,
      this.website,
      this.postalAddresses,
      this.emails,
      this.phones,
      this.avatar
  });

  ContactInfo.fromMap(Map map) {
      name = map["name"];
      company = map["company"];
      jobTitle = map["jobTitle"];
      website = map["website"];
      postalAddresses = (map["postalAddresses"] as Iterable) ?.map((m) => PostalAddress.fromMap(m));
      emails = (map["emails"] as Iterable)?.map((m) => Item.fromMap(m));
      phones = (map["phones"] as Iterable)?.map((m) => Item.fromMap(m));
      avatar = map["avatar"];
  }

  static _toMap(ContactInfo contact) {
      var emails = [];
      for (Item email in contact.emails ?? []) {
          emails.add(Item._toMap(email));
      }

      var phones = [];
      for (Item phone in contact.phones ?? []) {
          phones.add(Item._toMap(phone));
      }

      var postalAddresses = [];
      for (PostalAddress address in contact.postalAddresses ?? []) {
          postalAddresses.add(PostalAddress._toMap(address));
      }

      return {
          "name": contact.name,
          "company": contact.company,
          "jobTitle": contact.jobTitle,
          "website": contact.website,
          "emails": emails,
          "phones": phones,
          "postalAddresses": postalAddresses,
          "avatar": contact.avatar
      };
  }

}

class PostalAddress {
    String label;
    String street;
    String city;
    String postcode;
    String region;
    String country;
    String neighborhood;

    PostalAddress({
        this.label,
        this.street,
        this.city,
        this.postcode,
        this.region,
        this.country,
        this.neighborhood
    });

    PostalAddress.fromMap(Map map) {
        label = map["label"];
        street = map["street"];
        city = map["city"];
        postcode = map["postcode"];
        region = map["region"];
        country = map["country"];
        neighborhood = map["neighborhood"];
    }

    static _toMap(PostalAddress postalAddress) => {
        "label": postalAddress.label,
        "street": postalAddress.street,
        "city": postalAddress.city,
        "postcode": postalAddress.postcode,
        "region": postalAddress.region,
        "country": postalAddress.country,
        "neighborhood": postalAddress.neighborhood
    };

}

class Item {

    int label;
    String value;

    Item(this.label, this.value);

    Item.fromMap(Map map) {
        label = map["label"];
        value = map["value"];
    }

    static _toMap(Item item) => {"label": item.label, "value": item.value};

}