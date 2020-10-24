package lesson1;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortTimes(String inputName, String outputName) throws IOException, ParseException {
        List<Pair<String, Long>> list = new ArrayList<>();
        try (FileReader read = new FileReader(inputName);
             BufferedReader buffer = new BufferedReader(read)) {
            String str = buffer.readLine();
            while (str != null) {
                if (!(str.matches("([0][1-9]|[1][0-2]):[0-5][0-9]:[0-5][0-9]\\s(PM|AM)"))) {
                    throw new IllegalArgumentException();
                }
                else list.add(new Pair<>(str, new SimpleDateFormat("hh:mm:ss a").parse(str).getTime()));
                str = buffer.readLine();
            }
        }
        list.sort((a, b) -> (int) (a.getSecond() - b.getSecond()));
        try (FileWriter fileWriter = new FileWriter(outputName);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            for (Pair p : list) {
                writer.write(p.getFirst().toString());
                writer.newLine();
            }
        }
    } // Трудоёмкость O(N logN), Ресурсоёмкость O(N).

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        // Так как число элементов может быть огромным, а диапазон значений ограничен, применим сортировку подсчётом
        final double min = -273.0;
        final double max = 500.0;
        final int grade = 10;
        final int size = (int)((max - min) * grade) + 1;
        final int offset = size - (int)(max * grade) - 1;
        int[] count = new int[size];
        try (FileReader read = new FileReader(inputName);
             BufferedReader buffer = new BufferedReader(read)) {
            String str = buffer.readLine();
            while (str != null) {
                double temp = Double.parseDouble(str);
                if (temp > max || temp < min) {
                    throw new IllegalArgumentException();
                }
                count[(int) (temp * grade) + offset]++;
                str = buffer.readLine();
            }
        }
        try (FileWriter fileWriter = new FileWriter(outputName);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            double number;
            for (int i = 0; i < count.length; i++) {
                number = ((double) (i - offset)) / grade;
                String s = String.valueOf(number);
                for (int j = 0; j < count[i]; j++) {
                    writer.write(s);
                    writer.newLine();
                }
            }
        }
    } // Трудоёмкость O(N), Ресурсоёмкость O(1).

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) throws IOException {
        // Сортировка слиянием
        List<Integer> in = new ArrayList<>();
        try (FileReader read = new FileReader(inputName);
             BufferedReader buffer = new BufferedReader(read)) {
            String str = buffer.readLine();
            while (str != null) {
                int i = Integer.parseInt(str);
                if (i <= 0) throw new IllegalArgumentException();
                in.add(i);
                str = buffer.readLine();
            }
        }
        Integer[] worked = new Integer[in.size()];
        for (int i = 0; i < in.size(); i++) {
            worked[i] = in.get(i);
        }
        mergeSort(worked);
        int bestN = Integer.MAX_VALUE;
        int bCount = 0;
        int thisN = Integer.MAX_VALUE;
        int tCount = 0;
        for (int value : worked) {
            if (thisN != value) {
                tCount = 1;
                thisN = value;
            } else {
                tCount++;
            }
            if (tCount > bCount) {
                bestN = thisN;
                bCount = tCount;
            } else {
                if (tCount == bCount && thisN < bestN) {
                    bestN = thisN;
                }
            }
        }
        try (FileWriter fileWriter = new FileWriter(outputName);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            for (Integer integer : in) {
                if (integer != bestN) {
                    writer.write("" + integer);
                    writer.newLine();
                }
            }
            for (int j = 0; j < bCount; j++) {
                writer.write("" + bestN);
                writer.newLine();
            }
        }
    } // Трудоёмкость O(N logN), Ресурсоёмкость O(N).

    private static void mergeSort(Integer[] elements) {
        if (elements == null) return;
        if (elements.length < 2) return; // возврат из рекурсии если в массиве один элемент
        int middle = elements.length / 2;
        // Копируем левую часть массива (от начала до середины)
        Integer[] a1 = Arrays.copyOfRange(elements, 0, elements.length / 2);
        // Копируем правую часть массива (от середины до конца)
        Integer[] a2 = Arrays.copyOfRange(elements, elements.length / 2, elements.length);
        // Рекурсивно вызываем сортировку для поделенных массивов
        mergeSort(a1);
        mergeSort(a2);
        // Соединяем два отсортированных массива в один
        Integer[] aResult = new Integer[a1.length+a2.length];
        System.arraycopy(a2, 0, aResult, a1.length, a2.length);
        mergeArrays(a1, aResult); // Используем функцию слияния массивов из последней задачи
//        elements = aResult; // Так не работает!!!
        System.arraycopy(aResult, 0, elements, 0, aResult.length);
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        if (first.length > second.length) {
            throw new IllegalArgumentException();
        }
        int i = 0;
        int j = first.length;
        int currIndex = 0;
        while (i < first.length && j < second.length) {
            if (first[i].compareTo(second[j]) < 0) {
                second[currIndex++] = first[i++];
            } else {
                second[currIndex++] = second[j++];
            }
        }
        while (i < first.length) {
            second[currIndex++] = first[i++];
        }
        while (j < second.length) {
            second[currIndex++] = second[j++];
        }
    } // Трудоёмкость O(N*M), Ресурсоёмкость O(1).
}
