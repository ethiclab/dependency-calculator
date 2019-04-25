package it.ethiclab.depcal;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class FunctionSorterTest {

    @Test
    public void test_simple_order() {
        List<F> list = new ArrayList<>();
        // A calls B
        list.add(new F("A", "A = B;"));
        // B does not call any function
        list.add(new F("B", "B = 1;"));
        new FunctionSorter().sort(list, false);
        // we should call first B
        assertThat(list.get(0).getName()).isEqualTo("B");
        // and finally A
        assertThat(list.get(1).getName()).isEqualTo("A");
    }

    @Test(expected = CircularReferenceException.class)
    public void test_circular_reference() {
        List<F> list = new ArrayList<>();
        list.add(new F("A", "A = B;"));
        list.add(new F("B", "B = C;"));
        list.add(new F("C", "C = A;"));
        new FunctionSorter().sort(list, false);
    }

    @Test
    public void test_more_complex_example() {
        List<F> list = new ArrayList<>();

        list.add(new F("if", "", true));
        list.add(new F("D", "", true));
        list.add(new F("X", "", true));

        list.add(new F("A", "if\nA = X[B][C];"));
        list.add(new F("B", "B = D[C];"));
        list.add(new F("C", "C \n= \n3\n;"));
        new FunctionSorter().sort(list, false);
        int i = 0;
        assertThat(list.get(i++).getName()).isEqualTo("if");
        assertThat(list.get(i++).getName()).isEqualTo("D");
        assertThat(list.get(i++).getName()).isEqualTo("X");
        assertThat(list.get(i++).getName()).isEqualTo("C");
        assertThat(list.get(i++).getName()).isEqualTo("B");
        assertThat(list.get(i++).getName()).isEqualTo("A");
    }

    @Test(expected = DependencyException.class)
    public void test_undefined() {
        List<F> list = new ArrayList<>();
        list.add(new F("A", "A = X;"));
        new FunctionSorter().sort(list, false);
    }

    @Test
    public void test_undefined_ignored() {
        List<F> list = new ArrayList<>();
        list.add(new F("A", "A = X;"));
        new FunctionSorter().sort(list, true);
    }
}
