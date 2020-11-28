package lesson4;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
        private String next;
        private final StringBuilder charBuffer;
        private boolean removable;
        private boolean needNext;
        Stack<Iterator<Map.Entry<Character, Node>>> stack; // Стек итераторов детей

        private TrieIterator() {
            stack = new Stack<>();
            stack.push(root.children.entrySet().iterator());
            charBuffer = new StringBuilder();
            removable = false;
            needNext = true;
            next = null;
        }

        @Override
        public boolean hasNext() {
            if (needNext) {
                next = nextWord();
                needNext = false;
            }
            return next != null;
        }

        @Override
        public String next() {
            if (!hasNext()) throw new IllegalStateException();
            removable = true;
            needNext = true;
            return next;
        }

        @Override
        public void remove() {
            if (!removable) throw new IllegalStateException();
            Iterator<Map.Entry<Character, Node>> childrenIterator;
            childrenIterator = stack.peek();
            childrenIterator.remove();
            size--;
            removable = false;
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
                    childrenIterator = entry.getValue().children.entrySet().iterator();// Рассматриваем детей узла
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