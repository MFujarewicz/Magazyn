package com.magazyn.map;

import java.util.UUID;

public class Rack implements IRack {
	private UUID rack_uuid;
	private Rectangle bounds = new Rectangle();

	private boolean is_two_sided = false;
	private long number_of_allocation_units_per_row = 0;
	private long number_of_rows = 0;

	public Rack(UUID rack_uuid) {
		this.rack_uuid = rack_uuid;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public void twoSided(boolean is_two_sided) {
		this.is_two_sided = is_two_sided;
	}

	public void setNumberOfAllocationUnitsPerRow(long number_of_allocation_units_per_row) {
		this.number_of_allocation_units_per_row = number_of_allocation_units_per_row;
	}

	public void setNumberOfRows(long number_of_rows) {
		this.number_of_rows = number_of_rows;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public UUID getObjectUUID() {
		return rack_uuid;
	}

	@Override
	public boolean isTwoSided() {
		return is_two_sided;
	}

	@Override
	public long numberOfAllocationUnitsPerRow() {
		return number_of_allocation_units_per_row;
	}

	@Override
	public long numberOfRows() {
		return number_of_rows;
	}
	
}
