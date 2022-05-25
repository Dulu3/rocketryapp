package com.piotrdulewski.rocketryapp.domain;

import com.piotrdulewski.rocketryapp.calculating.domain.Fuels;
import com.piotrdulewski.rocketryapp.calculating.domain.KnDextroseDataDomain;
import com.piotrdulewski.rocketryapp.calculating.domain.KnSorbitolDataDomain;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BasicDataDomain {
    Double chamberDiameter;
    Double chamberLength;
    Double grainOutsideDiameter;
    Double grainCanalDiameter;
    Integer numberOfGrains;
    Double grainLength;
    Double grainEfficiency;
    Double KN;
    Double nozzleErosion;
    Integer erosiveBurningArea;
    Double atmosphericPressure;
    String fuelType;
    Integer isOutsideExposed;
    Integer isCanalExposed;
    Integer isEndExposed;
    Double nozzlePerformance;
    Double AeAt;
    Double burnEfficiency;
    Double grainPercent;

    public double getAbo(){
        double k = numberOfGrains * 2 * isEndExposed * Math.PI / 4 *
                (Math.pow(grainOutsideDiameter,2) - Math.pow(grainCanalDiameter,2));
        double s = numberOfGrains * isCanalExposed * Math.PI * grainCanalDiameter * grainLength;
        double z = numberOfGrains * isOutsideExposed * Math.PI * grainOutsideDiameter * grainLength;
        return k + s + z;
    }
    public double getAt(){
        return getAbo()/KN;
    }
    public double getAe(){ return  getAt()*AeAt;}
    public double getDt(){
        return Math.sqrt(getAt() * 4/ Math.PI);
    }
    public double getVc( ){return Math.PI / 4 * Math.pow(chamberDiameter,2) * chamberLength; }

    public Fuels getFuel(){
        if(fuelType.equals("KNO3/SORBITOL")){
            return new KnSorbitolDataDomain();
        }else if(fuelType.equals("KNO3/DEXTROSE")) {
           return new KnDextroseDataDomain();
        }
        return null;
    }

    public double getRealBurnTemp() {
        return getFuel().getBurnTemp() * burnEfficiency;
    }
}

