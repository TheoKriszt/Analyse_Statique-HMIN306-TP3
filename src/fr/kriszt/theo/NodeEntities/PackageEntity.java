package fr.kriszt.theo.NodeEntities;

import java.util.ArrayList;

public class PackageEntity  extends NodeEntity{

    public ArrayList<TypeEntity> classes = new ArrayList<>();

    public PackageEntity(String n) {
        super(n);
    }
}