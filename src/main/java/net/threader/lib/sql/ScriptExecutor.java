package net.threader.lib.sql;

import net.threader.lib.sql.acessor.DatabaseAcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class ScriptExecutor {

    private File file;
    private Connection connection;

    public ScriptExecutor(File file, Connection connection) {
        this.file = file;
        this.connection = connection;
    }

    public void execute() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            StringBuilder fileContent = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
            final String[] queries = fileContent.toString().split(";");
            Arrays.stream(queries).forEach((query) -> {
                try{
                    Statement st = connection.createStatement();
                    st.execute(query);
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
            });
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

}
