package com.piotrdulewski.rocketryapp.calculating;

import com.piotrdulewski.rocketryapp.calculating.dto.ChamberPressureDTO;
import com.piotrdulewski.rocketryapp.calculating.dto.GrainKnDTO;
import com.piotrdulewski.rocketryapp.calculating.dto.ThrustDTO;
import com.piotrdulewski.rocketryapp.domain.BasicDataDomain;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculateServiceFacade {

    CalculateGrainAndKn calculateGrainAndKn = new CalculateGrainAndKn();

    CalculateChamberPressure calculateChamberPressure = new CalculateChamberPressure();

    CalculateThrust calculateThrust = new CalculateThrust();

    public List<GrainKnDTO> calculateGrainAndKn(BasicDataDomain basicDataDomain){

        return calculateGrainAndKn.calculateGrainAndKn(basicDataDomain);
    }

    public List<ChamberPressureDTO> calculateChamberPressure(BasicDataDomain basicDataDomain){

        return calculateChamberPressure.calculateChamberPressure(basicDataDomain);
    }

    public List<ThrustDTO> calculateThrust(List<ChamberPressureDTO> list, BasicDataDomain basicDataDomain){
        calculateThrust.setList(list);
        return calculateThrust.calculateThrust(basicDataDomain);
    }


}
