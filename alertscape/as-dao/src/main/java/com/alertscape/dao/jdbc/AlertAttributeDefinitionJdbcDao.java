/**
 * 
 */
package com.alertscape.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.dao.AlertAttributeDefinitionDao;

/**
 * @author josh
 * 
 */
public class AlertAttributeDefinitionJdbcDao extends JdbcDaoSupport implements AlertAttributeDefinitionDao {
  /**
   * 
   */
  private static final AttributeDefinitionRowMapper ATTRIBUTE_DEFINITION_ROW_MAPPER = new AttributeDefinitionRowMapper();
  private static final String GET_ACTIVE_DEF_SQL = "select * from attribute_definitions where active=1";
  private static final String GET_DEF_SQL = "select * from attribute_definitions where attribute_name=?";
  private static final String INSERT_DEF_SQL = "insert into attribute_definitions (attribute_name) values (?)";
  private static final String UPDATE_DEF_SQL = "update attribute_definitions set active=? where attribute_definition_id=?";

  @SuppressWarnings("unchecked")
  public List<AlertAttributeDefinition> getActiveDefinitions() {
    return getJdbcTemplate().query(GET_ACTIVE_DEF_SQL, ATTRIBUTE_DEFINITION_ROW_MAPPER);
  }

  public AlertAttributeDefinition getDefinition(String name) {
    return (AlertAttributeDefinition) getJdbcTemplate().queryForObject(GET_DEF_SQL, ATTRIBUTE_DEFINITION_ROW_MAPPER);
  }

  public void save(AlertAttributeDefinition definition) {
    AlertAttributeDefinition existing = getDefinition(definition.getName());
    if (existing == null) {
      insert(definition);
    } else {
      update(definition);
    }
  }

  protected void insert(AlertAttributeDefinition definition) {
    Object[] args = new Object[1];
    
    int i = 0;
    args[i++] = definition.getName();
    
    getJdbcTemplate().update(INSERT_DEF_SQL, args);
  }

  protected void update(AlertAttributeDefinition definition) {
    Object[] args = new Object[2];
    
    int i = 0;
    args[i++] = definition.isActive();
    args[i++] = definition.getAttributeDefinitionId();
    
    getJdbcTemplate().update(UPDATE_DEF_SQL, args);
  }

  /**
   * @author josh
   * 
   */
  private static final class AttributeDefinitionRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      AlertAttributeDefinition def = new AlertAttributeDefinition();

      def.setActive(rs.getBoolean("active"));
      def.setAttributeDefinitionId(rs.getInt("attribute_definition_id"));
      def.setName(rs.getString("attribute_name"));

      return def;
    }
  }

}
