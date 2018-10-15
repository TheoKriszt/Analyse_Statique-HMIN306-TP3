package fr.kriszt.theo.NodeEntities;

import java.util.ArrayList;

public class MethodEntity  extends NodeEntity{

    public ArrayList<ParamEntity> params = new ArrayList<>();

    public MethodEntity(String n) {
        super(n);
    }
}