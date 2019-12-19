package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.rms.City;

import java.util.List;

public interface CityService {
    City getCityByName(String name);
    List<City> getAllCity();
    City addCity(City name);

}
