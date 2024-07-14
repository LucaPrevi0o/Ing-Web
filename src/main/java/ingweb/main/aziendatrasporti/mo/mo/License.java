package ingweb.main.aziendatrasporti.mo.mo;

import ingweb.main.aziendatrasporti.mo.ModelObject;

public class License extends ModelObject {

    String category;

    public License(String category) { this.category=category; }

    public Object[] asList() { return new Object[]{this.category}; }
    public Object[] data() { return new Object[]{this.category}; }
    public String display() { return this.category; }
    public String toString() { return this.category; }

    public boolean equals(Object o) {

        if (!(o instanceof License)) return false;
        return ((License)o).category.equals(this.category);
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category=category; }
}
