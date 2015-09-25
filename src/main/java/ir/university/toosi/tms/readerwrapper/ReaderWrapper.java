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
public class ReaderWrapper extends system.Object {
    
    //<generated-proxy>
    private static system.Type staticType;
    
    protected ReaderWrapper(net.sf.jni4net.inj.INJEnv __env, long __handle) {
            super(__env, __handle);
    }
    
    @net.sf.jni4net.attributes.ClrConstructor("()V")
    public ReaderWrapper() {
            super(((net.sf.jni4net.inj.INJEnv)(null)), 0);
        readerwrapper.ReaderWrapper.__ctorReaderWrapper0(this);
    }
    
    @net.sf.jni4net.attributes.ClrMethod("()V")
    private native static void __ctorReaderWrapper0(net.sf.jni4net.inj.IClrProxy thiz);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/GetAccessEventDataDelegate;)V")
    public native void addOnGetAccessEventData(readerwrapper.GetAccessEventDataDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/GetAccessEventDataDelegate;)V")
    public native void removeOnGetAccessEventData(readerwrapper.GetAccessEventDataDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/VerifyUserPassDelegate;)V")
    public native void addOnVerifyUserPass(readerwrapper.VerifyUserPassDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/VerifyUserPassDelegate;)V")
    public native void removeOnVerifyUserPass(readerwrapper.VerifyUserPassDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/VerifyCardDelegate;)V")
    public native void addOnVerifyCard(readerwrapper.VerifyCardDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/VerifyCardDelegate;)V")
    public native void removeOnVerifyCard(readerwrapper.VerifyCardDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/VerifyFinger1To1Delegate;)V")
    public native void addOnVerifyFinger1To1(readerwrapper.VerifyFinger1To1Delegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/VerifyFinger1To1Delegate;)V")
    public native void removeOnVerifyFinger1To1(readerwrapper.VerifyFinger1To1Delegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/VerifyFinger1ToNDelegate;)V")
    public native void addOnVerifyFinger1ToN(readerwrapper.VerifyFinger1ToNDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/VerifyFinger1ToNDelegate;)V")
    public native void removeOnVerifyFinger1ToN(readerwrapper.VerifyFinger1ToNDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/TerminalConnectedDelegate;)V")
    public native void addOnTerminalConnected(readerwrapper.TerminalConnectedDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/TerminalConnectedDelegate;)V")
    public native void removeOnTerminalConnected(readerwrapper.TerminalConnectedDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/TerminalDisConnectedDelegate;)V")
    public native void addOnTerminalDisConnected(readerwrapper.TerminalDisConnectedDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/TerminalDisConnectedDelegate;)V")
    public native void removeOnTerminalDisConnected(readerwrapper.TerminalDisConnectedDelegate value);
    
    @net.sf.jni4net.attributes.ClrMethod("(I)V")
    public native void StartService(int port);
    
    @net.sf.jni4net.attributes.ClrMethod("()V")
    public native void ShutDown();
    
    @net.sf.jni4net.attributes.ClrMethod("(IILReaderWrapper/AuthenticationType;Z)V")
    public native void SendAuthResultToTerminal(int terminalId, int userId, readerwrapper.AuthenticationType authenticationType, boolean isAuthenticated);
    
    @net.sf.jni4net.attributes.ClrMethod("([LReaderWrapper/Person;[B)LReaderWrapper/Person;")
    public native readerwrapper.Person Verify(readerwrapper.Person[] allUsers, byte[] fingerData);
    
    @net.sf.jni4net.attributes.ClrMethod("(LReaderWrapper/Person;[B)Z")
    public native boolean Verify(readerwrapper.Person person, byte[] fingerData);
    
    @net.sf.jni4net.attributes.ClrMethod("(I)V")
    public native void ForceOpenDoor(int terminalId);
    
    @net.sf.jni4net.attributes.ClrMethod("(I)V")
    public native void LockDoor(int terminalId);
    
    @net.sf.jni4net.attributes.ClrMethod("(I)V")
    public native void UnLockDoor(int terminalId);
    
    @net.sf.jni4net.attributes.ClrMethod("(I)V")
    public native void LockTerminal(int terminalId);
    
    @net.sf.jni4net.attributes.ClrMethod("(I)V")
    public native void UnLockTerminal(int terminalId);
    
    @net.sf.jni4net.attributes.ClrMethod("(IILReaderWrapper/GetUserInfoCompletedCallBackDelegate;)V")
    public native void GetUserInfo(int terminalId, int userId, readerwrapper.GetUserInfoCompletedCallBackDelegate callBack);
    
    @net.sf.jni4net.attributes.ClrMethod("(ILReaderWrapper/GetUserListCompletedCallBackDelegate;)V")
    public native void GetUserInfo(int terminalId, readerwrapper.GetUserListCompletedCallBackDelegate callBack);
    
    @net.sf.jni4net.attributes.ClrMethod("([I[LReaderWrapper/Person;ZLReaderWrapper/AddCompletedCallBackDelegate;)V")
    public native void AddUserInfo(int[] terminals, readerwrapper.Person[] persons, boolean isOverwrite, readerwrapper.AddCompletedCallBackDelegate callBack);
    
    @net.sf.jni4net.attributes.ClrMethod("()V")
    public native void InitAccessControlData();
    
    @net.sf.jni4net.attributes.ClrMethod("(I)V")
    public native void SetAccessControlDataToTerminal(int terminalId);
    
    @net.sf.jni4net.attributes.ClrMethod("(LSystem/String;ILSystem/String;)V")
    public native void SetAccessGroup(String code, int index, String accessTime);

    @net.sf.jni4net.attributes.ClrMethod("(LSystem/String;LSystem/String;LSystem/String;LSystem/String;LSystem/String;LSystem/String;LSystem/String;LSystem/String;LSystem/String;LSystem/String;)V")
    public native void SetAccessTime(String code, String sun, String mon, String tue, String wed, String thu, String fri, String sat, String hol, String holiday);

    @net.sf.jni4net.attributes.ClrMethod("(LSystem/String;III)V")
    public native void SetHoliday(String code, int index, int month, int day);

    @net.sf.jni4net.attributes.ClrMethod("(LSystem/String;IIIII)V")
    public native void SetTimezone(String code, int index, int startHour, int startMinute, int endHour, int endMinute);
    
    public static system.Type typeof() {
        return readerwrapper.ReaderWrapper.staticType;
    }
    
    private static void InitJNI(net.sf.jni4net.inj.INJEnv env, system.Type staticType) {
        readerwrapper.ReaderWrapper.staticType = staticType;
    }
    //</generated-proxy>
}
