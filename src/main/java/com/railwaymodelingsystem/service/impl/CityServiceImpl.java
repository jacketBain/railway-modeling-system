package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.rms.City;
import com.railwaymodelingsystem.repository.CityRepository;
import com.railwaymodelingsystem.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityRepository cityRepository;

    @Override
    public City getCityByName(String name) {
        return cityRepository.getByName(name);
    }

    @Override
    public List<City> getAllCity() {
        return cityRepository.findAll();
    }

    @Override
    public City addCity(City city) {
        return cityRepository.saveAndFlush(city);
    }
}
