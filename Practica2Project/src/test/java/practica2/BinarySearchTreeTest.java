package practica2;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class BinarySearchTreeTest {

    private BinarySearchTree<Integer> tree;
    
    @BeforeEach
    public void setUp() {
        tree = new BinarySearchTree<Integer>(Comparator.naturalOrder());
    }
    
    @Nested
    @DisplayName("insert")
    class InsertTests {

        @Test
        @DisplayName("debe insertar correctamente un nodo")
        void insertShouldAddValue() {
            tree.insert(10);
            assertTrue(tree.contains(10));
        }

        @Test
        @DisplayName("debe lanzar excepción si se inserta valor duplicado")
        void insertDuplicateThrowsException() {
            tree.insert(5);
            assertThrows(BinarySearchTreeException.class, () -> tree.insert(5));
        }

        @Test
        @DisplayName("debe lanzar excepción si se inserta valor null")
        void insertNullThrowsException() {
            assertThrows(BinarySearchTreeException.class, () -> tree.insert(null));
        }
    }
    @Nested
    @DisplayName("isLeaf")
    class IsLeafTests {

        @Test
        @DisplayName("debe devolver true si es hoja")
        void shouldBeLeaf() {
            tree.insert(7);
            assertTrue(tree.isLeaf());
        }

        @Test
        @DisplayName("debe devolver false si tiene hijos")
        void shouldNotBeLeaf() {
            tree.insert(7);
            tree.insert(3);
            assertFalse(tree.isLeaf());
        }
    }

    @Nested
    @DisplayName("contains")
    class ContainsTests {

        @Test
        @DisplayName("debe encontrar un valor insertado")
        void containsValue() {
            tree.insert(8);
            tree.insert(2);
            tree.insert(10);
            assertTrue(tree.contains(2));
            assertTrue(tree.contains(10));
        }

        @Test
        @DisplayName("debe devolver false si no está el valor")
        void doesNotContainValue() {
            tree.insert(8);
            assertFalse(tree.contains(3));
        }

        @Test
        @DisplayName("contains debe devolver false si el valor es null")
        void containsNullReturnsFalse() {
            tree.insert(10);
            assertFalse(tree.contains(null));
        }
    }

    @Nested
    @DisplayName("minimum y maximum")
    class MinMaxTests {

        @Test
        @DisplayName("mínimo debe devolver el valor más pequeño")
        void shouldReturnMinimum() {
            tree.insert(10);
            tree.insert(2);
            tree.insert(15);
            assertEquals(2, tree.minimum());
        }

        @Test
        @DisplayName("máximo debe devolver el valor más grande")
        void shouldReturnMaximum() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(5);
            assertEquals(20, tree.maximum());
        }

        @Test
        @DisplayName("debe lanzar excepción si el árbol está vacío")
        void minMaxOnEmptyTree() {
            assertThrows(BinarySearchTreeException.class, tree::minimum);
            assertThrows(BinarySearchTreeException.class, tree::maximum);
        }
    }

    @Nested
    @DisplayName("removeBranch")
    class RemoveBranchTests {

        @Test
        @DisplayName("debe eliminar la rama indicada")
        void shouldRemoveBranch() {
            tree.insert(10);
            tree.insert(5);
            tree.insert(3);
            tree.insert(7);

            tree.removeBranch(5);
            assertFalse(tree.contains(5));
            assertFalse(tree.contains(3));
            assertFalse(tree.contains(7));
        }

        @Test
        @DisplayName("no hace nada si se intenta eliminar una rama inexistente")
        void removeNonexistentBranchDoesNothing() {
            tree.insert(10);
            tree.insert(5);
            tree.removeBranch(99); 
            assertTrue(tree.contains(5)); 
        }

        @Test
        @DisplayName("no lanza excepción al eliminar rama en árbol vacío")
        void removeBranchFromEmptyTreeDoesNothing() {
            assertDoesNotThrow(() -> tree.removeBranch(10));
        }

        @Test
        @DisplayName("no lanza excepción si se pasa null a removeBranch")
        void removeBranchNullValueDoesNothing() {
            tree.insert(10);
            assertDoesNotThrow(() -> tree.removeBranch(null));
        }

        @Test
        @DisplayName("debe eliminar la rama derecha si coincide con el valor")
        void shouldRemoveRightBranchIfMatch() {
            tree.insert(10);
            tree.insert(15);  

            assertTrue(tree.contains(15));
            tree.removeBranch(15);
            assertFalse(tree.contains(15));
        }

        @Test
        @DisplayName("debe recorrer hacia la derecha si no coincide directamente con el hijo derecho")
        void shouldTraverseRightIfNoDirectMatch() {
            tree.insert(10);
            tree.insert(15);
            tree.insert(20); // deeper right

            tree.removeBranch(20);

            assertFalse(tree.contains(20));
            assertTrue(tree.contains(15)); // se mantiene el nodo 15
        }

        @Test
        @DisplayName("debe recorrer hacia la izquierda si no coincide directamente con el hijo izquierdo")
        void shouldTraverseLeftIfNoDirectMatch() {
            tree.insert(10);
            tree.insert(5);
            tree.insert(3); // más profundo por la izquierda

            tree.removeBranch(3);

            assertFalse(tree.contains(3));
            assertTrue(tree.contains(5)); // se mantiene el nodo 5
        }
    }

    @Nested
    @DisplayName("size y depth")
    class SizeAndDepthTests {

        @Test
        @DisplayName("size debe contar los nodos")
        void shouldCountNodes() {
            tree.insert(10);
            tree.insert(5);
            tree.insert(15);
            assertEquals(3, tree.size());
        }

        @Test
        @DisplayName("profundidad de árbol con hijos izquierdo y derecho")
        void depthWithLeftAndRightChildren() {
            tree.insert(10);
            tree.insert(5);  // hijo izquierdo
            tree.insert(15); // hijo derecho

            assertEquals(2, tree.depth());
        }
    }  

    @Nested
    @DisplayName("cuando el árbol está vacío")
    class WhenTreeIsEmpty {

        @Test
        @DisplayName("size() debe retornar 0")
        void size_shouldReturnZero() {
            assertEquals(0, tree.size());
        }

        @Test
        @DisplayName("depth() debe retornar 0")
        void depth_shouldReturnZero() {
            assertEquals(0, tree.depth());
        }

        @Test
        @DisplayName("contains() debe retornar false")
        void contains_shouldReturnFalse() {
            assertFalse(tree.contains(5));
        }

        @Test
        @DisplayName("isLeaf() debe retornar false")
        void isLeaf_shouldReturnFalse() {
            assertFalse(tree.isLeaf());
        }
    }

    @Nested
    @DisplayName("Pruebas de balanceo")
    class BalanceTests {
        @Test
        @DisplayName("balance() reorganiza el árbol")
        void balance_shouldReorganizeTree() {
            tree.insert(30);
            tree.insert(10);
            tree.insert(40);
            tree.insert(5);
            tree.insert(20);
            tree.insert(35);
            tree.insert(50);
            
            tree.balance();
            assertTrue(tree.depth() <= 3);
        }
    }

@Nested
@DisplayName("removeValue")
class RemoveValueTests {
    @Test
    @DisplayName("debe eliminar un nodo hoja correctamente")
    void shouldRemoveLeafNode() {
        tree.insert(10);
        tree.insert(5);
        tree.removeValue(5);
        assertFalse(tree.contains(5));
        assertEquals(1, tree.size());
    }

    

    @Test
    @DisplayName("debe eliminar un nodo con un solo hijo (derecho)")
    void shouldRemoveNodeWithRightChild() {
        tree.insert(10);
        tree.insert(15);
        tree.insert(20);
        tree.removeValue(15);
        assertFalse(tree.contains(15));
        assertTrue(tree.contains(20));
        assertEquals("10(,20)", tree.render());
    }

    @Test
    @DisplayName("debe eliminar un nodo con dos hijos")
    void shouldRemoveNodeWithTwoChildren() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(12);
        tree.insert(17);
        tree.removeValue(15);
        assertFalse(tree.contains(15));
        assertTrue(tree.contains(12));
        assertTrue(tree.contains(17));
        // Verificamos que el sucesor (12 o 17) haya tomado su lugar
        assertTrue(tree.render().contains("12") || tree.render().contains("17"));
    }

    @Test
    @DisplayName("no debe hacer nada si el valor no existe")
    void shouldDoNothingIfValueNotExists() {
        tree.insert(10);
        tree.removeValue(99);
        assertEquals(1, tree.size());
    }

    @Test
    @DisplayName("no debe lanzar excepción en árbol vacío")
    void shouldNotThrowOnEmptyTree() {
        assertDoesNotThrow(() -> tree.removeValue(10));
    }

}
@Nested
@DisplayName("render")
class RenderTests {
    @Test
    @DisplayName("debe devolver cadena vacía para árbol vacío")
    void shouldReturnEmptyStringForEmptyTree() {
        assertEquals("", tree.render());
    }

    @Test
    @DisplayName("debe devolver solo el valor para nodo hoja")
    void shouldReturnJustValueForLeafNode() {
        tree.insert(10);
        assertEquals("10", tree.render());
    }

    @Test
    @DisplayName("debe mostrar correctamente árbol con hijo izquierdo")
    void shouldShowLeftChildCorrectly() {
        tree.insert(10);
        tree.insert(5);
        assertEquals("10(5,)", tree.render());
    }

    @Test
    @DisplayName("debe mostrar correctamente árbol con hijo derecho")
    void shouldShowRightChildCorrectly() {
        tree.insert(10);
        tree.insert(15);
        assertEquals("10(,15)", tree.render());
    }

    @Test
    @DisplayName("debe mostrar correctamente árbol con ambos hijos")
    void shouldShowBothChildrenCorrectly() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        assertEquals("10(5,15)", tree.render());
    }

    @Test
    @DisplayName("debe mostrar correctamente árbol multinivel")
    void shouldShowMultiLevelTreeCorrectly() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(3);
        tree.insert(7);
        tree.insert(12);
        tree.insert(20);
        String result = tree.render();
        assertTrue(result.contains("10(5(3,7),15(12,20))") || 
                  result.contains("10(5(3,7),15(12,20))"));
    }
}

@Nested
@DisplayName("inOrder")
class InOrderTests {
    @Test
    @DisplayName("debe devolver lista vacía para árbol vacío")
    void shouldReturnEmptyListForEmptyTree() {
        assertTrue(tree.inOrder().isEmpty());
    }

    @Test
    @DisplayName("debe devolver lista con un elemento para árbol con un nodo")
    void shouldReturnSingleElementForSingleNodeTree() {
        tree.insert(10);
        List<Integer> result = tree.inOrder();
        assertEquals(1, result.size());
        assertEquals(10, result.get(0));
    }

    @Test
    @DisplayName("debe devolver elementos en orden correcto para árbol con múltiples nodos")
    void shouldReturnElementsInOrderForMultiNodeTree() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(3);
        tree.insert(7);
        tree.insert(12);
        tree.insert(20);
        
        List<Integer> expected = List.of(3, 5, 7, 10, 12, 15, 20);
        assertEquals(expected, tree.inOrder());
    }

   

}



}
