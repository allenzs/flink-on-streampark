package com.allen.flink.sql;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;


public class DatagenToPrintSqlJob {

    public static void main(String[] args) {
        // Use streaming mode.
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .build();

        // TableEnvironment is enough for executing Flink SQL.
        TableEnvironment tEnv = TableEnvironment.create(settings);

        // Optional: make local runs a bit more predictable.
        tEnv.getConfig().getConfiguration().setString("parallelism.default", "1");

        String createDatagen = "CREATE TABLE datagen (\n" +
                "    f_sequence INT,\n" +
                "    f_random INT,\n" +
                "    f_random_str STRING,\n" +
                "    ts AS localtimestamp,\n" +
                "    WATERMARK FOR ts AS ts\n" +
                ") WITH (\n" +
                "    'connector' = 'datagen',\n" +
                "    'rows-per-second'='5',\n" +
                "    'fields.f_sequence.kind'='sequence',\n" +
                "    'fields.f_sequence.start'='1',\n" +
                "    'fields.f_sequence.end'='500',\n" +
                "    'fields.f_random.min'='1',\n" +
                "    'fields.f_random.max'='500',\n" +
                "    'fields.f_random_str.length'='10'\n" +
                ")";

        String createPrint = "CREATE TABLE print_table (\n" +
                "    f_sequence INT,\n" +
                "    f_random INT,\n" +
                "    f_random_str STRING\n" +
                ") WITH (\n" +
                "    'connector' = 'print'\n" +
                ")";

        String insertSql = "INSERT INTO print_table SELECT f_sequence, f_random, f_random_str FROM datagen";

        tEnv.executeSql(createDatagen);
        tEnv.executeSql(createPrint);

        // This will keep running and printing results.
        tEnv.executeSql(insertSql);
    }
}
