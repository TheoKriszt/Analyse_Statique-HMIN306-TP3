package fr.kriszt.theo.NodeEntities;


public class NodeEntity {

    public String getName() {
        return name;
    }

    private String name;

    NodeEntity(String n){
        name = n;
    }


    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof NodeEntity)){
            return false;
        }
        NodeEntity node = (NodeEntity) o;

        return this.getClass().equals(o.getClass()) && node.getName().equals(this.getName());
    }


}
