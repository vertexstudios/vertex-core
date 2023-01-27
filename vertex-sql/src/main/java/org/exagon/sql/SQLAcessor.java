package org.exagon.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLAcessor {

    Connection getConnection() throws SQLException;

}
