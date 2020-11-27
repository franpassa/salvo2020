package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.Ship;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShipDTO {

    Ship ship;

    public ShipDTO(Ship ship) { this.ship = ship; }

    public Ship getShip() { return ship; }

    public void setShip(Ship ship) { this.ship = ship; }

    public Map<String, Object> makeShipDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("type",ship.getType());
        dto.put("locations",ship.getLocations());

        return dto;
    }
}
