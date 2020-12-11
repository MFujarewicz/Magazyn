package com.magazyn.database.repositories;

import java.util.List;

import com.magazyn.Map.IRack;

public interface NumberOfNonExistingRacksQuery {
    Long numberOfNonExistingRacks(List<IRack> racks_ids);
}
