package br.com.fabrleadr.utils.sql.model;

/**
 *
 * @author ltonietto
 */
public abstract class Constraint {

    private String name;

    public Constraint(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Constraint createInstance(String name) throws Exception {
        if(name.startsWith("PK")){
            return new PrimaryKey(name);
        } else if (name.startsWith("FK")){
            return new ForeignKey(name);
        } else if (name.startsWith("CK")){
            return new CheckConstraint(name);
        }
        throw new IllegalArgumentException(name);
    }
    
}
