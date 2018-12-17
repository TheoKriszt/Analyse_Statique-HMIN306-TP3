package fr.kriszt.theo.NodeEntities;

import java.util.ArrayList;

public class PackageEntity  extends NodeEntity{

    private ArrayList<InterfaceEntity> interfaces = new ArrayList<>();
    ArrayList<ClassEntity> classes = new ArrayList<>();

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