// ------------------------------------------------------------------------------
//  <autogenerated>
//      This code was generated by jni4net. See http://jni4net.sourceforge.net/ 
// 
//      Changes to this file may cause incorrect behavior and will be lost if 
//      the code is regenerated.
//  </autogenerated>
// ------------------------------------------------------------------------------

package ir.university.toosi.tms.readerwrapper;

@net.sf.jni4net.attributes.ClrType
public class Person extends system.Object {
    
    //<generated-proxy>
    private static system.Type staticType;
    
    protected Person(net.sf.jni4net.inj.INJEnv __env, long __handle) {
            super(__env, __handle);
    }
    
    @net.sf.jni4net.attributes.ClrConstructor("()V")
    public Person() {
            super(((net.sf.jni4net.inj.INJEnv)(null)), 0);
        Person.__ctorPerson0(this);
    }
    
    @net.sf.jni4net.attributes.ClrMethod("()V")
    private native static void __ctorPerson0(net.sf.jni4net.inj.IClrProxy thiz);
    
    @net.sf.jni4net.attributes.ClrMethod("()I")
    public native int getUserIdforTerminal();
    
    @net.sf.jni4net.attributes.ClrMethod("(I)V")
    public native void setUserIdforTerminal(int value);
    
    @net.sf.jni4net.attributes.ClrMethod("()LSystem/String;")
    public native String getUserName();

    @net.sf.jni4net.attributes.ClrMethod("(LSystem/String;)V")
    public native void setUserName(String value);

    @net.sf.jni4net.attributes.ClrMethod("()LSystem/String;")
    public native String getEmploymentCode();

    @net.sf.jni4net.attributes.ClrMethod("(LSystem/String;)V")
    public native void setEmploymentCode(String value);

    @net.sf.jni4net.attributes.ClrMethod("()LSystem/String;")
    public native String getAccessGroup();

    @net.sf.jni4net.attributes.ClrMethod("(LSystem/String;)V")
    public native void setAccessGroup(String value);

    @net.sf.jni4net.attributes.ClrMethod("()LReaderWrapper/AccessDateType;")
    public native AccessDateType getAccessDateType();

    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/AccessDateType;)V")
    public native void setAccessDateType(AccessDateType value);

    @net.sf.jni4net.attributes.ClrMethod("()LSystem/DateTime;")
    public native system.DateTime getStartAccessDate();

    @net.sf.jni4net.attributes.ClrMethod("(LSystem/DateTime;)V")
    public native void setStartAccessDate(system.DateTime value);

    @net.sf.jni4net.attributes.ClrMethod("()LSystem/DateTime;")
    public native system.DateTime getEndAccessDate();

    @net.sf.jni4net.attributes.ClrMethod("(LSystem/DateTime;)V")
    public native void setEndAccessDate(system.DateTime value);

    @net.sf.jni4net.attributes.ClrMethod("()LReaderWrapper/AuthenticationType;")
    public native AuthenticationType getAuthenticationType();

    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/AuthenticationType;)V")
    public native void setAuthenticationType(AuthenticationType value);

    @net.sf.jni4net.attributes.ClrMethod("()LSystem/String;")
    public native String getPasswordForTerminal();

    @net.sf.jni4net.attributes.ClrMethod("(LSystem/String;)V")
    public native void setPasswordForTerminal(String value);

    @net.sf.jni4net.attributes.ClrMethod("()[B")
    public native byte[] getPicture();

    @net.sf.jni4net.attributes.ClrMethod("([B)V")
    public native void setPicture(byte[] value);

    @net.sf.jni4net.attributes.ClrMethod("()[LSystem/String;")
    public native String[] getCards();

    @net.sf.jni4net.attributes.ClrMethod("([LSystem/String;)V")
    public native void setCards(String[] value);
    
    @net.sf.jni4net.attributes.ClrMethod("()[B")
    public native byte[] getFingerprints();
    
    @net.sf.jni4net.attributes.ClrMethod("([B)V")
    public native void setFingerprints(byte[] value);
    
    public static system.Type typeof() {
        return Person.staticType;
    }
    
    private static void InitJNI(net.sf.jni4net.inj.INJEnv env, system.Type staticType) {
        Person.staticType = staticType;
    }
    //</generated-proxy>
}
