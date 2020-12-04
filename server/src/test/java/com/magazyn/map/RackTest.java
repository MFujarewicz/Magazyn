package com.magazyn.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RackTest {
	@Test
	public void ConstructorTest() {
		Rack rack = new Rack(12);
		
		assertEquals(12, rack.getObjectID());
	}

	@Test
	public void BoundsTest() {
		Rack rack = new Rack(12);
		Rectangle rec = new Rectangle(1.0, 2.0, 3.0, 4.0, 5.0);
		assertEquals(rack.getBounds(), new Rectangle());

		rack.setBounds(rec);
		assertEquals(12, rack.getObjectID());
		assertEquals(rec, rack.getBounds());
	}

	@Test
	public void IRackTest() {
		Rack rack = new Rack(12);
		
		assertEquals(0, rack.numberOfAllocationUnitsPerRow());
		assertEquals(0, rack.numberOfRows());
		assertEquals(false, rack.isTwoSided());

		rack.setNumberOfAllocationUnitsPerRow(100);
		rack.setNumberOfRows(15);
		rack.twoSided(true);

		assertEquals(100, rack.numberOfAllocationUnitsPerRow());
		assertEquals(15, rack.numberOfRows());
		assertEquals(true, rack.isTwoSided());
	}
}
