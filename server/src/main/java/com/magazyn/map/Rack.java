package com.magazyn.map;

public class Rack implements IRack {
	private int rack_id;
	private Rectangle bounds = new Rectangle();

	private boolean is_two_sided = false;
	private long number_of_allocation_units_per_row = 0;
	private long number_of_rows = 0;

	public Rack(int rack_id) {
		this.rack_id = rack_id;
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
	public int getObjectID() {
		return rack_id;
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

	@Override
	public int hashCode() {
		return Integer.hashCode(rack_id);
	}
	
}
