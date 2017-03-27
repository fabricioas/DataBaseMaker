package br.com.fabled;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 *
 * @author ltonietto
 */
public class TesteFormatacaoNum {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String numero = null;
        String mascara = "#.0#";
        Number num;
        do {
            numero = JOptionPane.showInputDialog("Número");
            try {
                // testei com 1234,5 e escreveu 1234.5
                // testei com 1234,56 e escreveu 1234.56
                // Portanto, acho que funciona
                DecimalFormat df = new DecimalFormat("##########.00");
                num = df.parse(numero);
                mascara = JOptionPane.showInputDialog("Máscada saída: ", mascara);
                DecimalFormat saida = new DecimalFormat(mascara);
                saida.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
                System.out.println(saida.format(num));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } while (numero != null);
    }

}
