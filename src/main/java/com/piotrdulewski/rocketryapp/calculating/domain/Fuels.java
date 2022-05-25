package com.piotrdulewski.rocketryapp.calculating.domain;

public interface Fuels {
    double getBurnRate(Double pressure);
    double getN(Double pressure);
    double getMolMassBurn();
    double getRealDensity(Double pressure);
    double getHeatMixtureRatio();
    double getHeatRatio();
    double getBurnTemp();
}
