//Yael Martín Benzaquen y Paula Ríos Sánchez

package practica2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinarySearchTree<T> implements BinarySearchTreeStructure<T> {
    private Comparator<T> comparator;
    private T value;
    private BinarySearchTree<T> left;
    private BinarySearchTree<T> right;

    public BinarySearchTree(Comparator<T> comparator) {
        this.comparator = comparator;
        this.value = null;
        this.left = null;
        this.right = null;
    }

    @Override
    public void insert(T value) {
        if (value == null) throw new BinarySearchTreeException("Cannot insert null value.");
        if (this.value == null) {
            this.value = value;
        } else {
            int compare = comparator.compare(value, this.value);
            if (compare < 0) {
                if (left == null) left = new BinarySearchTree<>(comparator);
                left.insert(value);
            } else if (compare > 0) {
                if (right == null) right = new BinarySearchTree<>(comparator);
                right.insert(value);
            } else {
                throw new BinarySearchTreeException("Duplicate values are not allowed.");
            }
        }
    }

    @Override
    public boolean isLeaf() {
        return value != null && left == null && right == null;
    }

    @Override
    public boolean contains(T value) {
        if (value == null || this.value == null) {
            return false;
        }

        int cmp = comparator.compare(value, this.value);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return left != null && left.contains(value);
        } else {
            return right != null && right.contains(value);
        }
    }

    @Override
    public T minimum() {
        if (value == null) {
            throw new BinarySearchTreeException("Tree is empty.");
        }

        if (left == null) {
            return value;
        }

        return left.minimum();
    }

    @Override
    public T maximum() {
        if (value == null) {
            throw new BinarySearchTreeException("Tree is empty.");
        }

        if (right == null) {
            return value;
        }

        return right.maximum();
    }

    @Override
    public void removeBranch(T value) {
        if (value == null || this.value == null) {
            return;
        }

        int cmp = comparator.compare(value, this.value);
        if (cmp < 0 && left != null) {
            if (comparator.compare(value, left.value) == 0) {
                left = null;
            } else {
                left.removeBranch(value);
            }
        } else if (cmp > 0 && right != null) {
            if (comparator.compare(value, right.value) == 0) {
                right = null;
            } else {
                right.removeBranch(value);
            }
        }
    }

    @Override
    public int size() {
        if (this.value == null) return 0;
        int leftSize = (left != null) ? left.size() : 0;
        int rightSize = (right != null) ? right.size() : 0;
        return 1 + leftSize + rightSize;
    }

    @Override
    public int depth() {
        if (this.value == null) return 0;
        int leftDepth = (left != null) ? left.depth() : 0;
        int rightDepth = (right != null) ? right.depth() : 0;
        return 1 + Math.max(leftDepth, rightDepth);
    }

    public String render() {
        if (value == null) return "";
        String leftStr = (left != null) ? left.render() : "";
        String rightStr = (right != null) ? right.render() : "";
        return value + ((left != null || right != null) ? "(" + leftStr + "," + rightStr + ")" : "");
    }

    @Override
    public void removeValue(T value) {
        if (this.value == null) return;
        int compare = comparator.compare(value, this.value);
        if (compare == 0) {
            if (left != null && right != null) {
                this.value = right.minimum();
                right.removeValue(this.value);
            } else if (left != null) {
                this.value = left.value;
                right = left.right;
                left = left.left;
            } else if (right != null) {
                this.value = right.value;
                left = right.left;
                right = right.right;
            } else {
                this.value = null;
            }
        } else if (compare < 0 && left != null) {
            left.removeValue(value);
        } else if (compare > 0 && right != null) {
            right.removeValue(value);
        }
    }

    @Override
    public List<T> inOrder() {
        List<T> result = new ArrayList<>();
        if(value!=null) {
            if (left != null) result.addAll(left.inOrder());
            result.add(value);
            if (right != null) result.addAll(right.inOrder());
        }
        return result;
    }

    @Override
    public void balance() {
        if(value==null){
            return;
        }

        List<T> elements = this.inOrder();

        this.value = null;
        this.left = null;
        this.right = null;
       
        balanceInsert(elements, 0, elements.size() - 1);
    }

    private void balanceInsert(List<T> elements, int start, int end) {
        if (start > end){
            return; 
        } 
        int mid = (start + end) / 2;
        this.insert(elements.get(mid));
        balanceInsert(elements, start, mid - 1);
        balanceInsert(elements, mid + 1, end);
    }   
}
