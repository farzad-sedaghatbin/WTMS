package ir.university.toosi.tms.model.entity.personnel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Iterators;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.rule.RulePackage;

import javax.persistence.*;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Entity
@Table(name = "tb_organ")
@NamedQueries({
        @NamedQuery(
                name = "Organ.exist",
                query = "select count(o) from Organ o where  o.code = :code and o.deleted='0'"
        ),
        @NamedQuery(
                name = "Organ.list",
                query = "select o from Organ o  where o.deleted <> '1'"
        ),
        @NamedQuery(
                name = "Organ.active.list",
                query = "select o from Organ o where o.deleted <> '1' and o.parentOrgan is null"
        ),
        @NamedQuery(
                name = "Organ.active.by.parent.list",
                query = "select o from Organ o where o.deleted <> '1' and o.parentOrgan is not null and o.parentOrgan.id = :parentId"
        ),
        @NamedQuery(
                name = "Organ.findById",
                query = "select o from Organ o where o.id=:id"
        ),
        @NamedQuery(
                name = "Organ.findByRulePackageId",
                query = "select o from Organ o where o.rulePackage.id=:id and o.deleted <> '1'"
        )
})
public class Organ extends BaseEntity implements javax.swing.tree.TreeNode {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "code")
    private String code;
    @JsonProperty
    @Column(name = "title")
    private String title;
    @JsonProperty
    @ManyToOne
    private BLookup organType;
    @JsonProperty
    @ManyToOne
    private Organ parentOrgan;
    @JsonIgnore
    @OneToMany(mappedBy = "parentOrgan", fetch = FetchType.EAGER)
    private Set<Organ> childOrgans;
    @JsonProperty
    @Column(name = "inheritance")
    private boolean inheritance;
    @JsonProperty
    @ManyToOne
    private RulePackage rulePackage;
    @JsonIgnore
    @Transient
    private List<Organ> children = new ArrayList<>();

    public Organ() {
    }

    public Organ(String name, String code, String title, BLookup organType) {
        this.name = name;
        this.code = code;
        this.title = title;
        this.organType = organType;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BLookup getOrganType() {
        return organType;
    }

    public void setOrganType(BLookup organType) {
        this.organType = organType;
    }

    public Organ getParentOrgan() {
        return parentOrgan;
    }

    public void setParentOrgan(Organ parentOrgan) {
        this.parentOrgan = parentOrgan;
    }

    public Set<Organ> getChildOrgans() {
        return childOrgans;
    }

    public void setChildOrgans(Set<Organ> childOrgans) {
        this.childOrgans = childOrgans;
    }

    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }



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


}