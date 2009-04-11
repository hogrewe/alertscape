/**
 * 
 */
package com.alertscape.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.PredefinedTagProfile;
import com.alertscape.dao.AlertAttributeDefinitionDao;

/**
 * @author josh
 * 
 */
public class AlertAttributeDefinitionJdbcDao extends JdbcDaoSupport implements AlertAttributeDefinitionDao {
  /**
   * 
   */
	private static final ASLogger LOG = ASLogger.getLogger(AlertAttributeDefinitionJdbcDao.class);
	private static final AttributeDefinitionRowMapper ATTRIBUTE_DEFINITION_ROW_MAPPER = new AttributeDefinitionRowMapper();
  private static final String GET_ACTIVE_DEF_SQL = "select * from attribute_definitions where active=1";
  private static final String GET_DEF_SQL = "select * from attribute_definitions where attribute_name=?";
  private static final String INSERT_DEF_SQL = "insert into attribute_definitions (attribute_name) values (?)";
  private static final String UPDATE_DEF_SQL = "update attribute_definitions set active=? where attribute_definition_id=?";

  private static final String GET_ACTIVE_CAT_DEF_SQL="select def.name, def.allow_custom, def.active, valdef.value, valdef.is_default from category_def def, category_value_def valdef where def.sid=valdef.category_sid and def.active=1";
  private static final String GET_ACTIVE_LABEL_DEF_SQL="select distinct name from alert_labels";
  
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
  
  private static final class CategoryDefinitionMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
    	Map<String, Object> attr = new HashMap<String, Object>();
    	List<String> categoryList = new ArrayList<String>();
    	Set<String> categories = new HashSet<String>();
    	
    	while (rs.next())
    	{
    		// get the data from the next row
        String name = rs.getString("Name");
        boolean allowCustom = rs.getInt("Allow_Custom")==1;
        String value = rs.getString("Value");
        boolean isDefault = rs.getInt("Is_Default")==1;
        
        // add this category to the set of category names 
        categories.add(name);
        
        // check if we already have this one created
        PredefinedTagProfile profile = (PredefinedTagProfile)attr.get(name + "_tagProfile");
        if (profile == null)
        {
        	profile = new PredefinedTagProfile();
        	List<String> validValues = new ArrayList<String>();
        	validValues.add(value);
        	profile.setValidValues(validValues);
        	profile.setUserModifiable(allowCustom);        	
        	profile.setDefaultValue(value);
        	
        	attr.put(name + "_tagProfile", profile);
        }
        else
        {
        	profile.getValidValues().add(value);
        	if (isDefault)
        	{
        		profile.setDefaultValue(value);
        	}
        }
    	}

    	categoryList.addAll(categories);
    	attr.put("definedTagNames", categoryList);
    	
      return attr;    	
    	
    	
//  	// TODO - these are all hardcoded, they need to come from a file or a database instead
//		// remember that major tags / categories need to be predefined, and cannot have custom names, but they can have custom values
//		// may want to select * from the predefined values table, and join it with all of the values for the given tags in the database
//		// alphabetize, and provde the results back....
//		Map<String, Object> myMap = new HashMap<String, Object>();
//		List<String> fakeTagList = new ArrayList<String>();
//		fakeTagList.add("Status");
//		fakeTagList.add("Folder");
//		fakeTagList.add("Responsible Dept");
//		myMap.put(PredefinedTagConstants.DEFINED_TAGNAMES, fakeTagList);
//		
//		List<String> validStatuses = new ArrayList<String>();
//		validStatuses.add("Assigned");
//		validStatuses.add("Work in Progress");
//		validStatuses.add("Waiting for Customer");
//		validStatuses.add("Resolved");
//		PredefinedTagProfile statusProfile = new PredefinedTagProfile();
//		statusProfile.setValidValues(validStatuses);
//		statusProfile.setUserModifiable(false);
//		statusProfile.setDefaultValue("Assigned");
//		myMap.put("Status" + PredefinedTagConstants.TAGPROFILE_SUFFIX, statusProfile);
//	
//		List<String> validFolders = new ArrayList<String>();
//		validFolders.add("(None)");
//		validFolders.add("Resolved");
//		validFolders.add("Known Issue");
//		validFolders.add("Major Event");
//		PredefinedTagProfile folderProfile = new PredefinedTagProfile();
//		folderProfile.setValidValues(validFolders);
//		folderProfile.setUserModifiable(true);
//		folderProfile.setDefaultValue("(None)");
//		myMap.put("Folder" + PredefinedTagConstants.TAGPROFILE_SUFFIX, folderProfile);
//	
//		List<String> validDepts = new ArrayList<String>();		
//		validDepts.add("Tier 1 NOC");
//		validDepts.add("Tier 2 Customer Care");
//		validDepts.add("Tier 3 Engineering");
//		PredefinedTagProfile deptProfile = new PredefinedTagProfile();
//		deptProfile.setValidValues(validDepts);
//		deptProfile.setUserModifiable(false);
//		deptProfile.setDefaultValue("Tier 1 NOC");
//		myMap.put("Responsible Dept" + PredefinedTagConstants.TAGPROFILE_SUFFIX, deptProfile);
    }
  }  
  
  public Map<String, Object> getActiveCategoryDefinitions()
  {
  	Map<String, Object> attr = null;
    try 
    {    	
      List<Map<String, Object>> list = getJdbcTemplate().query(GET_ACTIVE_CAT_DEF_SQL, new CategoryDefinitionMapper());
      if (!list.isEmpty()) 
      {
        attr = list.get(0);
      }
    } 
    catch (DataAccessException e) 
    {
      LOG.info("Couldn't get category definitions", e);
    }  	
    
  	return attr;  	
  }
  
  private static final class LabelDefinitionMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
      Map<String, List<String>> labels = new HashMap<String, List<String>>();    	
      List<String> labels2 = new ArrayList<String>();
      labels.put("existingTagNames", labels2);  
      
    	while (rs.next())
    	{
    		// get the data from the next row
        String name = rs.getString("Name");
        LOG.debug("adding label " + name + " to list of valid labels");
        
        // add this label to the set of label names 
        labels2.add(name);
    	}
      
    	return labels;
    	
 //// TODO - these are all hardcoded, they need to come from a file or a database instead
 //// remember that minor tags / labels do NOT need to be predefined, and so they CAN have custom names, AND they can have custom values
 //// may want to select * from the predefined values table, and join it with all of the names/values for the in the database
 //// alphabetize, and provde the results back....
 //Map<String, List<String>> myMap = new HashMap<String, List<String>>();
 //List<String> fakeTagList = new ArrayList<String>();
 //fakeTagList.add("Location");
 //fakeTagList.add("Known Event Name");
 //fakeTagList.add("Ticket Number");
 //fakeTagList.add("Workflow Status");
 //myMap.put(CustomTagConstants.EXISTING_TAGNAMES, fakeTagList);
 //
 //return myMap;
    	
    }
  }  


  
  public Map<String, List<String>> getActiveLabelDefinitions()
  {
  	Map<String, List<String>> attr = null;
    try 
    {    	
      List<Map<String, List<String>>> list = getJdbcTemplate().query(GET_ACTIVE_LABEL_DEF_SQL, new LabelDefinitionMapper());
      if (!list.isEmpty()) 
      {
        attr = list.get(0);
      }
    } 
    catch (DataAccessException e) 
    {
      LOG.info("Couldn't get label definitions", e);
    }  	
  	
  	return attr;  	
  }  
  
  
  
}
