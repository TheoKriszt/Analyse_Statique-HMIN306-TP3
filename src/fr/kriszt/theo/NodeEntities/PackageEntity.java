package fr.kriszt.theo.NodeEntities;

import java.util.ArrayList;

public class PackageEntity  extends NodeEntity{

    protected ArrayList<InterfaceEntity> interfaces = new ArrayList<>();
    protected ArrayList<ClassEntity> classes = new ArrayList<>();

    public PackageEntity(String n) {
        super(n);
    }

    public void addInterface(InterfaceEntity i){
        interfaces.add(i);
    }

    public void addClass(ClassEntity c){
        classes.add(c);
    }


}