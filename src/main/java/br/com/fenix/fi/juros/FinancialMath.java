package br.com.fenix.fi.juros;
import java.util.Arrays;

public class FinancialMath {
    // Enum for payment type
    public enum PaymentType {
        POSTECIPADO, ANTECIPADO
    }

    // Class equivalent to Pascal record Mat
    public static class CashFlow {
        public double value;
        public int initialPeriod;
        public boolean initialPeriodSet;
        public int numberOfPeriods;
        public int interval;

        public CashFlow(double value, int initialPeriod, boolean initialPeriodSet, int numberOfPeriods, int interval) {
            this.value = value;
            this.initialPeriod = initialPeriod;
            this.initialPeriodSet = initialPeriodSet;
            this.numberOfPeriods = numberOfPeriods;
            this.interval = interval;
        }
    }

    // Constants
    private static final int MAX_FIN = 100; // Assumed maximum size for MatFin array
    private static final int MAX_TEN = 1000; // Assumed maximum iterations
    private static final double ERRO = 0.00000001; // Assumed error tolerance
    private static final String ERR_MAT_FIN = "Financial calculation error";

    // Global variables
    private static CashFlow[] matFin = new CashFlow[MAX_FIN + 1];
    private static int numTermo = 0;

    // Custom exception
    public static class FinancialException extends Exception {
        public FinancialException(String message) {
            super(message);
        }
    }

    // Fator de Valor Atual (Present Value Factor)
    public static double fatu(double n, double i) throws FinancialException {
        double aux;
        try {
            aux = (1 - Math.pow(1 + i, -n)) / i;
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in FATU: " + e.getMessage());
        }
        return aux;
    }

    // Fator de Valor Futuro (Future Value Factor)
    public static double fva(double n, double i) throws FinancialException {
        double aux;
        try {
            aux = (Math.pow(1 + i, n) - 1) / (Math.pow(1 + i, n) * i);
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in FVA: " + e.getMessage());
        }
        return aux;
    }

    // General financial calculation function
    public static double fx(double n, double i, double pv, double pmt, double fv, PaymentType s, boolean c) throws FinancialException {
        double aux;
        i = i / 100;
        int inicio = (s == PaymentType.POSTECIPADO) ? 0 : 1;

        try {
            if (Math.abs(n - Math.floor(n)) < ERRO) { // Non-fractional period
                aux = pv + (1 + inicio * i) * pmt * fva(n, i) + fv * Math.pow(1 + i, -n);
            } else {
                if (c) { // Compound interest for fractional period
                    aux = pv * Math.pow(1 + i, n - Math.floor(n)) +
                          (1 + inicio * i) * pmt * fva(Math.floor(n), i) +
                          fv * Math.pow(1 + i, -Math.floor(n));
                } else { // Simple interest for fractional period
                    aux = pv * (1 + i * (n - Math.floor(n))) +
                          (1 + inicio * i) * pmt * fva(Math.floor(n), i) +
                          fv * Math.pow(1 + i, -Math.floor(n));
                }
            }
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in FX: " + e.getMessage());
        }
        return aux;
    }

    // Simplified financial calculation for integer periods
    public static double fxN(double n, double i, double pv, double pmt, double fv, PaymentType s) throws FinancialException {
        double aux;
        i = i / 100;
        int inicio = (s == PaymentType.POSTECIPADO) ? 0 : 1;

        try {
            aux = pv + (1 + inicio * i) * pmt * fva(n, i) + fv * Math.pow(1 + i, -n);
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in FX_N: " + e.getMessage());
        }
        return aux;
    }

    // Calculate interest rate using bisection method
    public static double raizI(double n, double pv, double pmt, double fv, PaymentType s, boolean c) throws FinancialException {
        double taxaA = -99.99999999;
        double taxaB = 30.01;
        double taxaC = 0.0;
        int nro = 0;

        try {
            // Find interval where root exists
            while (fx(n, taxaA, pv, pmt, fv, s, c) * fx(n, taxaB, pv, pmt, fv, s, c) > 0 && nro < MAX_TEN) {
                taxaB *= 100;
                nro++;
            }
            if (nro >= MAX_TEN) throw new FinancialException(ERR_MAT_FIN);

            nro = 0;
            while (Math.abs(taxaA - taxaB) > ERRO && nro < MAX_TEN) {
                nro++;
                double aFx = fx(n, taxaA, pv, pmt, fv, s, c);
                if (Math.abs(aFx) < ERRO) return taxaA;

                double bFx = fx(n, taxaB, pv, pmt, fv, s, c);
                if (Math.abs(bFx) < ERRO) return taxaB;

                if (aFx * bFx < 0) {
                    taxaC = (taxaA + taxaB) / 2;
                    if (aFx * fx(n, taxaC, pv, pmt, fv, s, c) < 0) {
                        taxaB = taxaC;
                    } else {
                        taxaA = taxaC;
                    }
                }
            }
            if (nro >= MAX_TEN) throw new FinancialException(ERR_MAT_FIN);
            return (taxaA + taxaB) / 2;
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in Raiz_I: " + e.getMessage());
        }
    }

    // Calculate payment (PMT) using bisection method
    public static double raizPmt(double n, double i, double pv, double fv, PaymentType s, boolean c) throws FinancialException {
        double pmtA = -10.0;
        double pmtB = 10.0;
        double pmtC = 0.0;
        int nro = 0;

        try {
            while (fx(n, i, pv, pmtA, fv, s, c) * fx(n, i, pv, pmtB, fv, s, c) > 0 && nro < MAX_TEN) {
                pmtA *= 100;
                pmtB *= 100;
                nro++;
            }
            if (nro >= MAX_TEN) throw new FinancialException(ERR_MAT_FIN);

            nro = 0;
            while (Math.abs(pmtA - pmtB) > ERRO && nro < MAX_TEN) {
                nro++;
                double aAux = fx(n, i, pv, pmtA, fv, s, c);
                if (Math.abs(aAux) < ERRO) return pmtA;

                double bAux = fx(n, i, pv, pmtB, fv, s, c);
                if (Math.abs(bAux) < ERRO) return pmtB;

                if (aAux * bAux < 0) {
                    pmtC = (pmtA + pmtB) / 2;
                    if (aAux * fx(n, i, pv, pmtC, fv, s, c) < 0) {
                        pmtB = pmtC;
                    } else {
                        pmtA = pmtC;
                    }
                }
            }
            return (pmtA + pmtB) / 2;
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in Raiz_PMT: " + e.getMessage());
        }
    }

    // Calculate present value (PV) using bisection method
    public static double raizPv(double n, double i, double pmt, double fv, PaymentType s, boolean c) throws FinancialException {
        double pvA = -10.0;
        double pvB = 10.0;
        double pvC = 0.0;
        int nro = 0;

        try {
            while (fx(n, i, pvA, pmt, fv, s, c) * fx(n, i, pvB, pmt, fv, s, c) > 0 && nro < MAX_TEN) {
                pvA *= 100;
                pvB *= 100;
                nro++;
            }
            if (nro >= MAX_TEN) throw new FinancialException(ERR_MAT_FIN);

            nro = 0;
            while (Math.abs(pvA - pvB) > ERRO && nro < MAX_TEN) {
                nro++;
                double aAux = fx(n, i, pvA, pmt, fv, s, c);
                if (Math.abs(aAux) < ERRO) return pvA;

                double bAux = fx(n, i, pvB, pmt, fv, s, c);
                if (Math.abs(bAux) < ERRO) return pvB;

                if (aAux * bAux < 0) {
                    pvC = (pvA + pvB) / 2;
                    if (aAux * fx(n, i, pvC, pmt, fv, s, c) < 0) {
                        pvB = pvC;
                    } else {
                        pvA = pvC;
                    }
                }
            }
            return (pvA + pvB) / 2;
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in Raiz_PV: " + e.getMessage());
        }
    }

    // Calculate future value (FV) using bisection method
    public static double raizFv(double n, double i, double pv, double pmt, PaymentType s, boolean c) throws FinancialException {
        double fvA = -10.0;
        double fvB = 10.0;
        double fvC = 0.0;
        int nro = 0;

        try {
            while (fx(n, i, pv, pmt, fvA, s, c) * fx(n, i, pv, pmt, fvB, s, c) > 0 && nro < MAX_TEN) {
                fvA *= 100;
                fvB *= 100;
                nro++;
            }
            if (nro >= MAX_TEN) throw new FinancialException(ERR_MAT_FIN);

            nro = 0;
            while (Math.abs(fvA - fvB) > ERRO && nro < MAX_TEN) {
                nro++;
                double aAux = fx(n, i, pv, pmt, fvA, s, c);
                if (Math.abs(aAux) < ERRO) return fvA;

                double bAux = fx(n, i, pv, pmt, fvB, s, c);
                if (Math.abs(bAux) < ERRO) return fvB;

                if (aAux * bAux < 0) {
                    fvC = (fvA + fvB) / 2;
                    if (aAux * fx(n, i, pv, pmt, fvC, s, c) < 0) {
                        fvB = fvC;
                    } else {
                        fvA = fvC;
                    }
                }
            }
            return (fvA + fvB) / 2;
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in Raiz_FV: " + e.getMessage());
        }
    }

    // Calculate number of periods using bisection method
    public static double raizN(double i, double pv, double pmt, double fv, PaymentType s, boolean c) throws FinancialException {
        double nA = 0;
        double nB = 10.99;
        double nC = 0.0;
        int nro = 0;

        try {
            while (fx(nA, i, pv, pmt, fv, s, c) * fx(nB, i, pv, pmt, fv, s, c) > 0 && nro <= MAX_TEN) {
                nB *= 100;
                nro++;
            }
            if (nro >= MAX_TEN) throw new FinancialException(ERR_MAT_FIN);

            nro = 0;
            while (Math.abs(nA - nB) > ERRO && nro < MAX_TEN) {
                nro++;
                double aFx = fxN(nA, i, pv, pmt, fv, s);
                if (Math.abs(aFx) < ERRO) return nA;

                double bFx = fxN(nB, i, pv, pmt, fv, s);
                if (Math.abs(bFx) < ERRO) return nB;

                if (aFx * bFx < 0) {
                    nC = (nA + nB) / 2;
                    if (aFx * fxN(nC, i, pv, pmt, fv, s) < 0) {
                        nB = nC;
                    } else {
                        nA = nC;
                    }
                }
            }
            return (nA + nB) / 2;
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in Raiz_N: " + e.getMessage());
        }
    }

    // Calculate number of periods
    public static double numeroDePeriodo(double i, double pv, double pmt, double fv, PaymentType s, boolean c) throws FinancialException {
        if (i <= -100) throw new FinancialException(ERR_MAT_FIN);
        double auxN = raizN(i, pv, pmt, fv, s, c);
        return (auxN - Math.floor(auxN) >= 0.005) ? Math.floor(auxN) + 1 : Math.round(auxN);
    }

    // Calculate interest rate
    public static double taxa(double n, double pv, double pmt, double fv, PaymentType s, boolean c) throws FinancialException {
        if (n <= 0) throw new FinancialException(ERR_MAT_FIN);
        double auxI = raizI(n, pv, pmt, fv, s, c);
        return (auxI < 1E-8) ? 0 : auxI;
    }

    // Calculate payment (PMT)
    public static double valorPrestacao(double n, double i, double pv, double fv, PaymentType s, boolean c) throws FinancialException {
        if (n == 0 || i == 0 || i <= -100) throw new FinancialException(ERR_MAT_FIN);
        int inicio = (s == PaymentType.POSTECIPADO) ? 0 : 1;
        i = i / 100;

        double auxPmt;
        try {
            if (c) {
                auxPmt = -1 * ((pv * Math.pow(1 + i, n - Math.floor(n)) + fv * Math.pow(1 + i, -Math.floor(n))) /
                              ((1 + inicio * i) * fatu(Math.floor(n), i)));
            } else {
                auxPmt = -1 * ((pv * (1 + i * (n - Math.floor(n))) + fv * Math.pow(1 + i, -Math.floor(n))) /
                              ((1 + inicio * i) * fatu(Math.floor(n), i)));
            }
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in ValorPrestacao: " + e.getMessage());
        }
        return auxPmt;
    }

    // Calculate present value
    public static double valorPresente(double n, double i, double pmt, double fv, PaymentType s, boolean c) throws FinancialException {
        if (i <= -100) throw new FinancialException(ERR_MAT_FIN);
        int inicio = (s == PaymentType.POSTECIPADO) ? 0 : 1;
        i = i / 100;

        double auxPv;
        try {
            if (c) {
                auxPv = -1 * ((1 + inicio * i) * pmt * fatu(Math.floor(n), i) + fv * Math.pow(1 + i, -Math.floor(n))) /
                        Math.pow(1 + i, n - Math.floor(n));
            } else {
                auxPv = -1 * ((1 + inicio * i) * pmt * fatu(Math.floor(n), i) + fv * Math.pow(1 + i, -Math.floor(n))) /
                        (1 + i * (n - Math.floor(n)));
            }
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in ValorPresente: " + e.getMessage());
        }
        return auxPv;
    }

    // Calculate future value
    public static double valorFuturo(double n, double i, double pv, double pmt, PaymentType s, boolean c) throws FinancialException {
        if (i <= -100) throw new FinancialException(ERR_MAT_FIN);
        int inicio = (s == PaymentType.POSTECIPADO) ? 0 : 1;
        i = i / 100;

        double auxFv;
        try {
            if (c) {
                auxFv = -1 * (pv * Math.pow(1 + i, n - Math.floor(n)) + (1 + inicio * i) * pmt * fatu(Math.floor(n), i)) /
                        Math.pow(1 + i, -Math.floor(n));
            } else {
                auxFv = -1 * (pv * (1 + i * (n - Math.floor(n))) + (1 + inicio * i) * pmt * fatu(Math.floor(n), i)) /
                        Math.pow(1 + i, -Math.floor(n));
            }
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in ValorFuturo: " + e.getMessage());
        }
        return auxFv;
    }

    // Calculate Internal Rate of Return (IRR)
    public static double irr() throws FinancialException {
        double taxaA = -99.99999999;
        double taxaB = 30.01;
        double taxaC = 0.0;
        int nro = 0;

        try {
            while (npv(taxaA) * npv(taxaB) > 0 && nro < MAX_TEN) {
                taxaB *= 100;
                nro++;
            }
            if (nro >= MAX_TEN) throw new FinancialException(ERR_MAT_FIN);

            nro = 0;
            while (Math.abs(taxaA - taxaB) > ERRO && nro < MAX_TEN) {
                nro++;
                if (Math.abs(npv(taxaA)) < ERRO) return taxaA;
                if (Math.abs(npv(taxaB)) < ERRO) return taxaB;

                if (npv(taxaA) * npv(taxaB) < 0) {
                    taxaC = (taxaA + taxaB) / 2;
                    if (npv(taxaA) * npv(taxaC) < 0) {
                        taxaB = taxaC;
                    } else {
                        taxaA = taxaC;
                    }
                }
            }
            if (nro >= MAX_TEN) throw new FinancialException(ERR_MAT_FIN);
            taxaC = (taxaA + taxaB) / 2;
            return (taxaC < 1E-8) ? 0 : taxaC;
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in IRR: " + e.getMessage());
        }
    }

    // Calculate Net Present Value (NPV)
    public static double npv(double i) throws FinancialException {
        if (i <= -100) throw new FinancialException(ERR_MAT_FIN);
        double total = 0;
        double auxI = (i / 100) + 1;

        try {
            for (int ind = 0; ind <= numTermo; ind++) {
                double vNro = matFin[ind].initialPeriod;
                for (int count = 1; count <= matFin[ind].numberOfPeriods; count++) {
                    total += matFin[ind].value / Math.pow(auxI, vNro);
                    vNro += matFin[ind].interval;
                }
            }
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in NPV: " + e.getMessage());
        }
        return total;
    }

    // Calculate simple interest (360-day basis)
    public static double juroSimples360(double n, double i, double pv) throws FinancialException {
        try {
            i = i / 100;
            return (pv * i * n) / 360;
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in JuroSimples360: " + e.getMessage());
        }
    }

    // Calculate simple interest (365-day basis)
    public static double juroSimples365(double n, double i, double pv) throws FinancialException {
        try {
            i = i / 100;
            return (pv * i * n) / 365;
        } catch (ArithmeticException e) {
            throw new FinancialException("Arithmetic error in JuroSimples365: " + e.getMessage());
        }
    }
}