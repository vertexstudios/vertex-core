package org.exagon.sql;

import be.bendem.sqlstreams.SqlStream;
import be.bendem.sqlstreams.util.SqlConsumer;
import be.bendem.sqlstreams.util.SqlFunction;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.Getter;
import org.exagon.core.persistence.JDBCCommons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SQLDataSource implements SQLAcessor {

    @NotNull
    @Getter
    private Connection connection;

    @NotNull
    @Getter
    private HikariPool pool;

    @NotNull
    @Getter
    private final HikariDataSource source;

    @NotNull
    @Getter
    private final SqlStream stream;

    private static final AtomicInteger POOL_COUNTER = new AtomicInteger(0);
    public static final int MAXIMUM_POOLS = (Runtime.getRuntime().availableProcessors() * 2) + 1;
    public static final long MAXIMUM_LIFE_TIME = TimeUnit.MINUTES.toMillis(30);
    private static final int MINIMUM_IDLE_POOLS = Math.min(MAXIMUM_POOLS, 10);
    public static final long TIMED_OUT_AFTER = TimeUnit.SECONDS.toMillis(10);

    public SQLDataSource(@Nullable String driver,
                         @Nullable String url,
                         @NotNull String protocol,
                         @NotNull SQLCredentials credentials) {

        HikariConfig hikari = new HikariConfig();

        hikari.setJdbcUrl((url == null ? JDBCCommons.DEFAULT_JDBC_URL : url)
                .replace("{protocol}", protocol)
                .replace("{host}", credentials.getHost())
                .replace("{port}", credentials.getPort())
                .replace("{schema}", credentials.getSchema()));

        hikari.setUsername(credentials.getUser());
        hikari.setPassword(credentials.getPassword());

        hikari.setMaximumPoolSize(MAXIMUM_POOLS);
        hikari.setMinimumIdle(MINIMUM_IDLE_POOLS);
        hikari.setMaxLifetime(MAXIMUM_LIFE_TIME);
        hikari.setConnectionTimeout(TIMED_OUT_AFTER);

        Map<String, String> properties = new HashMap<>();
        properties.put("useUnicode", "true");
        properties.put("characterEncoding", "utf8");
        properties.put("cachePrepStmts", "true");
        properties.put("prepStmtCacheSize", "250");
        properties.put("prepStmtCacheSqlLimit", "2048");
        properties.put("useServerPrepStmts", "true");
        properties.put("useLocalSessionState", "true");
        properties.put("rewriteBatchedStatements", "true");
        properties.put("cacheResultSetMetadata", "true");
        properties.put("cacheServerConfiguration", "true");
        properties.put("elideSetAutoCommits", "true");
        properties.put("maintainTimeStats", "false");
        properties.put("alwaysSendSetIsolation", "false");
        properties.put("cacheCallableStmts", "true");
        properties.put("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

        for (final Map.Entry<String, String> property : properties.entrySet()) {
            hikari.addDataSourceProperty(property.getKey(), property.getValue());
        }

        this.source = new HikariDataSource(hikari);
        this.stream = SqlStream.connect(this.source);
    }

    public void update(@NotNull String statement,
                       @NotNull SqlConsumer<PreparedStatement> preparedStatementConsumer) {
        try (Connection conn = this.getConnection();
             PreparedStatement prepared = conn.prepareStatement(statement)) {

            preparedStatementConsumer.accept(prepared);
            prepared.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> Optional<T> findOne(@NotNull String statement,
                                   @NotNull SqlConsumer<PreparedStatement> preparedStatementConsumer,
                                   @NotNull SqlFunction<ResultSet, T> adapter) {
        try (Connection conn = this.getConnection();
             PreparedStatement prepared = conn.prepareStatement(statement)) {

            preparedStatementConsumer.accept(prepared);
            ResultSet rs = prepared.executeQuery();
            if(rs.next()) {
                return Optional.of(adapter.apply(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public <T> void findMany(@NotNull String statement,
                                    @NotNull SqlConsumer<PreparedStatement> preparedStatementConsumer,
                                    @NotNull SqlFunction<ResultSet, T> adapter,
                                    @NotNull Collection<T> storage) {
        try (Connection conn = this.getConnection();
             PreparedStatement prepared = conn.prepareStatement(statement)) {

            preparedStatementConsumer.accept(prepared);
            ResultSet rs = prepared.executeQuery();
            while(rs.next()) {
                storage.add(adapter.apply(rs));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
