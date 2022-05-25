package com.piotrdulewski.rocketryapp.calculating.domain;

import org.apache.commons.lang3.Range;

import java.util.HashMap;
import java.util.Map;

public class KnDextroseDataDomain implements Fuels {

    private static final Range<Double> FIRST_RANGE = Range.between(0.1 ,0.78);
    private static final Range<Double> SECOND_RANGE = Range.between(0.780 ,2.572);
    private static final Range<Double> THIRD_RANGE = Range.between(2.572 ,5.93);
    private static final Range<Double> FOURTH_RANGE = Range.between(5.93 ,8.502);
    private static final Range<Double> FIFTH_RANGE = Range.between(8.502 ,11.2);

    private final Map<Range<Double>,Double> mapBurnRate = new HashMap<>();
    private final Map<Range<Double>,Double> mapN = new HashMap<>();
    private static final double IDEAL_DENSITY = 1.879; //g/cm3
    private static final double MOL_MASS_BURN = 42.39;
    private static final double HEAT_RATIO_MIXTURE = 1.1308;
    private static final double HEAT_RATIO = 1.043;
    private static final double BURN_TEMP = 1710; //k

    public KnDextroseDataDomain() {
        mapBurnRate.put(FIRST_RANGE,8.875);
        mapBurnRate.put(SECOND_RANGE,7.553);
        mapBurnRate.put(THIRD_RANGE,3.841);
        mapBurnRate.put(FOURTH_RANGE,17.2);
        mapBurnRate.put(FIFTH_RANGE,4.775);
        mapN.put(FIRST_RANGE,0.619);
        mapN.put(SECOND_RANGE,-0.009);
        mapN.put(THIRD_RANGE,0.6888);
        mapN.put(FOURTH_RANGE,-0.148);
        mapN.put(FIFTH_RANGE,0.442);
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
