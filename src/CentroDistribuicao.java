public class CentroDistribuicao {
    public enum SITUACAO {
        NORMAL, SOBRAVISO, EMERGENCIA
    }

    public enum TIPOPOSTO {
        COMUM, ESTRATEGICO
    }

    public static final int MAX_ADITIVO = 500;
    public static final int MAX_ALCOOL = 2500;
    public static final int MAX_GASOLINA = 10000;
    private int tGasolina, tAditivo, tAlcool1, tAlcool2;
    private SITUACAO sitCentro;

    public CentroDistribuicao(int tAditivo, int tGasolina, int tAlcool1, int tAlcool2) {
        if (tGasolina < 0 || tGasolina > MAX_GASOLINA || tAditivo < 0 || tAditivo > MAX_ADITIVO || tAlcool1 < 0
                || tAlcool2 < 0 || tAlcool1 + tAlcool2 > MAX_ALCOOL || tAlcool1 != tAlcool2) {
            throw new IllegalArgumentException("Quantidade inválida de um ou mais componentes.");
        }
        this.tGasolina = tGasolina;
        this.tAditivo = tAditivo;
        this.tAlcool1 = tAlcool1;
        this.tAlcool2 = tAlcool2;
        defineSituacao();
    }

    public void defineSituacao() {
        if (tGasolina >= MAX_GASOLINA / 2 && tAditivo >= MAX_ADITIVO / 2 && tAlcool1 >= MAX_ALCOOL / 4
                && tAlcool2 >= MAX_ALCOOL / 4 && tAlcool1 == tAlcool2) {
            sitCentro = SITUACAO.NORMAL;
        } else if (tGasolina >= MAX_GASOLINA / 4 && tAditivo >= MAX_ADITIVO / 4 && tAlcool1 >= MAX_ALCOOL / 8
                && tAlcool2 >= MAX_ALCOOL / 8 && tAlcool1 == tAlcool2) {
            sitCentro = SITUACAO.SOBRAVISO;
        } else if (tGasolina < MAX_GASOLINA / 4 || tAditivo < MAX_ADITIVO / 4 || tAlcool1 < MAX_ALCOOL / 8
                || tAlcool2 < MAX_ALCOOL / 8 && tAlcool1 == tAlcool2) {
            sitCentro = SITUACAO.EMERGENCIA;
        }
    }

    public SITUACAO getSituacao() {
        return sitCentro;
    }

    public int gettGasolina() {
        return tGasolina;
    }

    public int gettAditivo() {
        return tAditivo;
    }

    public int gettAlcool1() {
        return tAlcool1;
    }

    public int gettAlcool2() {
        return tAlcool2;
    }

    public int recebeAditivo(int qtdade) {
        if (qtdade <= 0) {
            return -1;
        }

        int espacoTanque = MAX_ADITIVO - tAditivo;
        if (qtdade >= espacoTanque) {
            tAditivo = MAX_ADITIVO;
            defineSituacao();
            return espacoTanque;
        } else
            tAditivo += qtdade;
        defineSituacao();
        return qtdade;
    }

    public int recebeGasolina(int qtdade) {
        if (qtdade <= 0) {
            return -1;
        }

        int espacoTanque = MAX_GASOLINA - tGasolina;
        if (qtdade >= espacoTanque) {
            tGasolina = MAX_GASOLINA;
            defineSituacao();
            return espacoTanque;
        } else
            tGasolina += qtdade;
        defineSituacao();
        return qtdade;
    }

    public int recebeAlcool(int qtdade) {
        if (qtdade <= 0) {
            return -1;
        }

        int espacoTanque = MAX_ALCOOL - (tAlcool1 + tAlcool2);
        if (qtdade >= espacoTanque) {
            tAlcool1 = MAX_ALCOOL / 2;
            tAlcool2 = MAX_ALCOOL / 2;
            defineSituacao();
            return espacoTanque;
        } else
            tAlcool1 += qtdade / 2;
        tAlcool2 += qtdade / 2;
        defineSituacao();
        return qtdade;
    }

    public int[] encomendaCombustivel(int qtdade, TIPOPOSTO tipoPosto) {
        int[] encomenda = new int[4];
        int gasolinaEncomendada = 0;
        int aditivoEncomendado = 0;
        int alcoolEncomendado = 0;
        int erro = 0;

        if (getSituacao() == SITUACAO.NORMAL) {
            aditivoEncomendado = (int) (qtdade * 0.05);
            gasolinaEncomendada = (int) (qtdade * 0.7);
            alcoolEncomendado = (int) (qtdade * 0.25);
        }

        if (getSituacao() == SITUACAO.SOBRAVISO && tipoPosto == TIPOPOSTO.COMUM) {
            aditivoEncomendado = (int) (qtdade * 0.05) / 2;
            gasolinaEncomendada = (int) (qtdade * 0.7) / 2;
            alcoolEncomendado = (int) (qtdade * 0.25) / 2;
        }

        if (getSituacao() == SITUACAO.SOBRAVISO && tipoPosto == TIPOPOSTO.ESTRATEGICO) {
            aditivoEncomendado = (int) (qtdade * 0.05);
            gasolinaEncomendada = (int) (qtdade * 0.7);
            alcoolEncomendado = (int) (qtdade * 0.25);
        }

        if (getSituacao() == SITUACAO.EMERGENCIA && tipoPosto == TIPOPOSTO.ESTRATEGICO) {
            aditivoEncomendado = (int) (qtdade * 0.05) / 2;
            gasolinaEncomendada = (int) (qtdade * 0.7) / 2;
            alcoolEncomendado = (int) (qtdade * 0.25) / 2;
        }

        if (qtdade < 0 || tipoPosto != TIPOPOSTO.COMUM && tipoPosto != TIPOPOSTO.ESTRATEGICO) {
            erro = -7;
        }

        if (getSituacao() == SITUACAO.EMERGENCIA && tipoPosto == TIPOPOSTO.COMUM) {
            erro = -14;
        }

        if (gasolinaEncomendada > tGasolina || alcoolEncomendado > tAlcool1 + tAlcool2
                || aditivoEncomendado > tAditivo) {
            erro = -21;
        }

        if (erro < 0) {
            encomenda[0] = erro;
            encomenda[1] = 0;
            encomenda[2] = 0;
            encomenda[3] = 0;
            return encomenda;
        }

        else {
            tGasolina -= gasolinaEncomendada;
            tAditivo -= aditivoEncomendado;
            tAlcool1 -= (int) (alcoolEncomendado / 2);
            tAlcool2 -= (int) (alcoolEncomendado / 2);
            defineSituacao();
            encomenda[0] = tAditivo;
            encomenda[1] = tGasolina;
            encomenda[2] = tAlcool1;
            encomenda[3] = tAlcool2;
            return encomenda;
        }
    }
}