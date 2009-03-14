/**
 * 
 */
package com.alertscape.wizard.server;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author josh
 * 
 */
public class SqlRunner {
  private static final String END_LINE = ";";
  private Connection conn;

  public SqlRunner(Connection conn) {
    this.conn = conn;
  }

  public void run(Reader r) throws Exception {
    BufferedReader br = new BufferedReader(r);
    StringBuilder sql = new StringBuilder();
    String line = br.readLine();
    while (line != null) {
      line.trim();
      sql.append(" " + line);
      if (line.endsWith(END_LINE)) {
        runStatement(sql.toString());
        sql = new StringBuilder();
      }
      line = br.readLine();
    }
  }

  /**
   * @param sql
   * @throws SQLException
   */
  private void runStatement(String sql) throws SQLException {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement(sql);
      stmt.execute();
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
        }
      }
    }
  }
}
