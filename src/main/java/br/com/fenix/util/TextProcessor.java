package br.com.fenix.util;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextProcessor {

    // Lista de stop words (adicione mais palavras conforme necessário)
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "a", "o", "e", "de", "da", "do", "em", "por", "para", "com", 
        "é", "um", "uma", "os", "as", "que", "se", "na", "no", "ou", 
        "à", "ao", "nos", "nas", "dos", "das", "mas", "isso" ,
        "ct" , "pag","parc","ltda","order","sao","santa"

    ));
    public static String removeStopWords(String text) {
    	
   	
        return Arrays.stream(text.split("\\s+"))
                     .filter(word -> !STOP_WORDS.contains(word.toLowerCase()))
                     .collect(Collectors.joining(" "));
    }
    
    private static String removeEspaco(String texto) {
    	return texto.trim().replaceAll("\\s+", " ");
    }
    /**
     * Função que remove acentos e normaliza texto.
     * 
     * @param input O texto original.
     * @return O texto sem acentos e caracteres especiais.
     */
    private static String removeAcentos(String input) {
        if (input == null) {
            return "";
        }
        // Normaliza o texto para decompor acentos
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Remove os caracteres não ASCII (acentos e cedilha)
        return normalized.replaceAll("[^\\p{ASCII}]", "");
    }
    private static String removeNumeros(String input) {
        if (input == null) {
            return "";
        }

        // Remove os numeros e deixa Apenas letras e espaços
        return input.replaceAll("[^a-z ]", " ");
    }
    
    /**
     * Função que processa o texto, remove as stop words e retorna uma String 
     * 
     * @param  : texto  => O texto a ser processado.
     * @return : String => String sem acento, numeros e caracteres especiais 
     */
    public static String normalizaTexto( String texto) {
    	 String normalizedText = texto.toLowerCase();
    	if (texto == null) {
            return "";
        }
    	
        // Normalizar o texto (remover pontuação, deixar em minúsculas e remover acentos)
    	
    	 normalizedText = normalizedText.replaceAll("[+\\-*/]", " ");

    	 normalizedText = removeAcentos(normalizedText);
        
        normalizedText = removeNumeros(normalizedText );
        
        normalizedText = removeEspaco(normalizedText);
        
        normalizedText = removeStopWords(normalizedText);
        
       return normalizedText;     	
    }
    
    /**
     * Função que processa o texto, remove as stop words e retorna um mapa de palavras e seus hashes.
     * 
     * @param text O texto a ser processado.
     * @return Um Map com as palavras (chave) e seus hashes (valor).
     */
    public static Map<String, Integer> processText(String texto) {
        if (texto == null || texto.isEmpty()) {
            return Collections.emptyMap();
        }
        texto = normalizaTexto(texto);   
        Map<String, Integer> wordHashMap = Arrays.stream(texto.split("\\s+"))
                .filter(word -> !word.isBlank()) // Remover palavras vazias
                .distinct() // Evitar duplicados (opcional, remova se quiser manter repetição)
                .collect(Collectors.toMap(
                        word -> word,             // Palavra como chave
                        word -> word.hashCode()   // Hash como valor
                ));

        return wordHashMap;

        // Normalizar o texto (remover pontuação e deixar em minúsculas)
//        String normalizedText = text.toLowerCase().replaceAll("[^a-záéíóúâêîôûãõçàèìòùäëïöüñ ]", " ");
        
//        // Normalizar o texto (remover pontuação, deixar em minúsculas e remover acentos)
//        String normalizedText = removeAcentos(text.toLowerCase());
//        
//        normalizedText = removeNumeros(normalizedText );
//        
//        // Apenas letras e espaços
//        normalizedText = normalizedText.replaceAll("[^a-z ]", "");
//
////        normalizedText = .toLowerCase().replaceAll("[^a-\+záéíóúâêîôûãõçàèìòùäëïöüñ ]", " ");
//        
//        normalizedText = normalizedText.replaceAll("[+\\-*/]", " ");
        
        // Separar palavras, filtrar stop words e calcular hash
    }
    
    public static void main(String[] args) {
        String text = " Açafrão, Açaí, Açougue, Açúcar, Açucena, Açude, Araçá";

      text = "Academia Bod-ct A 02/12" ;
      
      text = normalizaTexto(text); 
      System.out.println( text); 
      
        Map<String, Integer> result = processText(text);

        // Imprimir as palavras e seus hashes
        result.forEach((word, hash) -> 
            System.out.println("Palavra: " + word + ", Hash: " + hash)
        );
        
        System.out.println( normalizaTexto( "Sup Santa Helena  02/02"));

        System.out.println( normalizaTexto( "Drogaria Rds*rd S 03/03"));
        
        System.out.println( normalizaTexto( "Parc=106 Drogarias02/06"));
        
        System.out.println( normalizaTexto( "Supermercado-ct   03/03"));
        System.out.println( normalizaTexto( "Petlove Saud*petl"));
        System.out.println( normalizaTexto( "Petlove*pl*order"));
        System.out.println( normalizaTexto( "Supermercado-ct Adaria"));
        System.out.println( normalizaTexto( "Mp*agualife"));
        System.out.println( normalizaTexto( "1 Rotativo 13jr6i"));
        System.out.println( normalizaTexto( "Academia Bod-ct A 01/12"));


        String teste = "Cacau Show";
        System.out.println(teste + teste.hashCode());
        
    }
}
