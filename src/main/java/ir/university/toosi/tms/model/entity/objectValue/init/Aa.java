//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.06 at 12:01:51 PM IRDT 
//


package ir.university.toosi.tms.model.entity.objectValue.init;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bb" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="_id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="AlertType" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="Color" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "bb"
})
@XmlRootElement(name = "aa")
public class Aa {

    @XmlElement(required = true)
    protected Bb bb;
    @XmlAttribute(name = "_id")
    @XmlSchemaType(name = "anySimpleType")
    protected String id;
    @XmlAttribute(name = "name")
    @XmlSchemaType(name = "anySimpleType")
    protected String name;
    @XmlAttribute(name = "AlertType")
    @XmlSchemaType(name = "anySimpleType")
    protected String alertType;
    @XmlAttribute(name = "Color")
    @XmlSchemaType(name = "anySimpleType")
    protected String color;

    /**
     * Gets the value of the bb property.
     *
     * @return possible object is
     * {@link Object }
     */
    public Bb getBb() {
        return bb;
    }

    /**
     * Sets the value of the bb property.
     *
     * @param value allowed object is
     *              {@link Object }
     */
    public void setBb(Bb value) {
        this.bb = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the alertType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAlertType() {
        return alertType;
    }

    /**
     * Sets the value of the alertType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAlertType(String value) {
        this.alertType = value;
    }

    /**
     * Gets the value of the color property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setColor(String value) {
        this.color = value;
    }

}
