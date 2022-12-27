package net.threader.lib.sql;

import net.threader.lib.sql.acessor.DatabaseAcessor;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class ScriptExecutor {

    private BufferedReader reader;
    private Connection connection;

    public ScriptExecutor(BufferedReader reader, Connection connection) {
        this.reader = reader;
        this.connection = connection;
    }

    public void execute() {
        try {
            StringBuilder fileContent = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
            final String[] queries = fileContent.toString().split(";");
            Arrays.stream(queries).forEach((query) -> {
                try {
                    Statement st = connection.createStatement();
                    st.execute(query);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
