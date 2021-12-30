# Simple Java Api service

### Requirements
* Java >= 17
* PostgreSQL >= 10
* Liquibase (snap or .jar)

### Database setup

Run following command to add entities schemas to database:
```
liquibase --defaultsFile=src/main/resources/liquibase.properties update
```

