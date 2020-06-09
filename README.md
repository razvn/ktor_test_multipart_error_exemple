# Ktor sample with tests running forver when multipart post

When running the test `testUploadApplication` it never stops. Neither success nor fail

If in `handlePost`method the `addHeader` and `setBody` method are commented works ok.

No problem when running the application for real and doing a curl:

```
curl -X POST http://localhost:8080 \
   --header 'Content-Type: multipart/form-data; boundary=---------BOUNDARY' \
   --data-binary @demo.html
```
