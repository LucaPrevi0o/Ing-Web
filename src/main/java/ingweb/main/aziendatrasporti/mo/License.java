package ingweb.main.aziendatrasporti.mo;

public class License implements ModelObject {

    String category;

    public License(String category) { this.category=category; }

    public Object[] asList() { return new Object[]{category}; }
    public Object[] data() { return asList(); }

    public boolean equals(Object o) {

        if (!(o instanceof License)) return false;
        return ((License)o).category.equals(this.category);
    }

    public String toString() { return category; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category=category; }
}
