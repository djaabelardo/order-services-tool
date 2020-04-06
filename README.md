# Order Services Tool

## Notes

1. Please make sure to have the latest version of Java 8 installed (at least 1.8.0_241).
2. Docker should be installed in your machine.
3. Default port `8080`.
4. Use Powershell or Git Bash when running the application in Windows. `start.sh` is provided at the root folder of the project.

## Google API (Distance Matrix)

1. Distance Matrix URL: https://maps.googleapis.com/maps/api/distancematrix/
2. Sample Request:
```
    - https://maps.googleapis.com/maps/api/distancematrix/json?origins=14.554729,121.024445&key=AIzaSyB1kvAJG6g3PHdlgjk7fl8tLl1YZL1p7Ww&destinations=13.385680,123.678818 
```
3. Sample Response:
```
{
   "destination_addresses": [
      "Unnamed Road, Malinao, Albay, Philippines"
   ],
   "origin_addresses": [
      "Ayala Ave, Makati, Metro Manila, Philippines"
   ],
   "rows": [
      {
         "elements": [
            {
               "distance": {
                  "text": "472 km",
                  "value": 471916
               },
               "duration": {
                  "text": "10 hours 48 mins",
                  "value": 38871
               },
               "status": "OK"
            }
         ]
      }
   ],
   "status": "OK"
}
```
## REST APIs

1.  POST 
 - Sample Request:
```
{
    "origin": ["14.554729", "121.024445"],
    "destination": ["13.385680", "123.678818"]
}
```
 - Sample Response:

```
{
    "id": "46ed7d69-0070-4c73-bb2e-a76ec255af2e",
    "distance": 471916,
    "status": "UNASSIGNED"
}
```

2. PATCH
  - Sample Request:
```
http://localhost:8080/orders/46ed7d69-0070-4c73-bb2e-a76ec255af2e

Request Body
{
    "status": "TAKEN"
}

```
  - Sample Response:
```
{
    "status": "SUCCESS"
}
```

3. GET
  - Sample Request:
```
http://localhost:8080/orders?page=1&limit=4
```
  - Sample Response:
```
[
    {
        "id": "46ed7d69-0070-4c73-bb2e-a76ec255af2e",
        "distance": 471916,
        "status": "TAKEN"
    },
    {
        "id": "8529e3f7-04b2-4ca6-a866-17c35212dc84",
        "distance": 471916,
        "status": "UNASSIGNED"
    }
]
```

