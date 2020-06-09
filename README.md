# Ktor sample with tests running forver when multipart post

When running the test `testUploadApplication` it never stops. Neither success nor fail

If in `handlePost`method the `addHeader` and `setBody` method are commented works ok.

No problem when running the application for real and doing a curl:

```
curl -F @README.md http://localhost:8080/stuck
```

Endoints:
 - `/stuck`: Running the test runs forever. Covered by `fail when multipart NOT USED` test. Just a `call.receiveMultipart()` 
 - `/stuck2`: Running the test runs forever. Coverted by `fail when multipart just readPart`. Just adding a `readPart()`
 - `/ok`: Success getting the filename. Coverd by `working when multiparts are read`. Call to `forEachPart`
 - `/ok2`: Success only forEachPart used. Coverd by `working when multiparts are just forEachPart`. Call to `forEachPart` that does nothing
 