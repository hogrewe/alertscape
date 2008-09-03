/**
 * 
 */
package com.alertscape.pump.onramp;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class AlertEquatorTest extends TestCase {
	private Alert a1;
	private Alert a2;
	private Alert a3;
	private Alert a4;
	private Alert sameAsa1;

	private AlertEquator itemDescEquator;
	private AlertEquator allEquator;

	@Override
	protected void setUp() throws Exception {
		a1 = new Alert();
		a1.setItem("item 1");
		a1.setShortDescription("short desc 1");
		Map<String, Object> majorTags1 = new HashMap<String, Object>();
		majorTags1.put("major1", "major 1 1");
		majorTags1.put("major2", "major 2 1");
		majorTags1.put("major3", "major 3 1");
		a1.setMajorTags(majorTags1);
		Map<String, Object> minorTags1 = new HashMap<String, Object>();
		minorTags1.put("minor1", "minor 1 1");
		minorTags1.put("minor2", "minor 2 1");
		minorTags1.put("minor3", "minor 3 1");
		a1.setMinorTags(minorTags1);

		a2 = new Alert();
		a2.setItem("item 2");
		a2.setShortDescription("short desc 2");
		Map<String, Object> majorTags2 = new HashMap<String, Object>();
		majorTags2.put("major1", "major 1 2");
		majorTags2.put("major2", "major 2 2");
		majorTags2.put("major3", "major 3 2");
		a2.setMajorTags(majorTags2);
		Map<String, Object> minorTags2 = new HashMap<String, Object>();
		minorTags2.put("minor1", "minor 1 2");
		minorTags2.put("minor2", "minor 2 2");
		minorTags2.put("minor3", "minor 3 2");
		a2.setMinorTags(minorTags2);

		a3 = new Alert();
		a3.setItem("item 2");
		Map<String, Object> majorTags3 = new HashMap<String, Object>();
		majorTags3.put("major1", "major 1 2");
		majorTags3.put("major2", "major 2 2");
		majorTags3.put("major3", "major 3 2");
		a3.setMajorTags(majorTags3);
		Map<String, Object> minorTags3 = new HashMap<String, Object>();
		minorTags3.put("minor1", "minor 1 2");
		minorTags3.put("minor2", "minor 2 2");
		minorTags3.put("minor3", "minor 3 2");
		a3.setMinorTags(minorTags3);

		a4 = new Alert();
		a4.setItem("item 1");
		a4.setShortDescription("short desc 1");
		Map<String, Object> majorTags4 = new HashMap<String, Object>();
		majorTags4.put("major1", "major 1 1");
		majorTags4.put("major3", "major 3 1");
		a4.setMajorTags(majorTags4);
		Map<String, Object> minorTags4 = new HashMap<String, Object>();
		minorTags4.put("minor1", "minor 1 1");
		minorTags4.put("minor2", "minor 2 1");
		minorTags4.put("minor3", "minor 3 1");
		a4.setMinorTags(minorTags4);

		sameAsa1 = new Alert();
		sameAsa1.setItem("item 1");
		sameAsa1.setShortDescription("short desc 1");
		Map<String, Object> majorTags1dup = new HashMap<String, Object>();
		majorTags1dup.put("major1", "major 1 1");
		majorTags1dup.put("major2", "major 2 1");
		majorTags1dup.put("major3", "major 3 1");
		sameAsa1.setMajorTags(majorTags1dup);
		Map<String, Object> minorTags1dup = new HashMap<String, Object>();
		minorTags1dup.put("minor1", "minor 1 1");
		minorTags1dup.put("minor2", "minor 2 1");
		minorTags1dup.put("minor3", "minor 3 1");
		sameAsa1.setMinorTags(minorTags1dup);

		List<String> emptyList = Collections.emptyList();
		itemDescEquator = new AlertEquator(Arrays.asList("item",
				"shortDescription"), emptyList, emptyList);
		allEquator = new AlertEquator(
				Arrays.asList("item", "shortDescription"), Arrays.asList(
						"major1", "major2"), Arrays.asList("minor2"));
	}

	/**
	 * Test method for
	 * {@link com.alertscape.pump.onramp.AlertEquator#equal(com.alertscape.common.model.Alert, com.alertscape.common.model.Alert)}
	 * .
	 */
	public void testEqual() {
		assertFalse(itemDescEquator.equal(a1, a2));
		assertFalse(itemDescEquator.equal(a1, a3));
		assertFalse(itemDescEquator.equal(a2, a3));
		assertTrue(itemDescEquator.equal(a1, a4));
		assertTrue(itemDescEquator.equal(a1, sameAsa1));
		
		assertFalse(allEquator.equal(a1, a2));
		assertFalse(allEquator.equal(a1, a4));
		assertFalse(allEquator.equal(a2, a3));
		assertTrue(allEquator.equal(a1, sameAsa1));
	}

	/**
	 * Test method for
	 * {@link com.alertscape.pump.onramp.AlertEquator#hashAlert(com.alertscape.common.model.Alert)}
	 * .
	 */
	public void testHashAlert() {
		assertTrue(itemDescEquator.hashAlert(a1) != itemDescEquator.hashAlert(a2));
		assertTrue(itemDescEquator.hashAlert(a1) != itemDescEquator.hashAlert(a3));
		assertTrue(itemDescEquator.hashAlert(a2) != itemDescEquator.hashAlert(a3));
		assertTrue(itemDescEquator.hashAlert(a1) == itemDescEquator.hashAlert(a4));
		assertTrue(itemDescEquator.hashAlert(a1) == itemDescEquator.hashAlert(sameAsa1));
		
		assertTrue(allEquator.hashAlert(a1) != allEquator.hashAlert(a2));
		assertTrue(allEquator.hashAlert(a1) != allEquator.hashAlert(a4));
		assertTrue(allEquator.hashAlert(a2) != allEquator.hashAlert(a3));
		assertTrue(allEquator.hashAlert(a1) == allEquator.hashAlert(sameAsa1));
	}

}
