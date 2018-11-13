package fr.kriszt.theo.NodeEntities;

import java.util.ArrayList;

public class NodeEntity {

    public String getName() {
        return name;
    }

    protected String name;

    public NodeEntity(String n){
        name = n;
    }

    public static ArrayList<PackageEntity> packages = new ArrayList<>();

    @Override
    public String toString(){
        return name;
    }


}
