package lesson7;

import kotlin.NotImplementedError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    public static String longestCommonSubSequence(String first, String second) {
        if (first.length() == 0 || second.length() == 0) return "";
        int[][] matrix = new int[first.length()][second.length()];
        int bestI = 0;
        int bestJ = 0;
        int maxLength = 0;
        for (int i = 0; i < first.length(); i++) {
            for (int j = 0; j < second.length(); j++) {
                if (first.charAt(i) == second.charAt(j)) {
                    if (i == 0 || j == 0) matrix[i][j] = 1;
                    else matrix[i][j] = matrix[i - 1][j - 1] + 1;
                    if (matrix[i][j] > maxLength) {
                        maxLength = matrix[i][j];
                        bestI = i;
                        bestJ = j;
                    }
                } else {
                    if (i == 0 && j == 0) matrix[i][j] = 0;
                    else if (i == 0) {
                        matrix[i][j] = matrix[i][j - 1];
                    } else if (j == 0) {
                        matrix[i][j] = matrix[i - 1][j];
                    } else matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
                }
            }
        }
        StringBuilder s = new StringBuilder();
        while (bestI >= 0 && bestJ >= 0) {
            if (first.charAt(bestI) == second.charAt(bestJ)) {
                s.append(first.charAt(bestI));
                bestI--;
                bestJ--;
            } else if (bestJ != 0 && matrix[bestI][bestJ] == matrix[bestI][bestJ - 1]) {
                bestJ--;
            } else bestI--;
        }
        return s.reverse().toString();
    } //Трудоёмкость O(M*N), Ресурсоёмкость O(M*N)

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        List<Integer> result = new ArrayList<>();
        if (list == null || list.isEmpty()) return result;
        if (list.size() == 1) return list;
        int[] counts = new int[list.size()];
        int[] ancestors = new int[list.size()];
        Arrays.fill(counts, 1);
        Arrays.fill(ancestors, -1);
        int maxIndex = 0;
        for (int j = 1; j < counts.length; j++) {
            for (int i = 0; i < j; i++) {
                if (list.get(j) > list.get(i) && counts[j] < counts[i]+1) {
                    counts[j] = counts[i] + 1;
                    ancestors[j] = i;
                }
            }
            if (counts[j] > counts[maxIndex]) maxIndex = j;
        }

        while (maxIndex >= 0) {
            result.add(0, list.get(maxIndex));
            maxIndex = ancestors[maxIndex];
        }
        return result;
    } //Трудоёмкость O(N^2), Ресурсоёмкость O(N)

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
