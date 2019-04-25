package it.ethiclab.depcal;

import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

public class FunctionSorter {
    StringWriter sw = new StringWriter();
    private GraphTS graphTS;

	public void sort(List<F> list, boolean ignoreUndefined) {

        List<F> nodes = new ArrayList<>();
	    List<F> unresolved = new ArrayList<>();
        List<F> resolved = new ArrayList<>();

        graphTS = new GraphTS(list.size());

        for (int i = 0; i < list.size(); i++) {
            F f = list.get(i);
            nodes.add(f);
            graphTS.addVertex(f);
        }

		for (F f : list) {
            F c = nodes.get(nodes.indexOf(f));
            String[] words = f.getCode() != null ? f.getCode().split("\\W") : new String[]{};

            List<String> needed = Arrays
                    .stream(words)
                    .filter(this::isNotNumber)
                    .filter(this::isNotEmpty)
                    .filter(s -> isNot(s, f.getName()))
                    .collect(Collectors.toList());

            needed
                    .stream()
                    .forEach(x -> {
                        int index = nodes.indexOf(new F(x, null));
                        if (index >= 0) {
                            F node = nodes.get(index);
                            graphTS.addEdge(nodes.indexOf(f), index);
                        }
                    });

            needed
                    .stream()
                    .filter(x -> !nodes.contains(new F(x, null)))
                    .forEach(x -> print(0, f.getName() + " -> depends on undefined symbol -> " + x));

            if (!ignoreUndefined && needed
                    .stream()
                    .filter(x -> !nodes.contains(new F(x, null))).count() > 0) {
                throw new DependencyException("symbols not found : \n" + sw.toString());
            }
        }

        nodes.stream().forEach(c -> depResolve(c, resolved, unresolved));

        list.clear();
        list.addAll(resolved);
	}

    private void print(int tab, String s) {
	    for (int i = 0; i < tab; i++) {
	        sw.write('\t');
        }
	    sw.write(s);
        sw.write('\n');
    }

    private boolean isNot(String s, String s2) {
	    return s.compareTo(s2) != 0;
    }

    private boolean isNotEmpty(String s) {
	    return s.trim().length() > 0;
    }

    private boolean isNotNumber(String s) {
	    try {
            Integer.parseInt(s);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private void depResolve(F c, List<F> resolved, List<F> unresolved) {
        /*unresolved.add(c);
        c.getEdges().forEach(sub -> {
            if (resolved.indexOf(sub) == -1) {
                if (unresolved.indexOf(sub) != -1) {
                    throw new CircularReferenceException("Circular reference detected: " + c.getName() + " -> " + sub.getName());
                }
                depResolve(sub, resolved, unresolved);
            }
        });
        resolved.add(c);
        unresolved.remove(unresolved.indexOf(c));*/
        resolved.clear();
        resolved.addAll(graphTS.topo());
    }
}
