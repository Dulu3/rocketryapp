package com.piotrdulewski.rocketryapp.calculating.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class GrainKnDTO {

    public Double x;
    public Double d;
    public Double D;
    public Double L;
    public Double tweb;
    public Double Abe;
    public Double Abc;
    public Double abs;
    public Double abtotal;
    public Double At;
    public Double Kn;
    public Double Dt;

    @Override
    public String toString() {
        return "GrainKnDTO{" +
                "x=" + x +
                ", d=" + d +
                ", D=" + D +
                ", L=" + L +
                ", tweb=" + tweb +
                ", Abe=" + Abe +
                ", Abc=" + Abc +
                ", abs=" + abs +
                ", abtotal=" + abtotal +
                ", At=" + At +
                ", Kn=" + Kn +
                '}';
    }
}


