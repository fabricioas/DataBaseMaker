package br.com.dbm.utils.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 *
 * @author ltonietto
 */
public class ScriptGenerator {

    /**
     * @param args the command line arguments
     */
    public static void execute() throws Exception {
        String inputFile;
        inputFile = JOptionPane.showInputDialog("Arquivo CSV com os dados");

        List<String[]> dados = new ArrayList();
        List<String> colunas = new ArrayList();
        readData(inputFile, colunas, dados);
        exportInserts(colunas, dados, inputFile.substring(0, inputFile.indexOf(".csv")));
    }

    private static void readData(String inputFile, List<String> colunas, List<String[]> dados) throws Exception {
        FileReader fr = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(fr);
        try {
            String line = br.readLine();
            String campos[] = line.split(";");
            for (String col : campos) {
                colunas.add(col);
            }

            while ((line = br.readLine()) != null) {
                if ("".equals(line)) {
                    continue;
                }
                campos = line.split(";");
                dados.add(campos);
            }
        } finally {
            br.close();
        }
    }

    private static void exportInserts(List<String> colunas, List<String[]> dados, String table) throws Exception {
        FileWriter fw = new FileWriter("inserts.sql");
        PrintWriter pw = new PrintWriter(fw);
        StringBuilder sqlColunas = new StringBuilder();
        String aux = "";
        for (String col : colunas) {
            sqlColunas.append(aux).append(col);
            aux = ", ";
        }

        try {
            String novaLinha = "";
            StringBuilder sql = new StringBuilder();
            for (String[] registro : dados) {
                sql.append(novaLinha);
                sql.append("INSERT INTO ").append(table).append(" (").append(sqlColunas).append(") VALUES ");
                sql.append("(");
                aux = "";
                for (int i = 0; i < colunas.size(); i++) {
                    if(registro[i].indexOf(".NEXTVAL") > 0){
                        sql.append(aux).append(registro[i]);
                    } else {
                        sql.append(aux).append("'").append(registro[i]).append("'");
                    }
                    aux = ", ";
                }
                sql.append(")");
                novaLinha = ";\n";
            }
            sql.append(";");
            pw.println(sql);
        } finally {
            pw.close();
        }
    }

}
