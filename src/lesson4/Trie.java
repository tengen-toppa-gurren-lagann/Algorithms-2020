package lesson4;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();
    }

    private Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return  new TrieIterator();
    }

    public class TrieIterator implements Iterator<String> {
        private String current;
        private String next;
        private StringBuilder charBuffer;
        private boolean removable;
        Stack<Iterator<Map.Entry<Character, Node>>> stack; // Стек итераторов детей
        Set<ConcurrentHashMap.Entry<Character, Node>> childrenSet; // Concurrent из-за ConcurrentModificationException

        private TrieIterator() {
            childrenSet = new HashSet<>(root.children.entrySet()); // Начинаем проход с детей корня
            stack = new Stack<>();
            stack.push(childrenSet.iterator());
            charBuffer = new StringBuilder();
            removable = false;
            current = null;
            next = nextWord();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public String next() {
            if (!hasNext()) throw new IllegalStateException();
            removable = true;
            current = next;
            next = nextWord();
            return current;
        }

        @Override
        public void remove() {
            if (!removable) throw new IllegalStateException();
            boolean removed = Trie.this.remove(current);
            removable = !removed;
        }

        private String nextWord() {
            String word = null;
            Iterator<Map.Entry<Character, Node>> childrenIterator;
            childrenIterator = stack.peek(); // Верхний итератор из стека
            if (childrenIterator == null) throw new IllegalStateException();
            while (word == null && (stack.size() > 1 || childrenIterator.hasNext())) { // Ищем пока не нашли слово и пока не вернулись к корню, либо у корня остались нерассмотренные дети
                while (!childrenIterator.hasNext() && charBuffer.length() != 0) { // Если у текущего узла нет детей и мы находимся не в корне поднимаемся наверх
                    charBuffer.deleteCharAt(charBuffer.length() - 1);
                    stack.pop();
                    childrenIterator = stack.peek();
                    if (childrenIterator == null) throw new IllegalStateException();
                }

            while (childrenIterator.hasNext()) { // Пока дети есть, собираем строку до тех пор, пока не встретим 0
                Map.Entry<Character, Node> entry = childrenIterator.next();
                if (entry.getKey() != (char) 0) {
                    charBuffer.append(entry.getKey());
                    childrenSet = new HashSet<>(entry.getValue().children.entrySet()); // Рассматриваем детей узла
                    childrenIterator = childrenSet.iterator();
                    stack.push(childrenIterator); // Помещаем в стек итератор следующего элемента
                } else { // Собрали слово
                    word = charBuffer.toString();
                    break;
                }
            }
        }
            return word;
        }
    }

}