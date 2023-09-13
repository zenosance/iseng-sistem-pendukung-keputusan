import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;

public class SpkWeek2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int kriteria, alternatif;

        System.out.println("Program Pengambilan Keputusan Sederhana");
        System.out.println("---------------------------------------");
        System.out.print("Masukkan jumlah kriteria   : ");
        kriteria = sc.nextInt();
        System.out.print("Masukkan jumlah alternatif : ");
        alternatif = sc.nextInt();
        float[][] listKriteriaAlternatif = new float[alternatif][kriteria];
        float[] bobotKriteria = new float[kriteria];

        System.out.println("\nIsikan skor kriteria tiap alternatif (pastikan sudah disesuaikan)");
        System.out.println("--------------------------------------");
        for (int i = 0; i < listKriteriaAlternatif.length; i++) {
            System.out.println("\nMasukkan data kriteria alternatif ke-" + (i + 1));
            for (int j = 0; j < listKriteriaAlternatif[i].length; j++) {
                System.out.print("Masukkan kriteria ke-" + (j + 1) + " : ");
                listKriteriaAlternatif[i][j] = sc.nextFloat();
            }
        }

        System.out.println("\nMasukkan bobot tiap kriteria:");
        for (int i = 0; i < kriteria; i++) {
            System.out.print("Masukkan bobot kriteria ke-" + (i + 1) + " : ");
            bobotKriteria[i] = sc.nextFloat();
        }

        float[][] normalizedMatrixBenefit = normalisasi(listKriteriaAlternatif, true);
        float[][] normalizedMatrixCost = normalisasi(listKriteriaAlternatif, false);

        float[] preferenceScoresWPMBenefit = wpm(normalizedMatrixBenefit, bobotKriteria);
        float[] preferenceScoresWPMCost = wpm(normalizedMatrixCost, bobotKriteria);
        float[] preferenceScoresWSMBenefit = wsm(normalizedMatrixBenefit, bobotKriteria);
        float[] preferenceScoresWSMCost = wsm(normalizedMatrixCost, bobotKriteria);

        Pair[] pairsWPMBenefit = new Pair[alternatif];
        Pair[] pairsWPMCost = new Pair[alternatif];
        Pair[] pairsWSMBenefit = new Pair[alternatif];
        Pair[] pairsWSMCost = new Pair[alternatif];
        
        for (int i = 0; i < alternatif; i++) {
            pairsWPMBenefit[i] = new Pair("Alternatif ke-" + (i + 1), preferenceScoresWPMBenefit[i]);
            pairsWPMCost[i] = new Pair("Alternatif ke-" + (i + 1), preferenceScoresWPMCost[i]);
            pairsWSMBenefit[i] = new Pair("Alternatif ke-" + (i + 1), preferenceScoresWSMBenefit[i]);
            pairsWSMCost[i] = new Pair("Alternatif ke-" + (i + 1), preferenceScoresWSMCost[i]);
        }

        Arrays.sort(pairsWPMBenefit, new Comparator<Pair>() {
            public int compare(Pair a, Pair b) {
                return Float.compare(b.score, a.score);
            }
        });

        Arrays.sort(pairsWPMCost, new Comparator<Pair>() {
            public int compare(Pair a, Pair b) {
                return Float.compare(b.score, a.score);
            }
        });

        Arrays.sort(pairsWSMBenefit, new Comparator<Pair>() {
            public int compare(Pair a, Pair b) {
                return Float.compare(b.score, a.score);
            }
        });

        Arrays.sort(pairsWSMCost, new Comparator<Pair>() {
            public int compare(Pair a, Pair b) {
                return Float.compare(b.score, a.score);
            }
        });

        System.out.println("\nHasil Preferensi WPM (Benefit):");
        printResults(pairsWPMBenefit);

        System.out.println("\nHasil Preferensi WPM (Cost):");
        printResults(pairsWPMCost);

        System.out.println("\nHasil Preferensi WSM (Benefit):");
        printResults(pairsWSMBenefit);

        System.out.println("\nHasil Preferensi WSM (Cost):");
        printResults(pairsWSMCost);
    }

    static float[][] normalisasi(float[][] listKA, boolean isBenefit) {
        float[][] normalizedMatrix = new float[listKA.length][listKA[0].length];

        for (int i = 0; i < listKA[0].length; i++) {
            float maxOrMin = isBenefit ? Float.MIN_VALUE : Float.MAX_VALUE;

            for (int j = 0; j < listKA.length; j++) {
                if (isBenefit) {
                    if (listKA[j][i] > maxOrMin) {
                        maxOrMin = listKA[j][i];
                    }
                } else {
                    if (listKA[j][i] < maxOrMin) {
                        maxOrMin = listKA[j][i];
                    }
                }
            }

            for (int j = 0; j < listKA.length; j++) {
                normalizedMatrix[j][i] = isBenefit ? listKA[j][i] / maxOrMin : maxOrMin / listKA[j][i];
            }
        }

        return normalizedMatrix;
    }

    static float[] wpm(float[][] normalizedMatrix, float[] bobotKriteria) {
        float[] preferenceScores = new float[normalizedMatrix.length];

        for (int i = 0; i < normalizedMatrix.length; i++) {
            float product = 1.0f;
            for (int j = 0; j < normalizedMatrix[i].length; j++) {
                product *= Math.pow(normalizedMatrix[i][j], bobotKriteria[j]);
            }
            preferenceScores[i] = product;
        }

        return preferenceScores;
    }

    static float[] wsm(float[][] normalizedMatrix, float[] bobotKriteria) {
        float[] preferenceScores = new float[normalizedMatrix.length];
    
        for (int i = 0; i < normalizedMatrix.length; i++) {
            float sum = 0.0f;
            for (int j = 0; j < normalizedMatrix[i].length; j++) {
                sum += normalizedMatrix[i][j] * bobotKriteria[j]; 
            }
            preferenceScores[i] = sum;
        }
    
        return preferenceScores;
    }

    static void printResults(Pair[] pairs) {
        System.out.println("Alternatif | Skor");
        System.out.println("------------------");
        for (int i = 0; i < pairs.length; i++) {
            System.out.println(pairs[i].label + " | " + pairs[i].score);
        }
    }
}

class Pair {
    String label;
    float score;

    public Pair(String label, float score) {
        this.label = label;
        this.score = score;
    }
}
