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
public abstract class VerifyCardDelegate extends system.MulticastDelegate {
    
    //<generated-proxy>
    private static system.Type staticType;
    
    protected VerifyCardDelegate(net.sf.jni4net.inj.INJEnv __env, long __handle) {
            super(__env, __handle);
    }
    
    protected VerifyCardDelegate() {
            super(((net.sf.jni4net.inj.INJEnv)(null)), 0);
    }
    
    @net.sf.jni4net.attributes.ClrMethod("(ILReaderWrapper/VirdiAuthMode;LSystem/String;)V")
    public abstract void Invoke(int terminalId, readerwrapper.VirdiAuthMode authMode, String card);

    @net.sf.jni4net.attributes.ClrMethod("(ILReaderWrapper/VirdiAuthMode;LSystem/String;LSystem/AsyncCallback;LSystem/Object;)LSystem/IAsyncResult;")
    public system.IAsyncResult BeginInvoke(int terminalId, readerwrapper.VirdiAuthMode authMode, String card, system.AsyncCallback callback, system.Object object) {
        throw new system.NotImplementedException();
    }
    
    @net.sf.jni4net.attributes.ClrMethod("(LSystem/IAsyncResult;)V")
    public void EndInvoke(system.IAsyncResult result) {
        throw new system.NotImplementedException();
    }
    
    public static system.Type typeof() {
        return readerwrapper.VerifyCardDelegate.staticType;
    }
    
    private static void InitJNI(net.sf.jni4net.inj.INJEnv env, system.Type staticType) {
        readerwrapper.VerifyCardDelegate.staticType = staticType;
    }
    //</generated-proxy>
}

//<generated-proxy>
@net.sf.jni4net.attributes.ClrProxy
class __VerifyCardDelegate extends readerwrapper.VerifyCardDelegate {
    
    protected __VerifyCardDelegate(net.sf.jni4net.inj.INJEnv __env, long __handle) {
            super(__env, __handle);
    }
    
    @net.sf.jni4net.attributes.ClrMethod("(ILReaderWrapper/VirdiAuthMode;LSystem/String;)V")
    public native void Invoke(int terminalId, readerwrapper.VirdiAuthMode authMode, String card);

    @net.sf.jni4net.attributes.ClrMethod("(ILReaderWrapper/VirdiAuthMode;LSystem/String;LSystem/AsyncCallback;LSystem/Object;)LSystem/IAsyncResult;")
    public native system.IAsyncResult BeginInvoke(int terminalId, readerwrapper.VirdiAuthMode authMode, String card, system.AsyncCallback callback, system.Object object);
    
    @net.sf.jni4net.attributes.ClrMethod("(LSystem/IAsyncResult;)V")
    public native void EndInvoke(system.IAsyncResult result);
}
//</generated-proxy>
