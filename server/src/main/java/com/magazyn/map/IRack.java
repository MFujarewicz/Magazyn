package com.magazyn.map;

public interface IRack extends IMapObject{
	public boolean isTwoSided();

	/**
	 * Get number of allocation units per row.
	 * 
	 * First side: from (left, top) to (left + width, top)
	 * Second side: from (left, top - height) to (left + width, top - height)
	 * With given angle
	 * 
	 * Allocation units numbering scheme
	 * First side:
	 *  First row:  [0, numberOfAllocationUnitsPerRow() - 1]
	 *  Second row: [numberOfAllocationUnitsPerRow(), 2 * numberOfAllocationUnitsPerRow() - 1]
	 *  .
	 *  .
	 *  .
	 * 
	 * Second side:
	 *  First row:  [numberOfAllocationUnitsPerRow() * numberOfRows(), numberOfAllocationUnitsPerRow() * (numberOfRows() + 1) - 1]
	 *  Second row: [numberOfAllocationUnitsPerRow() * (numberOfRows() + 1), numberOfAllocationUnitsPerRow() * (numberOfRows() + 2) - 1]
	 * 
	 * @return number Of allocation units per row
	 */
	public long numberOfAllocationUnitsPerRow();
	public long numberOfRows();
}
