package com.piotrdulewski.rocketryapp.calculating;

import com.piotrdulewski.rocketryapp.calculating.dto.GrainKnDTO;
import com.piotrdulewski.rocketryapp.domain.BasicDataDomain;

import java.util.ArrayList;
import java.util.List;

class CalculateGrainAndKn {

    final static double X_INC = 0.49f;

    public List<GrainKnDTO> calculateGrainAndKn(BasicDataDomain basicDataDomain) {

        List<GrainKnDTO> calculation = new ArrayList<>();
        GrainKnDTO grainKnDTO = new GrainKnDTO();
        grainKnDTO.Dt = basicDataDomain.getDt();
        grainKnDTO.x = 0.0;
        grainKnDTO.d = basicDataDomain.getGrainCanalDiameter() + basicDataDomain.getIsCanalExposed()
                * (2 * grainKnDTO.x);
        grainKnDTO.D = basicDataDomain.getGrainOutsideDiameter();
        grainKnDTO.L = basicDataDomain.getGrainLength()
                * basicDataDomain.getNumberOfGrains() - basicDataDomain.getIsEndExposed()
                * (2 * basicDataDomain.getNumberOfGrains() * grainKnDTO.x);
        grainKnDTO.tweb = (grainKnDTO.D - grainKnDTO.d) / 2;
        grainKnDTO.Abe = (basicDataDomain.getIsEndExposed() * 2
                * basicDataDomain.getNumberOfGrains() * Math.PI) / 4
                * (Math.pow(grainKnDTO.D, 2) - Math.pow(grainKnDTO.d, 2));
        grainKnDTO.Abc = 1 * Math.PI * grainKnDTO.d * grainKnDTO.L;
        grainKnDTO.abs = 0 * Math.PI * grainKnDTO.D * grainKnDTO.L;
        grainKnDTO.abtotal = grainKnDTO.Abe + grainKnDTO.Abc + grainKnDTO.abs;
        grainKnDTO.At = Math.PI / 4 * Math.pow((basicDataDomain.getDt() + 0 / grainKnDTO.tweb), 2);
        grainKnDTO.Kn = grainKnDTO.abtotal / grainKnDTO.At;
        calculation.add(grainKnDTO);
        GrainKnDTO grainKnDTOPrevious = grainKnDTO;

        while (true) {
            GrainKnDTO grainKnDTOL = new GrainKnDTO();

            grainKnDTOL.x = grainKnDTOPrevious.x + X_INC;
            grainKnDTOL.d = basicDataDomain.getGrainCanalDiameter() + basicDataDomain.getIsCanalExposed()
                    * (2 * grainKnDTOL.x);
            grainKnDTOL.D = basicDataDomain.getGrainOutsideDiameter();
            grainKnDTOL.L = basicDataDomain.getGrainLength() *
                    basicDataDomain.getNumberOfGrains() - basicDataDomain.getIsEndExposed() *
                    (2 * basicDataDomain.getNumberOfGrains() * grainKnDTOL.x);
            grainKnDTOL.tweb = (grainKnDTOL.D - grainKnDTOL.d) / 2;
            grainKnDTOL.Abe = (basicDataDomain.getIsEndExposed() * 2 *
                    basicDataDomain.getNumberOfGrains() * Math.PI) / 4 *
                    (Math.pow(grainKnDTOL.D, 2) - Math.pow(grainKnDTOL.d, 2));
            grainKnDTOL.Abc = 1 * Math.PI * grainKnDTOL.d * grainKnDTOL.L;
            grainKnDTOL.abs = 0 * Math.PI * grainKnDTOL.D * grainKnDTOL.L;
            grainKnDTOL.abtotal = grainKnDTOL.Abe + grainKnDTOL.Abc + grainKnDTOL.abs;
            grainKnDTOL.At = Math.PI / 4 * Math.pow((basicDataDomain.getDt() + basicDataDomain.getIsOutsideExposed()
                    * (grainKnDTO.tweb - grainKnDTOL.tweb) / grainKnDTO.tweb), 2);
            grainKnDTOL.Kn = grainKnDTOL.abtotal / grainKnDTOL.At;
            calculation.add(grainKnDTOL);
            grainKnDTOPrevious = grainKnDTOL;
            if (grainKnDTOL.d >= grainKnDTOL.D) break;
        }

        return calculation;
    }

}
