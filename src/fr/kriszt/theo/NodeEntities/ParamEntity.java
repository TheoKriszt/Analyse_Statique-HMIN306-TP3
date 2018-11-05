package fr.kriszt.theo.NodeEntities;

public class ParamEntity extends NodeEntity {

    protected String type;

    public ParamEntity(String n, String type) {
        super(n);
        this.type = type;
    }
}
