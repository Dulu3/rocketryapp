package com.piotrdulewski.rocketryapp.rest;

import com.piotrdulewski.rocketryapp.calculating.CalculateServiceFacade;
import com.piotrdulewski.rocketryapp.calculating.dto.ChamberPressureDTO;
import com.piotrdulewski.rocketryapp.calculating.dto.GrainKnDTO;
import com.piotrdulewski.rocketryapp.calculating.dto.ThrustDTO;
import com.piotrdulewski.rocketryapp.domain.BasicDataDomain;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CalculatingController {

    private final CalculateServiceFacade calculateServiceFacade;

    public CalculatingController(CalculateServiceFacade calculateServiceFacade) {
        this.calculateServiceFacade = calculateServiceFacade;
    }

    @PostMapping(path = "/calculateKn")
    public List<GrainKnDTO> calculateKn(@RequestBody BasicDataDomain basicDataDomain)  {
        return calculateServiceFacade.calculateGrainAndKn(basicDataDomain);
    }

    @PostMapping("/calculatePressure")
    public List<ChamberPressureDTO> calculatePressure(@RequestBody BasicDataDomain basicDataDomain)  {
        return calculateServiceFacade.calculateChamberPressure(basicDataDomain);
    }

    @PostMapping("/calculateThrust")
    public List<ThrustDTO>calculateThrust(@RequestBody BasicDataDomain basicDataDomain)  {
        return calculateServiceFacade.calculateThrust(calculatePressure(basicDataDomain), basicDataDomain);
    }
}
