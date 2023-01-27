package org.vertex.sql;

import be.bendem.sqlstreams.util.SqlConsumer;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.vertex.core.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

@AllArgsConstructor
public class SQLBatch {

    @NotNull private SQLDataSource source;

    @NotNull private LinkedList<Pair<String, SqlConsumer<PreparedStatement>>> batchs = new LinkedList<>();

    public SQLBatch bind(@NotNull String statement, @NotNull SqlConsumer<PreparedStatement> handler) {
        batchs.add(new Pair<>(statement, handler));
        return this;
    }

    public void execute() {
        if(batchs.size() < 1) {
            return;
        }

        try (Connection conn = source.getConnection()) {
            for (Pair<String, SqlConsumer<PreparedStatement>> batch : batchs) {
                try(PreparedStatement st = conn.prepareStatement(batch.getFirst())) {
                    batch.getSecond().accept(st);
                    st.execute();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
