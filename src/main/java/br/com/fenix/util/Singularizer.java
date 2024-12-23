package br.com.fenix.util;

import java.text.BreakIterator;
import java.util.Locale;

public class Singularizer {
    public static void main(String[] args) {
        String texto = "Os chapéus, corações e raízes estão nas ruas.";
        String textoSingular = converterParaSingular(texto);
        System.out.println(textoSingular);
        
        texto = "Afonsos Carnes Especia";
        textoSingular = converterParaSingular(texto);
         System.out.println(textoSingular);
    }

    public static String converterParaSingular(String texto) {
        StringBuilder resultado = new StringBuilder();
        BreakIterator boundary = BreakIterator.getWordInstance(new Locale("pt", "BR"));
        boundary.setText(texto);
        int start = boundary.first();
        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
            String palavra = texto.substring(start, end);
            if (Character.isLetterOrDigit(palavra.charAt(0))) {
                resultado.append(converterPalavraParaSingular(palavra));
            } else {
                resultado.append(palavra);
            }
        }
        return resultado.toString();
    }

    public static String converterPalavraParaSingular(String palavra) {
        if (palavra.endsWith("ões")) {
            return palavra.substring(0, palavra.length() - 3) + "ão";
        } else if (palavra.endsWith("ães")) {
            return palavra.substring(0, palavra.length() - 3) + "ão";
        } else if (palavra.endsWith("ãos")) {
            return palavra.substring(0, palavra.length() - 3) + "ão";
        } else if (palavra.endsWith("es")) {
            if (palavra.endsWith("res") || palavra.endsWith("zes") || palavra.endsWith("nes")) {
                return palavra.substring(0, palavra.length() - 2);
            } else if (palavra.endsWith("ais") || palavra.endsWith("eis") || palavra.endsWith("ois") || palavra.endsWith("uis")) {
                return palavra.substring(0, palavra.length() - 2) + "l";
            } else if (palavra.endsWith("is")) {
                return palavra.substring(0, palavra.length() - 1) + "l";
            } else {
                return palavra.substring(0, palavra.length() - 2);
            }
        } else if (palavra.endsWith("s")) {
            return palavra.substring(0, palavra.length() - 1);
        }
        return palavra;
    }
}
