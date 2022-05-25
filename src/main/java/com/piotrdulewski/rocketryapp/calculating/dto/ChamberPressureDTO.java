package com.piotrdulewski.rocketryapp.calculating.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class ChamberPressureDTO {

    public Double xi; //mm
    public Double d; //mm
    public Double D; //mm
    public Double L; //mm
    public Double tweb; //mm2
    public Double At; //mm2
    public Double A; //m2
    public Double Aduct;
    public Double Aat;
    public Double Ai;
    public Double G;
    public Double Po; //MPa
    public Double a; //mm/s
    public Double n;
    public Double r;
    public Double t; //sec
    public Double VgrainMM;
    public Double VgranM;
    public Double Vfree;
    public Double Mgrain; //kg
    public Double Mgen; //kg/s
    public Double Mnoz; //kg/s
    public Double Msto; //kg/s
    public Double MassSto; //kg
    public Double Pprod; //kg/m3
    public Double Paabs;
    public Double mpaabs;
    public Double Mpagage;

    @Override
    public String toString() {
        return "ChamberPressureDTO{" +
                "xi=" + xi +
                ", d=" + d +
                ", D=" + D +
                ", L=" + L +
                ", tweb=" + tweb +
                ", At=" + At +
                ", A=" + A +
                ", Aduct=" + Aduct +
                ", Aat=" + Aat +
                ", Ai=" + Ai +
                ", G=" + G +
                ", a=" + a +
                ", n=" + n +
                ", r=" + r +
                ", t=" + t +
                ", VgrainMM=" + VgrainMM +
                ", VgranM=" + VgranM +
                ", Vfree=" + Vfree +
                ", Mgrain=" + Mgrain +
                ", Mgen=" + Mgen +
                ", Mnoz=" + Mnoz +
                ", Msto=" + Msto +
                ", MassSto=" + MassSto +
                ", Po=" + Po +
                ", Pprod=" + Pprod +
                ", Paabs=" + Paabs +
                ", mpaabs=" + mpaabs +
                ", Mpagage=" + Mpagage +
                '}';
    }
}

