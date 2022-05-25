package com.piotrdulewski.rocketryapp.calculating;

import com.piotrdulewski.rocketryapp.calculating.domain.Fuels;
import com.piotrdulewski.rocketryapp.calculating.dto.ChamberPressureDTO;
import com.piotrdulewski.rocketryapp.calculating.dto.ThrustDTO;
import com.piotrdulewski.rocketryapp.domain.BasicDataDomain;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@NoArgsConstructor
@Setter
class CalculateThrust {

    private List<ChamberPressureDTO> list ;

    public List<ThrustDTO> calculateThrust(BasicDataDomain basicDataDomain){

        Fuels fuel = basicDataDomain.getFuel();
        double atmosphericPressure = basicDataDomain.getAtmosphericPressure();
        List<ThrustDTO> calculation = new ArrayList<>();
        ThrustDTO thrustDTO = new ThrustDTO();

        ChamberPressureDTO firstDTO = list.get(0);
        thrustDTO.Paabs = firstDTO.Paabs;
        thrustDTO.A = firstDTO.A;
        thrustDTO.At= firstDTO.At;
        thrustDTO.AeAt = basicDataDomain.getAeAt();
        thrustDTO.Me = calculateMachSpeed(fuel, basicDataDomain.getAeAt());
        thrustDTO.Pe = calculatePe(atmosphericPressure, fuel.getHeatRatio(), thrustDTO);
        thrustDTO.opt = 1d;
        thrustDTO.Cf = basicDataDomain.getNozzlePerformance();
        thrustDTO.Fn = 0d;
        thrustDTO.Flb = 0d;
        thrustDTO.t = firstDTO.t;
        thrustDTO.It = 0d;

        calculation.add(thrustDTO);

        boolean test = false;

        for(var x : list){
            if(test){
                if(x.tweb == null || x.tweb <= 0){
                    ThrustDTO last = calculation.get(calculation.size()-1);
                    thrustDTO = new ThrustDTO();
                    thrustDTO.Paabs = x.Mpagage * 1_000_000 + atmosphericPressure;
                    thrustDTO.t = x.t;
                    thrustDTO.A = last.A;
                    thrustDTO.At = last.At;
                    thrustDTO.Me = last.Me;
                    thrustDTO.Pe = calculatePe(atmosphericPressure, fuel.getHeatRatio(),thrustDTO);
                    thrustDTO.opt = calculateOpt(atmosphericPressure, fuel.getHeatRatio(), thrustDTO.Paabs);
                    thrustDTO.Cf = calculateCf(atmosphericPressure, basicDataDomain.getNozzlePerformance(),
                            fuel.getHeatRatio(), basicDataDomain.getAe() ,thrustDTO);
                    thrustDTO.Fn = thrustDTO.Paabs * thrustDTO.A * thrustDTO.Cf;
                    thrustDTO.Flb = thrustDTO.Fn / 4.4482;

                }else {
                    thrustDTO = new ThrustDTO();
                    thrustDTO.Paabs = x.Paabs;
                    thrustDTO.A = x.A;
                    thrustDTO.At = x.At;
                    thrustDTO.t = x.t;
                    thrustDTO.AeAt = basicDataDomain.getAt() * basicDataDomain.getAeAt() / thrustDTO.At;
                    thrustDTO.Me = calculation.get(calculation.size() - 1).Me;
                    thrustDTO.Pe = calculatePe(atmosphericPressure, fuel.getHeatRatio(), thrustDTO);
                    thrustDTO.opt = calculateOpt(atmosphericPressure, fuel.getHeatRatio(), thrustDTO.Paabs);
                    thrustDTO.Cf = calculateCf(atmosphericPressure, basicDataDomain.getNozzlePerformance(),
                            fuel.getHeatRatio(), basicDataDomain.getAe(), thrustDTO);
                    thrustDTO.Fn = thrustDTO.Paabs * thrustDTO.A * thrustDTO.Cf;
                    thrustDTO.Flb = thrustDTO.Fn / 4.4482;
                    thrustDTO.It = calculateIt(calculation, thrustDTO);

                }
                calculation.add(thrustDTO);


            }            test = true;
        }

        return calculation;
    }

    private Double calculateIt(List<ThrustDTO> calculation, ThrustDTO thrustDTO) {

        ThrustDTO prev = calculation.get(calculation.size() - 1);
        return (thrustDTO.Fn + prev.Fn) / 2 * (thrustDTO.t - prev.t);
    }

    private static double calculateMachSpeed(Fuels fuel,Double aeAt){
        if(aeAt == 1) {
            return 1;
        }
        Map<Double,Double> result = new LinkedHashMap<>();
         for(double i = 0.5; i < 10; i+=0.0001){
             result.put(i,Math.abs(aeAt - 1 / i * Math.pow(((1+(fuel.getHeatMixtureRatio() - 1 ) / 2
                             * Math.pow(i,2)) / (1 + (fuel.getHeatMixtureRatio() -1  ) / 2 )),
                     ((fuel.getHeatMixtureRatio() + 1 ) / 2 / (fuel.getHeatMixtureRatio() - 1 )))));
         }

        return result.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue)).get().getKey();
    }

    private static double calculatePe(double atmosphericPressure,double heatRation , ThrustDTO thrustDTO){
        double calculate = thrustDTO.Paabs/ Math.pow( 1 + (heatRation - 1)/ 2 * Math.pow(thrustDTO.Me,2),
                (heatRation) / (heatRation - 1 ));

        if(calculate < atmosphericPressure * 1_000_000){
            return  atmosphericPressure * 1_000_000;
        }else {
            return calculate;
        }


    }

    private static double calculateOpt(double atmosphericPressure, double heatRatio, double Paabs){
        return 1
                / (Math.pow(((heatRatio + 1) / 2), (1 / heatRatio))
                * Math.pow((atmosphericPressure * 1_000_000 / Paabs), (1 / heatRatio))
                * Math.sqrt(((heatRatio + 1) / (heatRatio - 1))
                *(1 - Math.pow((atmosphericPressure * 1_000_000 / Paabs), ((heatRatio -1 ) / heatRatio)))));
    }

    private static double calculateCf(double atmosphericPressure,double nozzlePerformance,double heatRatio, double Ae,ThrustDTO thrustDTO){
        return nozzlePerformance * Math.sqrt(2 * Math.pow(heatRatio, 2) / (heatRatio - 1) * Math.pow((2 / (heatRatio + 1)), ((heatRatio + 1 ) / ( heatRatio - 1)))
                * ( 1 - Math.pow((thrustDTO.Pe / thrustDTO.Paabs), ((heatRatio - 1) / heatRatio)) + (thrustDTO.Pe - atmosphericPressure * 1_000_000 )
                / thrustDTO.Paabs*( Ae /thrustDTO.At )));
    }

}
