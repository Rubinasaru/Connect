package com.example.demo.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TfidfUtil {

    public double computeSimilarity(String text1, String text2) {
        List<String> corpus = Arrays.asList(text1, text2);

        // Tokenize and build vocab
        Set<String> vocabulary = corpus.stream()
                .flatMap(s -> Arrays.stream(s.toLowerCase().split(",")))
                .map(String::trim)
                .collect(Collectors.toSet());

        List<Map<String, Double>> tfidfVectors = corpus.stream()
                .map(doc -> computeTfidfVector(doc, vocabulary, corpus))
                .collect(Collectors.toList());

        return cosineSimilarity(tfidfVectors.get(0), tfidfVectors.get(1));
    }

    private Map<String, Double> computeTfidfVector(String doc, Set<String> vocab, List<String> corpus) {
        Map<String, Double> vector = new HashMap<>();
        String[] tokens = Arrays.stream(doc.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toArray(String[]::new);

        for (String term : vocab) {
            double tf = Arrays.stream(tokens).filter(t -> t.equals(term)).count() / (double) tokens.length;
            double idf = Math.log((double) corpus.size() /
                    (1 + corpus.stream().filter(d -> d.toLowerCase().contains(term)).count()));
            vector.put(term, tf * idf);
        }

        return vector;
    }

    private double cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
        Set<String> allTerms = new HashSet<>(vec1.keySet());
        allTerms.addAll(vec2.keySet());

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (String term : allTerms) {
            double val1 = vec1.getOrDefault(term, 0.0);
            double val2 = vec2.getOrDefault(term, 0.0);
            dotProduct += val1 * val2;
            normA += val1 * val1;
            normB += val2 * val2;
        }

        return (normA == 0 || normB == 0) ? 0.0 : dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
