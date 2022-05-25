package com.piotrdulewski.rocketryapp.calculating;

import com.piotrdulewski.rocketryapp.calculating.domain.Fuels;
import com.piotrdulewski.rocketryapp.calculating.dto.ChamberPressureDTO;
import com.piotrdulewski.rocketryapp.domain.BasicDataDomain;

import java.util.ArrayList;
import java.util.List;

class CalculateChamberPressure {
    private final static double X_INC = 0.0294;
    private static final double GAS_CONST = 8314d; //J/mol-K

    public List<ChamberPressureDTO> calculateChamberPressure(BasicDataDomain basicDataDomain) {

        Fuels fuel = basicDataDomain.getFuel();
        List<ChamberPressureDTO> list = new ArrayList<>();

        ChamberPressureDTO chamberPressureDTO = new ChamberPressureDTO();
        chamberPressureDTO.xi = 0d;
        chamberPressureDTO.Po = basicDataDomain.getAtmosphericPressure();
        chamberPressureDTO.d = basicDataDomain.getGrainCanalDiameter();
        chamberPressureDTO.D = basicDataDomain.getGrainOutsideDiameter();
        chamberPressureDTO.L = basicDataDomain.getGrainLength()
                * basicDataDomain.getNumberOfGrains();
        chamberPressureDTO.tweb = (chamberPressureDTO.D - chamberPressureDTO.d) / 2;
        chamberPressureDTO.At = Math.PI / 4
                * Math.pow((basicDataDomain.getDt() + basicDataDomain.getNozzleErosion()
                * 0.0 / chamberPressureDTO.tweb), 2);
        chamberPressureDTO.A = chamberPressureDTO.At / 1000000;
        chamberPressureDTO.Ai = 0d;
        chamberPressureDTO.Aduct = Math.PI / 4
                * Math.pow(basicDataDomain.getChamberDiameter(), 2) - Math.PI / 4
                * (Math.pow(chamberPressureDTO.D, 2) - Math.pow(chamberPressureDTO.d, 2));
        chamberPressureDTO.Aat = chamberPressureDTO.Aduct / chamberPressureDTO.At;
        chamberPressureDTO.G = basicDataDomain.getErosiveBurningArea() - chamberPressureDTO.Aat;
        chamberPressureDTO.a = fuel.getBurnRate(basicDataDomain.getAtmosphericPressure());
        chamberPressureDTO.n = fuel.getN(basicDataDomain.getAtmosphericPressure());
        chamberPressureDTO.r = (1 + 0.0 * chamberPressureDTO.G) * chamberPressureDTO.a
                * Math.pow(basicDataDomain.getAtmosphericPressure(), chamberPressureDTO.n);
        chamberPressureDTO.t = 0.0;
        chamberPressureDTO.VgrainMM = Math.PI / 4
                * (Math.pow(chamberPressureDTO.D, 2) - Math.pow(chamberPressureDTO.d, 2))
                * chamberPressureDTO.L;
        chamberPressureDTO.VgranM = chamberPressureDTO.VgrainMM / Math.pow(1000, 3);
        chamberPressureDTO.Vfree = basicDataDomain.getVc() / Math.pow(1000, 3) - chamberPressureDTO.VgranM;
        chamberPressureDTO.Mgrain = fuel.getRealDensity(basicDataDomain.getGrainPercent())
                * chamberPressureDTO.VgrainMM / 1_000_000;
        chamberPressureDTO.Mgen = 0d;
        chamberPressureDTO.Mnoz = 0d;
        chamberPressureDTO.Msto = 0d;
        chamberPressureDTO.MassSto = 0d;
        chamberPressureDTO.Pprod = 0d;
        chamberPressureDTO.Paabs = chamberPressureDTO.Pprod * GAS_CONST / fuel.getMolMassBurn()
                * basicDataDomain.getRealBurnTemp() + basicDataDomain.getAtmosphericPressure() * 1_000_000;
        chamberPressureDTO.mpaabs = chamberPressureDTO.Paabs / 1_000_000;
        chamberPressureDTO.Po = chamberPressureDTO.mpaabs;
        chamberPressureDTO.Mpagage = chamberPressureDTO.Po - basicDataDomain.getAtmosphericPressure();
        ChamberPressureDTO chamberPressureDTOPrevious = chamberPressureDTO;
        list.add(chamberPressureDTO);


        while (true) {
            ChamberPressureDTO chamberPressureDTOL = new ChamberPressureDTO();
            chamberPressureDTOL.xi = chamberPressureDTOPrevious.xi + X_INC;
            chamberPressureDTOL.d = chamberPressureDTOPrevious.d + basicDataDomain.getIsCanalExposed() * (2 * X_INC);
            chamberPressureDTOL.D = chamberPressureDTOPrevious.D - basicDataDomain.getIsOutsideExposed() * 2 * X_INC;
            chamberPressureDTOL.L = chamberPressureDTOPrevious.L - basicDataDomain.getIsEndExposed()
                    * 2 * basicDataDomain.getNumberOfGrains() * X_INC;
            chamberPressureDTOL.tweb = (chamberPressureDTOL.D - chamberPressureDTOL.d) / 2;
            chamberPressureDTOL.At = calculateAt(chamberPressureDTO, chamberPressureDTOL, basicDataDomain);
            chamberPressureDTOL.A = chamberPressureDTOL.At / 1000000;
            chamberPressureDTOL.Aduct = calculateAduct(basicDataDomain, chamberPressureDTOL);
            chamberPressureDTOL.Aat = chamberPressureDTOL.Aduct / chamberPressureDTOL.At;
            chamberPressureDTOL.G = calculateG(chamberPressureDTOL, basicDataDomain);
            chamberPressureDTOL.a = fuel.getBurnRate(chamberPressureDTOPrevious.Po);
            chamberPressureDTOL.n = fuel.getN(chamberPressureDTOPrevious.Po);
            chamberPressureDTOL.r = calculate_r(chamberPressureDTOL,chamberPressureDTOPrevious);
            chamberPressureDTOL.t = X_INC / chamberPressureDTOL.r + chamberPressureDTOPrevious.t;
            chamberPressureDTOL.VgrainMM = calculateVgrainMM(chamberPressureDTOL);
            chamberPressureDTOL.VgranM = chamberPressureDTOL.VgrainMM / 1_000_000_000;
            chamberPressureDTOL.Vfree = basicDataDomain.getVc() / 1_000_000_000 - chamberPressureDTOL.VgranM;
            chamberPressureDTOL.Mgrain = fuel.getRealDensity(basicDataDomain.getGrainPercent())
                    * chamberPressureDTOL.VgrainMM / 1_000_000;
            chamberPressureDTOL.Mgen = (chamberPressureDTOPrevious.Mgrain - chamberPressureDTOL.Mgrain)
                    / (chamberPressureDTOL.t - chamberPressureDTOPrevious.t);
            chamberPressureDTOL.Ai = calculateAi(chamberPressureDTOL, chamberPressureDTOPrevious, basicDataDomain, fuel);
            chamberPressureDTOL.Mnoz = calculateMnoz(chamberPressureDTOL, chamberPressureDTOPrevious);
            chamberPressureDTOL.Msto = chamberPressureDTOL.Mgen - chamberPressureDTOL.Mnoz;
            chamberPressureDTOL.MassSto = calculateMassSto(chamberPressureDTOL, chamberPressureDTOPrevious);
            chamberPressureDTOL.Pprod = chamberPressureDTOL.MassSto / chamberPressureDTOL.Vfree;
            chamberPressureDTOL.Paabs = calculatePaabs(chamberPressureDTOL, fuel.getMolMassBurn(),
                    basicDataDomain.getAtmosphericPressure(), basicDataDomain.getRealBurnTemp());
            chamberPressureDTOL.mpaabs = chamberPressureDTOL.Paabs / 1_000_000;
            chamberPressureDTOL.Po = chamberPressureDTOL.mpaabs;
            chamberPressureDTOL.Mpagage = chamberPressureDTOL.Po - basicDataDomain.getAtmosphericPressure();
            if (chamberPressureDTOL.tweb <= 0) {
                chamberPressureDTOL.Mgrain = 0d;
                chamberPressureDTOL.tweb = 0d;
                chamberPressureDTOL.VgrainMM = 0d;
                chamberPressureDTOL.VgranM = 0d;
                chamberPressureDTOL.Msto = 0d;
                list.add(chamberPressureDTOL);
                break;
            }
            list.add(chamberPressureDTOL);
            chamberPressureDTOPrevious = chamberPressureDTOL;
        }

        ChamberPressureDTO chamberPressureDTOLast = list.get(list.size() - 1);

        while (true) {
            ChamberPressureDTO chamberPressureDTOL = new ChamberPressureDTO();
            ChamberPressureDTO chamberPressureDTOP = list.get(list.size() - 1);
            chamberPressureDTOL.t = chamberPressureDTOP.t + 0.000622;
            chamberPressureDTOL.mpaabs = calculatePaabsAfterBurn(chamberPressureDTOL,chamberPressureDTOLast, basicDataDomain, fuel);
            chamberPressureDTOL.Mpagage = chamberPressureDTOL.mpaabs - basicDataDomain.getAtmosphericPressure();
            list.add(chamberPressureDTOL);
            if (chamberPressureDTOL.Mpagage <= 0.7) break;
        }



        return list;
    }

    private Double calculatePaabsAfterBurn(ChamberPressureDTO chamberPressureDTOL, ChamberPressureDTO chamberPressureDTOLast, BasicDataDomain basicDataDomain, Fuels fuel) {
        return chamberPressureDTOLast.mpaabs * Math.exp(((-(GAS_CONST / basicDataDomain.getRealBurnTemp()))
                * basicDataDomain.getRealBurnTemp()) * chamberPressureDTOLast.A
                * (chamberPressureDTOL.t - chamberPressureDTOLast.t) / basicDataDomain.getVc()
                * 1_000_000_000 / calculateC(GAS_CONST / basicDataDomain.getRealBurnTemp(), fuel.getHeatMixtureRatio(), basicDataDomain.getRealBurnTemp()));
    }

    private Double calculateVgrainMM(ChamberPressureDTO chamberPressureDTOL) {
        return Math.PI / 4 * (Math.pow(chamberPressureDTOL.D, 2) - Math.pow(chamberPressureDTOL.d, 2)) * chamberPressureDTOL.L;
    }

    private Double calculate_r(ChamberPressureDTO chamberPressureDTOL, ChamberPressureDTO chamberPressureDTOPrevious) {
        return ((1 + 0.0 * chamberPressureDTOL.G) * chamberPressureDTOL.a)
                * Math.pow(chamberPressureDTOPrevious.Po, chamberPressureDTOL.n);
    }

    private Double calculateG(ChamberPressureDTO chamberPressureDTOL, BasicDataDomain basicDataDomain) {
        if (basicDataDomain.getErosiveBurningArea() - chamberPressureDTOL.Aat <= 0) {
            return 0d;
        } else {
            return basicDataDomain.getErosiveBurningArea() - chamberPressureDTOL.Aat;
        }
    }

    private static double calculateAduct(BasicDataDomain basicDataDomain, ChamberPressureDTO chamberPressureDTOL) {
        return Math.PI / 4 * Math.pow(basicDataDomain.getChamberDiameter(), 2) - Math.PI / 4
                * (Math.pow(chamberPressureDTOL.D, 2) - Math.pow(chamberPressureDTOL.d, 2));
    }

    private static double calculateAt(ChamberPressureDTO chamberPressureDTO, ChamberPressureDTO chamberPressureDTOL, BasicDataDomain basicDataDomain) {
        return Math.PI / 4
                * Math.pow((basicDataDomain.getDt() + basicDataDomain.getNozzleErosion() *
                (chamberPressureDTO.tweb - chamberPressureDTOL.tweb) / chamberPressureDTO.tweb), 2);
    }

    private static double calculateAi(ChamberPressureDTO chamberPressureDTOL, ChamberPressureDTO chamberPressureDTOP, BasicDataDomain basicDataDomain, Fuels fuel) {
        return (chamberPressureDTOP.Po - basicDataDomain.getAtmosphericPressure()) * 1_000_000 *
                chamberPressureDTOL.A
                / Math.sqrt((GAS_CONST / fuel.getMolMassBurn()) * basicDataDomain.getRealBurnTemp())
                * Math.sqrt(fuel.getHeatMixtureRatio())
                * Math.pow((2 / (fuel.getHeatMixtureRatio() + 1)), ((fuel.getHeatMixtureRatio() + 1) / 2) / (fuel.getHeatMixtureRatio() - 1));
    }

    private static double calculateMnoz(ChamberPressureDTO chamberPressureDTO, ChamberPressureDTO chamberPressureDTOP) {
        if (chamberPressureDTO.Mgen < chamberPressureDTO.Ai) {
            return chamberPressureDTO.Ai;
        } else if (chamberPressureDTOP.Po > 0) {
            return chamberPressureDTO.Ai;
        } else {
            return 0d;
        }
    }

    private static double calculateMassSto(ChamberPressureDTO chamberPressureDTOL, ChamberPressureDTO chamberPressureDTOPrevious) {
        return chamberPressureDTOL.Msto * (chamberPressureDTOL.t - chamberPressureDTOPrevious.t) + chamberPressureDTOPrevious.MassSto;
    }

    private static double calculatePaabs(ChamberPressureDTO chamberPressureDTO,
                                         double mollMassBurn, double atmosphericPressure,
                                         double REAL_BURN_TEMP) {
        return chamberPressureDTO.Pprod * GAS_CONST / mollMassBurn
                * REAL_BURN_TEMP + atmosphericPressure * 1_000_000;
    }

    private static double calculateC(double r, double k, double to) {
        return Math.sqrt(r * to / k * Math.pow((k + 1) / 2, (k + 1) / (k - 1)));
    }
}
