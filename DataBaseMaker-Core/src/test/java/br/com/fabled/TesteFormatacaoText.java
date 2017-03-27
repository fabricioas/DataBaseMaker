package br.com.fabled;

import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author ltonietto
 */
public class TesteFormatacaoText {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String original = null;
        String mascara = "#####-#";
        do {
            original = JOptionPane.showInputDialog("Informação");
            try {
                mascara = JOptionPane.showInputDialog("Máscada saída: ", mascara);
                MaskFormatter formatter = new MaskFormatter(mascara);
                formatter.setValueContainsLiteralCharacters(false);
                System.out.println(formatter.stringToValue(original));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } while (original != null);
    }

}
