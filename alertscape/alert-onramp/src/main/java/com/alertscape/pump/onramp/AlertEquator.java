/**
 * 
 */
package com.alertscape.pump.onramp;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class AlertEquator {
	private static final Object[] NO_ARGS = new Object[0];
	
	private List<Equator> equators;


	public boolean equal(Alert a1, Alert a2)
	{
		for (Equator eq : equators) {
			if(!eq.equal(a1, a2)) {
				return false;
			}
		}
		return true;
	}
	
	
	public int hashAlert(Alert a) {
		final int prime = 31;
		int result = 1;
		for (Equator eq : equators) {
			Object val = eq.getHashableValue(a);
			result = prime * result + ((val == null) ? 0 : val.hashCode());			
		}
		return result;
	}
	
	private interface Equator {
		boolean equal(Alert a1, Alert a2);
		Object getHashableValue(Alert a);
	}
	
	private abstract class AbstractEquator implements Equator {
		private boolean ignoreCase;
		
		public final boolean equal(Alert a1, Alert a2) {
			Object value1 = getValue(a1);
			Object value2 = getValue(a2);
			
			if(ignoreCase && value1 instanceof String && value2 instanceof String) {
				String s1 = (String) value1;
				String s2 = (String) value2;
				return s1.equalsIgnoreCase(s2);
			}
			
			if(value1 != null) {
				return value1.equals(value2);
			} else {
				return value2 == null;
			}
		}
		
		public Object getHashableValue(Alert a) {
			Object val = getValue(a);
			if(val != null && ignoreCase && val instanceof String) {
				return ((String)val).toUpperCase();
			}
			
			return val;
		}
		
		protected abstract Object getValue(Alert a);
	}

	private final class AttributeEquator extends AbstractEquator {
		
		private Method method;
		
		public AttributeEquator(String attributeName) {
			try {
				PropertyDescriptor descriptor = new PropertyDescriptor(attributeName, Alert.class);
				method = descriptor.getReadMethod();
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected Object getValue(Alert a) {
			try {
				return method.invoke(a, NO_ARGS);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
	
	private final class MinorTagEquator extends AbstractEquator {
		private String minorTagName;
		
		public MinorTagEquator(String minorTagName) {
			this.minorTagName = minorTagName;
		}

		@Override
		protected Object getValue(Alert a) {
			return a.getMinorTag(minorTagName);
		}

		
	}
	private final class MajorTagEquator extends AbstractEquator {
		private String majorTagName;
		
		public MajorTagEquator(String majorTagName) {
			this.majorTagName = majorTagName;
		}

		@Override
		protected Object getValue(Alert a) {
			return a.getMajorTag(majorTagName);
		}
		
	}
}
