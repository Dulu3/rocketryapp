package com.piotrdulewski.rocketryapp.calculating.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ThrustDTO {

    public Double Paabs;
    public Double A;
    public Double At;
    public Double AeAt;
    public Double Pe;
    public Double opt;
    public Double Cf;
    public Double Fn;
    public Double Flb;
    public Double t;
    public Double It;
    public Double Me;

    @Override
    public String toString() {
        return "ThrustDTO{" +
                "Paabs=" + Paabs +
                ", A=" + A +
                ", At=" + At +
                ", AeAt=" + AeAt +
                ", Pe=" + Pe +
                ", opt=" + opt +
                ", Cf=" + Cf +
                ", Fn=" + Fn +
                ", Flb=" + Flb +
                ", t=" + t +
                ", It=" + It +
                ", Me=" + Me +
                '}';
    }
}
