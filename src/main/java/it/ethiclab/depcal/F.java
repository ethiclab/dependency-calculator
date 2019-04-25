package it.ethiclab.depcal;

public class F {
    private final String name;
    private final String code;
    private final boolean external;

    public F(String name, String code, boolean external) {
        this.name = name;
        this.code = code;
        this.external = external;
    }

    public F(String name, String code) {
        this(name, code, false);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        F graphNode = (F) o;

        return name.equals(graphNode.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isExternal() {
        return external;
    }
}
