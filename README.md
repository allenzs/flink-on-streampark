# flink-on-streampark

A minimal Flink 1.17.2 SQL job that generates data with the `datagen` connector and prints it using the `print` connector.

## What it does

It executes the following SQL:

- Create `datagen` source table
- Create `print_table` sink table
- `INSERT INTO print_table SELECT ... FROM datagen`

Entry class: `com.allen.flink.sql.DatagenToPrintSqlJob`

## Build

```bat
mvn -q -DskipTests package
```

## Run (local)

After packaging, run the shaded jar:

```bat
java -jar target\flink-on-streampark-1.0-SNAPSHOT-shaded.jar
```

You should see rows printed to stdout.

## Notes

- This job runs indefinitely (datagen is unbounded). Stop it with Ctrl+C.
- If you want higher throughput, increase `'rows-per-second'` in the SQL.

