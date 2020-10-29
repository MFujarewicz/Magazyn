package com.magazyn.map;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class RackTest {
    @Test
    public void ConstructorTest() {
        UUID uuid = new UUID(1, 2);
        Rack rack = new Rack(uuid);
        
        Assert.assertEquals(uuid, rack.getObjectUUID());
    }

    @Test
    public void BoundsTest() {
        UUID uuid = new UUID(1, 2);
        Rack rack = new Rack(uuid);
        Rectangle rec = new Rectangle(1.0, 2.0, 3.0, 4.0, 5.0);
        Assert.assertEquals(rack.getBounds(), new Rectangle());

        rack.setBounds(rec);
        Assert.assertEquals(uuid, rack.getObjectUUID());
        Assert.assertEquals(rec, rack.getBounds());
    }

    @Test
    public void IRackTest() {
        UUID uuid = new UUID(1, 2);
        Rack rack = new Rack(uuid);
        
        Assert.assertEquals(0, rack.numberOfAllocationUnitsPerRow());
        Assert.assertEquals(0, rack.numberOfRows());
        Assert.assertEquals(false, rack.isTwoSided());

        rack.setNumberOfAllocationUnitsPerRow(100);
        rack.setNumberOfRows(15);
        rack.twoSided(true);

        Assert.assertEquals(100, rack.numberOfAllocationUnitsPerRow());
        Assert.assertEquals(15, rack.numberOfRows());
        Assert.assertEquals(true, rack.isTwoSided());
    }
}
