package org.exagon.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.exagon.core.persistence.DataSource;
import org.jetbrains.annotations.NotNull;

public class MongoDataSource implements DataSource {

    @Getter
    @NotNull
    private final MongoClient client;
    @Getter
    @NotNull
    private final MongoDatabase database;

    public MongoDataSource(@NotNull final MongoCredentials credentials) {
        this.client = MongoClients.create(credentials.getUri());
        this.database = this.client.getDatabase(credentials.getDatabase());
    }

    public MongoDatabase getDatabase(final String name) {
        return this.client.getDatabase(name);
    }

    public void close() {
        if (this.client != null) {
            this.client.close();
        }
    }

}
