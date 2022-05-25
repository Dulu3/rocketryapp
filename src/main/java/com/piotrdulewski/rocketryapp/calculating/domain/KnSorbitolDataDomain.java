package com.piotrdulewski.rocketryapp.calculating.domain;

import org.apache.commons.lang3.Range;

import java.util.HashMap;
import java.util.Map;

public class KnSorbitolDataDomain implements Fuels {

    private static final Range<Double> FIRST_RANGE = Range.between(0.1 ,0.807);
    private static final Range<Double> SECOND_RANGE = Range.between(0.807 ,1.503);
    private static final Range<Double> THIRD_RANGE = Range.between(1.503 ,3.793);
    private static final Range<Double> FOURTH_RANGE = Range.between(3.793 ,7.034);
    private static final Range<Double> FIFTH_RANGE = Range.between(7.034 ,10.670);

    private final Map<Range<Double>,Double> mapBurnRate = new HashMap<>();
    private final Map<Range<Double>,Double> mapN = new HashMap<>();
    private static final double IDEAL_DENSITY = 1.841; //g/cm3
    private static final double MOL_MASS_BURN = 39.9;
    private static final double HEAT_RATIO_MIXTURE = 1.1361;
    private static final double HEAT_RATIO = 1.042;
    private static final double BURN_TEMP = 1600; //k



    public KnSorbitolDataDomain() {
        mapBurnRate.put(FIRST_RANGE,10.708);
        mapBurnRate.put(SECOND_RANGE,8.763);
        mapBurnRate.put(THIRD_RANGE,7.852);
        mapBurnRate.put(FOURTH_RANGE,3.907);
        mapBurnRate.put(FIFTH_RANGE,9.653);
        mapN.put(FIRST_RANGE,0.625);
        mapN.put(SECOND_RANGE,-0.314);
        mapN.put(THIRD_RANGE,-0.0013);
        mapN.put(FOURTH_RANGE,0.535);
        mapN.put(FIFTH_RANGE,0.064);
    }

    @Override
    public double getBurnRate(Double pressure){
        double burnRate = 0;

        if(FIRST_RANGE.contains(pressure)){
            burnRate = mapBurnRate.get(FIRST_RANGE);
        }else if(SECOND_RANGE.contains(pressure)){
            burnRate = mapBurnRate.get(SECOND_RANGE);
        }else if(THIRD_RANGE.contains(pressure)){
            burnRate = mapBurnRate.get(THIRD_RANGE);
        }else if (FOURTH_RANGE.contains(pressure)){
            burnRate = mapBurnRate.get(FOURTH_RANGE);
        }else if(FIFTH_RANGE.contains(pressure)){
            burnRate = mapBurnRate.get(FIFTH_RANGE);
        }

        return burnRate;
    }

    @Override
    public double getN(Double pressure){
        double n = 0;

        if(FIRST_RANGE.contains(pressure)){
            n = mapN.get(FIRST_RANGE);
        }else if(SECOND_RANGE.contains(pressure)){
            n = mapN.get(SECOND_RANGE);
        }else if(THIRD_RANGE.contains(pressure)){
            n = mapN.get(THIRD_RANGE);
        }else if (FOURTH_RANGE.contains(pressure)){
            n = mapN.get(FOURTH_RANGE);
        }else if(FIFTH_RANGE.contains(pressure)){
            n = mapN.get(FIFTH_RANGE);
        }

        return n;
    }

    @Override
    public double getMolMassBurn() {
        return MOL_MASS_BURN;
    }
    @Override
    public double getRealDensity(Double percent){
        return IDEAL_DENSITY * percent;
    }
    @Override
    public double getHeatMixtureRatio(){
        return HEAT_RATIO_MIXTURE;
    }
    @Override
    public double getHeatRatio(){
        return HEAT_RATIO;
    }
    @Override
    public double getBurnTemp() {
        return BURN_TEMP;
    }


}
