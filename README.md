# native_contact

This library is forked from native_contact library. It comes with a solution to add the address in the contact (it wasn't working when the contact address wasn't structured.

A Flutter plugin to call native contacts view on Android and iOS devices.

## Usage

To use this plugin, download native_contact and add it as a dependency in your pubspec.yaml file.

## Example

``` dart
// Import package
native_contact:
    path: ./plugins/native_contact

// Add a contact
await NativeContact.addNewContact(contact);

```
