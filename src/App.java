public class App {
    public static void main(String[] args) throws Exception {
        CentroDistribuicao c = new CentroDistribuicao(400, 4999, 1000, 1000);
        System.out.println(c.getSituacao());
        System.out.println(c.recebeAlcool(1049));
        System.out.println(c.gettAlcool1());
        System.out.println(c.gettAlcool2());
        System.out.println(c.getSituacao());
        System.out.println(c.recebeGasolina(2001));
        System.out.println(c.getSituacao());
        System.out.println(c.gettGasolina());
        int[] enc = (c.encomendaCombustivel(1000, c.getComum()));
        System.out.println(enc[0]);
        System.out.println(enc[1]);
        System.out.println(enc[2]);
        System.out.println(enc[3]);
    }
}