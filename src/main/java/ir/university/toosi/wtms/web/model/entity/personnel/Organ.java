package ir.university.toosi.wtms.web.model.entity.personnel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Iterators;
import ir.university.toosi.wtms.web.model.entity.BLookup;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;
import ir.university.toosi.wtms.web.model.entity.rule.RulePackage;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author : Mostafa Rastgar
 * @version : 0.8
 */
@JsonIgnoreProperties(value = "@id")
public class Organ extends BaseEntity implements TreeNode {
    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String title;
    @JsonProperty
    private String code;
    @JsonProperty
    private BLookup organType;
    @JsonProperty
    private Organ parentOrgan;
    @JsonProperty
    private boolean inheritance;
    @JsonProperty
    private RulePackage rulePackage;
    @JsonIgnore
    private List<Organ> children = new ArrayList<>();
    @JsonIgnore
    private List<Person> persons = new ArrayList<>();


    public Organ(String name, String code, String title, BLookup organType) {
        this.name = name;
        this.code = code;
        this.title = title;
        this.organType = organType;
    }

    public Organ() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Organ getParentOrgan() {
        return parentOrgan;
    }

    public void setParentOrgan(Organ parentOrgan) {
        this.parentOrgan = parentOrgan;
    }

    public BLookup getOrganType() {
        return organType;
    }

    public void setOrganType(BLookup organType) {
        this.organType = organType;
    }



    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }
    @JsonIgnore
    public void addChild(Organ organ){
        children.add(organ);
    }

    @Override
    @JsonIgnore
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    @JsonIgnore
    public int getChildCount() {
        return children.size();
    }

    @Override
    @JsonIgnore
    public TreeNode getParent() {
        return parentOrgan;
    }

    @Override
    @JsonIgnore
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    @JsonIgnore
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    @JsonIgnore
    public Enumeration children() {
        return Iterators.asEnumeration(children.iterator());
    }

    @JsonIgnore
    public String getType(){
        return "organ";
    }

    public boolean isInheritance() {
        return inheritance;
    }

    public void setInheritance(boolean inheritance) {
        this.inheritance = inheritance;
    }

    @JsonIgnore
    public static List<Organ> prepareHierarchy(List<Organ> organs){
        List<Organ> finalOrgans = new ArrayList<>();
        for(Organ parentOrgan : organs){
            for(Organ childOrgan : organs){
                if(childOrgan.getParentOrgan() != null && childOrgan.getParentOrgan().getId() == parentOrgan.getId()){
                    parentOrgan.addChild(childOrgan);
                }
            }
            if(parentOrgan.getParentOrgan() == null){
                finalOrgans.add(parentOrgan);
            }
        }
        return finalOrgans;
    }
    @JsonIgnore
    public List<Person> getPersons() {
        return persons;
    }
    @JsonIgnore
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}

