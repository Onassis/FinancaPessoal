package br.com.fenix.util;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class TextProcessor {

    // Lista de stop words (adicione mais palavras conforme necessário)
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "a", "o", "e", "de", "da", "do", "em", "por", "para", "com", 
        "é", "um", "uma", "os", "as", "que", "se", "na", "no", "ou", 
        "à", "ao", "nos", "nas", "dos", "das", "mas", "isso"
    ));
    /**
     * Função que remove acentos e normaliza texto.
     * 
     * @param input O texto original.
     * @return O texto sem acentos e caracteres especiais.
     */
    private static String removeAccents(String input) {
        if (input == null) {
            return null;
        }
        // Normaliza o texto para decompor acentos
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Remove os caracteres não ASCII (acentos e cedilha)
        return normalized.replaceAll("[^\\p{ASCII}]", "");
    }
    /**
     * Função que processa o texto, remove as stop words e retorna um mapa de palavras e seus hashes.
     * 
     * @param text O texto a ser processado.
     * @return Um Map com as palavras (chave) e seus hashes (valor).
     */
    public static Map<String, Integer> processText(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyMap();
        }

        // Normalizar o texto (remover pontuação e deixar em minúsculas)
//        String normalizedText = text.toLowerCase().replaceAll("[^a-záéíóúâêîôûãõçàèìòùäëïöüñ ]", " ");
        
        // Normalizar o texto (remover pontuação, deixar em minúsculas e remover acentos)
        String normalizedText = removeAccents(text.toLowerCase());
        
//                .replaceAll("[^a-z ]", ""); // Apenas letras e espaços

//        normalizedText = .toLowerCase().replaceAll("[^a-\+záéíóúâêîôûãõçàèìòùäëïöüñ ]", " ");
        
        normalizedText = normalizedText.replaceAll("[+\\-*/]", " ");
        
        // Separar palavras, filtrar stop words e calcular hash
        Map<String, Integer> wordHashMap = Arrays.stream(normalizedText.split("\\s+"))
                .filter(word -> !STOP_WORDS.contains(word)) // Remover stop words
                .filter(word -> !word.isBlank()) // Remover palavras vazias
                .distinct() // Evitar duplicados (opcional, remova se quiser manter repetição)
                .collect(Collectors.toMap(
                        word -> word,             // Palavra como chave
                        word -> word.hashCode()   // Hash como valor
                ));

        return wordHashMap;
    }

    public static void main(String[] args) {
        String text = " Açafrão, Açaí, Açougue, Açúcar, Açucena, Açude, Araçá";

      text = "Academia Bod-ct A 02/12" ;
        
        Map<String, Integer> result = processText(text);

        // Imprimir as palavras e seus hashes
        result.forEach((word, hash) -> 
            System.out.println("Palavra: " + word + ", Hash: " + hash)
        );
        String teste = "Cacau Show";
        System.out.println(teste + teste.hashCode());
        
    }
}
