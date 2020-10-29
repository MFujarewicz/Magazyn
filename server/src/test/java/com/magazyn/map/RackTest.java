package com.magazyn.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class RackTest {
	@Test
	public void ConstructorTest() {
		UUID uuid = new UUID(1, 2);
		Rack rack = new Rack(uuid);
		
		assertEquals(uuid, rack.getObjectUUID());
	}

	@Test
	public void BoundsTest() {
		UUID uuid = new UUID(1, 2);
		Rack rack = new Rack(uuid);
		Rectangle rec = new Rectangle(1.0, 2.0, 3.0, 4.0, 5.0);
		assertEquals(rack.getBounds(), new Rectangle());

		rack.setBounds(rec);
		assertEquals(uuid, rack.getObjectUUID());
		assertEquals(rec, rack.getBounds());
	}

	@Test
	public void IRackTest() {
		UUID uuid = new UUID(1, 2);
		Rack rack = new Rack(uuid);
		
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
