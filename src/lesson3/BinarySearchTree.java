package lesson3;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Pair<Node<T>, Node<T>> findNodeAndParent(T value) {
        Node<T> node = root;
        Node<T> parent = null;
        while (node != null) {
            int result = node.value.compareTo(value);
            if (result > 0) {
                parent = node;
                node = node.left;
            } else if (result < 0) {
                parent = node;
                node = node.right;
            }
            else {
                break;
            }
        }
        return new  Pair<>(parent, node);
    }

    /**
     * Добавление элемента в дерево
     *
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     *
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     *
     * Пример
     */
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }

        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     *
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) throw new NullPointerException();
        if (!contains(o)) return false;
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Pair<Node<T>, Node<T>> pair = findNodeAndParent(t);
        Node<T> node = pair.getSecond();
        Node<T> parent = pair.getFirst();
        if (node.right == null && node.left == null) {
            if (parent == null) {
                root = null;
            } else {
                if (parent.left == node) parent.left = null;
                else parent.right = null;
            }
        }
        else if (node.left == null && node.right != null) {
                if (parent == null) {
                    root = node.right;
                } else {
                    if (parent.right == node) parent.right = node.right;
                    else parent.left = node.right;
                }
             }
             else if (node.right == null && node.left != null) {
                      if (parent == null) {
                root = node.left;
            } else {
                if (parent.left == node) parent.left = node.left;
                else parent.right = node.left;
            }
        }
        else
        if (node.right != null && node.left != null) {
            Node<T> min = node.right;
            Node<T> minParent = node;
            while (min.left != null) {
                minParent = min;
                min = min.left;
            }
            if (min == minParent.left) {
                minParent.left = min.right;
                min.right = node.right;
                min.left = node.left;
                if (parent == null) {
                    root = min;
                } else {
                    if (parent.left == node) {
                        parent.left = min;
                    } else parent.right = min;
                }
            }
            else {
                min.left = node.left;
                if (parent == null) {
                    root = node.right;
                } else {
                    if (parent.right == node) {
                        parent.right = min;
                    } else parent.left = min;
                }
            }
        }
        size--;
        return  true;
    } // Трудоемкость О(N), Ресурсоемкость О(1)

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }

    public class BinarySearchTreeIterator implements Iterator<T> {
        private Node<T> current;
        private Node<T> next;
        private boolean canRemove;

        private BinarySearchTreeIterator() {
            canRemove = false;
            current = null;
            next = root;
            if (next != null) {
                while (next.left != null) {
                    next = next.left;
                }
            }
        }


        /**
         * Проверка наличия следующего элемента
         *
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         *
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         *
         * Средняя
         */
        @Override
        public boolean hasNext() {
            return (next != null);
        } // Трудоемкость О(1), Ресурсоемкость О(1)

        /**
         * Получение следующего элемента
         *
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         *
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         *
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         *
         * Средняя
         */
        @Override
        public T next() {
            if (!hasNext()) {
                throw new IllegalStateException();
            }
            canRemove = true;
            current = next;
            if (next.right != null) {
                next = next.right;
                while (next.left != null) {
                    next = next.left;
                }
            } else {
                while (true) {
                    Node<T> parent = findNodeAndParent(next.value).getFirst();
                    if (parent == null) {
                        next = null;
                        break;
                    }
                    else if (next == parent.left) {
                            next = parent;
                            break;
                         }
                         else next = parent;
                }
            }
            return current.value;
        } // Трудоемкость О(N), Ресурсоемкость О(1)

        /**
         * Удаление предыдущего элемента
         *
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         *
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         *
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         *
         * Сложная
         */
        @Override
        public void remove() {
            if (canRemove) {
                canRemove = !BinarySearchTree.this.remove(current.value);
            } else {
                throw new IllegalStateException();
            }
        } // Трудоемкость О(N), Ресурсоемкость О(1)
    }

    // Класс, реализующий подмножество элементов BinarySearchTree в заданном диапазоне
    public class BSTSubset extends TreeSet<T> implements SortedSet<T> {
        BinarySearchTree<T> tree;
        T minValue;
        T maxValue;

        public BSTSubset(BinarySearchTree<T> bst, T min, T max) {
            tree = bst;
            minValue = min;
            maxValue = max;
        }

        @Override
        public int size() {
            int result = 0;
            Iterator<T> iterator = tree.iterator();
            while (iterator.hasNext()) {
                T value = iterator.next();
                if (isValid(value)) result++;
            }
            return result;
        }

        public boolean isValid(T value) {
            boolean result = false;
            if (minValue != null && maxValue != null) {
                result = value.compareTo(minValue) >= 0 && value.compareTo(maxValue) < 0;
            }
            if (minValue == null) {
                result = value.compareTo(maxValue) < 0;
            }
            if (maxValue == null) {
                result = value.compareTo(minValue) >= 0;
            }
            return result;
        }

        @Override
        public T first() {
            if (size() == 0) throw new NoSuchElementException();
            T result = null;
            T next;
            Iterator<T> iterator = tree.iterator();
            while (iterator.hasNext()) {
                next = iterator.next();
                if (next.compareTo(minValue) >= 0) {
                    result = next;
                    break;
                }
            }
            return result;
        }

        @Override
        public T last() {
            if (size() == 0) throw new NoSuchElementException();
            T result = null;
            T next;
            T cur = first();
            Iterator<T> iterator = tree.iterator();
            while (iterator.hasNext()) {
                next = iterator.next();
                if (next.compareTo(maxValue) >= 0) {
                    result = cur;
                    break;
                } else result = next;
                cur = next;
            }
            return result;
        }

        @Override
        public boolean contains(Object o) {
            @SuppressWarnings("unchecked")
            T t = (T)o;
            if (isValid(t)) return tree.contains(o);
            else return false;
        }

        @Override
        public boolean add(T t) {
            if (isValid(t)) return tree.add(t);
            else throw new IllegalArgumentException();
        }

        @Override
        public boolean remove(Object o) {
            @SuppressWarnings("unchecked")
            T t = (T)o;
            if (isValid(t)) {
                return tree.remove(o);
            }
            else throw new IllegalArgumentException();
        }
    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new BSTSubset(this, fromElement, toElement);
    } // Трудоемкость О(1), Ресурсоемкость О(1)

    /**
     * Подмножество всех элементов строго меньше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new BSTSubset(this, null, toElement);
    } // Трудоемкость О(1), Ресурсоемкость О(1)

    /**
     * Подмножество всех элементов нестрого больше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new BSTSubset(this, fromElement, null);
    } // Трудоемкость О(1), Ресурсоемкость О(1)

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

}